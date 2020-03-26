package com.nero.java.nio.transfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 两个FileChannel 通道之间数据传输
 */
public class FileFileChannel {
    public static void main(String[] args) throws IOException {
        //定义源通道
        RandomAccessFile source = new RandomAccessFile("source.txt", "rw");
        FileChannel sourceChannel = source.getChannel();

        //定义目标通道
        RandomAccessFile target = new RandomAccessFile("target.txt", "rw");
        FileChannel targetChannel = target.getChannel();

        //源通道向目标通道传输数据
        sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);

        source.close();
        target.close();
    }
}
