package Util;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统红包信息和系统频道所有信息都没有记录在log中
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
    private String regEx = "求组|组我|组队|进组|进队|来人|随便来|" +
            "来一|来两|来二|来1|来2|来个|1来|2来|3来|4来|5来|6来|7来|8来|9来|10来|" +
            "神来|星来|来输出|来治疗|来奶|来大奶|来活人|来尸体|组上|开组|开车|上车|" +
            "司机|求带|带我|组我|加我|的加|走一个|走起|正在大荒寻找有缘人,快来结交一下吧|" +
            "开门迎客,欢迎各位天选者前来探访|[我的空间]";
    private Pattern p = Pattern.compile(regEx);
    private int countAllValidReview = 0;
    private int countAllReview = 0;
    private int[] arrFileWriterCount = new int[8];
    private FileWriter fileWriter = null;
    private List<FileWriter> fileWriterList = new ArrayList<FileWriter>();

    private String []allDate = new String[]{"20160406","20160407",
            "20160408","20160409","20160412","20160413","20160414",
            "20160415","20160416","20160417","20160418","20160419",
            "20160420","20160427","20160428","20160429","20160430"};
    private String []allPartFile= new String[]{"00","01", "02","03","04","05","06",
            "07","08","09","10","11", "12","13","14","15"};

    private String []allTimePeriod= new String[]{
        "2016-04-06—19:04:00_2016-04-07-09:00:00.txt",
        "2016-04-07-10:00:00_2016-04-09-10:00:00.txt",
        "2016-04-12-18:53:00_2016-04-14-08:00:00.txt",
        "2016-04-14-10:00:00_2016-04-16-10:00:00.txt",
        "2016-04-16-17:23:00_2016-04-18-09:00:00.txt",
        "2016-04-18-10:00:00_2016-04-20-10:00:00.txt",
        "2016-04-27-17:09:00_2016-04-28-09:00:00.txt",
        "2016-04-28-10:00:00_2016-04-30-10:00:00.txt"};

    public void initialUserList()
    {
        userList[0] = FilterDataUtil.readUserAccountIDList("Data/FilteredConfig/UserList/4.6-4.9.txt");
        userList[1] = FilterDataUtil.readUserAccountIDList("Data/FilteredConfig/UserList/4.12-4.16.txt");
        userList[2] = FilterDataUtil.readUserAccountIDList("Data/FilteredConfig/UserList/4.16-4.20.txt");
        userList[3] = FilterDataUtil.readUserAccountIDList("Data/FilteredConfig/UserList/4.27-4.30.txt");
        for(int i=0;i<userList.length;i++)
        {
            System.err.println(allTimePeriod[i*2]+"用户名单数: "+userList[i].size());
            System.err.println(allTimePeriod[i*2+1]+"用户名单数: "+userList[i].size());
        }
    }

    @Test
    public void testFilterReview()
    {
        //初始化userList
        initialUserList();
        String inputFilePath = "Data/OriginData/dt=";
        try {
            fileWriter = new FileWriter("Data/allValidReview.txt");
            for (int i=0;i<allTimePeriod.length;i++)
            {
                fileWriterList.add(new FileWriter(allTimePeriod[i]));
            }

            for(String date:allDate)
            {
                for(String partFile:allPartFile )
                {
                    String inputFile = inputFilePath+date+"/"+partFile+"Chat"+date+"00_"+date+"23_"+date+"000000";
                    FilterReview(inputFile);
                }
            }
            System.err.println("All Review Count: "+ countAllReview);
            System.err.println("All Valid Review Count: "+ countAllValidReview);

            for(int i=0;i<arrFileWriterCount.length;i++)
            {
                System.err.println("the count of "+i+" period: "+ arrFileWriterCount[i]);
            }

            fileWriter.flush();
            fileWriter.close();
            for (int i=0;i<allTimePeriod.length;i++)
            {
                fileWriterList.get(i).flush();
                fileWriterList.get(i).close();
            }



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
                in = new BufferedReader(new InputStreamReader(bis), 25 * 1024 * 1024);//10M缓存
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis,encoding), 25 * 1024 * 1024);//10M缓存
            }

            FileWriter fw = new FileWriter(inputFile+"_output.txt");
            while (in.ready()) {
                String line = in.readLine();
                if(line==null)
                    continue;
                reviewInfo = line.split("\t");
                time = reviewInfo[0];
                account_id = reviewInfo[2];
                role_id = reviewInfo[5];
                time_stamp = Long.parseLong(reviewInfo[8]);//时间戳
                content = reviewInfo[9];//评论内容
                channel = reviewInfo[19];//频道,世界(world)
                //首先判断是不是时间范围内，且是否属于关注玩家的列表里
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


                //屏蔽组队频道和势力频道中的组队信息，（系统红包信息和系统发布的信息全都没有记录，所以不需要过滤）
                if((channel.equals("team")||channel.equals("faction"))&&content.contains("中创建了队伍,一起来玩吧"))
                {
                    //System.err.println(channel+"***********"+content);
                    continue;
                }


                //过滤掉含有屏蔽词的评论（包括特殊语句）
                Matcher m = p.matcher(content);
                if(m.find()) {
                    //System.err.println(channel + "***********" + content+"********"+m.find());
                    continue;
                }

                if(content.length()<10&&(content.contains("组")||content.contains("来")))
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
