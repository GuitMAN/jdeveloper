DECLARE 
partitionName VARCHAR2(255) :='soa-infra';
p_document  VARCHAR2(255) :='/deployed-composites/deployed-composites.xml';
--p_document    VARCHAR2 := '/deployed-composites';
BEGIN
     mds_internal_utils.printdocument(partitionName, p_document);
     mds_internal_utils.listContents('soa-infra',  p_document);
END;