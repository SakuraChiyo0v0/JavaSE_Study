package cilent;

import java.beans.beancontext.BeanContext;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.stream.Stream;
import java.util.zip.CheckedOutputStream;

public class client {
    public static void main(String[] args) throws IOException {

    /*
    客户端：
    拥有登录、注册、聊天功能。
    ① 当客户端启动之后，要求让用户选择是登录操作还是注册操作，需要循环。
    * 如果是登录操作，就输入用户名和密码，以下面的格式发送给服务端
        username=zhangsan&password=123
    * 如果是注册操作，就输入用户名和密码，以下面的格式发送给服务端
        username=zhangsan&password=123
    ② 登录成功之后，直接开始聊天。
    */
        Socket socket = new Socket("127.0.0.1", 10001);
        System.out.println("已经成功连接到服务器");

        while (true) {
            System.out.println("---------小宇聊天室---------");
            System.out.println("1.登录");
            System.out.println("2.注册");

            Scanner scanner = new Scanner(System.in);
            String select = scanner.nextLine();

            switch (select) {
                case "1" -> login(socket);
                case "2" -> registered(socket);
                default -> System.out.println("请输入正确的内容");
            }
        }
    }
    public static void login(Socket socket) throws IOException {
        Scanner sc=new Scanner(System.in);
        System.out.println("您进入了登陆界面");
        System.out.println("请输入用户名");
        String name=sc.nextLine();
        System.out.println("请输入密码");
        String password=sc.nextLine();

        StringJoiner sj=new StringJoiner(",");
        sj.add(name).add(password);
        String str=sj.toString();

        //发送密码
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("login");
        bw.newLine();
        bw.flush();

        bw.write(str);
        bw.newLine();
        bw.flush();

        //接受返回数据
        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String loginNum;
        loginNum = br.readLine();
        switch (loginNum)
        {
            case "1":
            {
                System.out.println("登陆成功");
                new Thread(new clientMyRunable(socket)).start();
                tall2all(socket);
                break;
            }

            case "2":
                System.out.println("密码错误!");
                break;
            case "3":
                System.out.println("用户不存在!");
                break;
        }
    }

    public static void registered(Socket socket) throws IOException {
        System.out.println("您进入了注册界面");
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入用户名");
        String name=sc.nextLine();
        System.out.println("请输入密码");
        String password=sc.nextLine();

        StringJoiner stringJoiner=new StringJoiner("=");
        String data=stringJoiner.add(name).add(password).toString();
        System.out.println(data);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("registered");
        bw.newLine();
        bw.flush();

        bw.write(data);
        bw.newLine();
        bw.flush();

        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String loginNum;
        loginNum = br.readLine();
        System.out.println(loginNum);
        switch (loginNum)
        {
            case "1":
                System.out.println("该用户名已被注册");
                break;
            case "2":
                System.out.println("注册成功");
                break;
        }
    }

    public static void tall2all(Socket socket) throws IOException {
        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Scanner sc=new Scanner(System.in);
        while(true)
        {
            System.out.println("请输入想发送的内容");
            String talk=sc.nextLine();
            bufferedWriter.write(talk);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }
}