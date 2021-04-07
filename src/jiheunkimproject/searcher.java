package jiheunkimproject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class searcher {	
	@SuppressWarnings({ "unchecked" })
	public void CalcSim(String postname, String query) throws Exception, Throwable {
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(query, true);
		String[] savekkma = new String[kl.size()];
		
		FileInputStream fileStream = new FileInputStream(postname);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		HashMap<String, List<String>> hashMap = (HashMap<String, List<String>>)object;
		
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbfactory.newDocumentBuilder();
        Document document = builder.parse(new BufferedInputStream(new FileInputStream("src/collection.xml")));
        document.getDocumentElement().normalize();
        
        Element root = document.getDocumentElement();
        NodeList list = root.getElementsByTagName("doc");
        // html 문서 개수
     	int N = list.getLength();
     	
		String[] title = new String[N];
		double[] inner_product = new double[N];
        
     	for(int i=0;i<N;i++) {
        	Element element = (Element) list.item(i);
        	title[i] = getChildren(element, "title");
            System.out.print(title[i] + "\t");
        }
     	System.out.println();
     	
     	for(int k=0;k<kl.size();k++) {
     		Keyword kwrd = kl.get(k);
			savekkma[k] = kwrd.getString();
     	}
     	
     	for(int k=0;k<savekkma.length;k++) {
     		if(hashMap.containsKey(savekkma[k])) {
     			List<String> value = hashMap.get(savekkma[k]);
     			for(int i=0;i<value.size()/2;i++) {
     				inner_product[Integer.parseInt(String.valueOf(value.get(i*2)))] += Double.parseDouble(String.valueOf(value.get(i*2+1)));
     			}
     		}
     	}
     	
     	int CalcSimsize = inner_product.length;
     	int[] titlerank = new int[CalcSimsize];
     	for(int i=0;i<CalcSimsize;i++) {
     		inner_product[i] = Math.round(inner_product[i]*100)/100.0;
     		titlerank[i] = i;
     		System.out.print(inner_product[i] + "\t");
     	}
     	System.out.println();
     	
     	for(int i=0;i<CalcSimsize;i++) {
     		for(int j=i+1;j<CalcSimsize;j++) {
     			if(inner_product[i]<inner_product[j]) {
         			int temp = titlerank[i];
         			titlerank[i] = titlerank[j];
         			titlerank[j] = temp;
         		}else if(inner_product[i]==inner_product[j]) {
         			if(titlerank[i]>titlerank[j]) {
         				int temp = titlerank[i];
             			titlerank[i] = titlerank[j];
             			titlerank[j] = temp;
         			}
         		}
     		}
     	}
     	System.out.println("***상위 3위 문서 title 출력***");
     	for(int i=0;i<3;i++) {
     		System.out.println((i+1) + "위: " + title[titlerank[i]]);
     	}
	}
	
	private static String getChildren(Element element, String tagName) {
		NodeList list = element.getElementsByTagName(tagName);
	    Element cElement = (Element) list.item(0);
	  
	    if(cElement.getFirstChild()!=null) {
	        return cElement.getFirstChild().getNodeValue(); 
	    } else {
	        return "";
	    }
	}
}
