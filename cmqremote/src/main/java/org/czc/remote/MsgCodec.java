package org.czc.remote;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class MsgCodec extends ByteToMessageCodec<TransferMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TransferMsg msg, ByteBuf out) throws Exception {
        if (msg != null) {
            out.writeInt(msg.getType());
            byte[] body = msg.getBody();
            if (body != null && body.length > 0) {
                out.writeInt(body.length);
                out.writeBytes(body);
            } else {
                out.writeInt(-1);
            }
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int rindx = 0;
        while (true) {
            if (in.readableBytes() >= Integer.BYTES * 2) {
                rindx = in.readerIndex();
                int type = in.readInt();
                int bodyLen = in.readInt();
                if (bodyLen == -1) {
                    TransferMsg m = new TransferMsg();
                    m.setType(type);
                    out.add(m);
                    continue;
                } else if (in.readableBytes() >= bodyLen) {
                    byte[] arr = new byte[bodyLen];
                    in.readBytes(arr, 0, bodyLen);
                    TransferMsg m = new TransferMsg();
                    m.setType(type);
                    m.setBody(arr);
                    out.add(m);
                } else {
                    in.readerIndex(rindx);
                    break;
                }
            } else {
                break;
            }
        }

    }
}
