package org.czc.cmq.broker;

import com.czc.cmq.util.UtilAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhichao on 19/3/17.
 */
public class CommitLog {

    private static Logger logger = LoggerFactory.getLogger(CommitLog.class);
    public static List<RandomAccessFile> logs = new ArrayList<RandomAccessFile>();
    public static final String COMMIT_LOG_NAME_SUFFIX = ".clog";
    private MappedByteBuffer buffer;
    private int pos = 0;

    public void init() throws IOException {
        String logFileName = Config.DEFAULT_DIR + "0" + COMMIT_LOG_NAME_SUFFIX;
        File file = new File(logFileName);
        boolean isNewFile = false;
        try {
            if (!file.exists()) {
                file.createNewFile();
                isNewFile = true;
            }
        } catch (Throwable t) {
            logger.error("create log file failed!", t);
            System.exit(1);
        }
        RandomAccessFile logFile = new RandomAccessFile(file, "rw");
        FileChannel channel = logFile.getChannel();
        if (isNewFile) {
            buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);
        } else {
            while (buffer.remaining() >= 8) {
                buffer.mark();
                int len = buffer.getInt();
                int crc = buffer.getInt();
                if (buffer.remaining() > len) {
                    byte[] arr = new byte[len];
                    buffer.get(arr);
                    if(UtilAll.crc32(arr) == crc) {
                        pos = buffer.position();
                    }else {
                        buffer.reset();
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("/Users/chenzhichao/Downloads/a", "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        String s = "woaini";
        byteBuffer.put(s.getBytes());
        channel.write(byteBuffer);
        ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
        channel.read(readBuffer, 0);
        byte[] arr = new byte[s.length()];
        ByteBuffer buffer = readBuffer.get(arr, 0, s.length());
        System.out.println(buffer == readBuffer);
        System.out.println(new String(s));
    }

}
