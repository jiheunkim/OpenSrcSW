package jiheunkimproject;

import java.io.IOException;

public class kuir {
	public static void main(String[] args) throws IOException, Throwable {
		// TODO Auto-generated method stub
//		String pathToXml = args[0];
		
		makeCollection mc = new makeCollection();
		makeKeyword mk = new makeKeyword();
		
		// doc 초기파일 작성
		mc.makeXML("src/data/");
//		// body에 내용 채우기
//		mc.makeBody(doc, pathToXml);
//		
		// 형태소 작업
		mk.kkma("src/collection.xml");
		// index XML파일 생성
//		mk.makeBody("index", doc);
	}

}
