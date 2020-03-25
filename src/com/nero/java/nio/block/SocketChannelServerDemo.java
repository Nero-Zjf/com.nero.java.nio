package com.nero.java.nio.block;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 阻塞式SocketChannelServer demo
 */
public class SocketChannelServerDemo {

    public static void main(String[] args) throws IOException {
        //打开socket服务器管道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //绑定服务器接口和地址（此示例默认localhost）
        ssc.socket().bind(new InetSocketAddress(8099));
        while (true) {
            System.out.println("socket服务器开始监听新连接");
            //socket服务器开始监听新连接
            SocketChannel ss = ssc.accept();
            System.out.println("接收到新连接");
            //开启SocketChannel线程接收管道数据
            SocketChannelThread sct = new SocketChannelThread(ss);
            new Thread(sct).start();
        }
    }

    /**
     * 实现Runnable 开启新线程方式接收数据
     */
    static class SocketChannelThread implements Runnable {
        private SocketChannel socketChannel;

        SocketChannelThread(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            //定义一个容量为48个字节的缓存
            ByteBuffer buffer = ByteBuffer.allocate(48);
            //定义StringBuilder用于组合接收的数据
            StringBuilder sb;
            try {
                //当socketChannel关闭时 返回-1
                while (socketChannel.read(buffer) != -1) {
                    sb = new StringBuilder();
                    buffer.flip();//使得缓存已做好准备被读取
                    while (buffer.hasRemaining()) {//判断是否还有可读取的数据
                        sb.append((char) buffer.get());//每次读取一个字节
                    }
                    buffer.clear();//清空缓存，使得缓存可以再被写入
                    System.out.println("接收到数据：" + sb.toString());
                }
                System.out.println("管道关闭");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
