package com.sunmyoung.tasktracker.controllers.settings;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Config {
    private static Node addressNode;
    private static Node loginNode;
    private static Node passwordNode;

    static {
        File inputFile = new File("src/main/resources/hibernate.cfg.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(inputFile);
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (document != null) {
            NodeList nodeList = document.getElementsByTagName("property");
            addressNode = nodeList.item(1);
            loginNode = nodeList.item(2);
            passwordNode = nodeList.item(3);
        }
    }

    public static String getAddress() {
        String address = addressNode.getTextContent();
        int start = address.indexOf("//") + 2;
        int end = address.indexOf('/', start);
        return address.substring(start, end);
    }

    public static String getLogin() {
        return loginNode.getTextContent();
    }

    public static String getPassword() {
        return passwordNode.getTextContent();
    }

    public static Boolean setAddress(String address) {
        address = String.format("jdbc:mysql://%s/sunmyoung?useSSL=false", address);
        addressNode.setTextContent(address);
        return addressNode.getTextContent().equals(address);
    }

    public static Boolean setLogin(String login) {
        loginNode.setTextContent(login);
        return loginNode.getTextContent().equals(login);
    }

    public static Boolean setPassword(String password) {
        passwordNode.setTextContent(password);
        return passwordNode.getTextContent().equals(password);
    }

}
