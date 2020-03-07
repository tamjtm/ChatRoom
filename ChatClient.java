import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient extends Application
{
    Socket s;
    DataInputStream dis;
    DataOutputStream dos;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        String ip = "127.0.0.1";
        int port = 8001;
        s = new Socket(ip, port);

        primaryStage.setTitle("Chat Box - " + s.getLocalPort());
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,20,20,20));
        Scene scene = new Scene(grid, 350, 300);
        primaryStage.setScene(scene);

        TextArea messageDisplay = new TextArea();
        messageDisplay.setEditable(false);
        messageDisplay.setFont(Font.font("Tahoma", FontWeight.NORMAL,14));
        grid.add(messageDisplay,0,0,2,1);

        TextField typingArea = new TextField();
        typingArea.setPrefWidth(250);
        typingArea.setFont(Font.font("Tahoma", FontWeight.NORMAL,14));
        grid.add(typingArea,0,1);

        Button btnSend = new Button("Send");
        grid.add(btnSend,1,1);

        primaryStage.show();

        try
        {
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            System.out.println("########## CLIENT-" + s.getLocalPort() + " ##########");
            System.out.println("server> " + dis.readUTF());      // display welcome message

            ReceivingHandler tReceive = new ReceivingHandler(s,dis,messageDisplay);
            tReceive.start();

            btnSend.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    String message;
                    try
                    {
                        message = typingArea.getText().toString();
                        dos.writeUTF(message);
                        dos.flush();
                        typingArea.setText("");
                        if (message.equals("exit"))
                        {
                            ((Stage)primaryStage.getScene().getWindow()).close();
                            s.close();
                            System.out.println("Client-" + s.getLocalPort() + " closed");
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            s.close();
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
