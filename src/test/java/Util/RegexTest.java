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
//        String content = "对的#123#123呵呵";
        String content = "##1#12";

        String regEx1 = "\\+q|\\+Q|^([#[0-9]]+)$";
        String regEx = "^(#[1-9]+)+$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(content);
        System.err.println(content+"*********************"+m.find());
    }

    public void testMain() {
        /**
         * 正则匹配： 由字母和数字组成，但不能为纯数字， 不能为纯字母。
         *
         * 密码由6位以上的字母和数字组成， 至少包含一个字母和数字， 不能由纯数字或字母组成。
         * 密码验证："^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$"
         *
         */
        Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]+$");
        String[] test = new String[]{
                "0123", "0a", "012aBc", "Abc0123", "ab0123C", "012abc3", "0a1b2c3d", // 匹配数据
                "", "abc", "012?&^", "abc&(", "&(*.", "www123   ", "www 123" // 违规数据
        };

        for(String t : test){
            System.out.println(String.format("%s \t match %b", t, pattern.matcher(t).find()));
        }

    }
}
