package com.nero.java.nio.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel 写入数据 demo
 */
public class WriteDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("test.txt", "rw");//mode-"rw" 表示可读写
        FileChannel fileChannel = aFile.getChannel();

        //定义一个48字节的Buffer
        ByteBuffer buf = ByteBuffer.allocate(48);

        //写入数据
        buf.put("hello".getBytes());
        buf.flip();
        while (buf.hasRemaining()) {
            fileChannel.write(buf, 10);//从文件的第10个字节开始写入
        }
        buf.clear();//清空缓存

        aFile.close();//最后关闭channel

        System.out.println("写入成功");
    }
}
