Rem $Header: bc4jrt/modules/adf-model/bin/adfbc_tune_statesnapshottables.sql /st_jdevadf_pt-11.1.1.9.0/1 2014/09/22 20:09:10 sekorupo Exp $
Rem Copyright (c) 2008, 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      adfbc_tune_statesnapshottables.sql - Tune the ADFbc snapshot tables for better performance
Rem
Rem    DESCRIPTION
Rem      This SQL script will modify the ADFbc snapshot table to use
Rem      SECUREFILES for the LOB column. Also sets the LOB attributes CACHE, 
Rem      NOLOGGING, COMPRESS. It also partitions the table and PK index.
Rem
Rem      Given the high concurrency of DMLs to the snapshot tables, it is 
Rem      recommended to execute this script ONLY during downtime period 
Rem      when there is NO user activity.
Rem
Rem    COMMAND
Rem      
Rem      @adfbc_tune_statesnapshottables.sql PS_TXN 
Rem
Rem      PS_TXN          - The txn table stores snapshots of pending changes
Rem                        made to BC4J application module instances. The snapshot information
Rem                        is stored as an XML document that encodes the unposted changes in
Rem                        an application module instance. Only pending data changes are
Rem                        stored in the snapshot, along with information about the current
Rem                        state of active iterators (i.e. "current row" pointers information).
Rem                        The value of the COLLID column corresponds to the value returned
Rem                        by the ApplicationModule.passivateState() method.
Rem
Rem
Rem    NOTES
Rem
Rem      This script assumes that you already ran the adfbc_create_statesnapshottables.sql
Rem      at some point of time and you want to tune the tables for high concurrent use.
Rem      The script requires that you are running a 10gR2 Oracle database or above.
Rem      It creates an alternate table with the same column definitions and migrates
Rem      the data created in the old table to the new one.
Rem
Rem      Please see the following whitepaper for more information about
Rem      the database object required by BC4J:
Rem
Rem      http://otn.oracle.com/products/jdev/htdocs/bc4j/bc4j_temp_tables.html
Rem
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    sekorupo 7/18/2012    Created
Rem
Rem    conn fusion/fusion
Rem dbdrv: sql ~PROD ~PATH ~FILE none none none sqlplus &phase=tbm \
Rem dbdrv: checkfile:~PROD:~PATH:~FILE
Rem For making autopatch security complaint, files with .sql running in
Rem sqlplus , sqlplus_single mode require a connect.
Rem first 2 param will be internally passed by autopatch as fusion credential
Rem All other parameters to be offset by 2

Rem connect &&1/&&2

SET ECHO OFF
SET FEEDBACK 1
SET NUMWIDTH 10
SET LINESIZE 80
SET TRIMSPOOL ON
SET TAB OFF
SET PAGESIZE 100

SET VERIFY OFF

SET SERVEROUTPUT ON SIZE UNLIMITED FORMAT WRAPPED;

WHENEVER SQLERROR EXIT FAILURE ROLLBACK;
WHENEVER OSERROR EXIT FAILURE ROLLBACK;

define adfbc_txn_tab_name = &1 ;
define adfbc_pk_suffix = _PK ;

define adfbc_txn_temp_tab_suffix = _TEMP ;
define adfbc_temp_pk_suffix = _PARTITION_PK ;

Rem Check if snapshot table already has recommended optimizations.

declare
  l_num_errors pls_integer;
begin

  for c1 in(select 1
            from dba_lobs
            where owner = sys_context('USERENV', 'CURRENT_USER') and 
            table_name='&adfbc_txn_tab_name' and  securefile='YES' and compression !='NONE' and
            logging = 'NO' and partitioned ='YES' and cache ='YES') 
  loop
    dbms_output.put_line('&adfbc_txn_tab_name table already has recommended optimizations.');
    return;
  end loop;

  /* Create the interim table that will be used by online redefinition of the table */
  execute immediate 'create table &adfbc_txn_tab_name&adfbc_txn_temp_tab_suffix(
                     id number(20) NOT NULL
                     , parentid number(20)
                     , collid number(10)
                     , content blob
                     , creation_date date DEFAULT sysdate
                     , constraint &adfbc_txn_tab_name&adfbc_temp_pk_suffix primary key (collid, id) using index global partition by hash(collid) partitions 64 enable)
                     segment creation immediate
                     lob (content) store as securefile(enable storage in row chunk 8192 retention compress cache nologging)
                     partition by hash(collid, id) partitions 128';

  /* Start redefining the table */
  DBMS_REDEFINITION.START_REDEF_TABLE(
    uname => sys_context('USERENV', 'CURRENT_USER'),
    orig_table => '&adfbc_txn_tab_name',
    int_table => '&adfbc_txn_tab_name&adfbc_txn_temp_tab_suffix',
    col_mapping => NULL,
    options_flag => 1,
    orderby_cols => NULL,
    part_name => NULL
  );

  /* Copy table dependents viz, grants, statistics */
  DBMS_REDEFINITION.COPY_TABLE_DEPENDENTS(
    uname => sys_context('USERENV', 'CURRENT_USER'),
    orig_table => '&adfbc_txn_tab_name',
    int_table => '&adfbc_txn_tab_name&adfbc_txn_temp_tab_suffix',
    copy_statistics => TRUE,
    num_errors => l_num_errors,
    copy_indexes => 0,
    copy_constraints => false,
    copy_privileges => true
  );

  /* Sync the interim table with the original table. */
  DBMS_REDEFINITION.SYNC_INTERIM_TABLE(sys_context('USERENV', 'CURRENT_USER'), '&adfbc_txn_tab_name', '&adfbc_txn_tab_name&adfbc_txn_temp_tab_suffix');

  /* Finish redefining the table */
  DBMS_REDEFINITION.FINISH_REDEF_TABLE(sys_context('USERENV', 'CURRENT_USER'), '&adfbc_txn_tab_name', '&adfbc_txn_tab_name&adfbc_txn_temp_tab_suffix');

  execute immediate 'drop table &adfbc_txn_tab_name&adfbc_txn_temp_tab_suffix';
end;

/

commit; 

exit;
