package com.nero.java.nio.noblock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 非阻塞式SocketChannelServer demo
 */
public class SocketChannelServerDemo {

    public static void main(String[] args) throws IOException {
        //创建Selector
        Selector selector = Selector.open();
        //打开socket服务器管道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//配置为非阻塞模式
        //绑定服务器接口和地址（此示例默认localhost）
        ssc.socket().bind(new InetSocketAddress(8099));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buf = ByteBuffer.allocate(48);
        StringBuilder sb;
        while (true) {
            int readyChannels = selector.select(5000);
//            System.out.println("readyChannels：" + readyChannels);
            if (readyChannels == 0) continue;
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                if (key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
//                    System.out.println("isAcceptable");
                    SocketChannel sc = ssc.accept();
                    if (sc != null) {
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("接收到新连接:" + key.channel().toString());
                    }

//                    System.out.println("socket服务器已开始监听连接");
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                    System.out.println("isConnectable");

                } else if (key.isReadable()) {
                    // a channel is ready for reading
//                    System.out.println("isReadable");
                    SocketChannel sc = (SocketChannel) key.channel();
                    try {
                        while (sc.read(buf) > 0) {
                            sb = new StringBuilder();
                            buf.flip();//使得缓存已做好准备被读取
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            while (buf.hasRemaining()) {//判断是否还有可读取的数据
                                byte[] bytes = new byte[buf.remaining()];
                                buf.get(bytes);//每次读取一个字节
                                baos.write(bytes);
                            }
                            buf.clear();//清空缓存，使得缓存可以再被写入
                            System.out.println("接收到数据：" + new String(baos.toByteArray()));

                            buf.put("hello".getBytes());
                            buf.flip();//使得缓存已做好准备被读取
                            while (buf.hasRemaining()) {//判断是否还有可读取的数据，Write()方法无法保证能写多少字节到SocketChannel。所以，我们重复调用write()直到Buffer没有要写的字节为止
                                sc.write(buf);//向Socket通道写入数据
                            }
                            buf.clear();//清空缓存，使得缓存可以再被写入
                            System.out.println("发送数据" + sc.toString());
                        }
                    } catch (IOException e) {
                        System.out.println("连接断开");
                        selector.keys().remove(key);
                    }
                } else if (key.isWritable()) {
                    // a channel is ready for writing
//                    System.out.println("isWritable");
                }
                iterator.remove();
            }
        }
    }
}
