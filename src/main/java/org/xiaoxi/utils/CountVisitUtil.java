package org.xiaoxi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xiaoxi.async.EventModel;
import org.xiaoxi.async.EventProducer;
import org.xiaoxi.async.EventType;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by YanYang on 2016/7/21.
 *
 * 此类主要用来延迟更新数据（现在内存中缓存一定时间的数据，然后在异步更新到数据库中)
 */
@Component
public class CountVisitUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(CountVisitUtil.class);

    @Autowired
    EventProducer eventProducer;

    //计数容器
    private static ConcurrentHashMap<String, Pair> map = new ConcurrentHashMap<String, Pair>(256);

    public void addHostVisit(String host, long expireTime) {
        Date curTime = new Date();
        if (!map.containsKey(host)) {
            Date expire = new Date(curTime.getTime() + expireTime);
            Pair pair = new Pair(1, expire);
            map.put(host, pair);
        } else {
            Pair pair = map.get(host);
            if (curTime.before(pair.expireTime)) {
                pair.count++;
                map.put(host, pair);
            } else {
                //异步更新url访问log
                //TODO
                System.out.println("异步更新访问次数");
                Map<String, Object> ext = new HashMap<String, Object>();
                ext.put("host", host);
                ext.put("cnt", pair.count);
                ext.put("time", pair.expireTime);
                EventModel eventModel = new EventModel(EventType.URL)
                        .setExt(ext);
                eventProducer.fireEvent(eventModel);
                map.remove(host);
            }
        }
    }

    class Pair {
        public int count;
        public Date expireTime;

        public Pair(int count, Date expireTime) {
            this.count = count;
            this.expireTime = expireTime;
        }
    }

    //启动一个看门狗 监视 计数器
    // TODO 任务部署前 请重新设置 初始时间
    public void afterPropertiesSet() throws Exception {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        // 设置初始时间 无论何时部署都会自动延迟到凌晨 03:00:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 02);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Date time = calendar.getTime();

        long DAY = 1000 * 60 * 60 * 24;
        long delay = time.getTime() - new Date().getTime();
        delay = delay > 0 ? delay : delay + DAY;

        // 固定任务（每天凌晨 03:00:00 执行一次定时任务
        scheduledExecutorService.scheduleAtFixedRate(new Dog(),
                delay, DAY, TimeUnit.MILLISECONDS);
    }


    //看门狗 类
    class Dog implements Runnable {
        public void run() {
            // TODO 内部消化异常， 避免由于异常导致看门狗死掉
            try {
                Date currentTime = new Date();
                for (Map.Entry<String, Pair> entry : map.entrySet()) {
                    String host = entry.getKey();
                    Pair pair = entry.getValue();
                    // 满足过期时间， 异步更新数据
                    if (currentTime.after(pair.expireTime)) {
                        // TODO 定时器异步更新数据
                        System.out.println("定时器异步更新数据访问次数");
                        Map<String, Object> ext = new HashMap<String, Object>();
                        ext.put("host", host);
                        ext.put("cnt", pair.count);
                        ext.put("time", pair.expireTime);
                        EventModel eventModel = new EventModel(EventType.URL)
                                .setExt(ext);
                        eventProducer.fireEvent(eventModel);
                        map.remove(host);
                    }
                }
            } catch (Exception e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                logger.error("data Dog 定时任务执行异常" + dateFormat.format(new Date()) +
                        "（异常内部消化，不会抛出）" + e.getMessage());
            }
        }
    }
}
