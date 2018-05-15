Rem
Rem creAuditTabs.sql
Rem
Rem Copyright (c) 2006, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      creAuditTabs.sql 
Rem
Rem    DESCRIPTION
Rem      Creates Tables and indexes for the IAU Schema in EBR DB
Rem
Rem    NOTES
Rem      
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    rkoul       04/10/14 - edit ID_SEQ def
Rem    rkoul       03/03/14 - add index on IAU_ID
Rem    sourajai    06/10/13 - Add server name to IAU_BASE
Rem    rkoul       06/04/13 - Creation - Edited a non-ebr copy of script
Rem
Rem TODO Remove ComponentID and make ComponentType NOT NULL

-- &&1 - Audit Admin Role
-- &&2 - Audit Admin Password
-- &&3 - Default TableSpace
-- &&4 - Temporary Table Splace

-- &&5 - Audit Append Role
-- &&6 - Audit Append Password

-- &&7 - Audit Viewer Role
-- &&8 - Audit Viewer Password
-- &&9 - Edition Name

@@../auditUser.sql &&1 &&2 &&3 &&4 &&5 &&6 &&7 &&8

GRANT CREATE TRIGGER to &&1;
GRANT CREATE VIEW TO &&1;

ALTER USER &&1 ENABLE EDITIONS ;
ALTER USER &&5 ENABLE EDITIONS ;
ALTER USER &&7 ENABLE EDITIONS ;
GRANT USE ON EDITION &&9 TO &&1 ;
GRANT USE ON EDITION &&9 TO &&5 ;
GRANT USE ON EDITION &&9 TO &&7 ;


ALTER SESSION SET CURRENT_SCHEMA=&&1;
ALTER SESSION SET EDITION=&&9;

CREATE TABLE E$IAU_BASE (  
        IAU_ID                      NUMBER , 
        IAU_OrgId                   VARCHAR(255) ,
        IAU_ComponentId             VARCHAR(255) , 
        IAU_ComponentType	    VARCHAR(255) ,
	IAU_InstanceId              VARCHAR(255) , 
        IAU_HostingClientId         VARCHAR(255) , 
        IAU_HostId                  VARCHAR(255) , 
        IAU_HostNwaddr              VARCHAR(255) , 
        IAU_ModuleId                VARCHAR(255) , 
        IAU_ProcessId               VARCHAR(255) , 
        IAU_OracleHome              VARCHAR(255) , 
        IAU_HomeInstance            VARCHAR(255) , 
        IAU_UpstreamComponentId     VARCHAR(255) , 
        IAU_DownstreamComponentId   VARCHAR(255) , 
        IAU_ECID                    VARCHAR(255) , 
	IAU_RID			    VARCHAR(255) ,
	IAU_ContextFields	    VARCHAR(2000) ,
        IAU_SessionId               VARCHAR(255) , 
        IAU_SecondarySessionId      VARCHAR(255) , 
        IAU_ApplicationName         VARCHAR(255) , 
	IAU_TargetComponentType     VARCHAR(255) ,
        IAU_EventType               VARCHAR(255) , 
        IAU_EventCategory           VARCHAR(255) , 
        IAU_EventStatus             NUMBER       , 
        IAU_TstzOriginating         TIMESTAMP    , 
        IAU_ThreadId                VARCHAR(255) , 
        IAU_ComponentName           VARCHAR(255) , 
        IAU_Initiator               VARCHAR(255) , 
        IAU_MessageText             VARCHAR(2000) , 
        IAU_FailureCode             VARCHAR(255) , 
        IAU_RemoteIP                VARCHAR(255) , 
        IAU_Target                  VARCHAR(255) , 
        IAU_Resource                VARCHAR(255) , 
        IAU_Roles                   VARCHAR(255) , 
        IAU_AuthenticationMethod    VARCHAR(255) ,
        IAU_TransactionId	    VARCHAR(255) ,
        IAU_DomainName              VARCHAR(255) ,
        IAU_ComponentData           CLOB ,
        IAU_AuditUser               VARCHAR(255) ,
        IAU_TenantId                VARCHAR(255) ,
        IAU_UserTenantId            VARCHAR(255) ,
        IAU_ServerName              VARCHAR(255)
);

CREATE OR REPLACE EDITIONING VIEW IAU_BASE AS
SELECT  IAU_ID                      ,
        IAU_OrgId                   ,
        IAU_ComponentId             ,
        IAU_ComponentType           ,
        IAU_InstanceId              ,
        IAU_HostingClientId         ,
        IAU_HostId                  ,
        IAU_HostNwaddr              ,
        IAU_ModuleId                ,
        IAU_ProcessId               ,
        IAU_OracleHome              ,
        IAU_HomeInstance            ,
        IAU_UpstreamComponentId     ,
        IAU_DownstreamComponentId   ,
        IAU_ECID                    ,
        IAU_RID                     ,
        IAU_ContextFields           ,
        IAU_SessionId               ,
        IAU_SecondarySessionId      ,
        IAU_ApplicationName         ,
        IAU_TargetComponentType     ,
        IAU_EventType               ,
        IAU_EventCategory           ,
        IAU_EventStatus             ,
        IAU_TstzOriginating         ,
        IAU_ThreadId                ,
        IAU_ComponentName           ,
        IAU_Initiator               ,
        IAU_MessageText             ,
        IAU_FailureCode             ,
        IAU_RemoteIP                ,
        IAU_Target                  ,
        IAU_Resource                ,
        IAU_Roles                   ,
        IAU_AuthenticationMethod    ,
        IAU_TransactionId           ,
        IAU_DomainName              ,
        IAU_ComponentData           ,
        IAU_AuditUser               ,
        IAU_TenantId                ,
        IAU_UserTenantId            , 
        IAU_ServerName FROM E$IAU_BASE ; 


-- INDEXES
CREATE INDEX EVENT_TIME_INDEX
ON E$IAU_BASE(IAU_TSTZORIGINATING);   

CREATE INDEX EVENT_INDEX
ON E$IAU_BASE(IAU_ID);   

-- SEQUENCE FOR AN ID FOR THE BASE TABLE
CREATE SEQUENCE ID_SEQ
START WITH 1
INCREMENT BY 120
CACHE 100
NOMAXVALUE;

-- PERMISSIONS 
GRANT ALL    ON IAU_BASE TO &&1;		
GRANT INSERT ON IAU_BASE TO &&5;		
GRANT SELECT ON IAU_BASE TO &&5;
GRANT SELECT ON IAU_BASE TO &&7;
GRANT SELECT ON ID_SEQ TO &&5;

-- SYNONYMS
CREATE or replace SYNONYM &&7..IAU_BASE FOR &&1..IAU_BASE;
CREATE or replace SYNONYM &&5..IAU_BASE FOR &&1..IAU_BASE;

CREATE or replace SYNONYM &&5..ID_SEQ FOR &&1..ID_SEQ;

-- START SCRIPTS TO CREATE ALL THE COMPONENT SPECIFIC TABS AND INDEXES
@@creComponentTabs.sql &&1 &&5 &&7

-- PL/SQL stored procedures/functions
@@../auditschema.pls &&1 &&5 &&7
@@../auditreports.pls &&1 &&5 &&7

-- START SCRIPTS TO CREATE BI TRANSLATION RELATED TABLES 
@@creDispNames.sql &&1 &&5 &&7

-- START SCRIPT TO POPULATE DISPLAY NAME TABLE
@@../disp_names.sql

-- create base tables for dynamic metadat model
@@auditGenericTabs.sql &&1 &&5 &&7

exit

