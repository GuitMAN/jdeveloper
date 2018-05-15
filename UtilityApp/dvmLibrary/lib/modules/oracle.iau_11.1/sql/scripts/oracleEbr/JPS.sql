Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/JPS.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem JPS.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      JPS.sql - <one-line expansion of the name>
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

-- SQL Script for JPS
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$JPS (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_CodeSource VARCHAR(1024) , 
	IAU_Principals VARCHAR(1024) , 
	IAU_InitiatorGUID VARCHAR(1024) , 
	IAU_Subject VARCHAR(1024) , 
	IAU_PermissionAction VARCHAR(1024) , 
	IAU_PermissionTarget VARCHAR(1024) , 
	IAU_PermissionClass VARCHAR(1024) , 
	IAU_mapName VARCHAR(1024) , 
	IAU_key VARCHAR(1024) , 
	IAU_PermissionScope VARCHAR(1024) , 
	IAU_ApplicationRole VARCHAR(1024) , 
	IAU_EnterpriseRoles VARCHAR(1024) , 
	IAU_InitiatorDN VARCHAR(1024) , 
	IAU_GUID VARCHAR(1024) , 
	IAU_Permission VARCHAR(1024) , 
	IAU_ModifiedAttributeName VARCHAR(1024) , 
	IAU_ModifiedAttributeValue VARCHAR(2048) , 
	IAU_PermissionSetName VARCHAR(1024) , 
	IAU_ResourceActions VARCHAR(1024) , 
	IAU_ResourceType VARCHAR(1024) ,
        IAU_stripeName VARCHAR(1024) ,
        IAU_keystoreName VARCHAR(1024) ,
        IAU_alias VARCHAR(1024) ,
        IAU_operation VARCHAR(1024),
        IAU_PermissionCheckResult VARCHAR(50),
        IAU_RuntimeResource VARCHAR(2048),
        IAU_RuntimeAction VARCHAR(1024),
        IAU_AppContext VARCHAR(2048),
        IAU_Direction VARCHAR(50),
        IAU_AccessResult VARCHAR(1024),
        IAU_ManagedApplication VARCHAR(1024),
        IAU_PolicyName VARCHAR(1024),
        IAU_PolicyDomainName VARCHAR(1024),
        IAU_PolicyCodeSource VARCHAR(1024),
        IAU_PolicyRules VARCHAR(2048),
        IAU_PermSets VARCHAR(2048),
        IAU_Obligations VARCHAR(1024),
        IAU_PolicySemantic VARCHAR(1024),
        IAU_PolicyPrincipalsOld VARCHAR(2048),
        IAU_PolicyCodeSourceOld VARCHAR(1024),
        IAU_PolicyRulesOld VARCHAR(2048),
        IAU_PermSetsOld VARCHAR(2048),
        IAU_ResourceActionsOld VARCHAR(2048),
        IAU_ObligationsOld VARCHAR(1024),
        IAU_Cascade VARCHAR(50),
        IAU_ResName VARCHAR(1024),
        IAU_ResTypeName VARCHAR(1024),
        IAU_ResourceAttributes VARCHAR(1024),
        IAU_ModifiedAttributeValueNew VARCHAR(2048),
        IAU_ModifiedAttributeValueOld VARCHAR(2048),
        IAU_ResourceAttributesOld VARCHAR(1024),
        IAU_RoleMembers VARCHAR(2048),
        IAU_ResourceNames VARCHAR(2048),
        IAU_PolicyPrincipals VARCHAR(2048),
        IAU_AdminRoleName VARCHAR(1024),
        IAU_PolicyAppRolePrincipals VARCHAR(2048),
        IAU_ResourceNameExpressions VARCHAR(2048),
        IAU_PolicyAppRolePrincipalsOld VARCHAR(2048),
        IAU_RoleMembersOld VARCHAR(2048),
        IAU_ResourceNamesOld VARCHAR(2048),
        IAU_ResourceNameExpressionsOld VARCHAR(2048),
        IAU_Flush VARCHAR(1024),
        IAU_PdpAddress VARCHAR(1024),
        IAU_PurgeTime VARCHAR(1024),
        IAU_ConfigurationId VARCHAR(1024)
);

CREATE OR REPLACE EDITIONING VIEW JPS AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_CodeSource,
        IAU_Principals,
        IAU_InitiatorGUID,
        IAU_Subject,
        IAU_PermissionAction,
        IAU_PermissionTarget,
        IAU_PermissionClass,
        IAU_mapName,
        IAU_key,
        IAU_PermissionScope,
        IAU_ApplicationRole,
        IAU_EnterpriseRoles,
        IAU_InitiatorDN,
        IAU_GUID,
        IAU_Permission,
        IAU_ModifiedAttributeName,
        IAU_ModifiedAttributeValue,
        IAU_PermissionSetName,
        IAU_ResourceActions,
        IAU_ResourceType,
        IAU_stripeName,
        IAU_keystoreName,
        IAU_alias,
        IAU_operation,
        IAU_PermissionCheckResult,
        IAU_RuntimeResource,
        IAU_RuntimeAction,
        IAU_AppContext,
        IAU_Direction,
        IAU_AccessResult,
        IAU_ManagedApplication,
        IAU_PolicyName,
        IAU_PolicyDomainName,
        IAU_PolicyCodeSource,
        IAU_PolicyRules,
        IAU_PermSets,
        IAU_Obligations,
        IAU_PolicySemantic,
         IAU_PolicyPrincipalsOld,
        IAU_PolicyCodeSourceOld,
        IAU_PolicyRulesOld,
        IAU_PermSetsOld,
        IAU_ResourceActionsOld,
        IAU_ObligationsOld,
        IAU_Cascade,
        IAU_ResName,
        IAU_ResTypeName,
        IAU_ResourceAttributes,
        IAU_ModifiedAttributeValueNew,
        IAU_ModifiedAttributeValueOld,
        IAU_ResourceAttributesOld,
        IAU_RoleMembers,
        IAU_ResourceNames,
        IAU_PolicyPrincipals,
        IAU_AdminRoleName,
        IAU_PolicyAppRolePrincipals,
        IAU_ResourceNameExpressions,
        IAU_PolicyAppRolePrincipalsOld,
        IAU_RoleMembersOld,
        IAU_ResourceNamesOld,
        IAU_ResourceNameExpressionsOld,
        IAU_Flush,
        IAU_PdpAddress,
        IAU_PurgeTime,
        IAU_ConfigurationId 
FROM E$JPS;

-- INDEX 
CREATE INDEX JPS_Index
ON E$JPS(IAU_TSTZORIGINATING);

CREATE INDEX JPS_Event_Index
ON E$JPS(IAU_ID);

-- PERMISSIONS 
GRANT ALL on JPS to &&1;
GRANT INSERT on JPS to &&2;
GRANT SELECT on JPS to &&2;
GRANT SELECT on JPS to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..JPS FOR &&1..JPS;
CREATE OR REPLACE SYNONYM &&2..JPS FOR &&1..JPS;
