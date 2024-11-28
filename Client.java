import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {

    Socket socket;
    //    BufferReader to read the data
    BufferedReader bufferedReader;
    //    For Writing the data printWrite
    PrintWriter printWriter;

//    Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);
//    Contrsuctor
    public Client()
    {
        try {
            System.out.println("Sending Request");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Client Connected");

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);

            startReading();
            startWriting();

            createGUI();
            handleEvents();
        }
        catch(Exception e)
        {
            System.out.println("Client Disconnected");
        }
    }
    private void handleEvents()
    {
            messageInput.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {

                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if(e.getKeyCode()==10)
                    {
                        String contentToSend = messageInput.getText();
                        messageArea.append("Me :" + contentToSend+"\n");
                        printWriter.println(contentToSend);
                        printWriter.flush();
                        messageInput.setText("");
                        messageInput.requestFocus();
                    }
                }
            });
    }
    private void createGUI()
    {
        this.setTitle("Client Messenger[END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null); // this will set our window to center
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//this will close your program on clicking X

//       Coding for coponent
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageInput.setEditable(false);

//        frame ka layout set karoge
        this.setLayout(new BorderLayout());

//        Adding the components to the frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);
    }
    public void startReading()
    {
//        thread - read karke data deta rahega
        Runnable r1=()->{
            System.out.println("reader Started..");

            try {
                while (true && !socket.isClosed()) {

                    String msg = bufferedReader.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Server Terminated the Chat");
                        JOptionPane.showMessageDialog(this,"Server Terminated the Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.append("Server :"+msg+"\n");
                }
            }catch (Exception e) {
                System.out.println("Server Disconnected");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting()
    {
//        Thread - data ko user se lega aur send karega client ko
        Runnable r2=()->
        {
            try {
            while (true) {
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
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        new Client();
    }
}
