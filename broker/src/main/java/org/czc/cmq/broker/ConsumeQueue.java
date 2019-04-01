package org.czc.cmq.broker;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ConsumeQueue {

    private long offset;
    private MappedByteBuffer byteBuffer;
    private FileChannel channel;

    public FileChannel getChannel() {
        return channel;
    }

    public void setChannel(FileChannel channel) {
        this.channel = channel;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public MappedByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(MappedByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }
}
