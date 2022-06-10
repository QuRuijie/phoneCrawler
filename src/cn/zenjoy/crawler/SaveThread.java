package cn.zenjoy.crawler;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class SaveThread extends Thread {
    public static String Model = "Model";

    @Override
    public void run() {
        boolean isFirst = true;
        try {
            File f = new File("./phone/phoneInfo.json");
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(write);

            // ------------Begin------------
            writer.write("[");
            // 如果插入线程没结束 || 队列里还有值
            while (true) {
                Map<String, String> map = Crawler.PhoneQue.take();
                // 结束标志
                if (map.containsKey("Over")) {
                    break;
                }
                // 符合要求的数据
                if (map.containsKey(Model)) {
                    // json格式化
                    if (!isFirst) writer.write(",");
                    isFirst = false;
                    // 封装新 Map
                    Map<String, Map<String, String>> data = new HashMap<>();
                    data.put(String.valueOf(map.get(Model)), map);

                    writer.write(new JSONObject(data).toString());
                }
            }
            writer.write("]");

            // -------------End-------------
            writer.close();
            System.out.println("Save Thread 结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
