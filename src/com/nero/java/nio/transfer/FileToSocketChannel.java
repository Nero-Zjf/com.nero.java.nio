package com.nero.java.nio.transfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * FileChannel通道向SocketChannel通道传输数据
 */
public class FileToSocketChannel {
    public static void main(String[] args) throws IOException {
        //FileChannel作为源通道
        RandomAccessFile source = new RandomAccessFile("source.txt", "rw");
        FileChannel sourceChannel = source.getChannel();

        //获取SocketChannel
        //需要先开启一个ServerSocketChannel服务器，可启动本项目的noblock.SocketChannelServerDemo
        SocketChannel targetChannel = SocketChannel.open(new InetSocketAddress(8099));

        //FileChannel 向 SocketChannel 传输数据
        sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
    }
}
