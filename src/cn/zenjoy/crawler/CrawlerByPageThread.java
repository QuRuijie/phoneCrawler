package cn.zenjoy.crawler;

import cn.zenjoy.util.CommonUtil;

import java.util.List;

public class CrawlerByPageThread extends Thread {
    @Override
    public void run() {
        // 重试次数
        int time = 0;
        String pageUrl = "";
        while (true) {
            try {
                // 说明这次是重试,先等他一会, 且一次才需要获取 pageUrl
                if (time != 0) {
                    Thread.sleep(1000 * 10);
                }else{
                    pageUrl = Crawler.PageQue.pollFirst();
                }
                System.out.println("第"+time+"次爬取:" + pageUrl);
                if (pageUrl == null) {
                    Crawler.WaitQue.put(Thread.currentThread());
                    break;
                }

                // 获取每一页的每个手机url
                List<String> list = CommonUtil.GetPhoneUrl(pageUrl);

                // 分别请求每一页的每个手机
                for (String phoneUrl : list) {
                    // 获取每个手机的详细信息 Map
                    Crawler.PhoneQue.put(CommonUtil.GetPhoneInfo(phoneUrl));

                    // 慢点慢点
                    Thread.sleep(100);
                }

                // 重置重试次数
                time = 0;
            } catch (Exception e) {
                //  等待一分钟后重试,首先尝试重试
                System.out.println("Retry: " + pageUrl);
                // 超过最大重试次数
                if (++time >= Crawler.MAX_RETRY_TIME) {
                    System.out.println(pageUrl + "Retry Fail !!!!!!");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
