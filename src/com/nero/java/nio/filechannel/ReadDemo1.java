package com.nero.java.nio.filechannel;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;

/**
 * FileChannel 读取数据并进行解码 demo
 */
public class ReadDemo1 {

    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("all.log", "r");//"r" 表示只读
        FileChannel fileChannel = aFile.getChannel();


        //定义一个48字节的Buffer
        ByteBuffer bbuf = ByteBuffer.allocate(48);
        CharBuffer cbuf = CharBuffer.allocate(48);
        int bytesRead = 0;
        //获取编码集(all.log使用utf-8编码)
        Charset charset = StandardCharsets.UTF_8;
        CharsetDecoder decoder = charset.newDecoder();

        while ((bytesRead = fileChannel.read(bbuf)) != -1) {//从FileChannel中将数据读出到buffer，返回读取的字节数，如果为-1 则表明读到文件末尾
            bbuf.flip();//使得buffer准备好被读取
            //解码
            CoderResult cr = decoder.decode(bbuf, cbuf, true);
            cbuf.flip();
            StringBuilder sb = new StringBuilder();
            while (cbuf.hasRemaining()) {
                char c = cbuf.get();
                sb.append(c);
            }

            //打印读取的文件内容
            System.out.print(sb);

            //byte[] bt = null;
            //if (bbuf.hasRemaining()) {//如果还存在未读取的字节，则说明存在字节没有解码成功。
            //    //缓存未解码的字节
            //    bt = new byte[bbuf.remaining()];
            //    bbuf.get(bt);
            //}
            //
            //bbuf.clear();//清空缓存 以便再次写入
            //if (bt != null) {
            //    bbuf.put(bt);//未解码的字节放入下一次解码中
            //}

            //此方法替换上面注释的逻辑
            bbuf.compact();

            cbuf.clear();
        }

        aFile.close();//最后关闭channel
    }
}
