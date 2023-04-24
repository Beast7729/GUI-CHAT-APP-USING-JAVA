// @author: CODEHUB
// Subscribe to our channel CODEHUB for more projects

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import java.io.*;

import java.lang.System;

public class CHATAPP extends JFrame implements ActionListener, WindowListener, KeyListener {
    // networking components
    public static Socket Mysocket; // Receiving messages
    public static Socket Outsocket; // Sending messages
    public static ServerSocket MyServer;

    //
    public static JLabel whoami, add, MyIP_L;

    public static JTextField PortTF, messageBox, MyIP;

    public static JButton connect, send;
    public static String content = "";
    public static String temp = " ", IpStored = "";

    public static JTextArea textAreaS;
    JScrollPane scrollPaneS, scrollPaneR;
    static JScrollBar vertical;
    // Threads
    static Receive r1 = new Receive();
    static Send s1 = new Send();

    CHATAPP(int port) throws Exception {

        JFrame f = new JFrame("WHATSCHAT");
        f.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        f.addWindowListener(this);
        f.setLayout(null);

        JPanel AdressBar = new JPanel(new FlowLayout());

        add = new JLabel("Enter Port to Connect: ");
        add.setFont(new Font("Serif", Font.BOLD, 20));
        PortTF = new JTextField(25);
        PortTF.setFont(new Font("Serif", Font.BOLD, 20));
        connect = new JButton("Connect");
        connect.setBackground(new Color(59, 89, 182));
        connect.setForeground(Color.WHITE);
        connect.setFocusPainted(false);
        connect.setFont(new Font("Tahoma", Font.BOLD, 12));
        connect.setPreferredSize(new Dimension(100, 30));
        connect.addActionListener(this);
        AdressBar.add(add);
        AdressBar.add(PortTF);
        AdressBar.add(connect);

        AdressBar.setBounds(50, 10, 800, 40);
        AdressBar.setBackground(Color.white);

        JPanel chatArea = new JPanel(new FlowLayout());

        chatArea.setBounds(25, 60, 850, 430);
        chatArea.setBackground(Color.black);
        textAreaS = new JTextArea(19, 40);
        textAreaS.setBackground(Color.gray);
        textAreaS.setFont(new Font("Tahoma", Font.BOLD, 18));
        textAreaS.setEditable(false);
        scrollPaneS = new JScrollPane(textAreaS);
        vertical = scrollPaneS.getVerticalScrollBar();
        scrollPaneS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatArea.add(scrollPaneS);

        JPanel MessageArea = new JPanel();
        messageBox = new JTextField(30);
        messageBox.setFont(new Font("Serif", Font.BOLD, 20));
        messageBox.addKeyListener(this);
        send = new JButton("Send");
        send.setBackground(new Color(59, 89, 182));
        send.setForeground(Color.WHITE);
        send.setFocusPainted(false);
        send.setFont(new Font("Tahoma", Font.BOLD, 12));
        send.setPreferredSize(new Dimension(100, 30));
        send.addActionListener(this);
        MessageArea.add(messageBox);
        MessageArea.add(send);

        MessageArea.setBounds(50, 500, 800, 40);
        MessageArea.setBackground(Color.white);

        MyIP_L = new JLabel("MYPORT: ");
        MyIP_L.setBounds(300, 550, 100, 30);
        MyIP_L.setFont(new Font("Tahoma", Font.BOLD, 20));
        MyIP = new JTextField(40);
        MyIP.setEditable(false);
        MyIP.setBounds(400, 550, 200, 30);
        MyIP.setFont(new Font("Tahoma", Font.BOLD, 20));

        whoami = new JLabel("");
        whoami.setBounds(450, 580, 200, 30);

        f.add(AdressBar);
        f.add(chatArea);
        f.add(MessageArea);
        f.add(MyIP_L);
        f.add(MyIP);
        f.add(whoami);
        f.setSize(900, 650);

        f.setLayout(null);
        f.setVisible(true);
        MyServer = new ServerSocket(port);
        MyIP.setText(MyIP.getText() + MyServer.getLocalPort());
        Mysocket = MyServer.accept();
        // System.out.println(MyServer.getLocalPort());

    }

    public static void MakeConnection(int port) throws Exception {
        Outsocket = new Socket(InetAddress.getLocalHost(), port);

        // System.out.println(MyServer.getLocalPort());
    }

    public static void main(String args[]) throws Exception {

        CHATAPP c = new CHATAPP(Integer.parseInt(args[0]));

        do {
            System.out.println("Waiting for connection : Please Connect to Other PC");
            CHATAPP.textAreaS.setText("Waiting for Connection");
        } while (CHATAPP.Outsocket == null);
        while ((Mysocket.isConnected()) && (Outsocket.isConnected())) {
            CHATAPP.textAreaS.setText("Connected Succesfully!");

            // System.out.println("CONNNECTED");
            ReceiveMessages();

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect) {
            try {
                MakeConnection(Integer.parseInt(PortTF.getText()));
            } catch (Exception e1) {
                // System.out.println("Line 51 failed: actionperformed");
            }
        } else if (e.getSource() == send) {
            CHATAPP.temp = CHATAPP.messageBox.getText();
            CHATAPP.content = CHATAPP.content + "You: " + temp;
            CHATAPP.vertical.setValue(CHATAPP.vertical.getMaximum());

            s1 = new Send();
            s1.start();
            // ReceiveMessages();

        }

    }

    static void ReceiveMessages() {
        System.gc();
        if (r1 == null) {

            // System.out.println("creating new thread");
            r1 = new Receive();

        }
        r1.start();

    }

    @Override
    public void keyPressed(KeyEvent KE) {
        if (KE.getKeyCode() == KeyEvent.VK_ENTER) {
            CHATAPP.temp = CHATAPP.messageBox.getText();
            CHATAPP.content = CHATAPP.content + "You: " + temp;
            CHATAPP.vertical.setValue(CHATAPP.vertical.getMaximum());

            s1 = new Send();
            s1.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent KE) {
    }

    @Override
    public void keyTyped(KeyEvent KE) {
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            if (Mysocket != null) {
                CHATAPP.Mysocket.close();

            }
            if (MyServer != null) {
                CHATAPP.MyServer.close();

            }
            if (Outsocket != null) {

                CHATAPP.Outsocket.close();
            }

            System.out.println("Closed");

            System.exit(0);

        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

}

class Receive extends Thread {
    DataInputStream IncomingMessage;

    @Override
    public void run() {

        try {
            while (true) {
                // System.out.println("INSIDE WHILEE RECEIVE");
                IncomingMessage = new DataInputStream(CHATAPP.Mysocket.getInputStream());
                String Message = null;
                while (Message == null) {

                    if (IncomingMessage.available() > 0) {
                        Message = IncomingMessage.readUTF();
                        // System.out.println("Message is " + Message);
                        break;
                    }
                    // System.out.println("Now will sleep:" + IncomingMessage.available());
                    sleep(500);

                }

                if (Message != null) {
                    CHATAPP.content = CHATAPP.content + "Anonymous says: " + Message + "\n";
                    CHATAPP.textAreaS.setText(CHATAPP.content);
                    CHATAPP.vertical.setValue(CHATAPP.vertical.getMaximum());

                }

                // System.out.println("Thread is running");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // static void waittt(){
    // sleep()
    // }
}

class Send extends Thread {
    DataOutputStream OutGoingMessage;

    @Override
    public void run() {
        try {
            OutGoingMessage = new DataOutputStream(CHATAPP.Outsocket.getOutputStream());
            String Message = CHATAPP.messageBox.getText();
            OutGoingMessage.writeUTF(Message);
            // System.out.println("Sent Successfully:" + Message);
            // System.out.println("Interrupted bEFORE");
            // OutGoingMessage.flush();
            CHATAPP.content = CHATAPP.content + "\n";
            CHATAPP.textAreaS.setText(CHATAPP.content);
            CHATAPP.messageBox.setText("");

            stop();
            // System.out.println("Interrupted after");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
