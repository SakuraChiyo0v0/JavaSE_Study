package cilent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class clientMyRunable extends Thread{
    Socket socket;

    public clientMyRunable(Socket socket)
    {
        this.socket=socket;
    }

    public void run()
    {
        System.out.println("成功进入服务器");
        while(true)
        {
            try {
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg= bufferedReader.readLine();
                System.out.println(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
