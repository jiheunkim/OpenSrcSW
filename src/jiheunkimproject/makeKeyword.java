package jiheunkimproject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class makeKeyword {
	public void kkma(String checkxml) {
		// TODO Auto-generated method stub
		try {
	        // ���丮 ����
	        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
	        // ���� ����
	        DocumentBuilder builder = dbfactory.newDocumentBuilder();
	        // Document������Ʈ ���
	        Document document = builder.parse(new BufferedInputStream(new FileInputStream(checkxml)));
	        
	        // ��Ʈ�� �ڽ� ��� ���
	        Element root = document.getDocumentElement();
	        // ������� ����Ʈ ���
	        NodeList list = root.getElementsByTagName("doc");
	        for (int i = 0; i < list.getLength(); i++) {
	            Element element = (Element) list.item(i);       
	            NodeList bodyList = element.getElementsByTagName("body");
	            String body = getChildren(element, "body");   
	            
	            for(int j=0;j<bodyList.getLength();j++) {
	            	element.removeChild(bodyList.item(j));
	            }
	            
	            Element elebody = document.createElement("body");
	            
	            // init KeywordExtractor
	    		KeywordExtractor ke = new KeywordExtractor();
	    		// extract keywords
	    		KeywordList kl = ke.extractKeyword(body, true);
	    		
	    		// print result
	    		for(int k=0;k<kl.size();k++) {
	    			Keyword kwrd = kl.get(k);
	    			elebody.appendChild(document.createTextNode(kwrd.getString() + ":" + kwrd.getCnt() + "#"));
	    			element.appendChild(elebody);
//	    			System.out.print(kwrd.getString() + ":" + kwrd.getCnt() + "#");
	    		}
	        }
	        
	        // XML ���Ϸ� ����
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		               
		    Transformer transformer = transformerFactory.newTransformer();
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		               
		    DOMSource source =new DOMSource(document);
		    StreamResult result = new StreamResult(new FileOutputStream(new File("src/index.xml")));
		               
		    transformer.transform(source, result);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static String getChildren(Element element, String tagName) {
		NodeList list = element.getElementsByTagName(tagName);
	    Element cElement = (Element) list.item(0);
	  
	    if(cElement.getFirstChild()!=null) {
	        return cElement.getFirstChild().getNodeValue(); 
	    } else {
	        return "";
	    }
	}
}