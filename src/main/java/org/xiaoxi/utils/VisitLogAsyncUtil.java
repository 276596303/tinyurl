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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by YanYang on 2016/7/22.
 */
@Component
public class VisitLogAsyncUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(VisitLogAsyncUtil.class);

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
    private static final int BLOCKING_SIZE = 1000;  //根据实际情况更改
    private BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<Object>(BLOCKING_SIZE);
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 12; //单位毫秒 //TODO 部署前更改

    private ReentrantLock mapModifyLock = new ReentrantLock();

    @Autowired
    EventProducer eventProducer;

    public Map<String, Object> getMap() {
        return map;
    }

    // 初始化bean 后置方法, 初始化完成后启动消费者线程
    public void afterPropertiesSet() throws Exception {
        //启动四个消费者线程 来消费 host log
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executorService.submit(new ConsumerHost());
        }

        // 启动 周期任务 来清理 未异步到数据库中的数据（防止内存溢出）
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

        // 固定任务（每天凌晨 03:00:00 执行一次定时任务） //TODO 部署前请更改delay时间
        scheduledExecutorService.scheduleAtFixedRate(new Dog(),
                delay, DAY, TimeUnit.MILLISECONDS);
    }

    public boolean addToBlockingQ(String host) {
        try {
            // 无可用空间时, offer不会等待， put会wait； 所以此处为了能够及时返回 用offer
            if (blockingQueue.offer(host)) {
                return true;
            } else {
                logger.error("Host-blockingQueue 中的排队数超过最大容量限制, 自动放弃任务. 最大容量为：" + BLOCKING_SIZE);
                return false;
            }
        } catch (Exception e) {
            logger.error("Host-blockingQueue阻塞超时, 丢弃任务");
            return false;
        }
    }

    // 消费者线程
    class ConsumerHost implements Runnable {
        public void run() {
            try {
                while (true) {
                    // 队列空时, take 会wait  poll不会wait ；所以此处不能用poll 否则会吃满cpu
                    String host = (String)blockingQueue.take();
                    addHostToMap(host);
                }
            } catch (Exception e) {
                logger.error("消费 host 出现中断异常 并内部消化" + e.getMessage());
            }
        }
    }

    //添加 host 到 map 中, 并判断是否可以 异步更新到数据库
    public boolean addHostToMap(String host) {
        mapModifyLock.tryLock();
        try {
            Date currentTime = new Date();
            if (!map.containsKey(host)) {
                Date expireTime = new Date(currentTime.getTime() + EXPIRE_TIME);
                map.put(host, new HostCount(1, expireTime));
            } else {
                HostCount hostCount = (HostCount)map.get(host);
                if (currentTime.before(hostCount.expireTime)) {
                    hostCount.count++;
                } else {
                    //异步更新url访问log
                    //TODO
                    System.out.println("异步更新访问次数");
                    Map<String, Object> ext = new HashMap<String, Object>();
                    ext.put("host", host);
                    ext.put("cnt", hostCount.count);
                    ext.put("time", hostCount.expireTime);
                    EventModel eventModel = new EventModel(EventType.URL)
                            .setExt(ext);
                    eventProducer.fireEvent(eventModel);
                    map.remove(host);
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("并发添加访问日志 失败");
            return false;
        } finally {
            mapModifyLock.unlock();
        }
    }


    //看门狗 类
    class Dog implements Runnable {
        public void run() {
            // TODO 内部消化异常， 避免由于异常导致看门狗死掉
            try {
                Date currentTime = new Date();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String host = entry.getKey();
                    HostCount hostCount = (HostCount)entry.getValue();
                    // 满足过期时间， 异步更新数据
                    if (currentTime.after(hostCount.expireTime)) {
                        // TODO 定时器异步更新数据
                        System.out.println("定时器异步更新数据访问次数");
                        Map<String, Object> ext = new HashMap<String, Object>();
                        ext.put("host", host);
                        ext.put("cnt", hostCount.count);
                        ext.put("time", hostCount.expireTime);
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

    class HostCount {
        public volatile int count;
        public Date expireTime;

        public HostCount(int count, Date expireTime) {
            this.count = count;
            this.expireTime = expireTime;
        }
    }
}
