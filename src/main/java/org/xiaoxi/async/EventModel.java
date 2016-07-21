package org.xiaoxi.async;


import java.util.Map;

/**
 * Created by YanYang on 2016/7/20.
 */
public class EventModel {
    private EventType eventType;
    private int entityId;
    private int entityType;
    private int ownerId;
    private Map<String, Object> ext;


    public EventModel() {

    }

    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }


    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public EventModel setOwnerId(int ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public EventModel setExt(Map<String, Object> ext) {
        this.ext = ext;
        return this;
    }
}
