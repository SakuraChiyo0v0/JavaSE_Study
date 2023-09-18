package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class server {
    static ArrayList<Socket> list=new ArrayList<>();
    //定义一个数组 保存已连接的用户
    public static void main(String[] args) throws IOException {
/*服务端：
① 先读取本地文件中所有的正确用户信息。
②  当有客户端来链接的时候，就开启一条线程。
③ 在线程里面判断当前用户是登录操作还是注册操作。
④ 登录，校验用户名和密码是否正确
⑤ 注册，校验用户名是否唯一，校验用户名和密码的格式是否正确
⑥ 如果登录成功，开始聊天
⑦ 如果注册成功，将用户信息写到本地，开始聊天*/



        ServerSocket ss=new ServerSocket(10001);

        System.out.println("已经接受");

        Properties prop=new Properties();
        FileInputStream fileInputStream=new FileInputStream("servicedir/userinfo.txt");
        prop.load(fileInputStream);
        fileInputStream.close();

        while(true)
        {
            Socket socket=ss.accept();
            System.out.println("有客户端链接");
            new Thread(new MyRunable(socket,prop)).start();
        }
    }
}
