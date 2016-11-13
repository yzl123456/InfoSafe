package hznu.grade15x.utils;

import java.util.Random;
/*
 * 此为工具类，得到16位纯数字的字符串
 * 特别要注意必须是纯数字的，纯英文字符的绝对不行，不然好像和谷歌提供的那个算法不匹配
 */
public class GetRandomString {
	private static String string = "123456789";   
	 
	public static String getRandomString(int length){
	    StringBuffer sb = new StringBuffer();
	    Random random=new Random();
	    
	    int len = string.length();
	    for (int i = 0; i < length; i++) {
	        sb.append(string.charAt(random.nextInt(8)));
	    }
	    return sb.toString();
	}
}
