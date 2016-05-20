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
public class SecondFilter
{
    private String encoding = null;//Ĭ�϶�ȡ�ļ�ʹ��UTF-8
    private String regEx1 = "����|ά��|����|�����|Ԥѡ��|����|�ռ�|�߼�|����|��ν|ר��|�츳|���|��ϻ|���|�޸�|����|����|����";//4.7�Ÿ���ǰ��Ĺؼ���
    private String regEx2 = "����|ά��|����|�����|Ԥѡ��|����|�ռ�|�߼�|����|��ν|ר��|��Ӣ��|����|����|�³���|���|����|����|����|" +
            "ʮ��|����ս|��Ŀ|����|����|����|�޸�|Ӣ��|���|����|����";//4.14�Ÿ���ǰ��Ĺؼ���
    private String regEx3 = "����|ά��|����|�츳|����|����|���|����|�ϳ�|����|����|�ռ�|���|�ɾ�|�¼�|�޸�|����|����";//4.18�Ÿ���ǰ��Ĺؼ���
    private String regEx4 = "����|ά��|����|ʦͽ|��ʦ|����|ʦ��|ͽ��|��ʦ|��ʦ|��Ӣ��|��о|����|����|����|���|ͨ��|�̵�|����|����|����ս|�޸�|����|����";//4.28�Ÿ���ǰ��Ĺؼ���
//    private String regEx = "^[\\pP\\pSa-zA-Z0-9]+$";//������
    private String []regExList = {regEx1,regEx2,regEx3,regEx4};
//    private Pattern pattern = Pattern.compile(regEx);
    private int countAllValidReview = 0;
    private int countAllReview = 0;
    private FileWriter fileWriter = null;


    //�˸�ʱ���
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
                String regEx = regExList[i/2];
                FilterReview(arrAllTimePeriod[i],regEx);
            }


            System.err.println("All Review Count: "+ countAllReview);
            System.err.println("All Valid Review Count: "+ countAllValidReview);

            for(int i=0;i<arrAllTimePeriod.length;i++)
            {
                String fileName = arrAllTimePeriod[i];
                System.err.println("the count of "+fileName.substring(0,fileName.length()-4)+"_secondFilter period: "+ arrCountAllTimePeriodList.get(i));
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
    public void FilterReview(String inputFile,String regEx)
    {
        try {
            BufferedInputStream bis =
                    new BufferedInputStream(new FileInputStream(new File(inputFile)));
            int countValidReview_inputFile = 0;
            BufferedReader in = null;
            String []reviewInfo = null;
            int countReview = 0;
            String content = null;

            if(encoding==null||encoding=="")
            {
                in = new BufferedReader(new InputStreamReader(bis), 25 * 1024 * 1024);//25M����
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis,encoding), 25 * 1024 * 1024);//25M����
            }
            String outputFile = inputFile.substring(0,inputFile.length()-16)+"_secondFilter.txt";
            FileWriter fileWriter_output = new FileWriter(outputFile);
            while (in.ready()) {
                String line = in.readLine();
                if(line==null)
                    continue;
                reviewInfo = line.split("\t");
                //�����ж��ǲ���ʱ�䷶Χ�ڣ����Ƿ����ڹ�ע��ҵ��б���
                countAllReview++;
                countReview++;
                //���˵��������δʵ����ۣ�����������䣩
//                content = reviewInfo[0];
                content = reviewInfo[9];
                Pattern pattern = Pattern.compile(regEx);
                Matcher m = pattern.matcher(content);
                if(!m.find()) {
                    //System.err.println(content);
                    continue;
                }



                countAllValidReview++;
                fileWriter.append(line + "\n");

                countValidReview_inputFile++;
//                fileWriter_output.append(countValidReview_inputFile+"\t"+line + "\n");
                fileWriter_output.append(line + "\n");
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
