package Util;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ϵͳ�����Ϣ��ϵͳƵ��������Ϣ��û�м�¼��log�У����Բ���Ҫ����
 * Created by hzzhuohaizhen on 2016/5/19.
 *
 */
public class FirstFilter
{
    private String encoding = null;//Ĭ�϶�ȡ�ļ�ʹ��UTF-8
    private String regEx = "\\+q|��Q|^(#[0-9]+)+$|^[\\pP\\pSa-zA-Z0-9]+$|^([0-9]*)$";//������
//    private String regEx = "^[\\pP\\pSa-zA-Z0-9]+$";//������

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
                System.err.println("the count of "+fileName.substring(0,fileName.length()-4)+"_firstFilter period: "+ arrCountAllTimePeriodList.get(i));
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
            String role_name = null;
            String channel = null;

            if(encoding==null||encoding=="")
            {
                in = new BufferedReader(new InputStreamReader(bis), 25 * 1024 * 1024);//25M����
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis,encoding), 25 * 1024 * 1024);//25M����
            }
            String outputFile = inputFile.substring(0,inputFile.length()-4)+"_firstFilter.txt";
            FileWriter fileWriter_output = new FileWriter(outputFile);
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
                role_name = reviewInfo[11];//Ƶ��,����(world)
                //�����ж��ǲ���ʱ�䷶Χ�ڣ����Ƿ����ڹ�ע��ҵ��б���
                countAllReview++;
                countReview++;
                //���˵��������δʵ����ۣ�����������䣩
                Matcher m = pattern.matcher(content);
                if(m.find()||content.length()<=1) {
                    System.err.println(content);
                    continue;
                }



                countAllValidReview++;
                fileWriter.append(line + "\n");

                countValidReview_inputFile++;
//                fileWriter_output.append(countValidReview_inputFile+"\t"+line + "\n");
//                fileWriter_output.append(content + "\t"+role_name+'\t'+channel+'\n');
                fileWriter_output.append(line+'\n');
            }
            arrCountAllTimePeriodList.add(countValidReview_inputFile);
            System.err.println(inputFile+"----------"+countReview);
            in.close();
            fileWriter_output.flush();
            fileWriter_output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
