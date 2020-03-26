package com.nero.java.nio.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 此线程负责向Pipe写入数据
 */
public class Thread1 implements Runnable {
    private Pipe.SinkChannel sinkChannel;

    public Thread1(Pipe.SinkChannel sinkChannel) {
        this.sinkChannel = sinkChannel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);

                //向Pipe.SinkChannel写入数据
                ByteBuffer buf = ByteBuffer.allocate(48);
                buf.put("hello".getBytes());
                buf.flip();
                while (buf.hasRemaining()) {
                    sinkChannel.write(buf);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}