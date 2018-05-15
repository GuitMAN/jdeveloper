"""
 Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved. 

Caution: This file is part of the WLST implementation. Do not edit or move
this file because this may cause WLST commands and scripts to fail. Do not
try to reuse the logic in this file or keep copies of this file because this
could cause your WLST scripts to fail when you upgrade to a different version
of WLST.

Oracle Fusion Middleware DFW Configuration Upgrade commands.

"""

import os
import sys
import getopt
import java.lang
from java.lang import Class
from java.lang import String
from java.lang import Exception

def printUsage():
  print ' '
  print 'Usage'
  print '-----'
  print ' '
  print 'wlst.sh <ORACLE_HOME>/common/scripts/wlst/upgradeDFWConfig.py [--userfile <user config file> --keyfile <user key file> --adminurl <admin url> --help]'
  print ' '
  print 'Arguments:'
  print '----------'
  print '-userfile: [optional] path to the userConfigFile'
  print '-keyfile:  [optional] path to the userKeyFile'
  print '-adminurl: [optional] AdminServer connection URL'
  print '-help:     display program usage'

def getServers():
  svrList = ls('/Servers', returnMap='true', returnType='a')
  serversStr = " "
  print ' '
  clustername = " "
  for token in svrList:
    token=token.strip().lstrip().rstrip()
    if not token == '':
      serversStr = serversStr+ token + ","
  print "List of servers : ", serversStr
  return serversStr

"""
dfwConigurationUpgrade command
"""
def dfwConigurationUpgrade():

  try:
    serverListStr = getServers()	
    serverArr=[]

    for token in serverListStr.split(","):
      token=token.strip().lstrip().rstrip()	
      if not token == '':
        serverArr.append(java.lang.String(token))

    for server in serverArr:
      addDefaultDumpSamplings(server)

    print "completed"
  except Exception, ex:
    print ex.getMsg()

"""
addDefaultDumpSamplings command
"""
def addDefaultDumpSamplings(serverName):

  try:
    print serverName, ' : '
    addDumpSample(sampleName="JVMThreadDump", diagnosticDumpName="jvm.threads", samplingInterval=30, rotationCount=20, dumpedImplicitly=1, toAppend=1, args={'timing' : 'true', 'context' : 'true', 'progressive' : 'true', 'depth' : '20', 'threshold' : '30000', 'event' : 'true'}, server=serverName)
    addDumpSample(sampleName="JavaClassHistogram", diagnosticDumpName="jvm.classhistogram", samplingInterval=1800, rotationCount=10, dumpedImplicitly=0, toAppend=1, server=serverName)
    print ' '
  except Exception, ex:
    print ex.getMsg()

userfile=""
keyfile=""
adminurl=""

try:
  options,remainder = getopt.getopt(sys.argv[1:],'', ['userfile=', 'keyfile=', 'adminurl=', 'help='])
except getopt.error, msg:
  printUsage()
  sys.exit()

for opt, arg in options:
  if opt == '--userfile':
    userfile = arg
  elif opt == '--keyfile':
    keyfile = arg
  elif opt == '--adminurl':
    adminurl = arg
  elif opt == '--help':
    displayUsage()
    exit()

try:

  if connected == "false":
    if ((userfile == '') or (keyfile == '')):
      connect()
    elif adminurl == '':
      connect(userConfigFile=userfile, userKeyFile=keyfile)
    else:
      connect(userConfigFile=userfile, userKeyFile=keyfile, url=adminurl)

  dfwConigurationUpgrade()
  disconnect()
  exit()
except getopt.error, msg:
  sys.exit()

