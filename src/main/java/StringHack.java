package main.java;

/**
 *
 * 做一些收尾的字符串操作
 * @author
 */
public class StringHack {
    
	/**
	 *
	 * 将转义字符 '\\'u 改变位 '\'u
     *
	 * @param string
	 * @return
	 */
    public String escapedCharacters(String string) {
        //        return string.replace("\\u", "u");
        return string.replaceAll("\\\\(u([0-9]|[a-f]){4})", "$1");
    }
}
