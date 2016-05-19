package Util;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统红包信息和系统频道所有信息都没有记录在log中，所以不需要过滤
 * Created by hzzhuohaizhen on 2016/5/19.
 *
 */
public class ThridFilter
{
    private String encoding = null;//默认读取文件使用UTF-8
    private String churnKeyword[] = {"流失","不想玩","走了","没法玩","弃坑","不玩了","滚蛋",
            "再见","拜拜","弃了","脱坑","解脱","卸载","删","不玩","弃游"};//流失关键词
    private int countChurnKeyword[] = new int[churnKeyword.length];

    private int countAllValidReview = 0;
    private int countAllReview = 0;
    private FileWriter fileWriter = null;


    //八个时间段
    private String [] arrAllTimePeriod = new String[]{
            "2016-04-06-19-04-00_2016-04-07-09-00-00_firstFilter.txt",
            "2016-04-07-10-00-00_2016-04-09-10-00-00_firstFilter.txt",
            "2016-04-12-18-53-00_2016-04-14-08-00-00_firstFilter.txt",
            "2016-04-14-10-00-00_2016-04-16-10-00-00_firstFilter.txt",
            "2016-04-16-17-23-00_2016-04-18-09-00-00_firstFilter.txt",
            "2016-04-18-10-00-00_2016-04-20-10-00-00_firstFilter.txt",
            "2016-04-27-17-09-00_2016-04-28-09-00-00_firstFilter.txt",
            "2016-04-28-10-00-00_2016-04-30-10-00-00_firstFilter.txt"};
    private List<Integer> arrCountAllTimePeriodList = new ArrayList<Integer>();


    @Test
    public void testFilterReview()
    {

        try {
            fileWriter = new FileWriter("Data/allValidReview.txt");
            for (int i = 0; i< arrAllTimePeriod.length; i++)
            {
                FilterReview(arrAllTimePeriod[i]);
            }


            System.err.println("All Review Count: "+ countAllReview);
            System.err.println("All Valid Review Count: "+ countAllValidReview);

            for(int i=0;i<arrAllTimePeriod.length;i++)
            {
                String fileName = arrAllTimePeriod[i];
                System.err.println("the count of "+fileName.substring(0,fileName.length()-4)+"_thirdFilter period: "+ arrCountAllTimePeriodList.get(i));
            }

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 对指定文件进行过滤评论
     * @param inputFile
     */
    public void FilterReview(String inputFile)
    {
        try {
            BufferedInputStream bis =
                    new BufferedInputStream(new FileInputStream(new File(inputFile)));
            int countValidReview_inputFile = 0;
            BufferedReader in = null;
            String []reviewInfo = null;
            int countReview = 0;
            long time_stamp = 0;
            String t_when = null;
            String axis = null;
            String account_id = null;
            String udid = null;
            String ip = null;
            String role_id = null;
            String scene = null;
            String app_channel = null;
            String chat_time = null;
            String content = null;
            boolean is_valid = false;
            String channel = null;

            if(encoding==null||encoding=="")
            {
                in = new BufferedReader(new InputStreamReader(bis), 25 * 1024 * 1024);//25M缓存
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis,encoding), 25 * 1024 * 1024);//25M缓存
            }
            String outputFile = inputFile.substring(0,inputFile.length()-16)+"_thirdFilter.txt";
            FileWriter fileWriter_output = new FileWriter(outputFile);
            while (in.ready()) {
                String line = in.readLine();
                if(line==null)
                    continue;
                reviewInfo = line.split("\t");
                t_when = reviewInfo[0];
                account_id = reviewInfo[2];
                role_id = reviewInfo[5];
                time_stamp = Long.parseLong(reviewInfo[8]);//时间戳
                content = reviewInfo[9];//评论内容
                channel = reviewInfo[19];//频道,世界(world)
                //首先判断是不是时间范围内，且是否属于关注玩家的列表里
                countAllReview++;
                countReview++;
                //过滤掉含有屏蔽词的评论（包括特殊语句）
//                System.err.println(content);
                for(int i = 0;i<churnKeyword.length;i++)
                {
                    if(content.contains(churnKeyword[i])) {
                        countChurnKeyword[i]++;
                        countAllValidReview++;
                        countValidReview_inputFile++;
                        break;
                    }
                }


                fileWriter.append(line + "\n");

                fileWriter_output.append(line + "\n");
            }
            arrCountAllTimePeriodList.add(countValidReview_inputFile);
            System.err.println("时间段:"+inputFile.substring(0,inputFile.length()-16)+"\t总评论数: "+countReview+"\t有流失词的评论数: "+countValidReview_inputFile);
            System.err.println("流失词出现评论数\t总评论数\t出现千分率");
            int temp = 0;
            for(int i = 0;i<churnKeyword.length;i++)
            {
                temp = temp+countChurnKeyword[i];
                countChurnKeyword[i] = 0;
            }
            System.err.println(temp+"/"+countReview+"/"+(1000.00*temp/countReview));

            in.close();
            fileWriter_output.flush();
            fileWriter_output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
