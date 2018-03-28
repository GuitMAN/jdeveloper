package dvmvalid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.Locator;





public class Main {

 //   public static Locator locator;


    //Cписок тегов DVM справочника
 //   public static ArrayList<String> cellname = new ArrayList<String>();
    //Массив Rows
//    public static ArrayList<List> Rows = new ArrayList<List>();
    public static CustomVaidateDVM CustomVaidate = new CustomVaidateDVM(null);
    public static String DirDVM = "";
    public static String dvm = "";
    // Загрузить свойства из файла lacc.prop

    public static void loadProperties() {
        Properties props = new Properties();

        File f = new File(".", "conf.prop");
        if (f.exists()) {
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(f));
                props.load(is);
                is.close();
            } catch (IOException ioe) {
                System.exit(1);
            }
            DirDVM = props.getProperty("Directory");
            dvm = props.getProperty("dvm");
            System.out.println(dvm);
        } else {
            System.out.println("Файл настроек не найден");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        dvmvalid.Main main = new dvmvalid.Main();

        loadProperties();
        String filename = "";

        if (dvm == "") {

            if (args[0] != null) {
                filename = args[0];
            } else {
                System.out.println("Необходимо в качестве параметра указать справочник dvm");
                return;
            }
        } else {
            filename = dvm;
        }
        filename = DirDVM + "\\" + filename;
        
      
        CustomVaidate.setFile(new File(filename));
        
        CustomVaidate.execute();
       
    //    f.execute();
       
    return;
    }



}

