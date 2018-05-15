Rem
Rem
Rem AdminServer.sql
Rem
Rem Copyright (c) 2007, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      AdminServer.sql - <one-line expansion of the name>
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

-- SQL Script for AdminServer
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role
CREATE TABLE E$AdminServer (
	IAU_ID NUMBER,
	IAU_TstzOriginating TIMESTAMP, 
	IAU_EventType VARCHAR(255), 
	IAU_EventCategory VARCHAR(255), 
	IAU_DeploymentOperation VARCHAR(255),
	IAU_DeploymentStatus VARCHAR(255),
	IAU_DeploymentApplicationId VARCHAR(255),
	IAU_DeploymentTarget VARCHAR(255)
);

CREATE OR REPLACE EDITIONING VIEW AdminServer AS
SELECT
        IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_DeploymentOperation,
        IAU_DeploymentStatus,
        IAU_DeploymentApplicationId,
        IAU_DeploymentTarget FROM E$AdminServer;

-- INDEX
CREATE INDEX AdminServer_Index
ON E$AdminServer(IAU_TSTZORIGINATING);

CREATE INDEX AdminServer_Event_Index
ON E$AdminServer(IAU_ID);

GRANT ALL on AdminServer to &&1;
GRANT INSERT on AdminServer to &&2;
GRANT SELECT on AdminServer to &&2;
GRANT SELECT on AdminServer to &&3;

-- SYNONYMS
CREATE or replace SYNONYM &&3..AdminServer FOR &&1..AdminServer;
CREATE or replace SYNONYM &&2..AdminServer FOR &&1..AdminServer;
