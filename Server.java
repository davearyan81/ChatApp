import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class Server {

    ServerSocket serverSocket ;
    Socket socket ;
//    BufferReader to read the data
    BufferedReader bufferedReader;
//    For Writing the data printWrite
    PrintWriter printWriter;
    public Server()
    {
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Server is ready to accept the request");
            System.out.println("Waiting");
            socket= serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);

            startReading();
            startWriting();
        }
        catch (IOException e)
        {

        }

    }
    public void startReading()
    {
//        thread - read karke data deta rahega
        Runnable r1=()->{
            System.out.println("reader Started..");

            try {


                while (true) {

                        String msg = bufferedReader.readLine();

                        if (msg.equals("exit")) {
                            System.out.println("Client Terminated the message");
                            socket.close();
                            break;
                        }
                        System.out.println("Client:" + msg);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
    };
        new Thread(r1).start();
    }

    public void startWriting()
    {
//        Thread - data ko user se lega aur send karega client ko
        Runnable r2=()->
        {
            System.out.println("writer Started..");
            try {
            while (!socket.isClosed()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String content = br.readLine();
                    printWriter.println(content);
                    printWriter.flush();
                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
            }
            }
            catch (IOException e) {
//                e.printStackTrace();
                System.out.println("Collection Closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server Started");
    new Server();
    }
}
