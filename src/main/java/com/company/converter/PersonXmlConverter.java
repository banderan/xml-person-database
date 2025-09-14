package com.company.converter;

import com.company.model.Person;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PersonXmlConverter {

    public Person fromXml(String fileName) throws Exception {
        Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new File(fileName));

        doc.getDocumentElement().normalize();

        return fillPerson(doc);
    }

    public void toXml(Person person, String fileName) throws Exception {
        Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .newDocument();

        Element root = fillXml(person, doc);
        doc.appendChild(root);

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        setTransformer(source, result);
    }

    private static void setTransformer(DOMSource source, StreamResult result)
            throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(source, result);
    }

    private static Person fillPerson(Document doc) {
        Person person = new Person();
        person.setPersonId(getElementByTag(doc, "personId"));
        person.setFirstName(getElementByTag(doc, "firstName"));
        person.setLastName(getElementByTag(doc, "lastName"));
        person.setMobile(getElementByTag(doc, "mobile"));
        person.setEmail(getElementByTag(doc, "email"));
        person.setPesel(getElementByTag(doc, "pesel"));
        return person;
    }

    private static String getElementByTag(Document doc, String tag) {
        String textContent = doc.getElementsByTagName(tag).item(0).getTextContent();
        return textContent != null ? textContent.trim() : "";
    }

    private static Element fillXml(Person person, Document doc) {
        Element root = doc.createElement("person");

        appendChildToRoot(doc, "personId", person.getPersonId(), root);
        appendChildToRoot(doc, "firstName", person.getFirstName(), root);
        appendChildToRoot(doc, "lastName", person.getLastName(), root);
        appendChildToRoot(doc, "mobile", person.getMobile(), root);
        appendChildToRoot(doc, "email", person.getEmail(), root);
        appendChildToRoot(doc, "pesel", person.getPesel(), root);

        return root;
    }

    private static void appendChildToRoot(
            Document doc, String tag, String personField, Element root) {
        Element personId = doc.createElement(tag);
        personId.setTextContent((personField != null) ? personField.trim() : "");
        root.appendChild(personId);
    }
}
