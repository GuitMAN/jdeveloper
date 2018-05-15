Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/SOA-B2B.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem SOA_B2B.sql
Rem
Rem Copyright (c) 2011, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      SOA_B2B.sql - <one-line expansion of the name>
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
Rem

CREATE TABLE E$SOA_B2B (
        IAU_ID NUMBER ,
        IAU_TstzOriginating TIMESTAMP ,
        IAU_EventType VARCHAR(255) ,
        IAU_EventCategory VARCHAR(255) ,
        IAU_Reason VARCHAR(255)
);

CREATE OR REPLACE EDITIONING VIEW SOA_B2B AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_Reason
FROM E$SOA_B2B;


-- INDEX 
CREATE INDEX SOA_B2B_Index
ON E$SOA_B2B(IAU_TSTZORIGINATING);

CREATE INDEX SOA_B2B_Event_Index
ON E$SOA_B2B(IAU_ID);

-- PERMISSIONS 
GRANT ALL on SOA_B2B to &&1;
GRANT INSERT on SOA_B2B to &&2;
GRANT SELECT on SOA_B2B to &&2;
GRANT SELECT on SOA_B2B to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..SOA_B2B FOR &&1..SOA_B2B;
CREATE OR REPLACE SYNONYM &&2..SOA_B2B FOR &&1..SOA_B2B;
