Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/OWSM-AGENT.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem OWSM_AGENT.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      OWSM_AGENT.sql - <one-line expansion of the name>
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

-- SQL Script for OWSM_AGENT
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$OWSM_AGENT (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_AppName VARCHAR(255) , 
	IAU_AssertionName VARCHAR(255) , 
	IAU_CompositeName VARCHAR(255) , 
	IAU_Endpoint VARCHAR(4000) , 
	IAU_AgentMode VARCHAR(255) , 
	IAU_ModelObjectName VARCHAR(255) , 
	IAU_Operation VARCHAR(255) , 
	IAU_ProcessingStage VARCHAR(255) , 
	IAU_Version NUMBER , 
	IAU_Protocol VARCHAR(255) 
);

CREATE OR REPLACE EDITIONING VIEW OWSM_AGENT AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_AppName,
        IAU_AssertionName,
        IAU_CompositeName,
        IAU_Endpoint,
        IAU_AgentMode,
        IAU_ModelObjectName,
        IAU_Operation,
        IAU_ProcessingStage,
        IAU_Version ,
        IAU_Protocol FROM E$OWSM_AGENT;

-- INDEX 
CREATE INDEX OWSM_AGENT_Index
ON E$OWSM_AGENT(IAU_TSTZORIGINATING);

CREATE INDEX OWSM_AGENT_Event_Index
ON E$OWSM_AGENT(IAU_ID);

-- PERMISSIONS 
GRANT ALL on OWSM_AGENT to &&1;
GRANT INSERT on OWSM_AGENT to &&2;
GRANT SELECT on OWSM_AGENT to &&2;
GRANT SELECT on OWSM_AGENT to &&3;

-- SYNONYMS
CREATE OR REPLACE SYNONYM &&3..OWSM_AGENT FOR &&1..OWSM_AGENT;
CREATE OR REPLACE SYNONYM &&2..OWSM_AGENT FOR &&1..OWSM_AGENT;
