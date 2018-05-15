Rem
Rem $Header: jdevadf/modules/adfm-business-editor/etc/apps/Taskflows/Model/src/EnsureADFExtensibleTableAndColumnUsagesProcedure.sql /st_jdevadf_patchset_ias/1 2014/05/02 13:09:58 spathak Exp $
Rem
Rem EnsureADFExtensibleColumnUsagesProcedure.sql
Rem
Rem Copyright (c) 2013, Oracle.  All rights reserved.
Rem
Rem    NAME
Rem
Rem      EnsureADFExtensibleTableAndColumnUsagesProcedure.sql 
Rem      Ensure ADF Extensible Table and Column Usages Procedure
Rem
Rem    DESCRIPTION
Rem
Rem      This procedure can be used to fix missing ADF table usage
Rem      and/or column allocation rows in the ADF Business Editor
Rem      runtime dictionary.
Rem
Rem    NOTES
Rem
Rem      Call this procedure to ensure that the ADF Business Editor
Rem      extensible column usage table reflects that all of the
Rem      extensible column names passed in are marked as allocated for
Rem      the entity object whose fully-qualified definition name is
Rem      passed in.
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    smuench     12/01/13 - Created
Rem
Rem    PARAMETERS
Rem
Rem      p_entitydef_fullname           - Required fully-qualified
Rem                                       entity definition name
Rem                                       (case-sensitive)
Rem
Rem      p_table_name                   - Optional table name used
Rem                                       in case table usage is 
Rem                                       missing. (case-sensitive)
Rem
Rem      p_table_name                   - Optional table name used
Rem                                       in case table usage is 
Rem                                       missing. (case-sensitive)
Rem
Rem      p_table_name                   - Optional table name used
Rem                                       in case table usage is 
Rem                                       missing. (case-sensitive)
Rem
Rem      p_extensible_column_list       - Optional comma-separated list
Rem                                       of extensible column names
Rem                                       (case-sensitive)
Rem
Rem    EXAMPLES
Rem
Rem      To list the currently allocated columns for an entity, enable
Rem      server output if using SQL*Plus, and omit the
Rem      p_extensible_column_list parameter:
Rem
Rem      SQL> set linesize 200
Rem      SQL> set serveroutput on
Rem      SQL> exec ensure_adf_ext_tab_col_usages('example.jedi.model.Dept')
Rem
Rem      To ensure that one or more registered extensible columns are
Rem      allocated, including a table usage if it is missing, provide 
Rem      values for all parameters and include the list of case-sensitive
Rem      column names in the p_extensible_column_list parameter
Rem      as a comma-separated list:
Rem
Rem      SQL> set linesize 200
Rem      SQL> set serveroutput on
Rem      SQL> begin
Rem        2>   ensure_adf_ext_tab_col_usages(
Rem        3>     p_entitydef_fullname     => 'example.jedi.model.Dept',
Rem        4>     p_custom_object          => FALSE,
Rem        5>     p_table_name             => 'JEDI_DEPT',
Rem        6>     p_auto_number            => FALSE,
Rem        7>     p_extensible_column_list => 
Rem        8>               'EXT_NUMBER_017,EXT_CHAR_002,EXT_DATE_007');
Rem        9> end;
Rem        10> /
Rem
Rem      If the table usage is already registered, then no new table
Rem      registration is performed. If the column name is already
Rem      registered, then no new allocation is required. If at least
Rem      one of the column names in the list provided required a new
Rem      column allocation row to be created, then the procedure will
Rem      generate a list of allocated columns to the DBMS OUTPUT
Rem      buffer. For example:
Rem
Rem      ======
Rem      BEFORE: entity example.jedi.model.Dept has extensible columns:
Rem      ======
Rem      JEDI_DEPT.EXT_CHAR_001
Rem      JEDI_DEPT.EXT_CHAR_002
Rem      =====
Rem      AFTER: entity example.jedi.model.Dept now has extensible columns:
Rem      =====
Rem      JEDI_DEPT.EXT_CHAR_001
Rem      JEDI_DEPT.EXT_CHAR_002
Rem      JEDI_DEPT.EXT_DATE_007
Rem      JEDI_DEPT.EXT_NUMBER_017
Rem

SET ECHO ON
SET FEEDBACK 1
SET NUMWIDTH 10
SET LINESIZE 80
SET TRIMSPOOL ON
SET TAB OFF
SET PAGESIZE 100

CREATE OR REPLACE PROCEDURE ENSURE_ADF_EXT_TAB_COL_USAGES(
    p_entitydef_fullname     VARCHAR2,
    p_custom_object          BOOLEAN  := FALSE,
    p_table_name             VARCHAR2 := NULL,
    p_auto_number            BOOLEAN  := FALSE,
    p_extensible_column_list VARCHAR2 := NULL)
AS
  l_allocated_some      BOOLEAN := FALSE;
  l_table_usage_id      NUMBER;
  l_table_id            NUMBER;
  l_table_name          VARCHAR2(30);
  l_ext_col_id          NUMBER;
  l_ext_col_usage_id    NUMBER;
  l_next_id             NUMBER;
  l_auto_number_nextval INTEGER;
  l_sandbox_text        VARCHAR2(255);
  l_context_col_value   VARCHAR2(255);
PROCEDURE show_allocated_columns
IS
BEGIN
  FOR extension_column_usage IN
  (SELECT ADF_EXTENSIBLE_TABLE_USAGE.ENTITYDEF_FULLNAME AS entity_definition_name,
    ADF_EXTENSIBLE_TABLE.TABLE_NAME                     AS allocated_table_name,
    ADF_EXTENSION_COLUMN.COLUMN_NAME                    AS allocated_ext_column_name,
    ADF_EXTENSIBLE_TABLE_USAGE.SANDBOX_NAME             AS sandbox_name
  FROM ADF_EXTENSION_COLUMN
  INNER JOIN ADF_EXTENSIBLE_TABLE
  ON ADF_EXTENSIBLE_TABLE.TABLE_ID = ADF_EXTENSION_COLUMN.TABLE_ID
  INNER JOIN ADF_EXTENSIBLE_TABLE_USAGE
  ON ADF_EXTENSIBLE_TABLE.TABLE_ID = ADF_EXTENSIBLE_TABLE_USAGE.TABLE_ID
  INNER JOIN ADF_EXTENSION_COLUMN_USAGE
  ON ADF_EXTENSION_COLUMN.COLUMN_ID                 = ADF_EXTENSION_COLUMN_USAGE.COLUMN_ID
  AND ADF_EXTENSIBLE_TABLE_USAGE.USAGE_ID           = ADF_EXTENSION_COLUMN_USAGE.TABLE_USAGE_ID
  AND ADF_EXTENSIBLE_TABLE_USAGE.ENTITYDEF_FULLNAME = p_entitydef_fullname
  ORDER BY entity_definition_name,
    allocated_table_name,
    allocated_ext_column_name
  )
  LOOP
    dbms_output.put_line(extension_column_usage.allocated_table_name||'.'||extension_column_usage.allocated_ext_column_name);
  END LOOP;
END;
BEGIN
  BEGIN
    -- Get the extensible table usage id by entitydef fullname
    SELECT tu.usage_id,
      tu.table_id,
      et.table_name
    INTO l_table_usage_id,
      l_table_id,
      l_table_name
    FROM adf_extensible_table_usage tu,
      adf_extensible_table et
    WHERE tu.entitydef_fullname = p_entitydef_fullname
    AND et.table_id             = tu.table_id;
  EXCEPTION
  WHEN NO_DATA_FOUND THEN
    dbms_output.put_line('WARNING: No table usage exists for entity '||p_entitydef_fullname);
    -- At this point, one of two things can be the case
    -- (1) The table for the entity is registered as extensible, but has not had its
    --     table usage created in this database yet, or
    -- (2) The table passed in for the entity is not registered as extensible
    BEGIN
      SELECT table_id
      INTO l_table_id
      FROM adf_extensible_table
      WHERE table_name = p_table_name;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
      -- If we get here we are in case (2) above
      IF (p_table_name IS NULL) THEN
        dbms_output.put_line('IGNORING: No extensible table name provided.');
      ELSE
        dbms_output.put_line('IGNORING: No extensible table exists named '||p_table_name);
      END IF;
      RETURN;
    END;
    -- If we get here we are in case (1) above, so insert the missing adf_extensible_table_usage
    -- row.
    dbms_output.put_line('ADDING: Table usage row for entity '||p_entitydef_fullname);
    IF p_custom_object THEN
      l_context_col_value := SUBSTR(p_entitydef_fullname,instr(p_entitydef_fullname,'.',-1)+1);
      IF p_auto_number THEN
        l_auto_number_nextval := 1;
      END IF;
    END IF;
    SELECT next_id INTO l_table_usage_id FROM S_ROW_ID FOR UPDATE nowait;
    INSERT
    INTO adf_extensible_table_usage
      (
        usage_id,
        entitydef_fullname,
        context_column_value,
        table_id,
        sandbox_name,
        auto_number_sequence_nextval
      )
      VALUES
      (
        l_table_usage_id,
        p_entitydef_fullname,
        l_context_col_value,
        l_table_id,
        NULL,
        l_auto_number_nextval
      );
    -- Bump the S_ROW_ID counter
    UPDATE s_row_id SET next_id = next_id + 1;
  END;
  dbms_output.put_line('======');
  dbms_output.put_line('BEFORE: entity '||p_entitydef_fullname||' has extensible columns:');
  dbms_output.put_line('======');
  show_allocated_columns();
  IF (p_extensible_column_list IS NOT NULL AND LENGTH(trim(p_extensible_column_list)) > 0) THEN
    -- Lock the S_ROW_ID table since we might be needing some ids
    SELECT next_id
    INTO l_next_id
    FROM S_ROW_ID FOR UPDATE nowait;
    -- Process the list of columns
    FOR extension_column IN
    (SELECT TRIM(REPLACE(REPLACE(NAME,CHR(10),NULL),CHR(13),NULL)) AS NAME
    FROM
      (SELECT REGEXP_SUBSTR (p_extensible_column_list, '[^,]+', 1, LEVEL) NAME
      FROM DUAL
        CONNECT BY REGEXP_SUBSTR (p_extensible_column_list, '[^,]+', 1, LEVEL) IS NOT NULL
      )
    )
    LOOP
      -- Get the extension_column id for the table related to this table usage
      BEGIN
        SELECT column_id
        INTO l_ext_col_id
        FROM adf_extension_column
        WHERE table_id  = l_table_id
        AND column_name = extension_column.name;
      EXCEPTION
      WHEN NO_DATA_FOUND THEN
        dbms_output.put_line('IGNORING: No extensible column named '''||extension_column.name||''' is registered for table '''||l_table_name||'''');
        CONTINUE;
      END;
      -- See if the column id is already marked as allocated for this table id
      BEGIN
        SELECT usage_id
        INTO l_ext_col_usage_id
        FROM adf_extension_column_usage
        WHERE table_usage_id = l_table_usage_id
        AND column_id        = l_ext_col_id;
      EXCEPTION
      WHEN NO_DATA_FOUND THEN
        dbms_output.put_line('WARNING: No column allocation row exists for '''||extension_column.name||''' for entity '||p_entitydef_fullname);
        dbms_output.put_line('ADDING: Column allocation row for '''||extension_column.name||''' for entity '||p_entitydef_fullname);
        -- Did not find an existing column allocation row, so insert one
        INSERT
        INTO ADF_EXTENSION_COLUMN_USAGE
          (
            usage_id,
            table_usage_id,
            column_id,
            sandbox_name
          )
          VALUES
          (
            l_next_id,
            l_table_usage_id,
            l_ext_col_id,
            NULL
          );
        -- Bump the S_ROW_ID counter
        l_next_id := l_next_id + 1;
        UPDATE s_row_id SET next_id = l_next_id;
        l_allocated_some := TRUE;
      END;
    END LOOP;
    -- this commit will release the lock on the S_ROW_ID table
    COMMIT;
    dbms_output.put_line('=====');
    IF (l_allocated_some) THEN
      dbms_output.put_line('AFTER: entity '||p_entitydef_fullname||' now has extensible columns allocated:');
      dbms_output.put_line('=====');
      show_allocated_columns();
    ELSE
      dbms_output.put_line('AFTER: entity '||p_entitydef_fullname||' has the same list of allocated columns');
      dbms_output.put_line('=====');
    END IF;
  END IF;
END ENSURE_ADF_EXT_TAB_COL_USAGES;
/
