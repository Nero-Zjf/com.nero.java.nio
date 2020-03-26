package com.nero.java.nio.noblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 非阻塞式SocketChannel demo
 */
public class SocketChannelClientDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        //开启Socket通道，默认使用localhost
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8099));
        System.out.println("Socket连接成功");
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        //开启Selector线程接收数据
        new Thread(new SelectorThread(selector)).start();

        Scanner in = new Scanner(System.in);

        //控制台输入发送内容
        ByteBuffer buf = ByteBuffer.allocate(48);
        while (in.hasNext()) {
            String inStr = in.nextLine();
            System.out.println("发送数据：" + inStr);

            buf.put(inStr.getBytes());
            buf.flip();//使得缓存已做好准备被读取
            while (buf.hasRemaining()) {//判断是否还有可读取的数据，Write()方法无法保证能写多少字节到SocketChannel。所以，我们重复调用write()直到Buffer没有要写的字节为止
                socketChannel.write(buf);//向Socket通道写入数据
            }
            buf.clear();//清空缓存，使得缓存可以再被写入
        }

    }

    /**
     * 运行Selector的线程（主要用于接收数据）
     */
    static class SelectorThread implements Runnable {
        private Selector selector;

        SelectorThread(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            ByteBuffer buf = ByteBuffer.allocate(48);
            StringBuilder sb;
            while (true) {
                int readyChannels = 0;
                try {
                    readyChannels = selector.select(5000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            System.out.println("readyChannels：" + readyChannels);
                if (readyChannels == 0) continue;
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = (SelectionKey) iterator.next();
                    if (key.isReadable()) {
                        // a channel is ready for reading
//                        System.out.println("isReadable");
                        SocketChannel sc = (SocketChannel) key.channel();
                        try {
                            while (sc.read(buf) > 0) {
                                sb = new StringBuilder();
                                buf.flip();//使得缓存已做好准备被读取
                                while (buf.hasRemaining()) {//判断是否还有可读取的数据
                                    sb.append((char) buf.get());//每次读取一个字节
                                }
                                buf.clear();//清空缓存，使得缓存可以再被写入
                                System.out.println("接收到数据：" + sb.toString());
                            }
                        } catch (IOException e) {
                            System.out.println("连接断开");
                            selector.keys().remove(key);
                        }
                    }
                    iterator.remove();
                }
            }
        }
    }

}
