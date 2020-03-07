import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer
{


    public static void main(String[] args) throws IOException
    {
        int port = 8001;
        ServerSocket ss = new ServerSocket(port);
        DataInputStream dis;
        DataOutputStream dos;

        System.out.println("########## SERVER-" + ss.getLocalPort() + " ##########");

        while (true)
        {
            Socket s = null;

            try
            {
                s = ss.accept();

                System.out.println("Hello Client-" + s.getPort());

                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());

                ClientHandler t = new ClientHandler(s, dis, dos);
                t.addTo();
                t.start();
            }
            catch (Exception e)
            {
                s.close();
                e.printStackTrace();
            }
        }
    }
}
