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
public class FilterDataUtilTest
{
    private String encoding = "utf-8";//Ĭ�϶�ȡ�ļ�ʹ��UTF-8
    private long [] arrStartDate1 = {1459940640,1460458380,1460798580,1461748140}; //����ǰ��ʼʱ���ʱ���
    private long [] arrEndDate1 = {1459990800,1460592000,1460941200,1461805200};   //����ǰ����ʱ���ʱ���
    private long [] arrStartDate2 = {1459994400,1460599200,1460944800,1461808800}; //���º����ʱ���ʱ���
    private long [] arrEndDate2 = {1460167200,1460772000,1461117600,1461981600};   //���º����ʱ���ʱ���
    private List [] arrUserList = new List[arrStartDate1.length];
    private String regEx = "����|����|���|����|����|����|�����|" +
            "��һ|����|����|��1|��2|����|1��|2��|3��|4��|5��|6��|7��|8��|9��|10��|" +
            "����|����|�����|������|����|������|������|��ʬ��|����|����|����|�ϳ�|" +
            "˾��|���|����|����|����|�ļ�|��һ��|����|���ڴ��Ѱ����Ե��,�����ύһ�°�|" +
            "����ӭ��,��ӭ��λ��ѡ��ǰ��̽��|[�ҵĿռ�]";

    private Pattern pattern = Pattern.compile(regEx);
    private int countAllValidReview = 0;
    private int countAllReview = 0;
    private int[] arrFileWriterCount = new int[8];
    private FileWriter fileWriter = null;
    private List<FileWriter> fileWriterList = new ArrayList<FileWriter>();

    //��������
    private String [] arrAllDate = new String[]{"20160406","20160407",
            "20160408","20160409","20160412","20160413","20160414",
            "20160415","20160416","20160417","20160418","20160419",
            "20160420","20160427","20160428","20160429","20160430"};
    //����ȡ�����ļ���ʽͷ
    private String [] arrAllPartFileHead = new String[]{"00","01", "02","03","04","05","06",
            "07","08","09","10","11", "12","13","14","15"};

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

    /**
     * @Description ��ʼ��UserList�������˸�ʱ���һ��
     * ���ķݲ�ͬ�ļ���û�������ÿ�ݼ��������������ʱ���
     */
    public void initialUserList()
    {
        arrUserList[0] = FilterDataUtil.readUserAccountIDList("Data/FilteredConfig/UserList/4.6-4.9.txt");
        arrUserList[1] = FilterDataUtil.readUserAccountIDList("Data/FilteredConfig/UserList/4.12-4.16.txt");
        arrUserList[2] = FilterDataUtil.readUserAccountIDList("Data/FilteredConfig/UserList/4.16-4.20.txt");
        arrUserList[3] = FilterDataUtil.readUserAccountIDList("Data/FilteredConfig/UserList/4.27-4.30.txt");
        for(int i = 0; i< arrUserList.length; i++)
        {
            System.err.println(arrAllTimePeriod[i*2]+"�û�������: "+ arrUserList[i].size());
            System.err.println(arrAllTimePeriod[i*2+1]+"�û�������: "+ arrUserList[i].size());
        }
    }

    @Test
    public void testFilterReview()
    {
        //��ʼ��userList
        initialUserList();
        String inputFilePath = "Data/OriginData/dt=";
        try {
            fileWriter = new FileWriter("Data/allValidReview.txt");
            for (int i = 0; i< arrAllTimePeriod.length; i++)
            {
                fileWriterList.add(new FileWriter(arrAllTimePeriod[i]));
            }

            for(String date: arrAllDate)
            {
                for(String partFile: arrAllPartFileHead)
                {
                    String inputFile = inputFilePath+date+"/"+partFile+"Chat"+date+"00_"+date+"23_"+date+"000000";
                    FilterReview(inputFile);
                }
            }
            System.err.println("All Review Count: "+ countAllReview);
            System.err.println("All Valid Review Count: "+ countAllValidReview);

            for(int i=0;i<arrFileWriterCount.length;i++)
            {
                System.err.println("the count of "+arrAllTimePeriod[i]+" period: "+ arrFileWriterCount[i]);
            }

            fileWriter.flush();
            fileWriter.close();
            for (int i = 0; i< arrAllTimePeriod.length; i++)
            {
                fileWriterList.get(i).flush();
                fileWriterList.get(i).close();
            }

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
            int countValidReview = 0;
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
            int fileWriterCount = -1;

            if(encoding==null)
            {
                in = new BufferedReader(new InputStreamReader(bis), 25 * 1024 * 1024);//25M����
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis,encoding), 25 * 1024 * 1024);//25M����
            }

            FileWriter fw = new FileWriter(inputFile+"_output.txt");
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
                is_valid = false;
                countAllReview++;

                for(int i = 0; i< arrStartDate1.length; i++)
                {
                    if((time_stamp>= arrStartDate1[i]&&time_stamp<= arrEndDate1[i]))
                    {
                        fileWriterCount = 2*i;
                        if(arrUserList[i].contains(role_id))
                            is_valid = true;
                        else
                            break;
                    }
                    else if((time_stamp>= arrStartDate2[i]&&time_stamp<= arrEndDate2[i]))
                    {
                        fileWriterCount = 2*i+1;
                        if(arrUserList[i].contains(role_id))
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
                Matcher m = pattern.matcher(content);
                if(m.find()) {
                    //System.err.println(channel + "***********" + content+"********"+m.find());
                    continue;
                }

                if(content.length()<=10&&(content.contains("��")||content.contains("��")))
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
