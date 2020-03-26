package com.nero.java.nio.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 此线程负责向Pipe读取数据
 */
public class Thread2 implements Runnable {
    private Pipe.SourceChannel sourceChannel;

    public Thread2(Pipe.SourceChannel sourceChannel) {
        this.sourceChannel = sourceChannel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);

                //从Pipe.SourceChannel读出数据
                ByteBuffer buf = ByteBuffer.allocate(48);
                sourceChannel.read(buf);
                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                System.out.println();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
