package jiheunkimproject;

import java.io.IOException;

public class kuir {
	public static void main(String[] args) throws IOException, Throwable {
		// TODO Auto-generated method stub
//		String pathToXml = args[0];
		
		makeCollection mc = new makeCollection();
		makeKeyword mk = new makeKeyword();
		
		// doc �ʱ����� �ۼ�
		mc.makeXML("src/data/");
//		// body�� ���� ä���
//		mc.makeBody(doc, pathToXml);
//		
		// ���¼� �۾�
		mk.kkma("src/collection.xml");
		// index XML���� ����
//		mk.makeBody("index", doc);
	}

}
