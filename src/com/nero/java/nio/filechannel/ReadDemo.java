package com.nero.java.nio.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * FileChannel 读取数据 demo
 */
public class ReadDemo {

    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("test.txt", "r");//"r" 表示只读
        FileChannel fileChannel = aFile.getChannel();

        //定义一个48字节的Buffer
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = 0;

        while ((bytesRead = fileChannel.read(buf)) != -1) {//从FileChannel中将数据读出到buffer，返回读取的字节数，如果为-1 则表明读到文件末尾
            System.out.println("Read " + bytesRead);
            buf.flip();//使得buffer准备好被读取
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());//一次读取一个字节
            }
            buf.clear();//清空缓存 以便再次写入
        }

        aFile.close();//最后关闭channel
    }
}
