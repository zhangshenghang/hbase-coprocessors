package wiki.hadoop.util;

public class test {

	
	public static void main(String[] args) {
		try {
		int i = 0/0;
		}catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.fillInStackTrace();
			e.printStackTrace();
		}
	}
}
