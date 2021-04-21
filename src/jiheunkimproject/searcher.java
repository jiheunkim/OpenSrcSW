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
   private int N;
   
   @SuppressWarnings("unchecked")
   public void CalcSim(String postname, String query) throws Exception, Throwable {
      // Innerproduct 가져오기
      double[] Ip = new double[N];
      Ip = Innerproduct(postname, query);
      System.out.println();
      
      // cosine similarity 구하기
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
        
      String[] title = new String[N];
      double[] Sim = new double[N];
      double Wq = 0;
        
        for(int i=0;i<N;i++) {
           Element element = (Element) list.item(i);
           title[i] = getChildren(element, "title");
           System.out.print(title[i] + "\t");
           Sim[i] = 0;
        }
        System.out.println();
        
        for(int k=0;k<kl.size();k++) {
           Keyword kwrd = kl.get(k);
         savekkma[k] = kwrd.getString();
         Wq += Math.pow(1, 2);
        }
        Wq = Math.sqrt(Wq);
        
        for(int k=0;k<savekkma.length;k++) {
           if(hashMap.containsKey(savekkma[k])) {
              List<String> value = hashMap.get(savekkma[k]);
              for(int i=0;i<value.size()/2;i++) {
                 Sim[Integer.parseInt(String.valueOf(value.get(i*2)))] += Math.pow(Double.parseDouble(String.valueOf(value.get(i*2+1))), 2);
              }
           }
        }
        
        int[] titlerank = new int[Sim.length];
        for(int i=0;i<Sim.length;i++) {
           if(Ip[i]==0) {
              Sim[i] = 0;
           }else {
              Sim[i] = Math.round(Ip[i]/(Wq*Math.sqrt(Sim[i]))*100)/100.0;
           }
           titlerank[i] = i;
          System.out.print(Sim[i]+"\t");
        }
        
        System.out.println();
      System.out.println("\n***코사인 유사도 기반 상위 3위 문서 title 출력***");
        for(int i=0;i<Sim.length;i++) {
           for(int j=i+1;j<Sim.length;j++) {
              if(Sim[i]<Sim[j]) {
                  int temp = titlerank[i];
                  titlerank[i] = titlerank[j];
                  titlerank[j] = temp;
               }else if(Sim[i]==Sim[j]) {
                  if(titlerank[i]>titlerank[j]) {
                     int temp = titlerank[i];
                      titlerank[i] = titlerank[j];
                      titlerank[j] = temp;
                  }
               }
           }
        }
        for(int i=0;i<3;i++) {
           System.out.println((i+1) + "위: " + title[titlerank[i]]);
        }
   }
   
   @SuppressWarnings({ "unchecked" })
   public double[] Innerproduct(String postname, String query) throws Exception, Throwable {
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
        N = list.getLength();
        
      String[] title = new String[N];
      double[] inner_product = new double[N];
        
        for(int i=0;i<N;i++) {
           Element element = (Element) list.item(i);
           title[i] = getChildren(element, "title");
        }
        
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
        for(int i=0;i<CalcSimsize;i++) {
           inner_product[i] = Math.round(inner_product[i]*100)/100.0;
        }

      return inner_product;
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