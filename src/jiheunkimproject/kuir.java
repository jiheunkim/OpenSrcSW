package jiheunkimproject;

import java.io.IOException;

public class kuir {
	public static void main(String[] args) throws IOException, Throwable {
		// TODO Auto-generated method stub
		if(args == null || args.length < 2) {
			System.out.println("ERROR: 인자를 입력하세요.");
			return;
		}
		
		// Arguments값: -c src/data/
		if("-c".equals(args[0])) {
			makeCollection mc = new makeCollection();
			mc.makeXML(args[1]);
		}else if("-k".equals(args[0])) {
			// Arguments값: -k src/collection.xml
			makeKeyword mk = new makeKeyword();
			mk.kkma(args[1]);
		}else if("-i".equals(args[0])) {
			// Arguments값: -i src/index.xml
			indexer ix = new indexer();
			ix.makepost(args[1]);
		}else if("-s".equals(args[0])) {
			// Arguments값 예시: -s src/index.post -q "라면에는 면, 분말 스프가 있다."
			searcher sc = new searcher();
			if("-q".equals(args[2])) {
				sc.CalcSim(args[1], args[3]);
			}
		}
	}

}
