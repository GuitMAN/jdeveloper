##########################################################################
# WLST script to configure a WLDF Module which consists of multiple
# WLDF Watch and WLDF Notification
# The script will:
# - Connect to a server, booting it first if necessary
# - Look up or create a WLDF System Resource
# - Create watch and watch rule on certain critical BEA error codes
#   representating unhandle Exceptions thrown by the applications, and
#   other critical errors
# - Configure WLDF watch to use a JMXNotification for notifying FMWDFW
#   which in turns generates incident reports
#
# Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved. 
##########################################################################

from java.util import Date
from java.text import SimpleDateFormat
from java.io import ByteArrayOutputStream
from java.io import StringBufferInputStream
from java.util import Properties
from java.lang import *
import jarray
import sys
import getopt
import string

def printUsage():
	print ' '
	print 'Usage'
	print '-----'
	print ' '
        print '<MW_ORA_HOME>/common/bin/wlst.sh config-fmwdfw.py --action <action> --prompt <y|n> --userfile <user credential file> --keyfile <key file> --user <admin user name> --password <admin password> --adminurl <admin url> --managed_server <managed server name> --module_name <name of an existing WLDF module for merging resources>'
        print 'where action is one of the following'
        print 'create  - create Module-FMWDFW and its resources (default)'
        print 'destroy - destroy Module-FMWDFW and its resources'
        print 'enable  - enable Module-FMWDFW by adding all servers into its target list'
        print 'disable - disable Module-FMWDFW by removing its targets'
        print 'merge   - create the resources of Module-FMWDFW underneath a system module specified by the module_name parameter'
        print ' '

# Function to handle script arguments of the variety 'n=v', where
# arguments are placed into a dictionary of nv pairs and returned
# to the caller
def argsToDict(args):
  d = {}
  for arg in args:
    #print "arg: " + arg
    pair = arg.split('=', 1)
    #print "pair: " + str(pair)
    if len(pair) == 2:
      # binary argument, store as key pair
      key = pair[0]
      val = pair[1]
      d[key] = val
    else:
      # Unary argument, story with empty (non-null) key
      d[arg] = ''
  print "Arguments: " + str(d)
  return d

# Returns the value found in the provided map, at the location
# specified by 'key'; if no entry exists in the map for 'key',
# the provided default is returned. 
def getValue(dict, key, default=None):
  ret = default
  if dict is not None:
    try:
      ret=dict[key]
    except KeyError:
      pass
  return ret

# Connect to the target server specified in the provide args
def connectIfNecessary(argsDict=None):
  # connect if necessary
  if connected == "false":
    promptStr=getValue(argsDict, "prompt", "y")
    userfile=getValue(argsDict, "userfile", None)
    keyfile=getValue(argsDict, "keyfile", None)
    if promptStr == "y":
      connect()
    else:
      if ((userfile == None) and (keyfile == None)):
        user=getValue(argsDict, "user", "weblogic")
        passwd=getValue(argsDict, "password", "weblogic")
        url=getValue(argsDict, "adminurl", "t3://localhost:7001")
        print "Connecting with [" + user + "," + url + "]"
        connect(user,passwd,url)
      else:
        connect(userfile, keyfile)

def enable(specSrv, wldfMod, force):
  wldfsysres = cmo.getWLDFSystemResources()
  hasTarget=false
  for mod in wldfsysres:
    if len(mod.getTargets())>0 and mod.getName() != "Module-FMWDFW":
      print mod.getTargets()
      hasTarget=true
  if hasTarget != true or force:
    for mod in wldfsysres:
      if mod.getName() != "Module-FMWDFW":
        print "Remove targets from " + mod.getName()
        mod.setTargets(None)
    # Target the System Resource to the specific server
    svrs = cmo.getServers()
    if specSrv==None:
      for svr in svrs:
        print "Add target: " + svr.getName()
        wldfMod.addTarget(svr)
    else:
      wldfServer=cmo.lookupServer(specSrv)
      print "Add specific target: " + wldfServer
      wldfMod.addTarget(wldfServer)

def disable(wldfMod):
  print "Remove targets from " + wldfMod.getName()
  wldfMod.setTargets(None)

def createChildResources(wldfmodulePath):
  print('Creating WLDF resources')
  cd(wldfmodulePath)
  # JMX Notification
  jmxnot=cmo.createJMXNotification("FMWDFW-notification")
  jmxnot.setNotificationType('oracle.dfw.wldfnotification')
  # Watch for Unhandle Exceptions
  watch=cmo.createWatch("UncheckedException")
  watch.setEnabled(1)
  watch.addNotification(jmxnot)
  watch.setRuleType('Log')
  watch.setEnabled(true)
  watch.setRuleExpression('(SEVERITY = \'Error\') AND ((MSGID = \'WL-101020\') OR (MSGID = \'WL-101017\') OR (MSGID = \'WL-000802\') OR (MSGID = \'BEA-101020\') OR (MSGID = \'BEA-101017\') OR (MSGID = \'BEA-000802\'))')
  watch.setAlarmType('AutomaticReset')
  watch.setAlarmResetPeriod(30000)
  # Watch for Deadlock Threads
  watch=cmo.createWatch("Deadlock")
  watch.setEnabled(1)
  watch.addNotification(jmxnot)
  watch.setRuleType('Log')
  watch.setEnabled(true)
  watch.setRuleExpression('((SEVERITY = \'Info\') OR (SEVERITY = \'Critical\')) AND ((MSGID = \'WL-000394\') OR (MSGID = \'BEA-000394\'))')
  watch.setAlarmType('AutomaticReset')
  watch.setAlarmResetPeriod(30000)
  # Watch for Stuck Threads
  watch=cmo.createWatch("StuckThread")
  watch.setEnabled(1)
  watch.addNotification(jmxnot)
  watch.setRuleType('Log')
  watch.setEnabled(true)
  watch.setRuleExpression('(SEVERITY = \'Error\') AND ((MSGID = \'WL-000337\') OR (MSGID = \'BEA-000337\'))')
  watch.setAlarmType('AutomaticReset')
  watch.setAlarmResetPeriod(600000)

destroy_resource = false
argsDict = argsToDict(sys.argv)
action=getValue(argsDict, "action", "create")

connectIfNecessary(argsDict)

edit() 
startEdit()

managed_server=getValue(argsDict, "managed_server", None)
merge_mod_name=getValue(argsDict, "module_name", None)
svrs = cmo.getServers()

# Look up or create the WLDF System resource
wldfResourceName = "Module-FMWDFW"
wldfWatchPath = "/WLDFSystemResources/" + wldfResourceName + "/WLDFResource/" + wldfResourceName + "/WatchNotification/" + wldfResourceName
myWldfVar = cmo.lookupSystemResource(wldfResourceName)

if myWldfVar != None and action=="destroy":
    print "Destroying WLDF FMWDFW System Resource: " + wldfResourceName
    cmo.destroyWLDFSystemResource(myWldfVar)
    save()
    activate()
    disconnect()
    exit()

if myWldfVar != None and action=="disable":
    print "Disabling WLDF-FMWDFW Watch"
    disable(myWldfVar)
    save()
    activate()
    disconnect()
    exit()

if myWldfVar != None and action=="enable":
    print "Enabling WLDF-FMWDFW Watch"
    enable(managed_server, myWldfVar, true)
    save()
    activate()
    disconnect()
    exit()

if myWldfVar==None and action=="create":
    print "Creating Module-FMWDFW: " + wldfResourceName
    myWldfVar=cmo.createWLDFSystemResource(wldfResourceName)
    myWldfVar.setDescription('Creates FMWDFW incidents based on unchecked Exceptions and critical errors')
    enable(managed_server, myWldfVar, false)
    createChildResources(wldfWatchPath)
    # Persist the changes
    save()
    activate()
    disconnect()
    exit()

if merge_mod_name!=None and action=="merge":
    print "Creating resources underneath " + merge_mod_name
    merge_mod=cmo.lookupSystemResource(merge_mod_name)
    mergeWldfPath = "/WLDFSystemResources/" + merge_mod_name + "/WLDFResource/" + merge_mod_name + "/WatchNotification/" + merge_mod_name
    createChildResources(mergeWldfPath)
    # Persist the changes
    save()
    activate()
    disconnect()
    exit()


