package org.czc.cmq.broker;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhichao on 19/3/17.
 */
public class CommitLog {

    public static List<RandomAccessFile> logs = new ArrayList<RandomAccessFile>();
    public static final String COMMIT_LOG_NAME_SUFFIX = ".clog";

    public static void init() {

    }

}
