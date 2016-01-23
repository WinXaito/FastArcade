package com.winxaito.main.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLLoader{
	private File xmlFile;
	
	public XMLLoader(){
		try{
			xmlFile = new File(XMLLoader.class.getResource("/levels/WinXaito/level.png").toString());
		}catch(NullPointerException e){
			xmlFile = null;
			e.printStackTrace();
		}
		
		if(xmlFile.exists()){
			try{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
			}catch(ParserConfigurationException e){
				e.printStackTrace();
			}catch(SAXException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}else{
			System.err.println("Le fichier n'existe pas");
		}
	}
}
