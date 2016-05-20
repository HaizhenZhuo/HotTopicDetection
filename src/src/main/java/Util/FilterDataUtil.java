package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzzhuohaizhen on 2016/5/16.
 */
public class FilterDataUtil
{
    /**
     * ����
     */
    public static String filterKeyWords(String line)
    {

        return line;
    }

    /**
     * ֻ�������б�����û�id
     * @param line
     * @return
     */
    public static String retainAccountID(String line,List<String> accountIDList)
    {
        return line;
    }

    /**
     * ���˷�����ID
     * @param line
     * @return
     */
    public static String filterServer(String line)
    {
        return line;
    }

    public static String filterWord(String line)
    {
        return line;
    }

    /**
     * ͨ��ʱ������������
     * @param line
     * @param startDate
     * @param endDate
     * @return
     */
    public static String filterReviewByTime(String line,long []startDate,long []endDate)
    {
        if(startDate.length<=0||startDate.length!=endDate.length)
        {
            System.err.println("��ʼʱ��ͽ�ֹʱ�䲻��");
            return null;
        }

        String []reviewInfo = line.split("\t");
        long time_stamp = Long.parseLong(reviewInfo[8]);
        String time = reviewInfo[0];
        boolean is_valid = false;
        for(int i=0;i<startDate.length;i++)
        {
            if(time_stamp>startDate[i] && time_stamp<endDate[i])
            {
                is_valid = true;
            }
        }
        if(!is_valid)
        {
            System.err.println("����ʱ�䷶Χ��--------------"+time);
            return null;
        }

        System.out.println(reviewInfo.length);
        int i = 0;
        for(String s:reviewInfo)
        {
            System.out.println((i++)+"***********"+s);
        }
        return line;
    }

    /**
     * ���ļ��ж�ȡ��ע��ҵ�ID�б�
     * @param filePath
     * @return
     */
    public static List<String> readUserAccountIDList(String filePath) {
        BufferedReader br = FileUtil.ReadFile(filePath);
        List<String> userList = new ArrayList<String>();
        String line = "";
        if(br==null)
            return null;
        try {
            line = br.readLine();//��һ�в���Ҫ
            line = br.readLine();
            while (line != null) {
                if ("".equals(line)) {
                    line = br.readLine();
                    continue;
                }
                String[] lineList = line.split("\t");
                userList.add(lineList[1]);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
