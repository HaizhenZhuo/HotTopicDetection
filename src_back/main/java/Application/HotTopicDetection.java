package Application;

import java.io.*;

/**
 * Created by hzzhuohaizhen on 2016/5/16.
 */
public class HotTopicDetection
{
    private int dateid;
    private int game_id;
    private int period; //从dateid算起一共perid天


    public HotTopicDetection(){}
    public HotTopicDetection(int dateid,int game_id)
    {
        this.dateid = dateid;
        this.game_id = game_id;
    }

    public static void main(String[] args)
    {
        String inputFile = "Data\\OriginData\\h12chatsample\\15Chat2016040100_2016040123_20160401000000";
        String outputFile = "D:\\Users\\hzzhuohaizhen\\IdeaProjects\\HotTopicDetection\\Data\\result.txt";
        String encoding = "utf-8";

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            BufferedReader in = null;
            if(encoding==null)
            {
                in = new BufferedReader(new InputStreamReader(bis), 10 * 1024 * 1024);//10M缓存
            }
            else
            {
                in = new BufferedReader(new InputStreamReader(bis, encoding), 10 * 1024 * 1024);//10M缓存
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
