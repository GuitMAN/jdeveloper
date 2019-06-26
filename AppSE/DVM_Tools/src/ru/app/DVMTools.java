package ru.app;

import java.io.File;

import java.io.IOException;
import java.io.StringWriter;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;







public class DVMTools {
    private static final String CLASS_NAME = DVMTools.class.getName();


    public DVMTools() {
        super();
    }

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
