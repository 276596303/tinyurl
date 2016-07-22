package org.xiaoxi.async.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoxi.async.EventHandler;
import org.xiaoxi.async.EventModel;
import org.xiaoxi.async.EventType;
import org.xiaoxi.entity.VisitLog;
import org.xiaoxi.service.impl.VisitLogService;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by YanYang on 2016/7/20.
 */
@Component
public class UrlHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(UrlHandler.class);

    @Autowired
    VisitLogService visitLogService;

    public void doHandle(EventModel model) {
        if (model != null) {
            try {
                int entityId = model.getEntityId();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = dateFormat.format(model.getExt().get("time"));
                Date visitTime = dateFormat.parse(dateTime);

                String host = model.getExt().get("host").toString();
                int cnt = Integer.valueOf(model.getExt().get("cnt").toString());

                VisitLog visitLog = new VisitLog();
                visitLog.setHost(host);
                visitLog.setCnt(cnt);
                visitLog.setVisitTime(visitTime);

                visitLogService.addVisitLog(visitLog);
            } catch (Exception e) {
                logger.error("处理器异常" + e.getMessage());
            }
        }
    }

    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.URL);
    }
}
