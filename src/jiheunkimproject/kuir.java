package jiheunkimproject;

import java.io.IOException;

public class kuir {
	public static void main(String[] args) throws IOException, Throwable {
		// TODO Auto-generated method stub
		if(args == null || args.length < 2) {
			System.out.println("ERROR: ���ڸ� �Է��ϼ���.");
			return;
		}
		
		if("-c".equals(args[0])) {
			makeCollection mc = new makeCollection();
			mc.makeXML(args[1]);
		}else if("-k".equals(args[0])) {
			makeKeyword mk = new makeKeyword();
			mk.kkma(args[1]);
		}
		
//		// doc �ʱ����� �ۼ�
//		mc.makeXML("src/data/");

//		// ���¼� �۾�
//		mk.kkma("src/collection.xml");
	}

}
