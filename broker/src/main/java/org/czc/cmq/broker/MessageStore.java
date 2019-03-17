package org.czc.cmq.broker;

/**
 * Created by chenzhichao on 19/3/17.
 */
public interface MessageStore {

    int putMessage(String topic, int patition, byte[] body);

    int pullMessage(String topic, int patition, long offset, long limit);
}
