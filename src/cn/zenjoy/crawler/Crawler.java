package cn.zenjoy.crawler;

import cn.zenjoy.util.CommonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.*;

public class Crawler {
    public static int THREAD_NUM = 2;
    public static int MAX_RETRY_TIME = 3;
    public static Map<String, String> Over = new HashMap<>();
    public static BlockingDeque<String> PageQue = new LinkedBlockingDeque<>();
    public static BlockingDeque<Thread> WaitQue = new LinkedBlockingDeque<>();
    public static BlockingDeque<Map<String, String>> PhoneQue = new LinkedBlockingDeque<>();

    static {
        Over.put("Over", "Over");
    }

    public static void main(String[] args) {
        // 启动保存文件线程
        new SaveThread().start();

        //新建一个文件
        File file = new File("./log.txt");
        PrintStream stream = null;
        try {
            //创建文件的输出流
            stream = new PrintStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            CommonUtil.GetPageUrlList();
            for (int i = 0; i < THREAD_NUM; i++) {
                // 启动插入线程
                new CrawlerByPageThread().start();
            }
            for (int i = 0; i < THREAD_NUM; i++) {
                System.out.println(WaitQue.take().getName() + "结束!");
            }
            // 说明全部线程都结束了, 发布结束标志
            PhoneQue.put(Over);
            System.out.println("main线程结束!");
        } catch (Exception e) {
            e.printStackTrace(stream);
        }
    }
}
