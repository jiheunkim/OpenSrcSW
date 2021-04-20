package midterm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// �Ǽ��� ���� ���� ������� midterm branch�� �� �־����ϴ�.
public class genSnippet {

	public static void main(String[] args) throws Exception, Throwable {
		if(args == null || args.length < 4) {
			System.out.println("ERROR: ���ڸ� �Է��ϼ���.");
			return;
		}
		
		if("-f".equals(args[0])) {
			genSnippet gs = new genSnippet();
			if("-q".equals(args[2])) {
				// filename�� src/input.txt
				gs.output(args[1], args[3]);
			}
		}
	}

	private void output(String filename, String keyword) throws Throwable, Exception {
		String[] splitkw = keyword.split(" ");
		int[] savecount = null;
		int n = 0;
		
        try(Scanner scan = new Scanner(new File(filename))) {
			while(scan.hasNextLine()) {
				String str = scan.nextLine();
				String[] temp = str.split(" ");
				for(int i=0;i<splitkw.length;i++) {
					if(splitkw[i].contains(str)) {
						int count = 0;
						count++;
						savecount[n] = count;
						n++;
					}
				}
				System.out.println(temp[0]+"\t"+temp[1]);
			}
		} catch (FileNotFoundException e) {
			System.err.println("���� �̸��� Ȯ���� �ּ���.");
		}
        
        
	}

}
