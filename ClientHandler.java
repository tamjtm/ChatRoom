import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler extends Thread
{
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket s;
    private static ArrayList<ClientHandler> handlerCollection = new ArrayList<ClientHandler>();

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    public void addTo()
    {
        handlerCollection.add(this);
    }

    public static void sendAll(String message, int senderID)
    {
        try
        {
            for(int i=0; i<handlerCollection.size(); i++)
            {
                ClientHandler handler = handlerCollection.get(i);
                handler.dos.writeUTF("Client-" + senderID + "> " +message);
                handler.dos.flush();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        String message;

        try
        {
            dos.writeUTF("Welcome to Server-" + s.getLocalPort());  // send message to new client

            while (true)
            {
                message = dis.readUTF();

                if (message.equals("exit"))
                {
                    System.out.println("Goodbye Client-" + s.getPort());
                    handlerCollection.remove(this);
                    break;
                }

                sendAll(message,s.getPort());
            }
            this.s.close();
            this.dis.close();
            this.dos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
