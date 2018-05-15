Rem
Rem    rkoul       06/04/13 - Creation - Edited a non-ebr copy of script
Rem

-- SQL Script for xmlpserver
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$xmlpserver (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_Format VARCHAR(30) , 
	IAU_Template VARCHAR(255) , 
	IAU_Locale VARCHAR(10) , 
	IAU_JobId VARCHAR(255) , 
	IAU_IsScheduled NUMBER , 
	IAU_OutputId VARCHAR(255) , 
	IAU_UserJobName VARCHAR(100) , 
	IAU_UserJobDescription VARCHAR(255) , 
	IAU_StartDate TIMESTAMP , 
	IAU_EndDate TIMESTAMP , 
	IAU_Bursting NUMBER , 
	IAU_JobGroup VARCHAR(255) , 
	IAU_RunType VARCHAR(255) , 
	IAU_OutputInfo CLOB , 
	IAU_DeliveryInfo CLOB , 
	IAU_RepublishId VARCHAR(255) , 
	IAU_FreeMemory NUMBER , 
	IAU_TotalMemory NUMBER , 
	IAU_DataSize NUMBER , 
	IAU_ProcessTime NUMBER , 
	IAU_OutputName VARCHAR(255) , 
	IAU_DeliveryMethod VARCHAR(255) , 
	IAU_DeliveryProperties CLOB 
);


CREATE OR REPLACE EDITIONING VIEW xmlpserver AS
SELECT  IAU_ID,
        IAU_TstzOriginating,
        IAU_EventType,
        IAU_EventCategory,
        IAU_Format,
        IAU_Template,
        IAU_Locale,
        IAU_JobId,
        IAU_IsScheduled,
        IAU_OutputId,
        IAU_UserJobName,
        IAU_UserJobDescription,
        IAU_StartDate,
        IAU_EndDate,
        IAU_Bursting,
        IAU_JobGroup,
        IAU_RunType,
        IAU_OutputInfo,
        IAU_DeliveryInfo,
        IAU_RepublishId,
        IAU_FreeMemory,
        IAU_TotalMemory,
        IAU_DataSize,
        IAU_ProcessTime,
        IAU_OutputName,
        IAU_DeliveryMethod,
        IAU_DeliveryProperties
FROM E$xmlpserver;

-- INDEX 
CREATE INDEX xmlpserver_Index
ON E$xmlpserver(IAU_TSTZORIGINATING);

CREATE INDEX xmlpserver_Event_Index
ON E$xmlpserver(IAU_ID);

-- PERMISSIONS 
GRANT ALL on xmlpserver to &&1;
GRANT INSERT on xmlpserver to &&2;
GRANT SELECT on xmlpserver to &&2;
GRANT SELECT on xmlpserver to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..xmlpserver FOR &&1..xmlpserver;
CREATE OR REPLACE SYNONYM &&2..xmlpserver FOR &&1..xmlpserver;
