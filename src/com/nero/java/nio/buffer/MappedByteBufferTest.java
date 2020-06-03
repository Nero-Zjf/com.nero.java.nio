package com.nero.java.nio.buffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

/**
 * 内存映射文件
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("all.log", "rw");
        //通过channel的map()方法获取MappedByteBuffer
        MappedByteBuffer buffer = file.getChannel()
                .map(FileChannel.MapMode.READ_WRITE, 0, file.length());

        //读取文件内容
        //byte[] buf = new byte[1024];
        //while (buffer.hasRemaining()) {
        //    if (buffer.remaining() < buf.length) { //防止抛出BufferUnderflowException异常
        //        buf = new byte[buffer.remaining()];
        //    }
        //
        //    buffer.get(buf);
        //}
        //读取文件内容（直接解码）
        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
        System.out.println(charBuffer);

        //在文件头部插入内容
        buffer.flip();
        buffer.put("nero 测试MappedByteBufferTest".getBytes(StandardCharsets.UTF_8));

        buffer.clear();
        file.close();
    }
}
