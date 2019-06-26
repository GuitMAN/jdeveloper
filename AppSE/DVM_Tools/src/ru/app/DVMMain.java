package ru.app;

import java.util.ArrayList;
import java.util.List;

import oracle.tip.dvm.entity.DVMRTObject;
import oracle.fabric.common.dvm.DVM;


public class DVMMain
{

    public static void main(String[] args)
    throws Exception
  {
      
      DVMTools tools = new DVMTools();
      DVMRTObject dvmObj = tools.getDVMRTObject("D:\\Neoflex_MDS\\Master\\oracle_mds\\v1\\src\\apps\\EDM\\VTB24\\Dvm\\cm.AccountingCategory.mappingTable.V1.dvm");
      
    // dvmObj.getRowValues("", "", "")[];

    Dvm dvm = new Dvm();
    dvm.rows = new Dvm.Rows();
    dvm.rows.row = new ArrayList<Dvm.Rows.Row>();
//    row.cell = new ArrayList<String>();
    dvm.rows.row.add(new Dvm.Rows.Row());
    dvm.
  //  dvm.setRows(dvm.rows);
    

  //  dvm.getDVMXRefAsXMLString();
    
    System.out.println(dvm.getName());
      return;                                
    }
                       

}
