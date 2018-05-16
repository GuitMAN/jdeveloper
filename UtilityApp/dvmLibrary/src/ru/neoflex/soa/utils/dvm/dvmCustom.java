
package ru.neoflex.soa.utils.dvm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import oracle.fabric.common.MetadataManager;
import oracle.fabric.common.dvm.DVM;
import oracle.fabric.common.xml.xpath.IXPathContext;
import oracle.fabric.common.xml.xpath.IXPathFunction;
import oracle.fabric.common.xml.xpath.XPathFunctionException;

import oracle.integration.platform.blocks.xpath.XPathContext;

import oracle.tip.dvm.DVMExtFunctions;
import oracle.tip.dvm.DVMManagerImpl;

import oracle.tip.dvm.entity.DVMRTObject;
import oracle.tip.dvm.exception.DVMException;
import oracle.tip.dvm.exception.DVMValidationException;

import oracle.xml.parser.v2.XMLDocument;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import weblogic.logging.NonCatalogLogger;


/**
 *
 * @author vdementev
 */
public class dvmCustom {

    private static final String CLASS_NAME = dvmCustom.class.getName();
    private final static NonCatalogLogger LOGGER = new NonCatalogLogger(CLASS_NAME);

    private static final String DVM_DEFAUL_VALUE = UUID.randomUUID().toString();

    public  dvmCustom (){
        super();
    }
    
    
    /**
     * Менеджер DVM
     */
    public static final DVMManagerImpl dvmManager = new DVMManagerImpl_new();

    /**
     * По умолчанию отключим валидацию true - справочник провалидирован, иначе
     * будем валидировать.
     */
    private static boolean isValidateDVM = true;

    /**
     * @return
     */
    public DVMManagerImpl getdvmManager() {
        return this.dvmManager;
    }

    public static class DVMManagerImpl_new extends DVMManagerImpl implements oracle.tip.dvm.DVMManager {

        private MetadataManager mTestMetadataMgr_new = null;

        private DVMRTObject getDVMRTObject(String dvmLoc) throws DVMException {

            MetadataManager mdm = null;
            DVMRTObject obj;

            try {

                if (this.mTestMetadataMgr_new == null) {
                    Object ctx = XPathContext.getXPathContext();

                    if (ctx == null) {
                        Object[] params = {dvmLoc};
                        throw new DVMValidationException(1571, params, null);
                    }
                    mdm = XPathContext.getXPathContext().getComposite().getMetadataManager();
                    //this.mTestMetadataMgr_new = mdm;
                    if (mdm == null) {
                        Object[] params = {dvmLoc};
                        throw new DVMValidationException(1572, params, null);
                    }
                } else {
                    mdm = this.mTestMetadataMgr_new;
                }

                dvmLoc = mdm.resolve(dvmLoc);

                DVM dvm = null;
                try {
                    oracle.fabric.common.dvm.DVMManager dvmMgr = mdm.getDVMManager();
                    dvm = dvmMgr.getDVM(dvmLoc);
                } catch (Exception e) {
                    e.printStackTrace();
                    Object[] params = {dvmLoc, e.getMessage()};
                    throw new DVMValidationException(1573, params, null);
                }

                obj = (DVMRTObject) dvm.getParsedDVM();

                LOGGER.notice(CLASS_NAME + ".getDVMRTObject. " + dvm.getDVMDocument() + "dvm.isValid():" + dvm.isValid() + "; dvm.isValidated():" + dvm.isValidated());
                if (obj == null) {

                    //Вызов кастомной валидации
                    dvm = customValidateDVM(dvm);

                    //Отключаем прохождение штатной валидации
                    //dvm.setIsValid(true);
                    //dvm.setIsValidated(true);
                    //Вызываем создание объекта без штатной валидации
                    obj = new DVMRTObject(dvm);

                    dvm.setParsedDVM(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Object[] params = {dvmLoc, e.getMessage()};
                throw new DVMValidationException(1572, params, null);
            }
            return obj;
        }

        /**
         * Реализация кастомной валидации DVM справочника
         *
         * @param dvm - DVM справочник, который необходимо проверить
         * @return DVM справочник с обновленными флагами валидации
         * @author Vladimir Dementev
         */
        public DVM customValidateDVM(DVM dvm) throws DVMValidationException {
            LOGGER.notice(CLASS_NAME + ".customValidateDVM. " + dvm.getDVMURI());
            int num_rows = 0;
            boolean error = false;

            XMLDocument dvmDocument = (XMLDocument) dvm.getDVMDocument();
            if (dvmDocument == null) {
                throw new DVMValidationException(1510, null, null);
            }

            //Провалидирован справочник и валидный ли он
            if ((!dvm.isValidated()) || (!dvm.isValid())) {
                try {
                    //Добавление комментария к исключению "Caused by: "
                    Throwable trowable = null;
                    String str_err = null;

                    //Cписок тегов DVM справочника
                    ArrayList<String> cellname = new ArrayList<String>();
                    //Массив Rows
                    ArrayList<List> Rows = new ArrayList<List>();

                    // Добавление массива Hashmap для более быстрого поиска дублей
                    int dup_num_str = 0;
                    int cur_num_str = 0;
                    Map<String, Integer> map_list = new HashMap<String, Integer>();

                    dvmDocument.getDocumentElement().normalize();

                    NodeList nodeList_columns = dvmDocument.getElementsByTagName("columns");
                    //System.out.println("<columns>");
                    Node fistNode = nodeList_columns.item(0);
                    Element elj = (Element) fistNode;
                    NodeList current_node = elj.getElementsByTagName("column");
                    for (int je = 0; je < current_node.getLength(); je++) {
                        if (current_node.item(je).getNodeType() == Node.ELEMENT_NODE) {
                            elj = (Element) current_node.item(je);
                            String attr_str = elj.getAttribute("name");
                            cellname.add(je, attr_str);
                        }
                    }

                    //Перемещаемся в корней тег rows
                    NodeList rows_node = dvmDocument.getElementsByTagName("rows");
                    num_rows = rows_node.getLength();
                    Node cur_node_rows = rows_node.item(0);

                    //Индекс элемента массиве
                    int i = 0;
                    //Перемещаемся на первый элемент блока row
                    NodeList row_node_lst = rows_node.item(0).getChildNodes();
                    for (int jrow = 0; jrow < row_node_lst.getLength(); jrow++) {

                        if (row_node_lst.item(jrow).getNodeType() == Node.ELEMENT_NODE) {
                            Node row_node = row_node_lst.item(jrow);

                            ArrayList<String> cur_list = new ArrayList<String>();

                            NodeList cell_node_lst = row_node.getChildNodes();
                            for (int jcell = 0; jcell < cell_node_lst.getLength(); jcell++) {

                                Node cell_node = cell_node_lst.item(jcell);

                                if (cell_node.getNodeType() == Node.ELEMENT_NODE) {
                                    String value = ".";
                                    value = cell_node.getTextContent();
                                    int ind = jcell / 2;
                                    cur_list.add(value);
                                }
                            }
                            Rows.add(cur_list);

                            String item_str = cur_list.toString();
                            if (!map_list.containsKey(item_str)) {

                                map_list.put(item_str, i);
                            } else {
                                error = true;

                                dup_num_str = map_list.get(item_str) * (cellname.size() + 2) + cellname.size() + 5;
                                cur_num_str = i * (cellname.size() + 2) + cellname.size() + 5;
                                str_err = str_err + " |" + CLASS_NAME + ".customValidateDVM Дубль найден [стр. " + dup_num_str + " и " + cur_num_str + " ]" + item_str + " ... [ERROR]";
                                LOGGER.notice(str_err);
                            }
                            i++;
                        }
                    }

                    if (error) {
                        LOGGER.notice(str_err);
                        LOGGER.notice("Внимание! Расчет строки с элементом не учитывает вставленные пробелы или комментарии");
                        LOGGER.notice("Валидация DVM справочника " + dvm.getDVMURI() + " не успешна! [ERROR]");
                        trowable = new Throwable(dvm.getDVMURI() + " is not valid. There are duplicates in lines " + str_err);
                        dvm.setIsValid(false);
                        dvm.setIsValidated(true);
                        throw new DVMValidationException(1510, null, trowable);
                    }

                    //1. наличие блоков <rows> должно быть =1.
                    if (num_rows == 1) {
                        LOGGER.notice(CLASS_NAME + ".customValidateDVM " + dvm.getDVMURI() + "Количество блоков <rows> = " + num_rows + " [OK]");
                    } else {
                        error = true;
                        LOGGER.error(CLASS_NAME + ".customValidateDVM " + dvm.getDVMURI() + "Количество блоков <rows> = " + num_rows + " [ERROR].");
                        //System.out.println("Количество блоков <rows> = " + num_rows + "[ERROR]");
                        dvm.setIsValid(false);
                        dvm.setIsValidated(true);
                        throw new DVMValidationException(1510, null, null);
                    }

                    LOGGER.notice(CLASS_NAME + ".customValidateDVM " + "Анализ структуры DVM справочника " + dvm.getDVMURI());
                    //System.out.println("Анализ структуры DVM справочника...");
                    int cur_num_cells = 0;
                    for (i = 0; i < Rows.size(); i++) {
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
                            //System.out.println("Нарушение целостности справочника. В блоке " + Rows.get(i) + ". Содержит " + cur_num_cells + " элемента(ов) <cell> вместо " + cellname.size());
                            LOGGER.error(CLASS_NAME + ".customValidateDVM " + "Нарушение целостности справочника. В блоке " + Rows.get(i) + ". Содержит " + cur_num_cells + " элемента(ов) <cell> вместо " + cellname.size() + " " + dvm.getDVMURI());
                            str_err = str_err + CLASS_NAME + ".customValidateDVM " + "Нарушение целостности справочника. В блоке " + Rows.get(i) + ". Содержит " + cur_num_cells + " элемента(ов) <cell> вместо " + cellname.size() + " " + dvm.getDVMURI();

                            //for (int j = 0; j < Rows.get(i).size(); j++) {
                            //       System.out.println("  <cell>"+Rows.get(i).get(j)+ "</cell>");
                            //}
                            int s = cellname.size();
                            LOGGER.error(CLASS_NAME + ".customValidateDVM " + "Должно быть = " + s + " элементов [ERROR] " + dvm.getDVMURI());
                            str_err = str_err + "Должно быть = \" + s + \" элементов [ERROR]";
                            //System.out.println("Должно быть = " + s + " элементов [ERROR]");
                            error = true;
                        }
                    }

                    if (error) {
                        dvm.setIsValid(false);
                        dvm.setIsValidated(true);
                        trowable = new Throwable(dvm.getDVMURI() + " is not valid. " + str_err);
                        throw new DVMValidationException(1510, null, trowable);
                    }

                    LOGGER.notice(CLASS_NAME + ".customValidateDVM " + "Валидация DVM справочника " + dvm.getDVMURI() + " успешно завершена! [OK]");
                    //Присваем флаги валидации
                    dvm.setIsValid(true);
                    dvm.setIsValidated(true);

                } catch (Exception e) {
                    LOGGER.error(CLASS_NAME + ".customValidateDVM " + e.getMessage() + " Валидация DVM справочника  " + dvm.getDVMURI() + " не успешная! [ERROR]");
                    e.printStackTrace();
                    dvm.setIsValid(false);
                    dvm.setIsValidated(true);
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


    /**
     * Возвращает значение из DVM
     * откорректированная версия из tsc.soa.utils.jar
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

            obj = dvmCustom.checkLookupValue(searchValue, args);
            return obj;
        }

        public Object call(IXPathContext ixPathContext, List<?> list) {
            return null;
        }
    }

    /**
     * Возвращает значение из DVM.
     *
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
                DVM_DEFAUL_VALUE, qualifierColumnName,
                qualifierValue);
        return checkLookupValue(searchValue,
                Arrays.asList(new String[]{dvmLoc, srcColumnName, srcValue, tgtColumnName,
            qualifierColumnName, qualifierValue})).toString();
    }

    /**
     * Возвращает значение из DVM.
     *
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
                DVM_DEFAUL_VALUE, qualifierColumnName,
                qualifierValue, qualifierColumnName1,
                qualifierValue1);
        return checkLookupValue(searchValue,
                Arrays.asList(new String[]{dvmLoc, srcColumnName, srcValue, tgtColumnName,
            qualifierColumnName, qualifierValue,
            qualifierColumnName1, qualifierValue1})).toString();
    }

    /**
     * Возвращает значение из DVM.
     *
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
                DVM_DEFAUL_VALUE, qualifierColumnName,
                qualifierValue, qualifierColumnName1,
                qualifierValue1, qualifierColumnName2,
                qualifierValue2);
        return checkLookupValue(searchValue,
                Arrays.asList(new String[]{dvmLoc, srcColumnName, srcValue, tgtColumnName,
            qualifierColumnName, qualifierValue,
            qualifierColumnName1, qualifierValue1,
            qualifierColumnName2, qualifierValue2})).toString();
    }

    /**
     * Возвращает значение из DVM.
     *
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
        String searchValue = dvmCustom.dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName,
                DVM_DEFAUL_VALUE, qualifierColumnName,
                qualifierValue, qualifierColumnName1,
                qualifierValue1, qualifierColumnName2,
                qualifierValue2, qualifierColumnName3,
                qualifierValue3);
        return checkLookupValue(searchValue,
                Arrays.asList(new String[]{dvmLoc, srcColumnName, srcValue, tgtColumnName,
            qualifierColumnName, qualifierValue,
            qualifierColumnName1, qualifierValue1,
            qualifierColumnName2, qualifierValue2,
            qualifierColumnName3, qualifierValue3})).toString();
    }

    /**
     * Возвращает значение из DVM.
     *
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
        String searchValue = dvmCustom.dvmManager.lookupValue(dvmLoc, srcColumnName, srcValue, tgtColumnName,
                DVM_DEFAUL_VALUE, qualifierColumnName,
                qualifierValue, qualifierColumnName1,
                qualifierValue1, qualifierColumnName2,
                qualifierValue2, qualifierColumnName3,
                qualifierValue3, qualifierColumnName4,
                qualifierValue4);
        return checkLookupValue(searchValue,
                Arrays.asList(new String[]{dvmLoc, srcColumnName, srcValue, tgtColumnName,
            qualifierColumnName, qualifierValue,
            qualifierColumnName1, qualifierValue1,
            qualifierColumnName2, qualifierValue2,
            qualifierColumnName3, qualifierValue3,
            qualifierColumnName4, qualifierValue4})).toString();
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
