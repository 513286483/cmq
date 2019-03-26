package org.czc.cmq.broker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

/**
 * Created by chenzhichao on 19/3/17.
 */
public class CommitLog {

    public static List<RandomAccessFile> logs = new ArrayList<RandomAccessFile>();
    public static final String COMMIT_LOG_NAME_SUFFIX = ".clog";
    private MappedByteBuffer buffer;
    FileChannel channel;

    public static int crc32(byte[] array, int offset, int length) {
        CRC32 crc32 = new CRC32();
        crc32.update(array, offset, length);
        return (int) (crc32.getValue() & 0x7FFFFFFF);
    }

    public void CommitLog() throws IOException {
        String logFileName = Config.DEFAULT_DIR + "0" + COMMIT_LOG_NAME_SUFFIX;
        File file = new File(logFileName);
        boolean newFile = false;
        if (!file.exists()) {
            file.createNewFile();
            newFile = true;
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(logFileName, "rw");
        channel = randomAccessFile.getChannel();
        buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0L, 2 * 1024 * 1024 * 1024);
        if (newFile) {
            buffer.position(buffer.limit());
            buffer.force();
            buffer.position(0);
        }
    }

    public void putMessage(ByteBuffer message) {

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
