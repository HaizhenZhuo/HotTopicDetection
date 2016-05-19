package Util;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ϵͳ�����Ϣ��ϵͳƵ��������Ϣ��û�м�¼��log�У����Բ���Ҫ����
 * Created by hzzhuohaizhen on 2016/5/16.
 * 602634+47905
 *
 */
public class FirstFilter
{
    private String encoding = "";//Ĭ�϶�ȡ�ļ�ʹ��UTF-8
    private String regEx = "\\+q|\\+Q|^[[#[0-9]*]*]$";
    private Pattern pattern = Pattern.compile(regEx);
    private int countAllValidReview = 0;
    private int countAllReview = 0;
    private FileWriter fileWriter = null;


    //�˸�ʱ���
    private String [] arrAllTimePeriod = new String[]{
        "2016-04-06-19-04-00_2016-04-07-09-00-00.txt",
        "2016-04-07-10-00-00_2016-04-09-10-00-00.txt",
        "2016-04-12-18-53-00_2016-04-14-08-00-00.txt",
        "2016-04-14-10-00-00_2016-04-16-10-00-00.txt",
        "2016-04-16-17-23-00_2016-04-18-09-00-00.txt",
        "2016-04-18-10-00-00_2016-04-20-10-00-00.txt",
        "2016-04-27-17-09-00_2016-04-28-09-00-00.txt",
        "2016-04-28-10-00-00_2016-04-30-10-00-00.txt"};
    private int [] arrCountAllTimePeriod = new int[8];


    @Test
    public void testFilterReview()
    {

        try {
            fileWriter = new FileWriter("Data/allValidReview.txt");
            for (int i = 0; i< arrAllTimePeriod.length; i++)
            {
                FilterReview(arrAllTimePeriod[i],arrCountAllTimePeriod[i]);
            }


            System.err.println("All Review Count: "+ countAllReview);
            System.err.println("All Valid Review Count: "+ countAllValidReview);

            for(int i=0;i<arrAllTimePeriod.length;i++)
            {
                System.err.println("the count of "+arrAllTimePeriod[i]+" period: "+ arrCountAllTimePeriod[i]);
            }

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * ��ָ���ļ����й�������
     * @param inputFile
     */
    public void FilterReview(String inputFile,int countInputFile)
    {
        try {
            BufferedInputStream bis =
                    new BufferedInputStream(new FileInputStream(new File(inputFile)));
            int countValidReview_inputFile = 0;
            BufferedReader in = null;
            String []reviewInfo = null;
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
                in = new BufferedReader(new InputStreamReader(bis), 25 * 1024 * 1024);//25M����
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis,encoding), 25 * 1024 * 1024);//25M����
            }

            FileWriter fileWriter_output = new FileWriter(inputFile+"_firstFilter.txt");
            while (in.ready()) {
                String line = in.readLine();
                if(line==null)
                    continue;
                reviewInfo = line.split("\t");
                t_when = reviewInfo[0];
                account_id = reviewInfo[2];
                role_id = reviewInfo[5];
                time_stamp = Long.parseLong(reviewInfo[8]);//ʱ���
                content = reviewInfo[9];//��������
                channel = reviewInfo[19];//Ƶ��,����(world)
                //�����ж��ǲ���ʱ�䷶Χ�ڣ����Ƿ����ڹ�ע��ҵ��б���
                countAllReview++;

//                ���˵��������δʵ����ۣ�����������䣩
//                Matcher m = pattern.matcher(content);
//                if(m.find()) {
//                    //System.err.println(channel + "***********" + content+"********"+m.find());
//                    continue;
//                }



                countAllValidReview++;
                fileWriter.append(line + "\n");

                countValidReview_inputFile++;
//                fileWriter_output.append(countValidReview_inputFile+"\t"+line + "\n");
                fileWriter_output.append(line + "\n");
            }
            countInputFile = countValidReview_inputFile;
            System.err.println(inputFile+"----------"+countValidReview_inputFile);
            in.close();
            fileWriter_output.flush();
            fileWriter_output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
