package org.czc.cmq.broker;

import com.czc.cmq.util.UtilAll;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class TopicManager {

    Logger logger = LoggerFactory.getLogger(TopicManager.class);

    private TreeSet<String> topicSet = new TreeSet<>();

    public void init() {
        File topicFile = new File(Config.DEFAULT_DIR + "topic.json");
        if (topicFile.exists()) {
            String topics = UtilAll.readAllFile(topicFile);
            if (StringUtils.isNotBlank(topics)) {
                topicSet.addAll(Arrays.asList(topics.split(",")));
            }
        } else {
            try {
                topicFile.createNewFile();
            } catch (IOException e) {
                logger.warn("create topic file failed !", e);
            }
        }
    }


    public Set<String> getTopicSet() {
        return topicSet;
    }
}
