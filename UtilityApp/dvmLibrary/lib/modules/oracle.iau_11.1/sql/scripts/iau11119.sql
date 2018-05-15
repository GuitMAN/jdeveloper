Rem
Rem $Header: entsec_ldap/java/src/oracle/security/audit/rcu/scripts/iau11119.sql /st_entsec_11.1.1.9.0/4 2015/02/13 11:13:46 rkoul Exp $
Rem
Rem iau11119.sql
Rem
Rem Copyright (c) 2013, 2015, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      iau11119.sql - <one-line expansion of the name>
Rem
Rem    DESCRIPTION
Rem      <short description of component this file declares/defines>
Rem
Rem    NOTES
Rem      <other useful comments, qualifications, etc.>
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    miqi        02/11/15 - add missing indexes/privs etc for ps5/ps6 to ps7
Rem                           upgrade
Rem    rkoul       04/10/14 - alter ID_SEQ
Rem    rkoul       03/18/14 - IAU_ID index for ps7 upgrade
Rem    palrao      07/10/13 - Created
Rem

ALTER SESSION SET CURRENT_SCHEMA=&&1;

ALTER TABLE IAU_BASE
ADD IAU_ServerName VARCHAR(255);

ALTER TABLE IAU_COMMON
ADD IAU_ServerName VARCHAR(255);

UPDATE IAU_SCHEMA_VERSION SET IAU_VERSION=1.2 WHERE IAU_COMPONENTTYPE='common';

--Bug 20510165: more privileges

GRANT SELECT on IAU_BASE to &&2;
GRANT SELECT on OIDCOMPONENT to &&2;
GRANT SELECT on WS_POLICYATTACHMENT to &&2;
GRANT SELECT on OWSM_PM_EJB to &&2;
GRANT SELECT on XMLPSERVER to &&2;
GRANT SELECT on SOA_HCFP to &&2;
GRANT SELECT on JPS to &&2;
GRANT SELECT on SOA_B2B to &&2;
GRANT SELECT on OVDCOMPONENT to &&2;
GRANT SELECT on WEBSERVICES to &&2;
GRANT SELECT on STS to &&2;
GRANT SELECT on OWSM_AGENT to &&2;
GRANT SELECT on OHSCOMPONENT to &&2;
GRANT SELECT on OAAM to &&2;
GRANT SELECT on WEBCACHECOMPONENT to &&2;
GRANT SELECT on REPORTSSERVERCOMPONENT to &&2;
GRANT SELECT on DIP to &&2;
GRANT SELECT on ADMINSERVER to &&2;
GRANT SELECT on OIF to &&2;
GRANT SELECT on OAM to &&2;

-- Bug 20510165: Create synonyms, these synonsyms missed in ps6 schema upgraded from ps5
DROP SYNONYM &&3..IAU_SCHMEA_VERSION;
CREATE or replace SYNONYM &&3..IAU_SCHEMA_VERSION FOR &&1..IAU_SCHEMA_VERSION;

CREATE or replace SYNONYM &&2..IAU_COMMON FOR &&1..IAU_COMMON;
CREATE or replace SYNONYM &&3..IAU_COMMON FOR &&1..IAU_COMMON;

CREATE or replace SYNONYM &&2..IAU_CUSTOM FOR &&1..IAU_CUSTOM;
CREATE or replace SYNONYM &&3..IAU_CUSTOM FOR &&1..IAU_CUSTOM;

CREATE or replace SYNONYM &&2..IAU_USERSESSION FOR &&1..IAU_USERSESSION;
CREATE or replace SYNONYM &&3..IAU_USERSESSION FOR &&1..IAU_USERSESSION;

CREATE or replace SYNONYM &&2..IAU_AUDITSERVICE FOR &&1..IAU_AUDITSERVICE;
CREATE or replace SYNONYM &&3..IAU_AUDITSERVICE FOR &&1..IAU_AUDITSERVICE;

CREATE or replace SYNONYM &&2..ID_SEQ FOR &&1..ID_SEQ;

ALTER SEQUENCE  ID_SEQ CACHE 100 INCREMENT BY 120;

-- indexes
CREATE INDEX AdminServer_Event_Index ON AdminServer(IAU_ID);
CREATE INDEX DIP_Event_Index ON DIP(IAU_ID);
CREATE INDEX JPS_Event_Index ON JPS(IAU_ID);
CREATE INDEX OAM_Event_Index ON OAM(IAU_ID);
CREATE INDEX OAAM_Event_Index ON OAAM(IAU_ID);
CREATE INDEX OHSComponent_Event_Index ON OHSComponent(IAU_ID);
CREATE INDEX OIDComponent_Event_Index ON OIDComponent(IAU_ID);
CREATE INDEX OIF_Event_Index ON OIF(IAU_ID);
CREATE INDEX OVDComponent_Event_Index ON OVDComponent(IAU_ID);
CREATE INDEX OWSM_AGENT_Event_Index ON OWSM_AGENT(IAU_ID);
CREATE INDEX OWSM_PM_EJB_Event_Index ON OWSM_PM_EJB(IAU_ID);
CREATE INDEX ReportsServer_Event_Index ON ReportsServerComponent(IAU_ID);
CREATE INDEX SOA_B2B_Event_Index ON SOA_B2B(IAU_ID);
CREATE INDEX SOA_HCFP_Event_Index ON SOA_HCFP(IAU_ID);
CREATE INDEX STS_Event_Index ON STS(IAU_ID);
CREATE INDEX WebCacheComponent_Event_Index ON WebCacheComponent(IAU_ID);
CREATE INDEX WebServices_Event_Index ON WebServices(IAU_ID);
CREATE INDEX WS_Policy_Event_Index ON WS_PolicyAttachment(IAU_ID);
CREATE INDEX xmlpserver_Event_Index ON xmlpserver(IAU_ID);

CREATE INDEX EVENT_INDEX ON IAU_BASE(IAU_ID);
CREATE INDEX DYN_IAU_RECORD_INDEX ON IAU_COMMON(IAU_ID);
CREATE INDEX DYN_IAU_RECORD_CUSTOM_INDEX ON IAU_CUSTOM(IAU_ID);
CREATE INDEX DYN_IAU_AUDITSERVICE_INDEX ON IAU_AuditService(IAU_ID);
CREATE INDEX DYN_IAU_RECORD_CUSTOM01_INDEX ON IAU_CUSTOM_01(IAU_ID);
CREATE INDEX DYN_IAU_USERSESSION_INDEX ON IAU_USERSESSION(IAU_ID);

CREATE INDEX DYN_USER_INDEX ON IAU_COMMON(IAU_AuditUser);
CREATE INDEX DYN_USER_TENANT_INDEX ON IAU_COMMON(IAU_UserTenantId);
CREATE INDEX DYN_TENANT_INDEX ON IAU_COMMON(IAU_TenantId);
CREATE INDEX DYN_COMPONENT_TYPE_INDEX ON IAU_COMMON(IAU_ComponentType);
CREATE INDEX DYN_EVENT_CATEGORY_INDEX ON IAU_COMMON(IAU_EventCategory);
CREATE INDEX DYN_EVENT_TYPE_INDEX ON IAU_COMMON(IAU_EventType);

begin
execute immediate 'REVOKE INSERT on IAU_SCHEMA_VERSION FROM &&2';
exception when others then null;
end;
/
GRANT SELECT on IAU_SCHEMA_VERSION to &&2;

COMMIT;
