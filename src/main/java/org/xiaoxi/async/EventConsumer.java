package org.xiaoxi.async;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.xiaoxi.utils.RedisAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by YanYang on 2016/7/21.
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    RedisAdapter redisAdapter;

    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType eventType : eventTypes) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<EventHandler>());
                    }

                    config.get(eventType).add(entry.getValue());
                }
            }
        }

        //启动线程池去消费message
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executorService.submit(new ConsumerThread());
        }

    }

    //消费线程类
    class ConsumerThread implements Runnable {

        public void run() {
            while (true) {
                String key = redisAdapter.getEventKey();
                List<String> messages = redisAdapter.brpop(0, key);

                synchronized (this) {
                    for (String message : messages) {
                        if (message.equals(key)) {
                            continue;
                        }

                        EventModel model = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(model.getEventType())) {
                            logger.error("不能处理的事件" + model.getEventType());
                            continue;
                        }

                        for (EventHandler eventHandler : config.get(model.getEventType())) {
                            eventHandler.doHandle(model);
                        }
                    }
                }
            }
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
