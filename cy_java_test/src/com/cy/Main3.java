package com.cy;

import java.io.UnsupportedEncodingException;

public class Main3 {

    public static void main(String[] args) throws Exception {
        new CharactersSplit().test();
    }

}

/**
 * 题目： 编写一个截取字符串的函数，输入为一个字符串和字节数， 输出为按字节截取的字符串。 但是要保证汉字不被截半个，如“我ABC”4，
 * 应该截为“我AB”，输入“我ABC汉DEF”，6， 应该输出为“我ABC”而不是“我ABC+汉的半个”。
 * 
 * 解决方法： 中文是占用2个字节的，英文是占用1一个字节， 所以先确定一种编码满足此条件。
 * GB2312、GBK、GB18030，CP936以及CNS11643都满足条件。
 * 
 * @author Eric
 * @version 1.0
 * 
 */
class CharactersSplit {

    /**
     * 
     * @param text
     *            目标字符串
     * @param length
     *            截取长度
     * @param encode
     *            采用的编码方式
     * @return
     * @throws UnsupportedEncodingException
     */
    public String substring(String text, int length, String encode) throws UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int currentLength = 0;
        for (char c : text.toCharArray()) {
            currentLength += String.valueOf(c).getBytes(encode).length;
            if (currentLength <= length) {
                sb.append(c);
            } else {
                break;
            }
        }
        return sb.toString();

    }

    public void test() throws Exception {
        String text = "我ABC汉DEF";
        int length1 = 3;
        int length2 = 6;
        String[] encodes = new String[] { "GB2312", "GBK", "GB18030", "CP936", "CNS11643" };
        for (String encode : encodes) {
            System.out.println(new StringBuilder().append("用").append(encode).append("编码截取字符串 -- 【")
                    .append(text).append("】").append(length1).append("个字节的结果是【")
                    .append(substring(text, length1, encode)).append("】").toString());
            System.out.println(new StringBuilder().append("用").append(encode).append("编码截取字符串 -- 【")
                    .append(text).append("】").append(length2).append("个字节的结果是【")
                    .append(substring(text, length2, encode)).append("】").toString());

        }
    }
}
