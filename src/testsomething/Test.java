package testsomething;

import java.util.ArrayList;

public class Test {
	
	public ArrayList<Integer> tachTu(String ip) {
		
		ArrayList<Integer> rs = new ArrayList<Integer>();
		for (int i = 0; i < ip.length(); i++) {
		    char c = ip.charAt(i);
		    //System.out.println(Character.isUpperCase(c));
		    if (Character.isUpperCase(c)) {
		    	rs.add(i);
		    }
		}
		
		return rs;
	}
	
	public ArrayList<String> listWord(String ip){
		ArrayList<Integer> listUpper = new ArrayList<Integer>();
		ArrayList<String> listString = new ArrayList<String>();
		
		listUpper = tachTu(ip);
		System.out.println(listUpper);
		if (listUpper.size() == 0) {
			listString.add(ip);
		}else {
			listString.add(ip.substring(0, listUpper.get(0)));
			for (int i=0 ; i<listUpper.size()-1 ; i++) {
				listString.add(ip.substring(listUpper.get(i), listUpper.get(i+1)));
			}
			listString.add(ip.substring(listUpper.get(listUpper.size()-1)));
		}
		
		return listString;
	}
	
	public static void main (String[] args) {
		Test ts = new Test();
		ArrayList<String> listString = new ArrayList<String>();
		String input = "somethingDoNotGetItRight";
		
		listString = ts.listWord(input);
		
		System.out.println(listString);
	}
}
