package main.java;

/**
 *
 * 工具函数
 * @author
 */
public class Tool {
    
	/**
	 * 
	 * 判断一个数是十进制几位数
	 * @param nums
	 * @return
	 */
    public static int lengthOfVar(int nums) {
        int length = 1;
        int sum = Build_in.firstChar; 
        while (nums > sum) {
            sum *= Build_in.notFirstChar;
            length++;
        }
        return length;
    }

	/**
	 *
	 * 随机生成一个变量名字符串
	 * @param nums
	 * @return
	 */
    public static String getRandomName(int nums) {
        int len = (int)Math.ceil(Math.random()*nums);
        if (len > 0) {
            char[] name = new char[len];
            name[0] = Build_in.varCharArray[(int)Math.floor(Math.random()*Build_in.firstChar)]; 
            for (int i = 1; i < len; i++) {
                name[i] = Build_in.varCharArray[(int)Math.floor(Math.random()*Build_in.notFirstChar)]; 
            }
            return new String(name);
        } else {
            return null;
        }
    }
}
