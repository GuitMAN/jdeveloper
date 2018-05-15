Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/oracleEbr/STS.sql /st_entsec_11.1.1.9.0/2 2014/03/05 08:49:33 rkoul Exp $
Rem
Rem STS.sql
Rem
Rem Copyright (c) 2010, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      STS.sql - <one-line expansion of the name>
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

CREATE TABLE E$STS (
        IAU_ID NUMBER ,
        IAU_TstzOriginating TIMESTAMP ,
        IAU_EventType VARCHAR(255) ,
        IAU_EventCategory VARCHAR(255),
        IAU_Requester VARCHAR(255),
	IAU_RelyingParty VARCHAR(255),
	IAU_UserID VARCHAR(255),
	IAU_TokenType VARCHAR(100),
	IAU_Token CLOB,
	IAU_TokenContext CLOB,
	IAU_Message CLOB,
	IAU_OperationType VARCHAR(255),
	IAU_OperationData CLOB,
	IAU_OldSettings CLOB,
	IAU_NewSettings CLOB,
	IAU_TemplateID VARCHAR(100),
	IAU_ProfileID VARCHAR(100),
	IAU_PartnerID VARCHAR(100),
	IAU_SettingsID VARCHAR(400)
);

CREATE OR REPLACE EDITIONING VIEW STS AS
SELECT  IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_Requester,
        IAU_RelyingParty,
        IAU_UserID,
        IAU_TokenType,
        IAU_Token,
        IAU_TokenContext,
        IAU_Message,
        IAU_OperationType,
        IAU_OperationData,
        IAU_OldSettings,
        IAU_NewSettings,
        IAU_TemplateID,
        IAU_ProfileID,
        IAU_PartnerID,
        IAU_SettingsID FROM E$STS;


-- INDEX 
CREATE INDEX STS_Index
ON E$STS(IAU_TSTZORIGINATING);

CREATE INDEX STS_Event_Index
ON E$STS(IAU_ID);

-- PERMISSIONS 
GRANT ALL on STS to &&1;
GRANT INSERT on STS to &&2;
GRANT SELECT on STS to &&2;
GRANT SELECT on STS to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..STS FOR &&1..STS;
CREATE OR REPLACE SYNONYM &&2..STS FOR &&1..STS;
