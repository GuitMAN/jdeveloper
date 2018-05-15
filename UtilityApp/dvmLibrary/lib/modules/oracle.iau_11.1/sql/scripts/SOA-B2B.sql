Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/SOA-B2B.sql /st_entsec_11.1.1.9.0/1 2014/03/05 08:49:33 rkoul Exp $
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
Rem    rkoul       02/26/14 - add index on IAU_ID
Rem    rkoul       10/16/12 - grant select to append user
Rem    skalyana    06/21/11 - Created
Rem

CREATE TABLE SOA_B2B (
        IAU_ID NUMBER ,
        IAU_TstzOriginating TIMESTAMP ,
        IAU_EventType VARCHAR(255) ,
        IAU_EventCategory VARCHAR(255) ,
        IAU_Reason VARCHAR(255)
);

-- INDEX 
CREATE INDEX SOA_B2B_Index
ON SOA_B2B(IAU_TSTZORIGINATING);

CREATE INDEX SOA_B2B_Event_Index
ON SOA_B2B(IAU_ID);

-- PERMISSIONS 
GRANT ALL on SOA_B2B to &&1;
GRANT INSERT on SOA_B2B to &&2;
GRANT SELECT on SOA_B2B to &&2;
GRANT SELECT on SOA_B2B to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..SOA_B2B FOR &&1..SOA_B2B;
CREATE OR REPLACE SYNONYM &&2..SOA_B2B FOR &&1..SOA_B2B;
