package com.bigdata.struct.string;

import javax.print.DocFlavor;
import java.util.HashMap;

public class StringUtil {


    /**
     * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
     *
     * @return
     */
    int lengthOfLongestSubstring(String str) {
        int max = 0;
        int begin = 0;
        int end = 0;
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < str.length(); i++) {
            if (!map.containsKey(str.charAt(i))) {
                map.put(str.charAt(i), i);
                end++;
            } else {
                begin = map.get(str.charAt(i))+1;
                map.put(str.charAt(i), end);
            }
            max = Math.max(max, end - begin);
        }
        System.out.println(max);
        return max;
    }

    public static void main(String[] args) {
        StringUtil stringUtil = new StringUtil();
        stringUtil.lengthOfLongestSubstring("bbbbb");
    }
}
