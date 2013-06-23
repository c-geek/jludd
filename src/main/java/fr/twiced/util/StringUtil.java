package fr.twiced.util;

public class StringUtil {

	public static boolean isFilled(String str){
		return !isEmpty(str);
	}

	public static boolean isEmpty(String str){
		return str == null || str.length() == 0;
	}
}
