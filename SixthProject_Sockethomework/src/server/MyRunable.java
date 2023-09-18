package server;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class MyRunable extends Thread{
    Socket socket;
    Properties prop;
    public MyRunable(Socket socket,Properties prop)
    {
        this.socket=socket;
        this.prop=prop;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String sel= br.readLine();
                switch (sel)
                {
                    case "login"->login(br);
                    case "registered"->registered(br);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void login(BufferedReader br) throws IOException {
        String string= br.readLine();

        String nameInput=string.split(",")[0];
        String passwordInput=string.split(",")[1];

        if(prop.containsKey(nameInput))
        {
            String password=prop.get(nameInput)+"";
            if(password.equals(passwordInput))
            {
                //登陆成功

                writeMessage("1");
                server.list.add(socket);
                talk2all(br,nameInput);
            }else {
                //密码错误
                writeMessage("2");
            }
        }else
        {
            //没有指定用户
            writeMessage("3");
        }

    }

    public void registered(BufferedReader br) throws IOException {
        String string= br.readLine();

        String nameInput=string.split("=")[0];
        String passwordInput=string.split("=")[1];

        if(prop.containsKey(nameInput))
        {
            //已被注册
           writeMessage("1");
        }else
        {
            prop.put(nameInput,passwordInput);

            FileOutputStream fileOutputStream=new FileOutputStream("servicedir/userinfo.txt");
            prop.store(fileOutputStream,string);
            writeMessage("2");
        }
    }

    public void writeMessage(String message) throws IOException {
        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void writeMessage(String message,Socket socket) throws IOException {
        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void talk2all(BufferedReader br,String userName) throws IOException {
        System.out.println(userName+"登陆进入服务器");
        while(true)
        {
            String message= br.readLine();
            System.out.println(userName+"发送了:"+message);

            for (Socket socket1 : server.list) {
                if(socket!=socket1)
                {
                    writeMessage(userName+"发送了:"+message,socket1);
                }
            }
        }
    }
}
