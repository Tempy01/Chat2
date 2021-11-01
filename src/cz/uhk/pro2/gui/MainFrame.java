package cz.uhk.pro2.gui;

import cz.uhk.pro2.models.ChatClient;
import cz.uhk.pro2.models.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    JTextField txtInputName, txtInputMessage;
    JButton btnLogin, btnSend;
    JTable tblLoggedUsers;
    JTextArea txtAreaChat;

    LoggedUsersTableModel loggedUsersTableModel;
    ChatClient chatClient;

    public MainFrame(int width, int height, ChatClient chatClient){
        super("Chat Client");
        this.chatClient = chatClient;
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initGui();
    }

    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());
        JPanel panelChat = new JPanel();
        JPanel panelFloor = new JPanel();
        JPanel panelLoggedUsers = new JPanel();
        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.LEFT)); //řazení z leva

        //Login
        initLoginPanel(panelLogin);

        //Chat
        initChatPanel(panelChat);

        //Users
        initLoggedUsersPanel(panelLoggedUsers);

        //Patička
        initFloorPanel(panelFloor);

        //implementace
        panelMain.add(panelLogin, BorderLayout.NORTH);
        panelMain.add(panelChat, BorderLayout.CENTER);
        panelMain.add(panelLoggedUsers, BorderLayout.EAST);
        panelMain.add(panelFloor, BorderLayout.SOUTH);
        add(panelMain);
    }

    private void initLoginPanel(JPanel panelLogin){
        txtInputName = new JTextField("", 30);
        panelLogin.add(new JLabel("Jméno: "));
        panelLogin.add(txtInputName);
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button login clicked - " + txtInputName.getText());
                if(chatClient.isAuthenticated()){
                    chatClient.logout();
                    btnLogin.setText("Login");
                    txtInputName.setEditable(true);
                    txtAreaChat.setEnabled(false);
                }
                else{
                    String userName = txtInputName.getText();
                    if(userName.length()<1){
                        JOptionPane.showMessageDialog(null, "Enter your username", "Chyba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    chatClient.login(userName);
                    btnLogin.setText("Logout");
                    txtInputName.setEditable(false);
                    txtAreaChat.setEnabled(true);
                }
            }
        });
        panelLogin.add(btnLogin);
    }

    private void initChatPanel(JPanel panelChat){
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.X_AXIS));
        txtAreaChat = new JTextArea();
        txtAreaChat.setAutoscrolls(true);
        txtAreaChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaChat);
        panelChat.add(scrollPane);
    }

    private void initLoggedUsersPanel(JPanel panel){
        Object[][] data = new Object[][]{
                {"1:1", "1:2"},
                {"2:1", "2:2"},
                {"3:1", "3:2"},
        };
        String[] colNames = new String[] {"Column1", "Column2"};


        //tblLoggedUsers = new JTable(data, colNames);
        loggedUsersTableModel = new LoggedUsersTableModel(chatClient);
        tblLoggedUsers = new JTable();
        tblLoggedUsers.setModel(loggedUsersTableModel);
        chatClient.addActionListenerLoggedUsersChanged(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedUsersTableModel.fireTableDataChanged();
            }
        });
        JScrollPane scrollPane = new JScrollPane(tblLoggedUsers);
        scrollPane.setPreferredSize(new Dimension(250,500));
        panel.add(scrollPane);
    }

    private void initFloorPanel(JPanel panelFloor){
        txtInputMessage = new JTextField("", 50);
        panelFloor.add(txtInputMessage);
        btnSend = new JButton("Pošli");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = txtInputMessage.getText();
                if(text.length()==0){
                    return;
                }
                if(!chatClient.isAuthenticated()){
                    return;
                }
                chatClient.sendMessage(text);
                txtInputMessage.setText("");
                refreshMessages();
            }
        });
        panelFloor.add(btnSend);
    }

    private void refreshMessages(){
        txtAreaChat.setText("");
        for(Message msg: chatClient.getMessages()){
            txtAreaChat.append(msg.toString());
            txtAreaChat.append("\n");
        }
    }
}
