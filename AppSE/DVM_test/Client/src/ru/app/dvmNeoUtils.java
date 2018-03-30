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
import oracle.fabric.common.dvm.DVMManager;
import oracle.fabric.common.xml.xpath.IXPathContext;
import oracle.fabric.common.xml.xpath.IXPathFunction;
import oracle.fabric.common.xml.xpath.XPathFunctionException;

import oracle.tip.dvm.DVMExtFunctions;
import oracle.tip.dvm.entity.DVMRTObject;
import oracle.tip.dvm.DVMManagerImpl;
import oracle.tip.dvm.DVMUtil;

import oracle.tip.dvm.entity.DVMRTObject;
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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class dvmNeoUtils {


    private static final String DVM_DEFAUL_VALUE = UUID.randomUUID().toString();
    private static final String CLASS_NAME = dvmNeoUtils.class.getName();

    /*
     * Менеджер DVM
     */
    private static final DVMManagerImpl_new dvmManager = new DVMManagerImpl_new();

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
    public DVMManagerImpl_new getdvmManager() {

        return this.dvmManager;
    }

    public static class DVMManagerImpl_new extends DVMManagerImpl {

        private MetadataManager mTestMetadataMgr_new = null;

        private DVMRTObject getDVMRTObject(String dvmLoc) throws DVMException {

            MetadataManager mdm = null;
            DVMRTObject obj;

            try { /*
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
            */

                DVM dvm = null;
                try {
                    oracle.fabric.common.dvm.DVMManager dvmMgr = mdm.getDVMManager();
                    dvm = dvmMgr.getDVM(dvmLoc);
                } catch (Exception e) {
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

                if (obj == null) {
                   
                    //Вызов кастомной валидации
                    dvm = CustomValidateDVM(dvm);
                    
                    //Вызываем создание объекта без штатной валидации
                    obj = new DVMRTObject(dvm);
                    dvm.setParsedDVM(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Object[] params = { dvmLoc, e.getMessage() };

                throw new DVMValidationException(1572, params, null);
            }
            return obj;
        }

        public DVM CustomValidateDVM(DVM dvm) throws  DVMValidationException {
            System.out.println("CustomValidateDVM");
            int num_rows = 0;
            boolean error = false;
            XMLDocument dvmDocument = null;          
            dvmDocument = (XMLDocument)dvm.getDVMDocument();
            if (dvmDocument == null) {
                  throw new DVMValidationException(1510, null, null);
                }
            
            
            
            
            if ((!dvm.isValidated()) || (!dvm.isValid())) {
                try {
                    //Cписок тегов DVM справочника
                      ArrayList<String> cellname = new ArrayList<String>();
                      //Массив Rows
                      ArrayList<List> Rows = new ArrayList<List>();

                    dvmDocument.getDocumentElement().normalize();
                    System.out.println("Root element <" + dvmDocument.getDocumentElement().getNodeName() + ">");

                    NodeList nodeList_columns = dvmDocument.getElementsByTagName("columns");
                    System.out.println("<columns>");
                    Node fistNode = nodeList_columns.item(0);
                    Element elj = (Element)fistNode;
                    NodeList current_node = elj.getElementsByTagName("column");
                    for (int je = 0; je < current_node.getLength(); je++) {
                        if (current_node.item(je).getNodeType() == Node.ELEMENT_NODE) {
                            //current_node.item(je).getChildNodes();
                            elj = (Element)current_node.item(je);
                            String attr_str = elj.getAttribute("name");

                            cellname.add(je, attr_str);
                        }
                    }
                    System.out.println("</columns>");

                    //Тестовый вывод

                    for (String a : cellname) {
                        System.out.println(cellname.indexOf(a) + ":" + a);
                    }

                    //Перемещаемся в корней тег rows
                    NodeList rows_node = dvmDocument.getElementsByTagName("rows");

                    num_rows = rows_node.getLength();

                    Node cur_node_rows = rows_node.item(0);
                    //System.out.println("-" +cur_node_rows.getNodeName());

                    //Перемещаемся на первый элемент блока row
                    NodeList row_node_lst = rows_node.item(0).getChildNodes();
                    for (int jrow = 0; jrow < row_node_lst.getLength(); jrow++) {

                        if (row_node_lst.item(jrow).getNodeType() == Node.ELEMENT_NODE) {
                            Node row_node = row_node_lst.item(jrow);
                            // System.out.println("--" + row_node.getNodeName()  + ":jrow=" + jrow);


                            ArrayList<String> cur_list = new ArrayList<String>();

                            NodeList cell_node_lst = row_node.getChildNodes();
                            for (int jcell = 0; jcell < cell_node_lst.getLength(); jcell++) {

                                Node cell_node = cell_node_lst.item(jcell);

                                //System.out.println(cell_node.getUserData(LocationAwareContentHandler.LINE_NUMBER_KEY_NAME));
                                if (cell_node.getNodeType() == Node.ELEMENT_NODE) {
                                    String value = ".";
                                    value = cell_node.getTextContent();
                                    int ind = jcell / 2;
                                    //String str = cell_node.getUserData("lineNumber");
                                    //String term = "/";
                                    //if (value =="") {term="/";} else {term="";}
                                    //   System.out.println(cell_node.getUserData("location")+ " : ---<" + cellname.get(jcell/2) + ">"+ value );
                                    cur_list.add(value);
                                }
                            }
                            Rows.add(cur_list);
                        }

                    }

                    //1. наличие блоков <rows> должно быть =1.
                    if (num_rows == 1) {
                        System.out.println("Количество блоков <rows> = " + num_rows + " [OK]");
                    } else {
                        error = true;
                        System.out.println("Количество блоков <rows> = " + num_rows + "[ERROR]");
                        Object[] params= {"Количество блоков <rows> = \" + num_rows + \"[ERROR]\""};
                        throw new DVMValidationException(1510, params, null);
                    }

                    System.out.println("Анализ структуры DVM справочника...");
                    //Пров
                    int cur_num_cells = 0;
                    for (int i = 0; i < Rows.size(); i++) {
                        //System.out.println("<row>");
                        //считем количество элементов <cell>
                        cur_num_cells = 0;
                        for (int j = 0; j < Rows.get(i).size(); j++) {
                            //  System.out.println("  <cell>"+Rows.get(i).get(j)+ "</cell>");

                            cur_num_cells++;
                        }
                        if (cur_num_cells == cellname.size()) {
                            // System.out.println("Количество блоков <cell> = " + cur_num_cells + " [OK]");
                        } else {
                            int num_str = i * (cellname.size() + 2) + cellname.size() + 5;
                            System.out.println("Ошибка! Нарушение целостности справочника. [стр. " + num_str + "]" + Rows.get(i) + " ... [ERROR]");
                            System.out.println("В блоке " + Rows.get(i) + ". Содержит " + cur_num_cells + " элемента(ов) <cell> вместо " + cellname.size());
                            for (int j = 0; j < Rows.get(i).size(); j++) {
                                //       System.out.println("  <cell>"+Rows.get(i).get(j)+ "</cell>");
                            }
                            int s = cellname.size();
                            System.out.println("Должно быть = " + s + " элементов [ERROR]");
                            error = true;
                        }
                    }

                    if (error) {
                        Object[] params =  {"Ошибка! Нарушение целостности справочника"};
                        throw new DVMValidationException(1510, params, null);
                    }

                    //Поиск дублей.
                    List<Boolean> flag = new ArrayList<Boolean>();
                    System.out.println("Поиск дублей ... ");

                    for (int i = 0; i < cellname.size(); i++) {
                        flag.add(true);
                    }
                    Throwable trowable = null;
                    String str_err = " ";
                    for (int i = 0; i < Rows.size(); i++) {
                        //  for (int j = 0; j < Rows.get(i).size(); j++) {

                        for (int ki = 0; ki < Rows.size(); ki++) {
                            for (int k = 0; k < cellname.size(); k++) {
                                flag.set(k, false);
                            }
                            boolean d = true;
                            if (i != ki) {

                                for (int kj = 0; kj < Rows.get(ki).size(); kj++) {

                                    if (Rows.get(i).get(kj).equals(Rows.get(ki).get(kj))) {
                                        flag.set(kj, true);
                                    } else {
                                        flag.set(kj, false);
                                    }
                                    // System.out.println(Rows.get(i).get(kj)+ " : " + Rows.get(ki).get(kj)) ;
                                    d = true;

                                }
                            }
                            for (int k = 0; k < cellname.size(); k++) {
                                d &= flag.get(k);
                            }
                            if (d) {
                                error = true;
                                int num_str = i * (cellname.size() + 2) + cellname.size() + 5;
                                str_err = str_err + CLASS_NAME + ".customValidateDVM " + "Дубль найден [стр. " + num_str + "]" + Rows.get(i) + " ... [ERROR] " + dvm.getDVMURI()+ " ";
                                System.out.println("Дубль найден [стр. " + num_str + "]" + Rows.get(i) + " ... [ERROR]");
                                
                            }
                        }
                        // }
                    }
                    if (error) {
                        System.out.println("Внимание! Расчет строки с элементом не учитывает вставленные пробелы или комментарии");
                        System.out.println("Валидация DVM справочника не успешная.");
                        trowable = new Throwable(dvm.getDVMURI() + " is not valid. " + str_err);
                        throw new DVMValidationException(1510, null, trowable);
                    }
                    
                    dvm.setIsValidated(true);
                    dvm.setIsValid(true);
                    System.out.print("Валидация справочника успешно завершена...");
                    System.out.println();

                } catch (Exception ex) {
                    
                    System.out.println("dvm.setIsValid -> false");
                    dvm.setIsValid(false);
                    System.out.println("dvm.setIsValidated -> true");
                    dvm.setIsValidated(true);
                    
                    ex.printStackTrace();
                    //Throwable a = new Throwable("Валидация DVM справочника не успешная.");
                    //a.fillInStackTrace(ex)
                    throw new DVMValidationException(1510, null, null);
                }
            }
            return dvm;
        }


        public void setMetadataManager(MetadataManager mmg) {
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
        public DVMRTObject_new(DVM dvm) throws DVMException {
            super(null);
            System.out.println("DVMRTObject_new (DVM dvm)");
        }
        // XMLDocument xmlDoc = CustomValidateDVM(dvm);
        /*
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

                //XMLDocument dvmDocument = null;
                XMLDocument dvmDocument = (XMLDocument)dvm.getDVMDocument();
                if (((!dvm.isValidated()) || (!dvm.isValid()))){
                    System.out.println("Кастомная валидация");
                    dvmDocument = CustomValidateDVM(dvm);

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
  */


        public XMLDocument validateDVM(DVM dvm) {
            System.out.println("DVMRTObject_new (DVM dvm)");
            int num_rows = 0;
            boolean error = false;
            XMLDocument dvmDocument = (XMLDocument)dvm.getDVMDocument();
            //Cписок тегов DVM справочника
            ArrayList<String> cellname = new ArrayList<String>();
            //Массив Rows
            ArrayList<List> Rows = new ArrayList<List>();

            try {


                //  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                //  DocumentBuilder db = dbf.newDocumentBuilder();
                // Document doc = db.parse(dvmDocument.);
                //setDocumentLocator();
                //       List<String> cell = new ArrayList<String>();
                //       ArrayList<List> cells =  new ArrayList<List>();

                dvmDocument.getDocumentElement().normalize();
                System.out.println("Root element <" + dvmDocument.getDocumentElement().getNodeName() + ">");

                NodeList nodeList_columns = dvmDocument.getElementsByTagName("columns");
                System.out.println("<columns>");
                Node fistNode = nodeList_columns.item(0);
                Element elj = (Element)fistNode;
                NodeList current_node = elj.getElementsByTagName("column");
                for (int je = 0; je < current_node.getLength(); je++) {
                    if (current_node.item(je).getNodeType() == Node.ELEMENT_NODE) {
                        //current_node.item(je).getChildNodes();
                        elj = (Element)current_node.item(je);
                        String attr_str = elj.getAttribute("name");

                        cellname.add(je, attr_str);
                    }
                }
                System.out.println("</columns>");

                //Тестовый вывод

                for (String a : cellname) {
                    System.out.println(cellname.indexOf(a) + ":" + a);
                }

                //Перемещаемся в корней тег rows
                NodeList rows_node = dvmDocument.getElementsByTagName("rows");

                num_rows = rows_node.getLength();

                Node cur_node_rows = rows_node.item(0);
                //System.out.println("-" +cur_node_rows.getNodeName());

                //Перемещаемся на первый элемент блока row
                NodeList row_node_lst = rows_node.item(0).getChildNodes();
                for (int jrow = 0; jrow < row_node_lst.getLength(); jrow++) {

                    if (row_node_lst.item(jrow).getNodeType() == Node.ELEMENT_NODE) {
                        Node row_node = row_node_lst.item(jrow);
                        // System.out.println("--" + row_node.getNodeName()  + ":jrow=" + jrow);


                        ArrayList<String> cur_list = new ArrayList<String>();

                        NodeList cell_node_lst = row_node.getChildNodes();
                        for (int jcell = 0; jcell < cell_node_lst.getLength(); jcell++) {

                            Node cell_node = cell_node_lst.item(jcell);

                            //System.out.println(cell_node.getUserData(LocationAwareContentHandler.LINE_NUMBER_KEY_NAME));
                            if (cell_node.getNodeType() == Node.ELEMENT_NODE) {
                                String value = ".";
                                value = cell_node.getTextContent();
                                int ind = jcell / 2;
                                //String str = cell_node.getUserData("lineNumber");
                                //String term = "/";
                                //if (value =="") {term="/";} else {term="";}
                                //   System.out.println(cell_node.getUserData("location")+ " : ---<" + cellname.get(jcell/2) + ">"+ value );
                                cur_list.add(value);
                            }
                        }
                        Rows.add(cur_list);
                    }

                }

                //1. наличие блоков <rows> должно быть =1.
                if (num_rows == 1) {
                    System.out.println("Количество блоков <rows> = " + num_rows + " [OK]");
                } else {
                    error = true;
                    System.out.println("Количество блоков <rows> = " + num_rows + "[ERROR]");

                    throw new DVMValidationException(1510, null, null);
                }

                System.out.println("Анализ структуры DVM справочника...");
                //Пров
                int cur_num_cells = 0;
                for (int i = 0; i < Rows.size(); i++) {
                    //System.out.println("<row>");
                    //считем количество элементов <cell>
                    cur_num_cells = 0;
                    for (int j = 0; j < Rows.get(i).size(); j++) {
                        //  System.out.println("  <cell>"+Rows.get(i).get(j)+ "</cell>");

                        cur_num_cells++;
                    }
                    if (cur_num_cells == cellname.size()) {
                        // System.out.println("Количество блоков <cell> = " + cur_num_cells + " [OK]");
                    } else {
                        System.out.println("Нарушение целостности справчоника. В блоке " + Rows.get(i) + ". Содержит " + cur_num_cells + " элемента(ов) <cell> вместо " + cellname.size());
                        for (int j = 0; j < Rows.get(i).size(); j++) {
                            //       System.out.println("  <cell>"+Rows.get(i).get(j)+ "</cell>");
                        }
                        int s = cellname.size();
                        System.out.println("Должно быть = " + s + " элементов [ERROR]");
                        error = true;
                    }
                }

                if (error) {
                    throw new DVMValidationException(1510, null, null);
                }

                //Поиск дублей.
                List<Boolean> flag = new ArrayList<Boolean>();
                System.out.println("Поиск дублей ... ");

                for (int i = 0; i < cellname.size(); i++) {
                    flag.add(true);
                }

                for (int i = 0; i < Rows.size(); i++) {
                    //  for (int j = 0; j < Rows.get(i).size(); j++) {

                    for (int ki = 0; ki < Rows.size(); ki++) {
                        for (int k = 0; k < cellname.size(); k++) {
                            flag.set(k, false);
                        }
                        boolean d = true;
                        if (i != ki) {

                            for (int kj = 0; kj < Rows.get(ki).size(); kj++) {

                                if (Rows.get(i).get(kj).equals(Rows.get(ki).get(kj))) {
                                    flag.set(kj, true);
                                } else {
                                    flag.set(kj, false);
                                }
                                // System.out.println(Rows.get(i).get(kj)+ " : " + Rows.get(ki).get(kj)) ;
                                d = true;

                            }
                        }
                        for (int k = 0; k < cellname.size(); k++) {
                            d &= flag.get(k);
                        }
                        if (d) {
                            error = true;
                            int num_str = i * (cellname.size() + 2) + cellname.size() + 5;
                            System.out.println("Дубль найден [стр. " + num_str + "]" + Rows.get(i) + " ... [ERROR]");

                        }
                    }
                    // }
                }
                if (error) {
                    System.out.println("Внимание! Расчет строки с элементом не учитывает вставленные пробелы или комментарии");
                    System.out.println("Валидация DVM справочника не успешная.");
                    throw new DVMValidationException(1510, null, null);
                }
                System.out.print("Валидация справочника успешно завершена...");
                System.out.println();


            } catch (Exception ex) {


                System.out.println(ex.fillInStackTrace());
            }

            return dvmDocument;
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

        String searchValue = dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName, DVM_DEFAUL_VALUE,
                                                    qualifierColumnName, qualifierValue);
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
                                     String qualifierColumnName, String qualifierValue, String qualifierColumnName1,
                                     String qualifierValue1) throws DVMException,

            XPathFunctionException {

        String searchValue = dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName, DVM_DEFAUL_VALUE,
                                                    qualifierColumnName, qualifierValue, qualifierColumnName1,
                                                    qualifierValue1);
        return checkLookupValue(searchValue,
                                Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                             qualifierColumnName, qualifierValue, qualifierColumnName1,
                                                             qualifierValue1 })).toString();
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
                                     String qualifierColumnName, String qualifierValue, String qualifierColumnName1,
                                     String qualifierValue1, String qualifierColumnName2,
                                     String qualifierValue2) throws DVMException, XPathFunctionException {

        String searchValue = dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName, DVM_DEFAUL_VALUE,
                                                    qualifierColumnName, qualifierValue, qualifierColumnName1,
                                                    qualifierValue1, qualifierColumnName2, qualifierValue2);
        return checkLookupValue(searchValue,
                                Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                             qualifierColumnName, qualifierValue, qualifierColumnName1,
                                                             qualifierValue1, qualifierColumnName2, qualifierValue2 }))
            .toString();
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
                                     String qualifierColumnName, String qualifierValue, String qualifierColumnName1,
                                     String qualifierValue1, String qualifierColumnName2, String qualifierValue2,
                                     String qualifierColumnName3, String qualifierValue3) throws DVMException,
                                                                                                 XPathFunctionException {
        // dvmNeoUtils.DVMManagerImpl_new dvm;
        String searchValue = dvmNeoUtils.dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                                DVM_DEFAUL_VALUE, qualifierColumnName, qualifierValue,
                                                                qualifierColumnName1, qualifierValue1,
                                                                qualifierColumnName2, qualifierValue2,
                                                                qualifierColumnName3, qualifierValue3);
        return checkLookupValue(searchValue,
                                Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                             qualifierColumnName, qualifierValue, qualifierColumnName1,
                                                             qualifierValue1, qualifierColumnName2, qualifierValue2,
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
                                     String qualifierColumnName, String qualifierValue, String qualifierColumnName1,
                                     String qualifierValue1, String qualifierColumnName2, String qualifierValue2,
                                     String qualifierColumnName3, String qualifierValue3, String qualifierColumnName4,
                                     String qualifierValue4) throws DVMException, XPathFunctionException {
        String searchValue = dvmNeoUtils.dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                                DVM_DEFAUL_VALUE, qualifierColumnName, qualifierValue,
                                                                qualifierColumnName1, qualifierValue1,
                                                                qualifierColumnName2, qualifierValue2,
                                                                qualifierColumnName3, qualifierValue3,
                                                                qualifierColumnName4, qualifierValue4);
        return checkLookupValue(searchValue,
                                Arrays.asList(new String[] { dvmLoc, srcColumnName, srcValue, tgtColumnName,
                                                             qualifierColumnName, qualifierValue, qualifierColumnName1,
                                                             qualifierValue1, qualifierColumnName2, qualifierValue2,
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
