package ru.app;

import org.apache.xmlbeans.XmlObject;




public class DVMClient {


     public static void main(String[] args) {
        try {
            /**
            *http://<host>:<managedServerPort>/soa-infra/services/<partition-name>/<compositeProjectName>/<dvm-name>.dvm
            */
            String dvmLoc = "D:\\NeoFlex_MDS\\Master\\oracle_mds\\v1\\src\\apps\\EDM\\VTB24\\Dvm\\cm.Currency.mappingTable.V2.dvm";

            String srcColumnName = "localObjectKey";
            String srcColumnVal = "810";
            String destColumnName = "cmObjectKey";
            String targetColumnName = "system";
            
            dvmNeoUtils m = new dvmNeoUtils();
            System.out.println("Отключаем валидацию");
            m.setisVailateDVM(false);
            String lookupVal;
            long startTime = 0;
            long timeSpent ;
            
            startTime = System.currentTimeMillis();
            System.out.println("Начало вывоза lookupValue... ");
           
           // lookupVal = m.lookupValue(dvmLoc, srcColumnName, srcColumnVal, destColumnName, "не найдено", targetColumnName,"cm.System.BQ","")
            lookupVal = m.lookupValue(dvmLoc, srcColumnName,  srcColumnVal, destColumnName,   targetColumnName, "cm.System.DWH") ;
            timeSpent = System.currentTimeMillis() - startTime;
            System.out.println("Затраченное время: " + timeSpent + " миллисекунд");
            System.out.println("Response lookupValue :->" + lookupVal);
                
            //Вызываем  справочник повторно         
            
            srcColumnVal = "PLN";
            System.out.println("Включаем валидацию и ждем пока провалидируется справочник");
            System.out.println("Начало вывоза lookupValue... ");
            m.setisVailateDVM(true);
            
            startTime = System.currentTimeMillis();
            lookupVal = m.lookupValue(dvmLoc, srcColumnName,  srcColumnVal, destColumnName,   targetColumnName, "cm.System.AC") ;            timeSpent = System.currentTimeMillis() - startTime;
            System.out.println("Затраченное время: " + timeSpent + " миллисекунд");
            System.out.println("Response lookupValue :->" + lookupVal);
            
            
            m.setisVailateDVM(false);
            System.out.println("Снова отключаем валидацию и ждем пока провалидируется справочник");
            
            startTime = System.currentTimeMillis();
            lookupVal = m.lookupValue(dvmLoc, srcColumnName,  srcColumnVal, destColumnName,   targetColumnName, "cm.System.AC") ;            timeSpent = System.currentTimeMillis() - startTime;
            System.out.println("Затраченное время: " + timeSpent + " миллисекунд");
            System.out.println("Response lookupValue :->" + lookupVal);
            
            
           
            
            startTime = System.currentTimeMillis();
            lookupVal = m.lookupValue(dvmLoc, srcColumnName,  srcColumnVal, destColumnName,   targetColumnName, "cm.System.AC") ;            timeSpent = System.currentTimeMillis() - startTime;
            System.out.println("Затраченное время: " + timeSpent + " миллисекунд");
            System.out.println("Response lookupValue :->" + lookupVal);
       
        } catch (Exception e) {
            System.out.println("!!!!");
            e.printStackTrace();
        }
    }

}
