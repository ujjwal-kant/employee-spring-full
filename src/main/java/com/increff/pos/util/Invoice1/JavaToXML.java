package com.increff.pos.util.Invoice1;

import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.increff.pos.model.data.PdfData;
import com.increff.pos.model.data.PdfListData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

@Service
public class JavaToXML {

    public static final String xmlFilePath = "src/main/resources/apache\\name.xml";

    public void xmlGenerator(PdfData d){
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("invoice");
            document.appendChild(root);

            //invoice elements
            Element invoiceNumber = document.createElement("invoicenumber");
            invoiceNumber.appendChild(document.createTextNode(""+d.getOrderId()));
            root.appendChild(invoiceNumber);

            Element invoiceDate = document.createElement("invoicedate");
            invoiceDate.appendChild(document.createTextNode(""+d.getInvoiceDate()));
            root.appendChild(invoiceDate);

            Element invoiceTime = document.createElement("invoicetime");
            invoiceTime.appendChild(document.createTextNode(""+d.getInvoiceTime()));
            root.appendChild(invoiceTime);

            //address element
            Element address = document.createElement("address");

            root.appendChild(address);

            // set an attribute to address element
            Attr attr = document.createAttribute("type");
            attr.setValue("from");
            address.setAttributeNode(attr);

            //you can also use address.setAttribute("type", "from") for this

            // address sub elements
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode("Increff"));
            address.appendChild(name);

            Element building = document.createElement("building");
            building.appendChild(document.createTextNode("2nd floor, Enzyme Tech Park,"));
            address.appendChild(building);

            Element area = document.createElement("area");
            area.appendChild(document.createTextNode("Sector 6, HSR Layout,"));
            address.appendChild(area);

            Element city = document.createElement("city");
            city.appendChild(document.createTextNode("Bengaluru, Karnataka 560102"));
            address.appendChild(city);

            // create lineitems element
            Element lineitems = document.createElement("lineitems");

            root.appendChild(lineitems);

            for(PdfListData l : d.getItemList()){
                Element lineitem = document.createElement("lineitem");

                lineitems.appendChild(lineitem);

                // create lineitems sub element
                Element sno = document.createElement("sno");
                sno.appendChild(document.createTextNode(""+l.getSno()));
                lineitem.appendChild(sno);

                Element barcode = document.createElement("barcode");
                barcode.appendChild(document.createTextNode(""+l.getBarcode()));
                lineitem.appendChild(barcode);

                Element qty = document.createElement("quantity");
                qty.appendChild(document.createTextNode(""+l.getQuantity()));
                lineitem.appendChild(qty);

                Element desc = document.createElement("product");
                desc.appendChild(document.createTextNode(""+l.getProduct()));
                lineitem.appendChild(desc);

                Element unitPrice = document.createElement("unitprice");
                unitPrice.appendChild(document.createTextNode(""+l.getUnitPrice()));
                lineitem.appendChild(unitPrice);

                Element amount = document.createElement("amount");
                amount.appendChild(document.createTextNode(""+l.getAmount()));
                lineitem.appendChild(amount);

            }

            // System.out.println(d.getTotal());
            double val = d.getTotal();
            val = val*100;
            val = Math.round(val);
            val = val /100;

            Element total = document.createElement("total");
            total.appendChild(document.createTextNode(""+val));
            root.appendChild(total);


            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
//            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
            File file = new File(xmlFilePath);
            final StreamResult streamResult = new StreamResult(file.toURI().getPath());

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging 

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}