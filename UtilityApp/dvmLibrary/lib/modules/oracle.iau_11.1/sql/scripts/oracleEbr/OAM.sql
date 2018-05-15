Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/OAM.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem OAM.sql
Rem
Rem Copyright (c) 2009, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      OAM.sql - <one-line expansion of the name>
Rem
Rem    DESCRIPTION
Rem      <short description of component this file declares/defines>
Rem
Rem    NOTES
Rem      <other useful comments, qualifications, etc.>
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    rkoul       03/03/14 - add index on IAU_ID
Rem    rkoul       06/04/13 - Creation - Edited a non-ebr copy of script
Rem

-- SQL Script for OAM
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$OAM (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_ApplicationDomainName VARCHAR(255) , 
	IAU_AuthenticationSchemeID VARCHAR(255) , 
	IAU_AgentID VARCHAR(255) , 
	IAU_SSOSessionID VARCHAR(255) , 
	IAU_AdditionalInfo CLOB, 
	IAU_AuthorizationScheme VARCHAR(255) , 
	IAU_UserDN VARCHAR(255) , 
	IAU_ResourceID VARCHAR(255) , 
	IAU_AuthorizationPolicyID VARCHAR(255) , 
	IAU_AuthenticationPolicyID VARCHAR(255) , 
	IAU_UserID VARCHAR(255) , 
	IAU_ResourceHost VARCHAR(255) , 
	IAU_RequestID VARCHAR(255) , 
	IAU_PolicyName VARCHAR(255) , 
	IAU_SchemeName VARCHAR(255) , 
	IAU_ResourceHostName VARCHAR(255) , 
	IAU_OldAttributes VARCHAR(4000) , 
	IAU_NewAttributes VARCHAR(4000) , 
        IAU_SchmeType VARCHAR(40) ,
	IAU_ResponseType VARCHAR(255) , 
	IAU_AgentType VARCHAR(255) , 
	IAU_ConstraintType VARCHAR(255) , 
	IAU_InstanceName VARCHAR(255) , 
	IAU_DataSourceName VARCHAR(255) , 
	IAU_DataSourceType VARCHAR(255) , 
	IAU_HostIdentifierName VARCHAR(255) , 
	IAU_ResourceURI VARCHAR(1024) , 
        IAU_ResourceTemplateName VARCHAR(255),
	IAU_Impersonator VARCHAR(255),
	IAU_OldSettings CLOB,
	IAU_NewSettings CLOB,
	IAU_ResourceType VARCHAR(255),
	IAU_PolicyObjectID VARCHAR(255),
	IAU_ReadOnly VARCHAR(255),
	IAU_PolicyAdminContext CLOB,
	IAU_PolicyType VARCHAR(255),
	IAU_ProtectionLevel VARCHAR(255),
	IAU_ServiceURI VARCHAR(1024),
	IAU_ServiceIdentifier VARCHAR(255),
	IAU_ServiceOperation VARCHAR(255),
	IAU_AdminRoleName VARCHAR(255),
	IAU_ClientIPAddress VARCHAR(255),
	IAU_SessionCreationTime VARCHAR(255),
	IAU_SessionExpirationTime VARCHAR(255),
	IAU_SessionLastUpdateTime VARCHAR(255),
	IAU_SessionLastAccessTime VARCHAR(255),
	IAU_IdentityDomain VARCHAR(255),
	IAU_GenericAttribute1 VARCHAR(255),
	IAU_GenericAttribute2 VARCHAR(255),
	IAU_GenericAttribute3 VARCHAR(255),
	IAU_GenericAttribute4 VARCHAR(255),
	IAU_GenericAttribute5 VARCHAR(255),
	IAU_ResourceOperations VARCHAR(255)
);

CREATE OR REPLACE EDITIONING VIEW OAM AS
SELECT  IAU_ID  ,
        IAU_TstzOriginating  ,
        IAU_EventType  ,
        IAU_EventCategory  ,
        IAU_ApplicationDomainName  ,
        IAU_AuthenticationSchemeID  ,
        IAU_AgentID  ,
        IAU_SSOSessionID  ,
        IAU_AdditionalInfo ,
        IAU_AuthorizationScheme  ,
        IAU_UserDN  ,
        IAU_ResourceID  ,
        IAU_AuthorizationPolicyID  ,
        IAU_AuthenticationPolicyID  ,
        IAU_UserID  ,
        IAU_ResourceHost  ,
        IAU_RequestID  ,
        IAU_PolicyName  ,
        IAU_SchemeName  ,
        IAU_ResourceHostName  ,
        IAU_OldAttributes  ,
        IAU_NewAttributes  ,
        IAU_SchmeType  ,
        IAU_ResponseType  ,
        IAU_AgentType  ,
        IAU_ConstraintType  ,
        IAU_InstanceName  ,
        IAU_DataSourceName  ,
        IAU_DataSourceType  ,
        IAU_HostIdentifierName  ,
        IAU_ResourceURI  ,
        IAU_ResourceTemplateName ,
        IAU_Impersonator ,
        IAU_OldSettings ,
        IAU_NewSettings ,
        IAU_ResourceType ,
        IAU_PolicyObjectID ,
        IAU_ReadOnly ,
        IAU_PolicyAdminContext ,
        IAU_PolicyType ,
        IAU_ProtectionLevel ,
        IAU_ServiceURI ,
        IAU_ServiceIdentifier ,
        IAU_ServiceOperation ,
        IAU_AdminRoleName ,
        IAU_ClientIPAddress ,
        IAU_SessionCreationTime ,
        IAU_SessionExpirationTime ,
        IAU_SessionLastUpdateTime ,
        IAU_SessionLastAccessTime ,
        IAU_IdentityDomain ,
        IAU_GenericAttribute1 ,
        IAU_GenericAttribute2 ,
        IAU_GenericAttribute3 ,
        IAU_GenericAttribute4 ,
        IAU_GenericAttribute5 ,
        IAU_ResourceOperations FROM E$OAM;

-- INDEX 
CREATE INDEX OAM_Index
ON E$OAM(IAU_TSTZORIGINATING);

CREATE INDEX OAM_Event_Index
ON E$OAM(IAU_ID);

-- PERMISSIONS 
GRANT ALL on OAM to &&1;
GRANT INSERT on OAM to &&2;
GRANT SELECT on OAM to &&2;
GRANT SELECT on OAM to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..OAM FOR &&1..OAM;
CREATE OR REPLACE SYNONYM &&2..OAM FOR &&1..OAM;
