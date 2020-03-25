package com.nero.java.nio.block;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 阻塞式SocketChannel demo
 */
public class SocketChannelClientDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        //开启Socket通道，默认使用localhost
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8099));
        //定义一个容量为48个字节的缓存
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        //向缓存写入字节
        byteBuffer.put("hello".getBytes());
        byteBuffer.flip();//使得缓存已做好准备被读取
        while (byteBuffer.hasRemaining()) {//判断是否还有可读取的数据，Write()方法无法保证能写多少字节到SocketChannel。所以，我们重复调用write()直到Buffer没有要写的字节为止
            socketChannel.write(byteBuffer);//向Socket通道写入数据
        }
        byteBuffer.clear();//清空缓存，使得缓存可以再被写入

        Thread.sleep(5000);//模拟5s后再次发送一条消息

        byteBuffer.put("world".getBytes());
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
        byteBuffer.clear();

        socketChannel.close();//关闭通道，如果未关闭通道退出程序，通道另外一端将抛出IOException
    }
}
