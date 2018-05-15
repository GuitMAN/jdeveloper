Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/OIDComponent.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem OID.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      OID.sql - <one-line expansion of the name>
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

-- SQL Script for OIDComponent
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$OIDComponent (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_custEventStatusDetail VARCHAR(255) , 
	IAU_custEventOp VARCHAR(255) 
);

CREATE OR REPLACE EDITIONING VIEW OIDComponent AS
SELECT  IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_custEventStatusDetail,
        IAU_custEventOp FROM E$OIDComponent;

CREATE OR REPLACE SYNONYM OID FOR OIDComponent;

-- INDEX 
CREATE INDEX OIDComponent_Index
ON E$OIDComponent(IAU_TSTZORIGINATING);

CREATE INDEX OIDComponent_Event_Index
ON E$OIDComponent(IAU_ID);

-- PERMISSIONS 
GRANT ALL on OIDComponent to &&1;
GRANT INSERT on OIDComponent to &&2;
GRANT SELECT on OIDComponent to &&2;
GRANT SELECT on OIDComponent to &&3;

-- SYNONYMS
CREATE OR REPLACE SYNONYM &&3..OIDComponent FOR &&1..OIDComponent;
CREATE OR REPLACE SYNONYM &&2..OIDComponent FOR &&1..OIDComponent;
