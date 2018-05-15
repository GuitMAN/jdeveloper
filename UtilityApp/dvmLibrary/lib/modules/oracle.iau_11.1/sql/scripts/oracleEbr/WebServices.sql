Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/WebServices.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem WebServices.sql
Rem
Rem Copyright (c) 2008, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      WebServices.sql - <one-line expansion of the name>
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

-- SQL Script for WebServices
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$WebServices (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_Protocol VARCHAR(255) , 
	IAU_Endpoint VARCHAR(4000) , 
	IAU_Operation VARCHAR(255) , 
	IAU_FaultUri VARCHAR(4000) , 
	IAU_URI VARCHAR(4000) , 
	IAU_Source VARCHAR(255) 
);

CREATE OR REPLACE EDITIONING VIEW WebServices AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_Protocol,
        IAU_Endpoint,
        IAU_Operation ,
        IAU_FaultUri ,
        IAU_URI ,
        IAU_Source FROM E$WebServices;

-- INDEX 
CREATE INDEX WebServices_Index
ON E$WebServices(IAU_TSTZORIGINATING);

CREATE INDEX WebServices_Event_Index
ON E$WebServices(IAU_ID);

-- PERMISSIONS 
GRANT ALL on WebServices to &&1;
GRANT INSERT on WebServices to &&2;
GRANT SELECT on WebServices to &&2;
GRANT SELECT on WebServices to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..WebServices FOR &&1..WebServices;
CREATE OR REPLACE SYNONYM &&2..WebServices FOR &&1..WebServices;
