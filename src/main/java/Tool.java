package main.java;

public class Tool {
    public static int lengthOfVar(int nums) {
        int length = 1;
        int sum = Build_in.firstChar; 
        while (nums > sum) {
            sum *= Build_in.notFirstChar;
            length++;
        }
        return length;
    }

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
