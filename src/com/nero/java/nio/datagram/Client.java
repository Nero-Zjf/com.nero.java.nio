package com.nero.java.nio.datagram;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * nio udp 实现客户端 (单播模式)
 */
public class Client {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        //channel.bind(new InetSocketAddress(8055));//显示指定通道使用哪个端口，如果不设置，将由系统指定
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.put("hello".getBytes());
        buf.flip();
        //向服务器发送数据
        channel.send(buf,new InetSocketAddress("localhost",8099));
        buf.clear();
        //接收数据
        channel.receive(buf);
        buf.flip();
        while (buf.hasRemaining()) {
            System.out.print((char) buf.get());
        }
        System.out.println();
        buf.clear();
        channel.close();
    }
}
