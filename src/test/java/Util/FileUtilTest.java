package Util;

import org.junit.Test;

/**
 * Created by hzzhuohaizhen on 2016/5/16.
 */
public class FileUtilTest
{
    @Test
    public void testLargeFileIO()
    {
        String inputFile = "D:\\Users\\hzzhuohaizhen\\IdeaProjects\\HotTopicDetection" +
                "\\Data\\OriginData\\h12chatsample\\15Chat2016040100_2016040123_20160401000000";
        String outputFile = "D:\\Users\\hzzhuohaizhen\\IdeaProjects\\HotTopicDetection\\Data\\testOutputFile.txt";
        FileUtil.largeFileIO(inputFile,outputFile,"utf-8");
    }
}
