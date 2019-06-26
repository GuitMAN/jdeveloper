package ru.app;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class DVMTools {
    private static final String CLASS_NAME = DVMTools.class.getName();


    public DVMTools() {
        super();
    }

    /**
     * Возвращает класс Dvm.
     * @param dvmLoc - путь к файлу DVM
     */
    Dvm LoadDVMFile(String docLoc) {

        Dvm customer = null;

        try {
            File file = new File(docLoc);
            if (file != null) {
                System.out.print ( file.toString());
                JAXBContext context = JAXBContext.newInstance(Dvm.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                customer = (Dvm) unmarshaller.unmarshal(file);
                System.out.println (" OK");
            }
        } catch (JAXBException ex) {
            System.out.println("<ERROR> "+ DVMTools.class.getName());
        }

        return customer;


    }


}
