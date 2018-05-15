Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/WS-PolicyAttachment.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem WS-PolicyAttachment.sql
Rem
Rem Copyright (c) 2008, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      WS-PolicyAttachment.sql - <one-line expansion of the name>
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

-- SQL Script for WS_PolicyAttachment
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$WS_PolicyAttachment (
        IAU_ID NUMBER ,
        IAU_TstzOriginating TIMESTAMP ,
        IAU_EventType VARCHAR(255) ,
        IAU_EventCategory VARCHAR(255) ,
        IAU_PolicyChangeType VARCHAR(255) ,
        IAU_PolicyURI VARCHAR(4000) ,
        IAU_PolicyCategory VARCHAR(255) ,
        IAU_PolicyStatus VARCHAR(255) ,
        IAU_ServiceEndPoint VARCHAR(4000) ,
        IAU_PolicySubjRescPattern VARCHAR(4000)
);

CREATE OR REPLACE EDITIONING VIEW WS_PolicyAttachment AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_PolicyChangeType,
        IAU_PolicyURI,
        IAU_PolicyCategory,
        IAU_PolicyStatus,
        IAU_ServiceEndPoint,
        IAU_PolicySubjRescPattern FROM E$WS_PolicyAttachment;


-- INDEX 
CREATE INDEX WS_PolicyAttachment_Index
ON E$WS_PolicyAttachment(IAU_TSTZORIGINATING);

CREATE INDEX WS_Policy_Event_Index
ON E$WS_PolicyAttachment(IAU_ID);

-- PERMISSIONS 
GRANT ALL on WS_PolicyAttachment to &&1;
GRANT INSERT on WS_PolicyAttachment to &&2;
GRANT SELECT on WS_PolicyAttachment to &&2;
GRANT SELECT on WS_PolicyAttachment to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..WS_PolicyAttachment FOR &&1..WS_PolicyAttachment;
CREATE OR REPLACE SYNONYM &&2..WS_PolicyAttachment FOR &&1..WS_PolicyAttachment;
