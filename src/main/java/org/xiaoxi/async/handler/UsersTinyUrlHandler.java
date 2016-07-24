package org.xiaoxi.async.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoxi.async.EventHandler;
import org.xiaoxi.async.EventModel;
import org.xiaoxi.async.EventType;
import org.xiaoxi.entity.UsersTinyUrl;
import org.xiaoxi.service.UsersTinyUrlService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by YanYang on 2016/7/24.
 */
@Component
public class UsersTinyUrlHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(UsersTinyUrlHandler.class);

    @Autowired
    UsersTinyUrlService usersTinyUrlService;

    public void doHandle(EventModel model) {
        if (model != null) {
            Map<String, Object> ext = model.getExt();
            if (ext != null) {
                try {
                    if (ext.get("userId") != null && ext.get("shortId") != null && ext.get("createTime") != null) {
                        int userId = Integer.valueOf(ext.get("userId").toString());
                        int shortId = Integer.valueOf(ext.get("shortId").toString());
                        Date date = new Date((Long)ext.get("createTime"));
                        UsersTinyUrl usersTinyUrl = new UsersTinyUrl();
                        usersTinyUrl.setUserId(userId);
                        usersTinyUrl.setShortUrlId(shortId);
                        usersTinyUrl.setCreateTime(date);
                        //TODO
                        System.out.println("异步更新tinyurl到用户表" + usersTinyUrl);
                        usersTinyUrlService.addTinyUrlToUser(usersTinyUrl);
                    }
                }catch (Exception e) {
                    logger.error("执行异步更新tinyurl到用户表失败" + e.getMessage());
                }
            }
        }
    }

    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.USERS_TINYURL);
    }
}
