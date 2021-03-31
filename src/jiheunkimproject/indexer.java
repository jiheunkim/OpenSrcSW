package jiheunkimproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class indexer {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void makepost(String xmlname) throws IOException, ClassNotFoundException, Throwable {
		// ���丮 ����
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        // ���� ����
        DocumentBuilder builder = dbfactory.newDocumentBuilder();
        // Document������Ʈ ���
        Document document = builder.parse(new File(xmlname));
        document.getDocumentElement().normalize();
        
        // ��Ʈ�� �ڽ� ��� ���
        Element root = document.getDocumentElement();
        // ������� ����Ʈ ���
        NodeList list = root.getElementsByTagName("doc");
        // html ���� ����
		int N = list.getLength();
		
        HashMap foodMap = new HashMap();
        
        String[][] key = new String[N][];
        int[][] tf = new int[N][];
        String[] allbody;
        
        //�ܾ�� Ƚ�� �迭 ����
        for(int i=0;i<N;i++) {
        	Element element = (Element) list.item(i);       
            String body = getChildren(element, "body");
            allbody = body.split(":|#");
            key[i] = new String[allbody.length/2];
            tf[i] = new int[allbody.length/2];
            for(int j=0;j<allbody.length/2;j++) {
            	key[i][j] = allbody[2*j];
            	tf[i][j] = Integer.parseInt(allbody[2*j+1]);
            }
        }
        
        //�ߺ� ����
        int count = 0;
        ArrayList<String> keyword = new ArrayList<String>();
        keyword.add(key[0][0]);
        
        for(int i=0;i<N;i++) {
        	for(int j=0;j<key[i].length;j++) {
        		for(String Key : keyword) {
        			if(Key.equals(key[i][j])) {
        				count++;
        			}
        		}
        		if(count==0) {
        			keyword.add(key[i][j]);
        		}else {
        			count=0;
        		}
        	}
        }

        int kwsize = keyword.size();
        int[] cnt = new int[kwsize];
        for(int i=0;i<kwsize;i++) {
        	cnt[i]=0;
        }
        
        for(int i=0;i<N;i++) {
        	for(int j=0;j<key[i].length;j++) {
        		for(int k=0;k<kwsize;k++) {
        			if(keyword.get(k).equals(key[i][j])) {
        				cnt[k]++;
        			}
        		}
        	}
        }

        for(int i=0;i<kwsize;i++) {
        	ArrayList<Object> value = new ArrayList<Object>();
        	for(int j=0;j<N;j++) {
        		for(int k=0;k<key[j].length;k++) {
        			if(keyword.get(i).equals(key[j][k])) {
        				value.add(j);
        				float weight = (float)(Math.round((tf[j][k]*Math.log((float)N/(float)cnt[i]))*100.0)/100.0);
        				value.add(weight);
        			}
        		}
        	}
        	foodMap.put(keyword.get(i), value);
        	System.out.println(keyword.get(i)+" -> " + foodMap.get(keyword.get(i)));
        }
	}

	private String getChildren(Element element, String tagName) {
		NodeList list = element.getElementsByTagName(tagName);
	    Element cElement = (Element) list.item(0);
	  
	    if(cElement.getFirstChild()!=null) {
	        return cElement.getFirstChild().getNodeValue(); 
	    } else {
	        return "";
	    }
	}

}
