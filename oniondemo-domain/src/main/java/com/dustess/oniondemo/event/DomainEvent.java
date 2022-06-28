package com.dustess.oniondemo.event;

public interface DomainEvent {

    /**
     * 租户标识
     * @return String
     */
    String account();

    /**
     * 聚合类型
     * @return String
     */
    String aggregateType();

    /**
     * 聚合ID
     * @return String
     */
    String aggregateId();

    /**
     * 事件类型
     * @return String
     */
    String eventType();

    /**
     * 事件版本
     * @return String
     */
    String eventVersion();
}
