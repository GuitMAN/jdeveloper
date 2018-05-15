Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/ReportsServerComponent.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem ReportsServer.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      ReportsServer.sql - <one-line expansion of the name>
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

-- SQL Script for ReportsServerComponent
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$ReportsServerComponent (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) 
);

CREATE OR REPLACE EDITIONING VIEW ReportsServerComponent AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory FROM E$ReportsServerComponent;

CREATE OR REPLACE SYNONYM ReportsServer FOR ReportsServerComponent;

-- INDEX 
CREATE INDEX ReportsServerComponent_Index
ON E$ReportsServerComponent(IAU_TSTZORIGINATING);

CREATE INDEX ReportsServer_Event_Index
ON E$ReportsServerComponent(IAU_ID);

-- PERMISSIONS 
GRANT ALL on ReportsServerComponent to &&1;
GRANT INSERT on ReportsServerComponent to &&2;
GRANT SELECT on ReportsServerComponent to &&2;
GRANT SELECT on ReportsServerComponent to &&3;

-- SYNONYMS
CREATE OR REPLACE SYNONYM &&3..ReportsServerComponent FOR &&1..ReportsServerComponent;
CREATE OR REPLACE SYNONYM &&2..ReportsServerComponent FOR &&1..ReportsServerComponent;
