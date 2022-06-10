package cn.zenjoy.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.text.StrBuilder;
import cn.zenjoy.crawler.Crawler;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;


public class CommonUtil {
    public static String LOGIN_URL = "https://phonedb.net/index.php?m=device&s=list";
    public static String PHONE_URL = "https://phonedb.net/";
    public static String USER_AGENT = "User-Agent";
    public static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";

    // 获取当前手机详情页的手机信息
    public static Map<String, String> GetPhoneInfo(String phoneUrl) throws Exception {
        Document d = GetDocumentByUrl(phoneUrl);
        Map<String, String> map = new HashMap<>();

        List<Element> tr_list = d.select("tr");
        new StrBuilder();
        for (Element tr : tr_list) {
            String strong = tr.getElementsByTag("strong").eq(0).text();
            if (strong.isEmpty()) {
                continue;
            }
            String value = tr.getElementsByTag("td").eq(1).text().trim();
            map.put(strong, value);
        }
        return map;
    }

    // 分页页面获取当前页的每个Phone的Url
    public static List<String> GetPhoneUrl(String pageUrl) throws Exception {
        Document d = GetDocumentByUrl(pageUrl);
        List<String> list = new ArrayList<>();

        Elements div_list = d.getElementsByClass("content_block_title");
        for (Element div : div_list) {
            Elements link = div.getElementsByTag("a").eq(0);
            list.add(link.attr("href"));
        }

        return list;
    }

    // 获取所有分页的Url
    public static void GetPageUrlList() throws Exception {
        // 获取响应文档
        Document d = GetDocumentByUrl(LOGIN_URL);

        Elements div_list = d.getElementsByClass("container");
        for (Element div : div_list) {
            String text = "Device Specs Pages:";
            // 不是分支直接continue
            if (!div.text().startsWith(text)) {
                continue;
            }

            Elements a_list = div.getElementsByTag("a");
            for (Element a : a_list) {
                // 必须是页数的链接
                if (a.getElementsByAttributeValueContaining("title", "Jump to page").size() == 0 || a.attr("href").isEmpty()) {
                    continue;
                }
                // 加入数组
                Crawler.PageQue.addLast(a.attr("href"));
            }
            break;
        }
    }

    // 根据url 获取 Dom
    public static Document GetDocumentByUrl(String url) throws Exception {
        // 获取connection
        Connection con = Jsoup.connect(getRealUrl(url));
        // 设置响应体无限大
        con.maxBodySize(0);
        // 设置超时时间为一分钟
        con.timeout(1000 * 60);
        // 配置模拟浏览器
        con.header(USER_AGENT, USER_AGENT_VALUE);
        // 获取响应
        Response rs = con.execute();
        // 转换为dom🌲
        return Jsoup.parse(rs.body());
    }

    // 保存Map到Json文件中
    public static void SavePhoneJsonToFile(Map<String, Map<String, String>> phoneMap, int page) {
        try {
            File f = new File("./page" + page + ".txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(write);
            for (Map.Entry<String, Map<String, String>> pageEntry : phoneMap.entrySet()) {
                writer.write("================================================" + pageEntry.getKey() + "================================================" + "\n");
                for (Map.Entry<String, String> phoneEntry : pageEntry.getValue().entrySet()) {
                    writer.write(phoneEntry.getKey() + ":" + phoneEntry.getValue() + "\n");
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getRealUrl(String url) {
        if (url.startsWith(PHONE_URL)) {
            return url;
        }
        return PHONE_URL + url;
    }
}

