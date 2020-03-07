import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ReceivingHandler extends Thread
{
    private DataInputStream dis;
    private Socket s;
    private TextArea textArea;

    public ReceivingHandler(Socket s, DataInputStream dis, TextArea textArea)
    {
        this.s = s;
        this.dis = dis;
        this.textArea = textArea;
    }

    @Override
    public void run()
    {
        String message;
        try
        {
            while (!s.isClosed())
            {
                if(dis.available() > 0)
                {
                    message = dis.readUTF();
                    textArea.appendText(message + "\n");
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
