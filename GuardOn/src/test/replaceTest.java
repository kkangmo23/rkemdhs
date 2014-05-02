package test;

public class replaceTest {

	public static void main(String[] args) {
		String str = "_08?+<?S\"@";		
		
		if (str.contains("@"))
		str = str.replaceAll("@", "\\$");
		if (str.contains("\""))
		str = str.replaceAll("\"", "#");
		if (str.contains("/"))
		str = str.replaceAll("/", "_");
		
		System.out.println(str);
		
		

	}

}
