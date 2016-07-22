package org.xiaoxi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YanYang on 2016/7/21.
 */
public final class HostDecodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(HostDecodeUtil.class);

    public static String getHost(String url) {
        try {
            String host = null;
            if (url != null) {
                int firstIndex = url.indexOf("//");
                if (firstIndex > 0) {
                    url = url.substring(firstIndex + 2, url.length());
                }
                int secondIndex = url.indexOf("/");
                if (secondIndex > 0) {
                    host = url.substring(0, secondIndex);
                } else {
                    host = url;
                }
            }
            return host;
        } catch (Exception e) {
            logger.error("从长网址" + url + "中获取 host 异常" + e.getMessage());
            return null;
        }
    }


    public static void main(String[] args) {
        String url = "www.baidu.com/123";
        String host = getHost(url);
        System.out.println(url);
    }
}
