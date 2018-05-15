Rem
Rem    rkoul       06/04/13 - Creation - Edited a non-ebr copy of script
Rem

-- SQL Script for OAAM
-- &&1 - Audit Admin Role
-- &&2 - Audit Append Role
-- &&3 - Audit Viewer Role

CREATE TABLE E$OAAM (
	IAU_ID NUMBER , 
	IAU_TstzOriginating TIMESTAMP , 
	IAU_EventType VARCHAR(255) , 
	IAU_EventCategory VARCHAR(255) , 
	IAU_ActionNotes VARCHAR(4000) , 
	IAU_CaseActionEnum VARCHAR(4000) , 
	IAU_CaseActionResult NUMBER ,
	IAU_CaseChallengeQuestion VARCHAR(4000) , 
	IAU_CaseChallengeResult NUMBER , 
	IAU_CaseDisposition VARCHAR(4000) , 
	IAU_CaseExprDurationInHrs INTEGER , 
	IAU_CaseId NUMBER ,
	IAU_CaseIds VARCHAR(4000) , 
	IAU_CaseSeverity VARCHAR(4000) , 
	IAU_CaseStatus VARCHAR(4000) , 
	IAU_CaseSubActionEnum INTEGER , 
	IAU_Description VARCHAR(4000) , 
	IAU_GroupId NUMBER ,
	IAU_GroupIds VARCHAR(4000) , 
	IAU_GroupName VARCHAR(4000) , 
	IAU_GroupDetails VARCHAR(4000) , 
	IAU_GroupElementId NUMBER , 
	IAU_GroupElementIds VARCHAR(4000) , 
	IAU_GroupElementValue VARCHAR(4000) , 
	IAU_GroupElementsDetails VARCHAR(4000) , 
	IAU_KBACategoryId NUMBER , 
	IAU_KBACategoryIds VARCHAR(4000) , 
	IAU_KBACategoryName VARCHAR(4000) , 
	IAU_KBACategoryDetails VARCHAR(4000) , 
	IAU_KBAQuestionId NUMBER , 
	IAU_KBAQuestionIds VARCHAR(4000) , 
	IAU_KBAQuestion VARCHAR(4000) , 
	IAU_KBAQuestionType INTEGER , 
	IAU_KBAQuestionDetails VARCHAR(4000) , 
	IAU_KBAValidationId NUMBER , 
	IAU_KBAValidationIds VARCHAR(4000) , 
	IAU_KBAValidationName VARCHAR(4000) , 
	IAU_KBAValidationDetails VARCHAR(4000) , 
	IAU_KBARegLogicDetails VARCHAR(4000) , 
	IAU_KBAAnswerLogicDetails VARCHAR(4000) , 
	IAU_LoginId VARCHAR(255) , 
	IAU_PolicyDetails VARCHAR(4000) , 
	IAU_PolicyId NUMBER , 
	IAU_PolicyIds VARCHAR(4000) , 
	IAU_PolicyName VARCHAR(4000), 
	IAU_PolicyOverrideDetails VARCHAR(4000) , 
	IAU_PolicyOverrideId NUMBER , 
	IAU_PolicyOverrideIds VARCHAR(4000) , 
	IAU_PolicyOverrideRowId NUMBER , 
	IAU_PolicyRuleMapId NUMBER , 
	IAU_PolicyRuleMapIds VARCHAR(4000) , 
	IAU_PolicyRuleMapDetails VARCHAR(4000) , 
	IAU_RuleId NUMBER , 
	IAU_RuleConditionId NUMBER , 
	IAU_RuleConditionIds VARCHAR(4000) , 
	IAU_RuleName VARCHAR(4000) , 
	IAU_RuleDetails VARCHAR(4000) , 
	IAU_RuleConditionMapId NUMBER , 
	IAU_RuleConditionMapIds VARCHAR(4000) , 
	IAU_RuleParamValueDetails VARCHAR(4000) , 
	IAU_SourcePolicyId NUMBER , 
	IAU_UserGroupName VARCHAR(255) , 
	IAU_UserId NUMBER , 
	IAU_UserIds VARCHAR(4000),
        IAU_CaseType INTEGER,
        IAU_SessionIds VARCHAR(4000),
        IAU_JobId NUMBER,
        IAU_JobInstanceIds VARCHAR(4000),
        IAU_JobIds VARCHAR(4000),
        IAU_JobName VARCHAR(4000),
        IAU_JobType INTEGER,
        IAU_ScheduleType INTEGER,
        IAU_Result NUMBER,
        IAU_CustomJobType VARCHAR(4000),
        IAU_CustomJobTypeId NUMBER,
        IAU_PatternDetails VARCHAR(4000),
        IAU_PatternIds VARCHAR(4000),
        IAU_PatternStatus VARCHAR(4000),
        IAU_PatternId NUMBER,
        IAU_PatternParam VARCHAR(4000),
        IAU_PatternParamIds VARCHAR(4000),
        IAU_TransactionDef VARCHAR(4000),
        IAU_TransactionDefIds VARCHAR(4000),
        IAU_TransactionDefStatus INTEGER,
        IAU_TransactionEntityDefMap VARCHAR(4000),
        IAU_TransactionEntityDefMapIds VARCHAR(4000),
        IAU_TransactionDefId NUMBER,
        IAU_DataElementDefArray VARCHAR(4000),
        IAU_DataElementDefIds VARCHAR(4000),
        IAU_TransactionDataMapping VARCHAR(4000),
        IAU_TransactionDataMappingIds VARCHAR(4000),
        IAU_TransactionEntityMapId NUMBER,
        IAU_SnapshotData VARCHAR(4000),
        IAU_SnapshotDiffTree VARCHAR(4000),
        IAU_IsDeleteSnapshotItems NUMBER, 
        IAU_SnapshotIds VARCHAR(4000),
        IAU_ImpExpPropertiesArgs VARCHAR(4000),
        IAU_DynamicAction VARCHAR(4000),
        IAU_DynamicActionIds VARCHAR(4000),
        IAU_DynamicActionInstance VARCHAR(4000),
        IAU_DynamicActionInstanceIds VARCHAR(4000),
        IAU_DynamicActionInstStatus INTEGER,
        IAU_PolicySet VARCHAR(4000),
        IAU_PolicySetId NUMBER,
        IAU_ScoreActions VARCHAR(4000),
        IAU_ActionOverrides VARCHAR(4000),
        IAU_ScoreActionIds VARCHAR(4000),
        IAU_ActionOverrideBlockIds VARCHAR(4000),
        IAU_PropertyName VARCHAR(4000),
        IAU_PropertyValue VARCHAR(4000),
        IAU_PropertyNames VARCHAR(4000),
        IAU_EntityDef VARCHAR(4000),
        IAU_EntityDefIds VARCHAR(4000),
        IAU_EntityDefStatus INTEGER,
        IAU_EntityDefId NUMBER,
        IAU_IdSchemeElemDefIds VARCHAR(4000),
        IAU_KeyGenScheme INTEGER,
        IAU_DisplayElemDefIds VARCHAR(4000),
        IAU_EntityDefsMap VARCHAR(4000),
        IAU_RecentLoginsSearchQuery VARCHAR(4000),
        IAU_RequestId VARCHAR(4000),
        IAU_TrxLogId NUMBER,
        IAU_RuntimeType INTEGER,
        IAU_PolicySetLogId NUMBER,
        IAU_EntityDefsMapIds VARCHAR(4000)
);

CREATE OR REPLACE EDITIONING VIEW OAAM AS
SELECT  IAU_ID  ,
        IAU_TstzOriginating,
        IAU_EventType  ,
        IAU_EventCategory  ,
        IAU_ActionNotes  ,
        IAU_CaseActionEnum  ,
        IAU_CaseActionResult  ,
        IAU_CaseChallengeQuestion  ,
        IAU_CaseChallengeResult  ,
        IAU_CaseDisposition  ,
        IAU_CaseExprDurationInHrs,
        IAU_CaseId  ,
        IAU_CaseIds  ,
        IAU_CaseSeverity  ,
        IAU_CaseStatus  ,
        IAU_CaseSubActionEnum ,
        IAU_Description  ,
        IAU_GroupId  ,
        IAU_GroupIds  ,
        IAU_GroupName  ,
        IAU_GroupDetails  ,
        IAU_GroupElementId  ,
        IAU_GroupElementIds  ,
        IAU_GroupElementValue  ,
        IAU_GroupElementsDetails  ,
        IAU_KBACategoryId  ,
        IAU_KBACategoryIds  ,
        IAU_KBACategoryName  ,
        IAU_KBACategoryDetails  ,
        IAU_KBAQuestionId  ,
        IAU_KBAQuestionIds  ,
        IAU_KBAQuestion  ,
        IAU_KBAQuestionType,
        IAU_KBAQuestionDetails  ,
        IAU_KBAValidationId  ,
        IAU_KBAValidationIds  ,
        IAU_KBAValidationName  ,
        IAU_KBAValidationDetails  ,
        IAU_KBARegLogicDetails  ,
        IAU_KBAAnswerLogicDetails  ,
        IAU_LoginId  ,
        IAU_PolicyDetails  ,
        IAU_PolicyId  ,
         IAU_PolicyIds  ,
        IAU_PolicyName ,
        IAU_PolicyOverrideDetails  ,
        IAU_PolicyOverrideId  ,
        IAU_PolicyOverrideIds  ,
        IAU_PolicyOverrideRowId  ,
        IAU_PolicyRuleMapId  ,
        IAU_PolicyRuleMapIds  ,
        IAU_PolicyRuleMapDetails  ,
        IAU_RuleId  ,
        IAU_RuleConditionId  ,
        IAU_RuleConditionIds  ,
        IAU_RuleName  ,
        IAU_RuleDetails  ,
        IAU_RuleConditionMapId  ,
        IAU_RuleConditionMapIds  ,
        IAU_RuleParamValueDetails  ,
        IAU_SourcePolicyId  ,
        IAU_UserGroupName  ,
        IAU_UserId  ,
        IAU_UserIds ,
        IAU_CaseType,
        IAU_SessionIds ,
        IAU_JobId ,
        IAU_JobInstanceIds ,
        IAU_JobIds ,
        IAU_JobName ,
        IAU_JobType,
        IAU_ScheduleType,
        IAU_Result,
        IAU_CustomJobType ,
        IAU_CustomJobTypeId ,
        IAU_PatternDetails ,
        IAU_PatternIds ,
        IAU_PatternStatus ,
        IAU_PatternId ,
        IAU_PatternParam ,
        IAU_PatternParamIds ,
        IAU_TransactionDef ,
        IAU_TransactionDefIds ,
        IAU_TransactionDefStatus,
        IAU_TransactionEntityDefMap ,
        IAU_TransactionEntityDefMapIds ,
        IAU_TransactionDefId ,
        IAU_DataElementDefArray ,
        IAU_DataElementDefIds ,
        IAU_TransactionDataMapping ,
        IAU_TransactionDataMappingIds ,
        IAU_TransactionEntityMapId ,
        IAU_SnapshotData ,
        IAU_SnapshotDiffTree ,
        IAU_IsDeleteSnapshotItems ,
        IAU_SnapshotIds ,
        IAU_ImpExpPropertiesArgs ,
        IAU_DynamicAction ,
        IAU_DynamicActionIds ,
        IAU_DynamicActionInstance ,
        IAU_DynamicActionInstanceIds ,
        IAU_DynamicActionInstStatus,
        IAU_PolicySet ,
        IAU_PolicySetId ,
        IAU_ScoreActions ,
        IAU_ActionOverrides ,
        IAU_ScoreActionIds ,
        IAU_ActionOverrideBlockIds ,
        IAU_PropertyName ,
        IAU_PropertyValue ,
        IAU_PropertyNames ,
        IAU_EntityDef ,
        IAU_EntityDefIds ,
        IAU_EntityDefStatus,
        IAU_EntityDefId ,
        IAU_IdSchemeElemDefIds ,
        IAU_KeyGenScheme,
        IAU_DisplayElemDefIds ,
        IAU_EntityDefsMap ,
        IAU_RecentLoginsSearchQuery ,
        IAU_RequestId ,
        IAU_TrxLogId ,
        IAU_RuntimeType,
        IAU_PolicySetLogId ,
        IAU_EntityDefsMapIds FROM E$OAAM;


-- INDEX 
CREATE INDEX OAAM_Index
ON E$OAAM(IAU_TSTZORIGINATING);

CREATE INDEX OAAM_Event_Index
ON E$OAAM(IAU_ID);

-- PERMISSIONS 
GRANT ALL on OAAM to &&1;
GRANT INSERT on OAAM to &&2;
GRANT SELECT on OAAM to &&2;
GRANT SELECT on OAAM to &&3;

-- SYNONYMS 
CREATE OR REPLACE SYNONYM &&3..OAAM FOR &&1..OAAM;
CREATE OR REPLACE SYNONYM &&2..OAAM FOR &&1..OAAM;
