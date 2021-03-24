package jiheunkimproject;

import java.io.File;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class makeCollection {	
	public void makeXML(String filename) throws Throwable {
		// 파일 정보 읽어서 배열로
	    int num = 0;
	    File path = new File(filename);
	    File[] files = path.listFiles();
	    int fileNum = files.length;
	    
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	      
	    org.w3c.dom.Document document = docBuilder.newDocument();
	    Document jsoupdocument = null;
	      
	    // docs element
	    org.w3c.dom.Element docs = document.createElement("docs");
	    document.appendChild(docs);
	      
	    for(int i = num;i < fileNum;i++) {
	    	// doc element
	        org.w3c.dom.Element doc = document.createElement("doc");
	        docs.appendChild(doc);
	         
	        String strnum = i + "";
	        doc.setAttribute("id", strnum);
	       
	        jsoupdocument = Jsoup.parse(files[i], "UTF-8");
	      
	        // title element
	        org.w3c.dom.Element title = document.createElement("title");
	        title.setTextContent(jsoupdocument.title());
	        doc.appendChild(title);
	         
	        // body element
	        org.w3c.dom.Element body = document.createElement("body");
	        body.setTextContent(jsoupdocument.body().text());
	        doc.appendChild(body);
	    }
	      
	    // XML 파일로 쓰기
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	               
	    Transformer transformer = transformerFactory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	               
	    DOMSource source =new DOMSource(document);
	    StreamResult result = new StreamResult(new FileOutputStream(new File("src/collection.xml")));
	               
	    transformer.transform(source, result);
	}
}
