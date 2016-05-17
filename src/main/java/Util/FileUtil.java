package Util;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by hzzhuohaizhen on 2016/5/12.
 * @Description �ļ�����
 */
public class FileUtil {
    /**
     * @Description ��ȡ�ļ�
     * @param pathname
     */
    public static BufferedReader ReadFile(String pathname)
    {
        try {
            /* ����TXT�ļ� */
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // ����һ������������reader
            BufferedReader br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������
            return br;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description ʹ��ָ�������ʽ��ȡ�ļ�
     * @param pathname
     */
    public static BufferedReader ReadFile(String pathname,String encoding)
    {
        try {
            /* ����TXT�ļ� */
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),encoding); // ����һ������������reader
            BufferedReader br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������
            return br;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description д���ļ�
     * @param pathname
     */
    public static void WirteFile(String text,String pathname)
    {
        try {
            /* д��Txt�ļ� */
            File writename = new File(pathname);
            writename.createNewFile(); // �������ļ�
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(text); // \r\n��Ϊ����
            out.flush(); // �ѻ���������ѹ���ļ�
            out.close(); // ���ǵùر��ļ�

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description ��ȡ���ļ�
     * @param inputFile
     * @param outputFile
     */
    public static void largeFileIO(String inputFile, String outputFile,String encoding) {

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            BufferedReader in = null;
            if(encoding==null)
            {
                in = new BufferedReader(new InputStreamReader(bis), 10 * 1024 * 1024);//10M����
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);//10M����
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

