package com.nero.java.nio.datagram;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * nio udp 实现服务端 (单播模式)
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //获取DatagramChannel 并绑定接口
        DatagramChannel channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(8099));

        ByteBuffer buf = ByteBuffer.allocate(48);
        //接收客户端消息
        SocketAddress socketAddress = channel.receive(buf);//socketAddress包含数据包携带的远程主机地址和端口等信息

        buf.flip();
        while (buf.hasRemaining()) {
            System.out.print((char) buf.get());
        }
        System.out.println();

        buf.clear();
        buf.put("hello".getBytes());
        buf.flip();
        //向客户端发送消息
        channel.send(buf, socketAddress);
        channel.close();
    }
}
