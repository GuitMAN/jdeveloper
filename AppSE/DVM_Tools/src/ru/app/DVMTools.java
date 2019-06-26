package ru.app;

import java.io.File;

import java.io.StringWriter;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;


import oracle.fabric.common.MetadataManager;
import oracle.fabric.common.dvm.DVM;

import oracle.tip.dvm.entity.DVMRTObject;


import oracle.tip.dvm.DVMManagerImpl;
import oracle.tip.dvm.DVMExtFunctions;
import oracle.tip.dvm.exception.DVMValidationException;

import oracle.xml.parser.v2.XMLDocument;


public class DVMTools
{
  private static final String CLASS_NAME = DVMTools.class.getName();

  public DVMTools()
  {
    super();
  }

  DVM LoadDVMFile(String docLoc)
  {

    MetadataManager mdm = null;
    DVMRTObject obj = null;
    DVM dvm = null;

    try
    {
      //если адрес является локальным путем, а не ссылка
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db;
      db = dbf.newDocumentBuilder();
      dbf.setNamespaceAware(true);
      Document doc = db.parse(new File(docLoc));
      dvm = new DVM(doc, "");
      obj = (DVMRTObject) dvm.getParsedDVM();
      System.out.println(CLASS_NAME + ".LoadDVMFile. " + dvm.getDVMURI());
      if (obj == null)
      {
        System.out.println("dvm.isValid():" + dvm.isValid() +
                           "; dvm.isValidated():" + dvm.isValidated());
        //Вызов кастомной валидации
        dvm = CustomValidateDVM(dvm);
        System.out.println("dvm.isValid():" + dvm.isValid() +
                           "; dvm.isValidated():" + dvm.isValidated());
        //Вызываем создание объекта без штатной валидации
        obj = new DVMRTObject(dvm);
        dvm.setParsedDVM(obj);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Object[] params =
      {
        docLoc, e.getMessage()
      };
    }
    return dvm;


  }


  public DVMRTObject getDVMRTObject(String dvmLoc)
  {
    DVMRTObject obj = null;
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      dbf.setNamespaceAware(true);
      Document doc;
      doc = db.parse((new File(dvmLoc)));

      //          printXML(doc);

      XMLDocument xmlDoc = null;
      xmlDoc = new XMLDocument();
      xmlDoc.appendChild(xmlDoc.importNode(doc.getDocumentElement(), true));

      // printXML(doc);

      DVM dvm = new DVM(xmlDoc, "");

      obj = (DVMRTObject) dvm.getParsedDVM();
      if (obj == null)
      {
        obj = new DVMRTObject(dvm);
        dvm.setParsedDVM(obj);
      }
      return obj;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return obj;
  }


  public static void printXML(Document doc)
    throws Exception
  {
    TransformerFactory tfactory = TransformerFactory.newInstance();
    Transformer xform = tfactory.newTransformer();
    javax.xml.transform.Source src = new DOMSource(doc);
    java.io.StringWriter writer = new StringWriter();
    Result result = new javax.xml.transform.stream.StreamResult(writer);
    xform.transform(src, result);
    System.out.println("payload dvm :" + writer.toString());
  }






  public DVM CustomValidateDVM(DVM dvm)
    throws DVMValidationException
  {
    System.out.println(CLASS_NAME + ".customValidateDVM. " +
                       dvm.getDVMDocument());
    int num_rows = 0;
    boolean error = false;
    XMLDocument dvmDocument;
    dvmDocument = null;
    dvmDocument = (XMLDocument) dvm.getDVMDocument();
    if (dvmDocument == null)
    {
      throw new DVMValidationException(1510, null, null);
    }

    Throwable trowable = null;
    String str_err = "";

    if ((!dvm.isValidated()) || (!dvm.isValid()))
    {
      try
      {
        //Cписок тегов DVM справочника
        ArrayList<String> cellname = new ArrayList<String>();
        //Массив Rows
        ArrayList<List> Rows = new ArrayList<List>();

        int dup_num_str = 0;
        int cur_num_str = 0;
        Map<String, Integer> map_list = new HashMap<String, Integer>();


        dvmDocument.getDocumentElement().normalize();
        System.out.println("Root element <" +
                           dvmDocument.getDocumentElement().getNodeName() +
                           ">");

        NodeList nodeList_columns =
          dvmDocument.getElementsByTagName("columns");
        System.out.println("<columns>");
        Node fistNode = nodeList_columns.item(0);
        Element elj = (Element) fistNode;
        NodeList current_node = elj.getElementsByTagName("column");
        for (int je = 0; je < current_node.getLength(); je++)
        {
          if (current_node.item(je).getNodeType() == Node.ELEMENT_NODE)
          {
            //current_node.item(je).getChildNodes();
            elj = (Element) current_node.item(je);
            String attr_str = elj.getAttribute("name");

            cellname.add(je, attr_str);
          }
        }
        System.out.println("</columns>");

        //Тестовый вывод

        for (String a: cellname)
        {
          System.out.println(cellname.indexOf(a) + ":" + a);
        }

        //Перемещаемся в корней тег rows
        NodeList rows_node = dvmDocument.getElementsByTagName("rows");

        num_rows = rows_node.getLength();

        Node cur_node_rows = rows_node.item(0);
        //System.out.println("-" +cur_node_rows.getNodeName());

        //Индекс элемента массиве
        int i = 0;
        //Перемещаемся на первый элемент блока row
        NodeList row_node_lst = rows_node.item(0).getChildNodes();
        for (int jrow = 0; jrow < row_node_lst.getLength(); jrow++)
        {

          if (row_node_lst.item(jrow).getNodeType() == Node.ELEMENT_NODE)
          {
            Node row_node = row_node_lst.item(jrow);

            ArrayList<String> cur_list = new ArrayList<String>();

            NodeList cell_node_lst = row_node.getChildNodes();
            for (int jcell = 0; jcell < cell_node_lst.getLength(); jcell++)
            {

              Node cell_node = cell_node_lst.item(jcell);

              if (cell_node.getNodeType() == Node.ELEMENT_NODE)
              {
                String value = ".";
                value = cell_node.getTextContent();
                int ind = jcell / 2;
                cur_list.add(value);
              }
            }
            Rows.add(cur_list);

            String item_str = cur_list.toString();
            if (!map_list.containsKey(item_str))
            {

              map_list.put(item_str, i);
              // System.out.println(map.get(item_str) + " " + item_str);
            }
            else
            {
              error = true;

              dup_num_str =
                map_list.get(item_str) * (cellname.size() + 2) +
                cellname.size() + 5;
              cur_num_str = i * (cellname.size() + 2) + cellname.size() + 5;
              str_err =
                str_err + " |" + CLASS_NAME +
                ".customValidateDVM Дубль найден [стр. " + dup_num_str +
                " и " + cur_num_str + " ]" + item_str + " ... [ERROR]";
              System.out.println(str_err);
            }
            i++;
          }

        }

        if (error)
        {
          System.out.println("Внимание! Расчет строки с элементом не учитывает вставленные пробелы или комментарии");
          System.out.println("Валидация DVM справочника не успешная.");
          trowable =
            new Throwable(dvm.getDVMURI() +
                          " is not valid. There are duplicates in lines " +
                          dup_num_str + " and " + cur_num_str);
          throw new DVMValidationException(1510, null, trowable);
        }


        //1. наличие блоков <rows> должно быть =1.
        if (num_rows == 1)
        {
          System.out.println("Количество блоков <rows> = " + num_rows +
                             " [OK]");
        }
        else
        {
          error = true;
          System.out.println("Количество блоков <rows> = " + num_rows +
                             "[ERROR]");
          Object[] params =
          {
            "Количество блоков <rows> = \" + num_rows + \"[ERROR]\""
          };
          throw new DVMValidationException(1510, params, null);
        }

        System.out.println("Анализ структуры DVM справочника...");
        //Пров
        int cur_num_cells = 0;
        for (i = 0; i < Rows.size(); i++)
        {
          //System.out.println("<row>");
          //считем количество элементов <cell>
          cur_num_cells = 0;
          for (int j = 0; j < Rows.get(i).size(); j++)
          {
            //  System.out.println("  <cell>"+Rows.get(i).get(j)+ "</cell>");

            cur_num_cells++;
          }
          if (cur_num_cells == cellname.size())
          {
            // System.out.println("Количество блоков <cell> = " + cur_num_cells + " [OK]");
          }
          else
          {
            int num_str = i * (cellname.size() + 2) + cellname.size() + 5;
            System.out.println("Ошибка! Нарушение целостности справочника. [стр. " +
                               num_str + "]" + Rows.get(i) +
                               " ... [ERROR]");
            System.out.println("В блоке " + Rows.get(i) + ". Содержит " +
                               cur_num_cells +
                               " элемента(ов) <cell> вместо " +
                               cellname.size());
            for (int j = 0; j < Rows.get(i).size(); j++)
            {
              //       System.out.println("  <cell>"+Rows.get(i).get(j)+ "</cell>");
            }
            int s = cellname.size();
            System.out.println("Должно быть = " + s + " элементов [ERROR]");
            error = true;
          }
        }

        if (error)
        {
          Object[] params =
          {
            "Ошибка! Нарушение целостности справочника"
          };
          throw new DVMValidationException(1510, params, null);
        }
        /*
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
                          str_err = str_err + CLASS_NAME + ".customValidateDVM " + "Дубль найден [стр. " + num_str + "]" + Rows.get(i) + " ... [ERROR] " + dvm.getDVMURI()+ " ";
                          System.out.println("Дубль найден [стр. " + num_str + "]" + Rows.get(i) + " ... [ERROR]");

                      }
                  }
                  // }
              }


  //*/

        /*

              Integer i = 0;
              for (List<String> item : Rows) {
                  String item_str = item.toString();
                  i++;
                  if (!map.containsKey(item_str)) {

                      map.put(item_str, i);
                    //  System.out.println(map.get(str) + " " + str);
                  } else {
                      error = true;

                      dup_num_str = map.get(item_str) * (cellname.size() + 2) + cellname.size() + 5;
                      cur_num_str = i * (cellname.size() + 2) + cellname.size() + 5;
                      str_err = str_err + " |"+ CLASS_NAME + ".customValidateDVM Дубль найден [стр. " + dup_num_str + " и " + cur_num_str + " ]" + Rows.get(i) + " ... [ERROR]";
                      System.out.println(str_err);
                      //  dup.put(str, 1);
                  }
              }

  ///*/


        dvm.setIsValidated(true);
        dvm.setIsValid(true);
        System.out.print("Валидация DVM справочника успешно завершена..." +
                         dvm.getDVMURI() + " [OK]");
        System.out.println();

      }
      catch (Exception ex)
      {

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


}
