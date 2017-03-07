/*
Author: Nagoor Saheb Shaik
Description: This class connects to XML and gets data from XML. check the data periodically
*/

package Dev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class XMLHandler extends UMLController{
	public static URL url =null;
	private static Document xmlDocument;
	
	public XMLHandler() {
			
		String xmlString = getXmlFromWeb();  // get the webpage source
		xmlDocument = stringToDom(xmlString);     // convert xml string to xml DOM object
	
}

private String getXmlFromWeb() {
String xmlString =null;
  try {
	     url = new URL("https://rates.fxcm.com/RatesXML");
	     HttpURLConnection httpCon = (HttpURLConnection) url.openConnection(); 
	        
	     httpCon.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
         BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
         String inputLine;
         StringBuilder xmlResponse = new StringBuilder();
         while ((inputLine = in.readLine()) != null){
        	 xmlResponse.append(inputLine);
        	 xmlResponse.append(System.getProperty("line.separator"));
         }
            
         in.close();
         xmlString = xmlResponse.toString();
         httpCon.disconnect();
	        
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
	    } 
		return xmlString;
	}
	
public static Document stringToDom(String xmlSource) {

   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
   DocumentBuilder builder; 
   Document document =null;;
try  
{  
    builder = factory.newDocumentBuilder();  
    document = builder.parse( new InputSource( new StringReader( xmlSource ) ) );  
} catch (Exception e) {  
    e.printStackTrace();  
} 

return document;
}

public boolean verifyInputs(String currencyExchange, float targetRate) {
	try{
	
	xmlDocument.getDocumentElement().normalize(); 
    NodeList nList = xmlDocument.getElementsByTagName("Rate");
     for (int temp = 0; temp < nList.getLength(); temp++) {
      Node nNode = nList.item(temp);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {
         Element eElement = (Element) nNode; 
          if(eElement.getAttribute("Symbol").equalsIgnoreCase(currencyExchange)){ // verify for valid exchange rate symbol
        	  verified = true;
        	  break;
          }
        }
     }
    } catch (Exception e) {
     e.printStackTrace();
    }
	return verified;
}

public boolean targetCheck(String currencyExchange, float targetRate) {
	float _valueCheck = 0;
	String xmlString = getXmlFromWeb();  // get the webpage source
	xmlDocument = stringToDom(xmlString);     // convert xml string to xml DOM object
  // updates the xml by the time to check;
	 try {
	       
	        xmlDocument.getDocumentElement().normalize(); 
	        NodeList nList = xmlDocument.getElementsByTagName("Rate");
	        
	        for (int temp = 0; temp < nList.getLength(); temp++) {
	          Node nNode = nList.item(temp);
	          if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            Element eElement = (Element) nNode; 
	             
	            if(eElement.getAttribute("Symbol").equalsIgnoreCase(currencyExchange)){ // verify for valid exchange rate symbol
	            	_valueCheck = Float.parseFloat(eElement.getElementsByTagName("Bid").item(0).getTextContent());
	            	if(_valueCheck >= targetRate){  // target is equal or greater than expected value
	          		  targetReached = true;
	          		  break;
	          	     }
	                else{  // if target is not reached leave notification to user displaying current value. (optional). w can also display rest of the details too
	            		System.out.println("Target Not Reached. Current Bid rate is "+eElement.getElementsByTagName("Bid").item(0).getTextContent());
	                 }
	            }
	           }
	        }
	       } catch (Exception e) {
	        e.printStackTrace();
	       }
	
	
	return targetReached;
}
  
}