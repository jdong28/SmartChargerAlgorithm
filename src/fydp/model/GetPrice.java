package fydp.model;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class GetPrice extends DefaultHandler {

	public static String getElectricityPrice() {
		String s = new String(); 
		//private double[] prices = new double[48];
		//XStream xstream = new XStream();

		s = getPrice("http://reports.ieso.ca/public/PredispMktPrice/PUB_PredispMktPrice_20170221_v21.xml");

        try {
            File inputFile = new File("input.txt");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            UserHandler userhandler = new UserHandler();
            saxParser.parse(inputFile, userhandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return s;
	}

	public static String getPrice(String getURL) {
		System.out.println("Result: " + getURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConnection = null;
		InputStreamReader in = null;

		try {
			URL url = new URL(getURL);
			urlConnection = url.openConnection();
			if (urlConnection != null){
				// 60 seconds
				urlConnection.setReadTimeout(60000);
			}
			if (urlConnection != null && urlConnection.getInputStream() != null){
				in = new InputStreamReader(urlConnection.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null){
					int cp;
					while ((cp = bufferedReader.read()) != -1){
						sb.append((char) cp);
					}
				bufferedReader.close();
				}
			}
			in.close();
		} catch(Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ getURL, e);
		}

		return sb.toString();
	}
}

class UserHandler extends DefaultHandler {

    boolean bFirstName = false;
    boolean bLastName = false;
    boolean bNickName = false;
    boolean bMarks = false;
    String rollNo = null;

    @Override
    public void startElement(String uri,
                             String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase("student")) {
            rollNo = attributes.getValue("rollno");
        }
        if(("393").equals(rollNo) &&
                qName.equalsIgnoreCase("student")){
            System.out.println("Start Element :" + qName);
        }
        if (qName.equalsIgnoreCase("firstname")) {
            bFirstName = true;
        } else if (qName.equalsIgnoreCase("lastname")) {
            bLastName = true;
        } else if (qName.equalsIgnoreCase("nickname")) {
            bNickName = true;
        }
        else if (qName.equalsIgnoreCase("marks")) {
            bMarks = true;
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("student")) {
            if(("393").equals(rollNo)
                    && qName.equalsIgnoreCase("student"))
                System.out.println("End Element :" + qName);
        }
    }


    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {

        if (bFirstName && ("393").equals(rollNo)) {
            //age element, set Employee age
            System.out.println("First Name: " +
                    new String(ch, start, length));
            bFirstName = false;
        } else if (bLastName && ("393").equals(rollNo)) {
            System.out.println("Last Name: " +
                    new String(ch, start, length));
            bLastName = false;
        } else if (bNickName && ("393").equals(rollNo)) {
            System.out.println("Nick Name: " +
                    new String(ch, start, length));
            bNickName = false;
        } else if (bMarks && ("393").equals(rollNo)) {
            System.out.println("Marks: " +
                    new String(ch, start, length));
            bMarks = false;
        }
    }
}
