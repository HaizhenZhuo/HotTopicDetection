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
public class SecondFilter
{
    private String encoding = null;//默认读取文件使用UTF-8
    private String regEx1 = "更新|维护|网易|武道会|预选赛|正赛|终极|高级|雕像|称谓|专属|天赋|礼盒|魂匣|红包|修复|免疫|问题|调整";//4.7号更新前后的关键词
    private String regEx2 = "更新|维护|网易|武道会|预选赛|正赛|终极|高级|雕像|称谓|专属|新英雄|毒王|江南|新场景|翅膀|羽翼|二阶|天灾|" +
            "十星|势力战|题目|排行|调整|问题|修复|英灵|凤凰|精华|考试";//4.14号更新前后的关键词
    private String regEx3 = "更新|维护|网易|天赋|治疗|羽翼|翅膀|福袋|合成|传功|考试|空间|光辉|成就|事迹|修复|问题|调整";//4.18号更新前后的关键词
    private String regEx4 = "更新|维护|网易|师徒|拜师|试炼|师父|徒弟|良师|出师|新英雄|可芯|花灵|海寂|羽翼|翅膀|通宝|商店|答题|节日|势力战|修复|问题|调整";//4.28号更新前后的关键词
//    private String regEx = "^[\\pP\\pSa-zA-Z0-9]+$";//纯符号
    private String []regExList = {regEx1,regEx2,regEx3,regEx4};
//    private Pattern pattern = Pattern.compile(regEx);
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
     * 对指定文件进行过滤评论
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
                in = new BufferedReader(new InputStreamReader(bis), 25 * 1024 * 1024);//25M缓存
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis,encoding), 25 * 1024 * 1024);//25M缓存
            }
            String outputFile = inputFile.substring(0,inputFile.length()-16)+"_secondFilter.txt";
            FileWriter fileWriter_output = new FileWriter(outputFile);
            while (in.ready()) {
                String line = in.readLine();
                if(line==null)
                    continue;
                reviewInfo = line.split("\t");
                //首先判断是不是时间范围内，且是否属于关注玩家的列表里
                countAllReview++;
                countReview++;
                //过滤掉含有屏蔽词的评论（包括特殊语句）
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
