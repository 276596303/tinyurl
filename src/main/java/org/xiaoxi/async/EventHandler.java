package org.xiaoxi.async;

import java.util.List;

/**
 * Created by YanYang on 2016/7/20.
 */
public interface EventHandler {

    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
