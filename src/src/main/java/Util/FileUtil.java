package Util;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by hzzhuohaizhen on 2016/5/12.
 * @Description 文件工具
 */
public class FileUtil {
    /**
     * @Description 读取文件
     * @param pathname
     */
    public static BufferedReader ReadFile(String pathname)
    {
        try {
            /* 读入TXT文件 */
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            return br;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description 使用指定编码格式读取文件
     * @param pathname
     */
    public static BufferedReader ReadFile(String pathname,String encoding)
    {
        try {
            /* 读入TXT文件 */
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),encoding); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            return br;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description 写入文件
     * @param pathname
     */
    public static void WirteFile(String text,String pathname)
    {
        try {
            /* 写入Txt文件 */
            File writename = new File(pathname);
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(text); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 读取大文件
     * @param inputFile
     * @param outputFile
     */
    public static void largeFileIO(String inputFile, String outputFile,String encoding) {

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            BufferedReader in = null;
            if(encoding==null)
            {
                in = new BufferedReader(new InputStreamReader(bis), 10 * 1024 * 1024);//10M缓存
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);//10M缓存
            }

            FileWriter fw = new FileWriter(outputFile);
            while (in.ready()) {
                String line = in.readLine();
                fw.append(line + "\n");
            }
            in.close();
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

