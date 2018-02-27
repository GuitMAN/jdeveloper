package ru.app;


import java.io.File;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.fabric.common.MetadataManager;
import oracle.fabric.common.dvm.DVM;
//import oracle.fabric.common.dvm.DVMManager;
import oracle.fabric.common.xml.xpath.IXPathContext;
import oracle.fabric.common.xml.xpath.IXPathFunction;
import oracle.fabric.common.xml.xpath.XPathFunctionException;

import oracle.tip.dvm.DVMExtFunctions;
import oracle.tip.dvm.entity.DVMRTObject;
import oracle.tip.dvm.DVMManagerImpl;
import oracle.tip.dvm.DVMUtil;
//import oracle.tip.dvm.LookupValue;
//import oracle.tip.dvm.entity.DVMRTObject;
import oracle.tip.dvm.exception.DVMException;

import oracle.tip.dvm.exception.DVMValidationException;
//import oracle.tip.dvm.sdk.util.DVMConstants;

import oracle.xml.parser.v2.XMLDocument;

//import oracle.xml.parser.v2.XMLElement;

//import oracle.xml.parser.v2.XSLException;

//import oracle.integration.platform.blocks.xpath.XPathContext;

import oracle.tip.dvm.sdk.util.XMLUtil;

import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;


//import javax.xml.xpath.XPathFunctionException;

import oracle.integration.platform.blocks.xpath.XPathContext;


public class dvmNeoUtils {

 
    private static final String DVM_DEFAUL_VALUE = UUID.randomUUID().toString();
    
    
    /*
     * Менеджер DVM
     */
    private static final  DVMManagerImpl_new dvmManager = new DVMManagerImpl_new();
    
    /*
     * По умолчанию отключим валидацию
     * true - справочник провалидирован, иначе будем валидировать.
     */
    private static boolean isValidateDVM = true;
    
    
    
    dvmNeoUtils() {
        super();
    }


    /**
     * @return
     */
    public  DVMManagerImpl_new getdvmManager(){
        
        return this.dvmManager;
        }

    public static class DVMManagerImpl_new extends DVMManagerImpl{
        
        private MetadataManager mTestMetadataMgr_new = null;
       
        private DVMRTObject getDVMRTObject(String dvmLoc) throws DVMException
         {   
            
           MetadataManager mdm = null;
           DVMRTObject obj;
           
           try
           {
               if (this.mTestMetadataMgr_new == null)
             {
                    Object ctx;
                    ctx = XPathContext.getXPathContext();
                
                    if (ctx == null)
               {
                 Object[] params = { dvmLoc };
                 throw new DVMValidationException(1571, params, null);
               }
               mdm = XPathContext.getXPathContext().getComposite().getMetadataManager();
               if (mdm == null)
               {
                    
                 Object[] params = { dvmLoc };
                 throw new DVMValidationException(1572, params, null);
               }
             }
             else
             {
               mdm = this.mTestMetadataMgr_new;
             }



             dvmLoc = mdm.resolve(dvmLoc);
            
  
             DVM dvm = null;
             try
             {
               oracle.fabric.common.dvm.DVMManager dvmMgr = mdm.getDVMManager();            
               dvm = dvmMgr.getDVM(dvmLoc);
             }
             catch (Exception e)
             {
                 //если адрес является локальным путем, а не ссылка
                 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                             DocumentBuilder db = dbf.newDocumentBuilder();
                             dbf.setNamespaceAware(true);
                             Document doc = db.parse(new File(dvmLoc));
                             dvm = new DVM(doc, "");
                        //     Object[] params = { dvmLoc, e.getMessage() };
              // mLogger.severe(e.getMessage());
            //   throw new DVMValidationException(1573, params, null);
             }
             obj = (DVMRTObject)dvm.getParsedDVM();
             //отключаем валидацию
             dvm.setIsValid(dvmNeoUtils.isValidateDVM);
             dvm.setIsValidated(dvmNeoUtils.isValidateDVM);
             if (obj == null)
             {
               obj = new DVMRTObject(dvm);
               dvm.setParsedDVM(obj);
             }
           }
           catch (Exception e)
           {
             e.printStackTrace();
             Object[] params = { dvmLoc, e.getMessage() };
             
             throw new DVMValidationException(1572, params, null);
           }
           return obj;
         }
       
    
        public void setMetadataManager(MetadataManager mmg)
        {
            this.mTestMetadataMgr_new = mmg;

        }
        
        public String lookupValue(String dvmLoc, String srcColumnName, String srcValue, String tgtColumnName,
                                  String defaultValue, String... qualifiers) throws DVMException {
            // mLogger.fine("The input parameters for dvm:lookupValue are " + dvmLoc + "," + srcColumnName + "," + srcValue + "," + tgtColumnName + "and qualifiers are ");
            if (qualifiers != null) {
                StringBuilder logq = new StringBuilder();
                for (int i = 0; i < qualifiers.length; i++) {
                    logq.append("," + qualifiers[i]);
                }
                //    mLogger.fine("Qualifiers: " + logq.toString());
            } else {
                //   mLogger.fine("Qualifiers: " + null);
            }
            if (defaultValue == null) {
                defaultValue = "";
            }

            DVMRTObject obj;
            obj = getDVMRTObject(dvmLoc);
            if ((qualifiers != null) && (qualifiers.length % 2 > 0)) {
                String[] newQualifiers = new String[qualifiers.length + 1];
                System.arraycopy(qualifiers, 0, newQualifiers, 0, qualifiers.length);
                newQualifiers[(newQualifiers.length - 1)] = "";
                qualifiers = newQualifiers;
            }
            String lookedUpValue = obj.getValue(srcColumnName, srcValue, tgtColumnName, qualifiers);
            //   mLogger.fine("lookedUpValue : " + lookedUpValue);
            if (lookedUpValue == null) {
                lookedUpValue = defaultValue;
            }
            //  mLogger.fine("The output of dvm:lookupValue is " + lookedUpValue);
            return lookedUpValue;
        }
         
    }     
       




    public static class DVMRTObject_new extends DVMRTObject {
            private String dvmLocation = null;
            private Map<String, Map> columnsToValues = null;
             private String[] rowValues = null;
             private Map<String, DVMColumn> columnNames = null;
             private boolean hasQualifierColumns = false;
             private boolean isQualifersOrdered = false;
             private DVMColumn[] qualifierColumnArray = null;
             private int numberOfQualifierColumns = 0;


        /**
         * @param dvm
         * @throws DVMException
         */
        public DVMRTObject_new (DVM dvm)  throws DVMException
              {
            super(null);
              // XMLDocument xmlDoc = validateDVM(dvm);
            
                XMLDocument dvmDocument = (XMLDocument)dvm.getDVMDocument();
                
                  if (((!dvm.isValidated()) || (!dvm.isValid())))
                      {
                        ArrayList<String> errorParams = new ArrayList<String>();
                        int result = XMLUtil.isDVMDocumentValid(dvmDocument, errorParams);
                        if ((result != 1) && (result != 0)) {
                          throw new DVMException(result, errorParams.toArray(), null);
                        }
                        if (result != 1)
                        {
                          dvm.setIsValid(false);
                          dvm.setIsValidated(true);
                          throw new DVMValidationException(1510, null, null);
                        }
                        dvm.setIsValid(true);
                        dvm.setIsValidated(true);
                      }
                
                  if (dvmDocument == null) {
                        throw new DVMValidationException(1510, null, null);
                      }
                  
                this.dvmLocation = dvm.getDVMURI();
                
                this.columnNames = new HashMap(10, 0.75F);
                
                DVMUtil.fillColumnNames(this, dvmDocument, this.columnNames);
                for (DVMColumn col : this.columnNames.values()) {
                  if (col.isQualifier())
                  {
                    if (!this.hasQualifierColumns) {
                      this.hasQualifierColumns = true;
                    }
                    if ((!this.isQualifersOrdered) && (col.getOrder() != -1)) {
                      this.isQualifersOrdered = true;
                    }
                    if (this.qualifierColumnArray == null) {
                      this.qualifierColumnArray = new DVMColumn[this.columnNames.values().size()];
                    }
                    int position = col.getPosition();
                    this.qualifierColumnArray[(position - 1)] = col;
                    this.numberOfQualifierColumns += 1;
                  }
                }
                this.columnsToValues = new HashMap();
                this.rowValues = DVMUtil.fillRowIds(dvmDocument, this.columnsToValues);
              }
        
        
        }
   

    /*
     * Включение и отключение валидации справчоника
     * true - Отключение валидации
     * false - Включение валидации
    */

    public void setisVailateDVM(boolean flag) {
        /*
         * Инвертируем значение
         * true - дает понять системе, что справочник уже провалидирован
         * false - запустит валидацию справочника
        */
        this.isValidateDVM = !flag;
    }

    public boolean getisVailateDVM() {
        return this.isValidateDVM;
    }



    private static Object checkLookupValue(Object searchValue, ArrayList args) {
        return null;
    }

 

 
   
    /* Возвращает значение из DVM
   / * откорректированная версия из tsc.soa.utils.jar
    */

    public static class lookupValue implements IXPathFunction {

        public Object call(IXPathContext ixPathContext, ArrayList args) throws XPathFunctionException {

            if (args.size() < 5) {
                throw new XPathFunctionException("Incorrect number of arguments (" + args.size() + ").");
            } else {
            }
            args.add(4, DVM_DEFAUL_VALUE);
            DVMExtFunctions.LookupValue dvmValue = new DVMExtFunctions.LookupValue();

            Object searchValue = dvmValue.call(ixPathContext, args);
            Object obj;
            
            obj = dvmNeoUtils.checkLookupValue(searchValue, args);
            return obj;
        }

        public Object call(IXPathContext ixPathContext, List<?> list) {
            return null;
        }
    }
 
  

 
     

        /**
         * Возвращает значение из DVM.
         * @param dvmLoc - путь к файлу DVM
         * @param srcColumnName - колонка источник
         * @param srcValue - значение источника
         * @param tgtColumnName - колонка назначения
         * @param qualifierColumnName - имя колонки квалификатора
         * @param qualifierValue - значение колонки квалификатора
         * @return значение из DVM
         * @throws DVMException
         * @throws XPathFunctionException
         */
        public static String lookupValue(String dvmLoc, String srcColumnName, String srcValue, String tgtColumnName,
                                         String qualifierColumnName, String qualifierValue) throws DVMException,
                                                                                                   XPathFunctionException {
      
        String searchValue = dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                         DVM_DEFAUL_VALUE, qualifierColumnName, qualifierValue);
        return checkLookupValue(searchValue,
                                    Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                                 qualifierColumnName, qualifierValue })).toString();
        }

        /**
         * Возвращает значение из DVM.
         * @param dvmLoc - путь к файлу DVM
         * @param srcColumnName - колонка источник
         * @param srcValue - значение источника
         * @param tgtColumnName - колонка назначения
         * @param qualifierColumnName - имя колонки квалификатора
         * @param qualifierValue - значение колонки квалификатора
         * @param qualifierColumnName1 - имя колонки квалификатора
         * @param qualifierValue1 - значение колонки квалификатора
         * @return значение из DVM
         * @throws DVMException
         * @throws XPathFunctionException
         */
        public static String lookupValue(String dvmLoc, String srcColumnName, String srcValue, String tgtColumnName,
                                          String qualifierColumnName, String qualifierValue,
                                          String qualifierColumnName1, String qualifierValue1) throws DVMException,
            
                                                                                                      XPathFunctionException {
            
            String searchValue = dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                         DVM_DEFAUL_VALUE, qualifierColumnName, qualifierValue,
                                                         qualifierColumnName1, qualifierValue1);
            return checkLookupValue(searchValue,
                                    Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                                 qualifierColumnName, qualifierValue,
                                                                 qualifierColumnName1, qualifierValue1 })).toString();
        }

        /**
         * Возвращает значение из DVM.
         * @param dvmLoc - путь к файлу DVM
         * @param srcColumnName - колонка источник
         * @param srcValue - значение источника
         * @param tgtColumnName - колонка назначения
         * @param qualifierColumnName - имя колонки квалификатора
         * @param qualifierValue - значение колонки квалификатора
         * @param qualifierColumnNameX - имя колонки квалификатора
         * @param qualifierValueX - значение колонки квалификатора
         * @return значение из DVM
         * @throws DVMException
         * @throws XPathFunctionException
         */
        public static String lookupValue(String dvmLoc, String srcColumnName, String srcValue, String tgtColumnName,
                                         String qualifierColumnName, String qualifierValue,
                                         String qualifierColumnName1, String qualifierValue1,
                                         String qualifierColumnName2, String qualifierValue2) throws DVMException,
                                                                                                     XPathFunctionException {
            
            String searchValue = dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                         DVM_DEFAUL_VALUE, qualifierColumnName, qualifierValue,
                                                         qualifierColumnName1, qualifierValue1, qualifierColumnName2,
                                                         qualifierValue2);
            return checkLookupValue(searchValue,
                                    Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                                 qualifierColumnName, qualifierValue,
                                                                 qualifierColumnName1, qualifierValue1,
                                                                 qualifierColumnName2, qualifierValue2 })).toString();
        }

        /**
         * Возвращает значение из DVM.
         * @param dvmLoc - путь к файлу DVM
         * @param srcColumnName - колонка источник
         * @param srcValue - значение источника
         * @param tgtColumnName - колонка назначения
         * @param qualifierColumnName - имя колонки квалификатора
         * @param qualifierValue - значение колонки квалификатора
         * @param qualifierColumnNameX - имя колонки квалификатора
         * @param qualifierValueX - значение колонки квалификатора
         * @return значение из DVM
         * @throws DVMException
         * @throws XPathFunctionException
         */
        public static String lookupValue(String dvmLoc, String srcColumnName, String srcValue, String tgtColumnName,
                                         String qualifierColumnName, String qualifierValue,
                                         String qualifierColumnName1, String qualifierValue1,
                                         String qualifierColumnName2, String qualifierValue2,
                                         String qualifierColumnName3, String qualifierValue3) throws DVMException,
                                                                                                     XPathFunctionException {
          // dvmNeoUtils.DVMManagerImpl_new dvm;
            String searchValue = dvmNeoUtils.dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                         DVM_DEFAUL_VALUE, qualifierColumnName, qualifierValue,
                                                         qualifierColumnName1, qualifierValue1, qualifierColumnName2,
                                                         qualifierValue2, qualifierColumnName3, qualifierValue3);
            return checkLookupValue(searchValue,
                                    Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                                 qualifierColumnName, qualifierValue,
                                                                 qualifierColumnName1, qualifierValue1,
                                                                 qualifierColumnName2, qualifierValue2,
                                                                 qualifierColumnName3, qualifierValue3 })).toString();
        }

        /**
         * Возвращает значение из DVM.
         * @param dvmLoc - путь к файлу DVM
         * @param srcColumnName - колонка источник
         * @param srcValue - значение источника
         * @param tgtColumnName - колонка назначения
         * @param qualifierColumnName - имя колонки квалификатора
         * @param qualifierValue - значение колонки квалификатора
         * @param qualifierColumnNameX - имя колонки квалификатора
         * @param qualifierValueX - значение колонки квалификатора
         * @return значение из DVM
         * @throws DVMException
         * @throws XPathFunctionException
         */
        public static String lookupValue(String dvmLoc, String srcColumnName, String srcValue, String tgtColumnName,
                                         String qualifierColumnName, String qualifierValue,
                                         String qualifierColumnName1, String qualifierValue1,
                                         String qualifierColumnName2, String qualifierValue2,
                                         String qualifierColumnName3, String qualifierValue3,
                                         String qualifierColumnName4, String qualifierValue4) throws DVMException,
                                                                                                     XPathFunctionException {
            String          searchValue = dvmNeoUtils.dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName, DVM_DEFAUL_VALUE,
                                                  qualifierColumnName, qualifierValue, qualifierColumnName1,
                                                  qualifierValue1, qualifierColumnName2, qualifierValue2,
                                                  qualifierColumnName3, qualifierValue3, qualifierColumnName4,
                                                  qualifierValue4);
            return checkLookupValue(searchValue,
                                    Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                                 qualifierColumnName, qualifierValue,
                                                                 qualifierColumnName1, qualifierValue1,
                                                                 qualifierColumnName2, qualifierValue2,
                                                                 qualifierColumnName3, qualifierValue3,
                                                                 qualifierColumnName4, qualifierValue4 })).toString();
        }

    /*    public Object call(IXPathContext ixPathContext, java.util.List<?> list) {
            return null;
        }
*/
        private final static Object checkLookupValue(Object searchValue, List args) throws XPathFunctionException {
            if (searchValue == null || DVM_DEFAUL_VALUE.equals(searchValue)) {
                throw new XPathFunctionException("Value not found in DVM = '" + args.get(0) + "', srcColumnName = '" + args.get(1) + "', srcValue = '" + args.get(2) + "', tgtColumnName = '" + args.get(3) + "'. Args: " + args.toString());
            }

            return searchValue;
        }


}
