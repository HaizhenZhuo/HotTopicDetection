package Util;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hzzhuohaizhen on 2016/5/16.
 * 602634+47905
 *
 */
public class FilterDataUtilTest
{
    private String encoding = "utf-8";
    private long [] startDate1 = {1459940640,1460458380,1460798580,1461748140};
    private long [] endDate1 = {1459990800,1460592000,1460941200,1461805200};
    private long [] startDate2 = {1459994400,1460599200,1460944800,1461808800};
    private long [] endDate2 = {1460167200,1460772000,1461117600,1461981600};
    private List []userList = new List[startDate1.length];
    private String regEx = "����|����|���|����|����|����|�����|" +
            "��һ|����|����|��1|��2|����|1��|2��|3��|4��|5��|6��|7��|8��|9��|10��|" +
            "����|����|�����|������|����|������|������|��ʬ��|����|����|����|�ϳ�|" +
            "˾��|���|����|����|����|�ļ�|��һ��|����|���ڴ��Ѱ����Ե��,�����ύһ�°�|" +
            "����ӭ��,��ӭ��λ��ѡ��ǰ��̽��|[�ҵĿռ�]";
    private Pattern p = Pattern.compile(regEx);
    private int countAllValidReview = 0;
    private int countAllReview = 0;
    private int[] arrFileWriterCount = new int[8];
    private FileWriter fileWriter = null;
    private FileWriter fileWriter1 = null;
    private FileWriter fileWriter2 = null;
    private FileWriter fileWriter3 = null;
    private FileWriter fileWriter4 = null;
    private FileWriter fileWriter5 = null;
    private FileWriter fileWriter6 = null;
    private FileWriter fileWriter7 = null;
    private FileWriter fileWriter8 = null;
    private List<FileWriter> fileWriterList = new ArrayList<FileWriter>();

    private String []allDate = new String[]{"20160406","20160407",
            "20160408","20160409","20160412","20160413","20160414",
            "20160415","20160416","20160417","20160418","20160419",
            "20160420","20160427","20160428","20160429","20160430"};
    private String []allPartFile= new String[]{"00","01", "02","03","04","05","06",
            "07","08","09","10","11", "12","13","14","15"};
//    private String []allDate = new String[]{"20160406"};
//    private String []allPartFile= new String[]{"15"};
    private String []allTimePeriod= new String[]{
            "01.txt","02.txt","03.txt","04.txt","05.txt","06.txt","07.txt","08.txt"};

    public void initialUserList()
    {
        userList[0] = FilterDataUtil.readUserAccountIDList("Data\\FilteredConfig\\UserList\\4.6-4.9.txt");
        userList[1] = FilterDataUtil.readUserAccountIDList("Data\\FilteredConfig\\UserList\\4.12-4.16.txt");
        userList[2] = FilterDataUtil.readUserAccountIDList("Data\\FilteredConfig\\UserList\\4.16-4.20.txt");
        userList[3] = FilterDataUtil.readUserAccountIDList("Data\\FilteredConfig\\UserList\\4.27-4.30.txt");
        for(int i=0;i<userList.length;i++)
        {
            //System.err.println("userList����--------------"+userList[i].size());
        }
    }

    @Test
    public void testFilterReview()
    {
        //��ʼ��userList
        initialUserList();
        String inputFilePath = "Data\\OriginData\\dt=";
        try {
            fileWriter = new FileWriter("Data\\allValidReview.txt");
            fileWriter1 = new FileWriter(allTimePeriod[0]);
            fileWriter2 = new FileWriter(allTimePeriod[1]);
            fileWriter3 = new FileWriter(allTimePeriod[2]);
            fileWriter4 = new FileWriter(allTimePeriod[3]);
            fileWriter5 = new FileWriter(allTimePeriod[4]);
            fileWriter6 = new FileWriter(allTimePeriod[5]);
            fileWriter7 = new FileWriter(allTimePeriod[6]);
            fileWriter8 = new FileWriter(allTimePeriod[7]);
            fileWriterList.add(fileWriter1);
            fileWriterList.add(fileWriter2);
            fileWriterList.add(fileWriter3);
            fileWriterList.add(fileWriter4);
            fileWriterList.add(fileWriter5);
            fileWriterList.add(fileWriter6);
            fileWriterList.add(fileWriter7);
            fileWriterList.add(fileWriter8);

            for(String date:allDate)
            {
                for(String partFile:allPartFile )
                {
                    String inputFile = inputFilePath+date+"\\"+partFile+"Chat"+date+"00_"+date+"23_"+date+"000000";
                    FilterReview(inputFile);
                }
            }
            System.err.print("All Review Count: "+ countAllValidReview);
            System.err.print("All Valid Review Count: "+ countAllValidReview);

            for(int i=0;i<arrFileWriterCount.length;i++)
            {
                System.err.print("the count of "+i+" period: "+ arrFileWriterCount[i]);
            }

            fileWriter.flush();
            fileWriter1.flush();
            fileWriter2.flush();
            fileWriter3.flush();
            fileWriter4.flush();
            fileWriter5.flush();
            fileWriter6.flush();
            fileWriter7.flush();
            fileWriter8.flush();
            fileWriter.close();
            fileWriter1.close();
            fileWriter2.close();
            fileWriter3.close();
            fileWriter4.close();
            fileWriter5.close();
            fileWriter6.close();
            fileWriter7.close();
            fileWriter8.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void FilterReview(String inputFile)
    {
        try {
            BufferedInputStream bis =
                    new BufferedInputStream(new FileInputStream(new File(inputFile)));
            int countValidReview = 0;
            BufferedReader in = null;
            String []reviewInfo = null;
            long time_stamp = 0;
            String time = null;
            boolean is_valid = false;
            String account_id = null;
            String role_id = null;
            String content = null;
            String channel = null;
            int fileWriterCount = -1;

            if(encoding==null)
            {
                in = new BufferedReader(new InputStreamReader(bis), 25 * 1024 * 1024);//10M����
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis,encoding), 25 * 1024 * 1024);//10M����
            }

            FileWriter fw = new FileWriter(inputFile+"_output.txt");
            while (in.ready()) {
                String line = in.readLine();
                if(line==null)
                    continue;
                reviewInfo = line.split("\t");
                time_stamp = Long.parseLong(reviewInfo[8]);
                time = reviewInfo[0];
                account_id = reviewInfo[2];
                role_id = reviewInfo[5];
                content = reviewInfo[9];
                channel = reviewInfo[19];
                //�����ж��ǲ���ʱ�䷶Χ�ڣ����Ƿ����ڹ�ע��ҵ��б���
                is_valid = false;
                countAllReview++;

                for(int i = 0; i< startDate1.length; i++)
                {
                    if((time_stamp>= startDate1[i]&&time_stamp<= endDate1[i]))
                    {
                        fileWriterCount = 2*i;
                        if(userList[i].contains(role_id))
                            is_valid = true;
                        else
                            break;
                    }
                    else if((time_stamp>= startDate2[i]&&time_stamp<= endDate2[i]))
                    {
                        fileWriterCount = 2*i+1;
                        if(userList[i].contains(role_id))
                            is_valid = true;
                        else
                            break;
                    }
                }
                if(!is_valid)
                    continue;


                //�������Ƶ��������Ƶ���е������Ϣ����ϵͳ�����Ϣ��ϵͳ��������Ϣȫ��û�м�¼�����Բ���Ҫ���ˣ�
                if((channel.equals("team")||channel.equals("faction"))&&content.contains("�д����˶���,һ�������"))
                {
                    //System.err.println(channel+"***********"+content);
                    continue;
                }


                //���˵��������δʵ����ۣ�����������䣩
                Matcher m = p.matcher(content);
                if(m.find()) {
                    //System.err.println(channel + "***********" + content+"********"+m.find());
                    continue;
                }

                if(content.length()<10&&(content.contains("��")||content.contains("��")))
                {
                    //System.err.println(channel+"----------"+content);
                    continue;
                }

                countValidReview++;
                countAllValidReview++;
                fileWriterList.get(fileWriterCount).append(line + "\n");
                arrFileWriterCount[fileWriterCount]++;
                fileWriter.append(line + "\n");
                fw.append(countValidReview+"\t"+line + "\n");
            }
            System.err.println(inputFile+"----------"+countValidReview);
            in.close();
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}