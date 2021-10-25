package cz.uhk.pro2.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    JTextField txtInputName, txtInputMessage;
    JButton btnLogin, btnSend;
    JTextArea txtAreaChat;

    public MainFrame(int width, int height){
        super("Chat Client");
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());
        JPanel panelChat = new JPanel();
        JPanel panelFloor = new JPanel();
        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.LEFT)); //řazení z leva

        //Login
        txtInputName = new JTextField("", 30);
        panelLogin.add(new JLabel("Jméno: "));
        panelLogin.add(txtInputName);
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button login clicked - " + txtInputName.getText());
            }
        });
        panelLogin.add(btnLogin);

        //Chat
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.X_AXIS));
        txtAreaChat = new JTextArea();
        txtAreaChat.setAutoscrolls(true);
        txtAreaChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaChat);
        panelChat.add(scrollPane);

        //Patička
        txtInputMessage = new JTextField("", 50);
        panelFloor.add(txtInputMessage);
        btnSend = new JButton("Pošli");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button send clicked - " + txtInputMessage.getText());
            }
        });
        panelFloor.add(btnSend);

        //implementace
        panelMain.add(panelLogin, BorderLayout.NORTH);
        panelMain.add(panelChat, BorderLayout.CENTER);
        panelMain.add(panelFloor, BorderLayout.SOUTH);
        add(panelMain);
    }
}
