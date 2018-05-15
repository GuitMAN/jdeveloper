Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/WebCacheComponent.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem WC.sql
Rem
Rem Copyright (c) 2006, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      WC.sql - Create The WC table
Rem
Rem    DESCRIPTION
Rem      Creates the WC table
Rem
Rem    NOTES
Rem      
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    rkoul       03/03/14 - add index on IAU_ID
Rem    rkoul       06/04/13 - Creation - Edited a non-ebr copy of script
Rem

-- SQL Script for WebCacheComponent
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$WebCacheComponent (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) 
);

CREATE OR REPLACE EDITIONING VIEW WebCacheComponent AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory FROM E$WebCacheComponent;

CREATE OR REPLACE SYNONYM WebCache FOR WebCacheComponent;

-- INDEX 
CREATE INDEX WebCacheComponent_Index
ON E$WebCacheComponent(IAU_TSTZORIGINATING);

CREATE INDEX WebCacheComponent_Event_Index
ON E$WebCacheComponent(IAU_ID);

-- PERMISSIONS 
GRANT ALL on WebCacheComponent to &&1;
GRANT INSERT on WebCacheComponent to &&2;
GRANT SELECT on WebCacheComponent to &&2;
GRANT SELECT on WebCacheComponent to &&3;

-- SYNONYMS
CREATE OR REPLACE SYNONYM &&3..WebCacheComponent FOR &&1..WebCacheComponent;
CREATE OR REPLACE SYNONYM &&2..WebCacheComponent FOR &&1..WebCacheComponent;
