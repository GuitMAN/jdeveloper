package dvmvalid;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CustomVaidateDVM {


    private File dvmXml;
    
    
    //Список сообщений
    private ArrayList<String> resp_list;
    //Cписок тегов DVM справочника
    private static ArrayList<String> cellname = new ArrayList<String>();
    //Массив Rows
    private static ArrayList<List> Rows = new ArrayList<List>();


    public CustomVaidateDVM(File dvmXml) {
        this.dvmXml = dvmXml;
      //  super();
    }
    
    
    public void setFile(File dvmXml) {
            this.dvmXml = dvmXml;
        };
    
    public ArrayList<String> execute (){
        
       int num_rows = 0;
       boolean error = false;
       
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(dvmXml);
            //setDocumentLocator();
            //       List<String> cell = new ArrayList<String>();
            //       ArrayList<List> cells =  new ArrayList<List>();

            doc.getDocumentElement().normalize();
            System.out.println("Root element <" + doc.getDocumentElement().getNodeName() + ">");

            NodeList nodeList_columns = doc.getElementsByTagName("columns");
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
            NodeList rows_node = doc.getElementsByTagName("rows");

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

                return null;
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
                    System.out.println("Нарушение целостности справчоника. В блоке " + Rows.get(i) + ". Содержит " + cur_num_cells + " элемента(ов) <cell> вместо " + cellname.size() );
                    for (int j = 0; j < Rows.get(i).size(); j++) {
                        //       System.out.println("  <cell>"+Rows.get(i).get(j)+ "</cell>");
                    }
                    int s = cellname.size();
                    System.out.println("Должно быть = " + s + " элементов [ERROR]");
                    error = true;
                }
            }

            if (error) {
                return null;
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
                        for (int k = 0; k < cellname.size(); k++) 
                       {
                            d &= flag.get(k);
                        }
                        if (d) {
                            error = true;
                            int num_str = i*(cellname.size()+2)+cellname.size()+5;
                            System.out.println("Дубль найден [стр. "+ num_str +"]" + Rows.get(i) + " ... [ERROR]");
                            
                        }
                    }
               // }
            }
            if (error) {
                System.out.println("Внимание! Расчет строки с элементом не учитывает вставленные пробелы или комментарии");
                System.out.println("Валидация DVM справочника не успешная.");
                return null;
            }
            System.out.print("Валидация справочника успешно завершена...");
            System.out.println();


        } catch (Exception ex) {


            System.out.println(ex.fillInStackTrace());
        }
        return null;
    }
    
    
    
}
