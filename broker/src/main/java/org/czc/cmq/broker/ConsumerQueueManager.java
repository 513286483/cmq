package org.czc.cmq.broker;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerQueueManager {

    private ConcurrentHashMap<String, HashMap<Integer, ConsumeQueue>> topicQueueMap = new ConcurrentHashMap<>();
    private TopicManager topicManager;
    private static final int MSG_FILE_LOGIC_SIZE = Integer.MAX_VALUE / Config.MSG_SIZE;

    public ConsumerQueueManager(TopicManager topicManager) {
        this.topicManager = topicManager;
    }

    public void init() throws IOException {
        Set<String> topicSet = topicManager.getTopicSet();
        File dir = new File(Config.DEFAULT_DIR + "queuefile");
        File[] topicDirs = dir.listFiles();
        for (File topic : topicDirs) {
            HashMap<Integer, ConsumeQueue> map = new HashMap<>();
            if (topicSet.contains(topic.getName())) {
                File[] queueFiles = topic.listFiles();
                if (queueFiles == null || queueFiles.length == 0) {
                    for (int i = 0; i < 4; i++) {
                        ConsumeQueue consumeQueue = new ConsumeQueue();
                        consumeQueue.setOffset(0L);
                        File queueFile = new File(topic.getAbsolutePath() + File.separator + i);
                        queueFile.createNewFile();
                        RandomAccessFile r = new RandomAccessFile(queueFile, "rw");
                        consumeQueue.setChannel(r.getChannel());
                        consumeQueue.setByteBuffer(r.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, MSG_FILE_LOGIC_SIZE * Config.MSG_SIZE));
                        map.put(i, consumeQueue);
                    }
                } else {
                    for (File queue : queueFiles) {
                        ConsumeQueue consumeQueue = new ConsumeQueue();
                        File queueFile = new File(Config.DEFAULT_DIR + "queuefile");
                        RandomAccessFile r = new RandomAccessFile(queueFile, "rw");
                        FileChannel channel = r.getChannel();
                        consumeQueue.setChannel(channel);
                        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, MSG_FILE_LOGIC_SIZE * Config.MSG_SIZE);
                        consumeQueue.setByteBuffer(buffer);
                        ByteBuffer buffer1 = buffer.slice();
                        while (buffer1.remaining() > 0) {
                            int p = buffer1.position();
                            long offset = buffer1.getLong();
                            buffer.getInt();
                            long size = buffer.getLong();
                            if (offset <= 0 || size <= 0) {
                                consumeQueue.setOffset(p);
                                break;
                            }
                        }
                        map.put(Integer.parseInt(queue.getName()), consumeQueue);
                    }
                }
                topicQueueMap.put(topic.getName(), map);
            }
        }

    }

    public TopicManager getTopicManager() {
        return topicManager;
    }

    public void setTopicManager(TopicManager topicManager) {
        this.topicManager = topicManager;
    }


}
