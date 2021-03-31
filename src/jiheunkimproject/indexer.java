package jiheunkimproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class indexer {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void makepost(String xmlname) throws IOException, ClassNotFoundException, Throwable {
		// 팩토리 생성
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        // 빌더 생성
        DocumentBuilder builder = dbfactory.newDocumentBuilder();
        // Document오브젝트 취득
        Document document = builder.parse(new File(xmlname));
        document.getDocumentElement().normalize();
        
        // 루트의 자식 노드 취득
        Element root = document.getDocumentElement();
        // 각노드의 리스트 취득
        NodeList list = root.getElementsByTagName("doc");
        // html 문서 개수
		int N = list.getLength();
		
        HashMap foodMap = new HashMap();
        
        String[][] key = new String[N][];
        int[][] tf = new int[N][];    
        
        for(int i=0;i<N;i++) {
        	Element element = (Element) list.item(i);       
            String body = getChildren(element, "body");
            key[i] = body.split("#");
        }
        
        for(int i=0;i<key.length;i++) {
        	for(int j=0;j<key[i].length;j++) {		
        		String[] totallist = key[i][j].split(":");
        		String finalkey = totallist[0];
        		int tfnum = Integer.valueOf(totallist[1]);
        		int df = calculatedf(finalkey, key);
        		double origin = tfnum*Math.log(N/df);
        		double weight = (Math.round(origin*100)/100.0);
        		makeHashmap(finalkey,weight,i,foodMap);
        		System.out.println(finalkey+" -> "+ weight + " -> " + foodMap.get(finalkey));
        	}
        }
        
		FileOutputStream fileoutStream = new FileOutputStream("src/index.post");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileoutStream);
		objectOutputStream.writeObject(foodMap);
		objectOutputStream.close();
		
		//Hashmap 객체 출력
		FileInputStream fileinStream = new FileInputStream("src/index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileinStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		HashMap hashMap = (HashMap) object;
		Iterator<String> it = hashMap.keySet().iterator();
		
		while(it.hasNext()) {
			String Key = it.next();
			System.out.println(Key + " -> " + foodMap.get(Key));
		}
	}

	private void makeHashmap(String key, double weight, int i, HashMap<String, ArrayList<Object>> foodMap) throws IOException {
		if(foodMap.containsKey(key)) {
			ArrayList<Object> value = (ArrayList<Object>) foodMap.get(key);
			value.add(i);
			value.add(weight);
			foodMap.put(key, value);
		}else {
			ArrayList<Object> value = new ArrayList<Object>();
			value.add(i);
			value.add(weight);
			foodMap.put(key, value);
		}
	}

	private int calculatedf(String key, String[][] bodystr) {
		int df = 0;
		for(int i=0;i<bodystr.length;i++) {
			boolean condition = false;
			for(int j=0;j<bodystr[i].length;j++) {
				String checkstr = bodystr[i][j];
				if(checkstr.contains(key)) {
					condition = true;
				}
			}
			if(condition==true) {
				df++;
			}
		}
		return df;
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
