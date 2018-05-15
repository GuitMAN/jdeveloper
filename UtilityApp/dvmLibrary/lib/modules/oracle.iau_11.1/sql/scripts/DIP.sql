Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/DIP.sql /st_entsec_11.1.1.9.0/1 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem DIP.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      DIP.sql - <one-line expansion of the name>
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

-- SQL Script for DIP
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE DIP (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_AssociateProfileName VARCHAR(512) , 
	IAU_ProfileName VARCHAR(512) , 
	IAU_EntryDN VARCHAR(1024) , 
	IAU_ProvEvent VARCHAR(2048) , 
	IAU_JobName VARCHAR(128) , 
	IAU_JobType VARCHAR(128) 
);

-- INDEX 
CREATE INDEX DIP_Index
ON DIP(IAU_TSTZORIGINATING);

CREATE INDEX DIP_Event_Index
ON DIP(IAU_ID);

-- PERMISSIONS 
GRANT ALL on DIP to &&1;
GRANT INSERT on DIP to &&2;
GRANT SELECT on DIP to &&2;
GRANT SELECT on DIP to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..DIP FOR &&1..DIP;
CREATE OR REPLACE SYNONYM &&2..DIP FOR &&1..DIP;
