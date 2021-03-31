package jiheunkimproject;

import java.io.IOException;

public class kuir {
	public static void main(String[] args) throws IOException, Throwable {
		// TODO Auto-generated method stub
		if(args == null || args.length < 2) {
			System.out.println("ERROR: 인자를 입력하세요.");
			return;
		}
		
		if("-c".equals(args[0])) {
			makeCollection mc = new makeCollection();
			mc.makeXML(args[1]);
		}else if("-k".equals(args[0])) {
			makeKeyword mk = new makeKeyword();
			mk.kkma(args[1]);
		}else if("-i".equals(args[0])) {
			indexer ix = new indexer();
			ix.makepost(args[1]);
		}
	}

}
