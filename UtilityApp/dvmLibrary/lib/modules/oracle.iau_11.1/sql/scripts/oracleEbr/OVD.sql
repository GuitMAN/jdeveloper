Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/OVD.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem OVD.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      OVD.sql - <one-line expansion of the name>
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

-- Sql Script for OVDComponent
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$OVDComponent (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_ServiceOperation VARCHAR(255) 
);

CREATE OR REPLACE EDITIONING VIEW OVDComponent AS
SELECT IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_ServiceOperation FROM E$OVDComponent;

CREATE OR REPLACE SYNONYM OVD FOR OVDComponent;

-- INDEX 
CREATE INDEX OVDComponent_Index
ON E$OVDComponent(IAU_TSTZORIGINATING);

CREATE INDEX OVDComponent_Event_Index
ON E$OVDComponent(IAU_ID);

-- PERMISSIONS 
GRANT ALL on OVDComponent to &&1;
GRANT INSERT on OVDComponent to &&2;
GRANT SELECT on OVDComponent to &&2;
GRANT SELECT on OVDComponent to &&3;

-- SYNONYMS
CREATE OR REPLACE SYNONYM &&3..OVDComponent FOR &&1..OVDComponent;
CREATE OR REPLACE SYNONYM &&2..OVDComponent FOR &&1..OVDComponent;
