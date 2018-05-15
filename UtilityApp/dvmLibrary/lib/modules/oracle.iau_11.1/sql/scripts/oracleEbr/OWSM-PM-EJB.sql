Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/OWSM-PM-EJB.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem OWSM_PM_EJB.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      OWSM_PM_EJB.sql - <one-line expansion of the name>
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

-- SQL Script for OWSM_PM_EJB
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$OWSM_PM_EJB (  
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_Version NUMBER , 
	IAU_ToVersion NUMBER 
);
   

CREATE OR REPLACE EDITIONING VIEW OWSM_PM_EJB AS
SELECT IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_Version,
        IAU_ToVersion FROM E$OWSM_PM_EJB;

-- INDEX 
CREATE INDEX OWSM_PM_EJB_Index
ON E$OWSM_PM_EJB(IAU_TSTZORIGINATING);

CREATE INDEX OWSM_PM_EJB_Event_Index
ON E$OWSM_PM_EJB(IAU_ID);

-- PERMISSIONS 
GRANT ALL on OWSM_PM_EJB to &&1;
GRANT INSERT on OWSM_PM_EJB to &&2;
GRANT SELECT on OWSM_PM_EJB to &&2;
GRANT SELECT on OWSM_PM_EJB to &&3;

-- SYNONYMS
CREATE OR REPLACE SYNONYM &&3..OWSM_PM_EJB FOR &&1..OWSM_PM_EJB;
CREATE OR REPLACE SYNONYM &&2..OWSM_PM_EJB FOR &&1..OWSM_PM_EJB;
