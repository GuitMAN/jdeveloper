"""
 Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved. 

Caution: This file is part of the WLST implementation. Do not edit or move
this file because this may cause WLST commands and scripts to fail. Do not
try to reuse the logic in this file or keep copies of this file because this
could cause your WLST scripts to fail when you upgrade to a different version
of WLST.

Oracle Fusion Middleware Click History Upgrade commands.

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
  print '$MW_HOME/oracle_common/common/bin/wlst.sh $MW_HOME/oracle_common/modules/oracle.dms_11.1.1/scripts/upgradeClickhistory.py [--userfile <user config file> --keyfile <user key file> --adminurl <admin url> --help]'
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
clickhistoryUpgrade command
"""
def clickhistoryUpgrade():

  try:
    serverListStr = getServers()	
    serverArr=[]

    for token in serverListStr.split(","):
      token=token.strip().lstrip().rstrip()	
      if not token == '':
        serverArr.append(java.lang.String(token))

    for server in serverArr:
      updateClickhistoryHandler(server)

    print "Completed Click History upgraded."
  except Exception, ex:
    print ex.getMsg()

"""
updateClickhistoryHandler command
"""
def updateClickhistoryHandler(serverName):

  try:
    print 'Updating Click History configuration for ', serverName, ' : '
    print ' '
    configureLogHandler(name="apps-clickhistory-handler", addProperty=1, propertyName="mergeStartEndMessage", propertyValue="false", target=serverName.toString())
    configureLogHandler(name="apps-clickhistory-handler", addProperty=1, propertyName="saveToDisk", propertyValue="true", target=serverName.toString())
    configureLogHandler(name="apps-clickhistory-handler", addProperty=1, propertyName="supplementalAttributes", propertyValue="CH_CST,CH_CET,CH_VID,CH_WID,CH_CID_EC,CH_CNM,CH_FAM,CH_CMP,CH_RVD,CH_RNM,CH_RTY,CH_TYP,CH_EID,CH_PEI,CH_TTT,CH_RRT,CH_PRT,CH_RNT", target=serverName.toString())
    configureLogHandler(name="apps-clickhistory-handler", path="${domain.home}/servers/${weblogic.Name}/logs/${weblogic.Name}-clickhistory.log", maxFileSize="10485760", maxLogSize="52428800", target=serverName.toString())
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

  clickhistoryUpgrade()
  disconnect()
  exit()
except getopt.error, msg:
  sys.exit()

