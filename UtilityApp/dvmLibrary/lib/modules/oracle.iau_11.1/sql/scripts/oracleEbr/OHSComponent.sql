Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/OHSComponent.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem OHS.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      OHS.sql - <one-line expansion of the name>
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

-- SQL Script for OHSComponent
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$OHSComponent (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_Reason CLOB , 
	IAU_SSLConnection VARCHAR(255) , 
	IAU_AuthorizationType VARCHAR(255) 
);

CREATE OR REPLACE EDITIONING VIEW OHSComponent AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_Reason CLOB,
        IAU_SSLConnection,
        IAU_AuthorizationType FROM E$OHSComponent;

CREATE OR REPLACE SYNONYM OHS FOR OHSComponent;

-- INDEX 
CREATE INDEX OHSComponent_Index
ON E$OHSComponent(IAU_TSTZORIGINATING);

CREATE INDEX OHSComponent_Event_Index
ON E$OHSComponent(IAU_ID);

-- PERMISSIONS 
GRANT ALL on OHSComponent to &&1;
GRANT INSERT on OHSComponent to &&2;
GRANT SELECT on OHSComponent to &&2;
GRANT SELECT on OHSComponent to &&3;

-- SYNONYMS
CREATE OR REPLACE SYNONYM &&3..OHSComponent FOR &&1..OHSComponent;
CREATE OR REPLACE SYNONYM &&2..OHSComponent FOR &&1..OHSComponent;
