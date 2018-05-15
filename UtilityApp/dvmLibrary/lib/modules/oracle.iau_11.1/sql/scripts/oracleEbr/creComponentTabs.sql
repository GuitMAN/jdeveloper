Rem
Rem creComponentTabs.sql
Rem
Rem Copyright (c) 2006, 2013, Oracle and/or its affiliates. 
Rem All rights reserved. 
Rem
Rem    NAME
Rem     creComponentTabs.sql
Rem
Rem    DESCRIPTION
Rem      Creates Component Specific Tables
Rem
Rem    NOTES
Rem
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    rkoul       06/04/13 - Creation - Edited a non-ebr copy of script
Rem
Rem
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

-- Call all of the supplied sql files that contains 
-- code to create all the event specific tables and
-- indexes


@@WebCacheComponent.sql &&1 &&2 &&3 
@@OVD.sql &&1 &&2 &&3
@@OIDComponent.sql &&1 &&2 &&3
@@OWSM-PM-EJB.sql &&1 &&2 &&3
@@OWSM-AGENT.sql &&1 &&2 &&3
@@DIP.sql &&1 &&2 &&3
@@OHSComponent.sql &&1 &&2 &&3
@@JPS.sql &&1 &&2 &&3
@@AdminServer.sql &&1 &&2 &&3
@@ReportsServerComponent.sql &&1 &&2 &&3
@@WebServices.sql &&1 &&2 &&3
@@WS-PolicyAttachment.sql &&1 &&2 &&3
@@OIF.sql &&1 &&2 &&3
@@OAAM.sql &&1 &&2 &&3
@@OAM.sql &&1 &&2 &&3
@@STS.sql &&1 &&2 &&3
@@SOA-B2B.sql &&1 &&2 &&3
@@SOA-HCFP.sql &&1 &&2 &&3
@@XMLPSERVER.sql &&1 &&2 &&3
