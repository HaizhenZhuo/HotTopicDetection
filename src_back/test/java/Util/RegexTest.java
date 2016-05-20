package Util;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzzhuohaizhen on 2016/5/19.
 */
public class RegexTest
{
    @Test
    public void testRegex()
    {
        String content2 = "�Ե�#123#123�Ǻ�";
        String content3 = "##1#12+Q";
        String content = "O(��_��)O";

        String regEx2 = "\\+q|\\+Q|^(#[0-9]+)+$"; //������
        String regEx3 = "^([0-9]*)$";//������
        String regEx4 = "^[\\pP\\pS]+$";//������
        String regEx = "\\+q|\\+Q|^(#[0-9]+)+$|^[\\pP\\pS]+$|^([0-9]*)$";//������
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(content);
        System.err.println(content+"*********************"+m.find());
    }

    public void testMain() {
        /**
         * ����ƥ�䣺 ����ĸ��������ɣ�������Ϊ�����֣� ����Ϊ����ĸ��
         *
         * ������6λ���ϵ���ĸ��������ɣ� ���ٰ���һ����ĸ�����֣� �����ɴ����ֻ���ĸ��ɡ�
         * ������֤��"^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$"
         *
         */
        Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]+$");
        String[] test = new String[]{
                "0123", "0a", "012aBc", "Abc0123", "ab0123C", "012abc3", "0a1b2c3d", // ƥ������
                "", "abc", "012?&^", "abc&(", "&(*.", "www123   ", "www 123" // Υ������
        };

        for(String t : test){
            System.out.println(String.format("%s \t match %b", t, pattern.matcher(t).find()));
        }

    }
}
