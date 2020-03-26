package com.nero.java.nio.pipe;

import java.io.IOException;
import java.nio.channels.Pipe;

/**
 * 演示使用pipe实现两条线程之间 单方向通信
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //开启一个pipe
        Pipe pipe = Pipe.open();

        //启动两条线程
        new Thread(new Thread1(pipe.sink())).start();
        new Thread(new Thread2(pipe.source())).start();
    }
}
