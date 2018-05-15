Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/OIDComponent.sql /st_entsec_11.1.1.9.0/1 2014/03/05 08:49:33 rkoul Exp $
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
Rem    rkoul       02/26/14 - add index on IAU_ID
Rem    rkoul       10/16/12 - grant select to append user
Rem    sregmi      07/12/07 - Created
Rem

-- SQL Script for OIDComponent
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE OIDComponent (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_custEventStatusDetail VARCHAR(255) , 
	IAU_custEventOp VARCHAR(255) 
);

CREATE OR REPLACE SYNONYM OID FOR OIDComponent;

-- INDEX 
CREATE INDEX OIDComponent_Index
ON OIDComponent(IAU_TSTZORIGINATING);

CREATE INDEX OIDComponent_Event_Index
ON OIDComponent(IAU_ID);

-- PERMISSIONS 
GRANT ALL on OIDComponent to &&1;
GRANT INSERT on OIDComponent to &&2;
GRANT SELECT on OIDComponent to &&2;
GRANT SELECT on OIDComponent to &&3;

-- SYNONYMS
CREATE OR REPLACE SYNONYM &&3..OIDComponent FOR &&1..OIDComponent;
CREATE OR REPLACE SYNONYM &&2..OIDComponent FOR &&1..OIDComponent;
