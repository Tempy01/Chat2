package cz.uhk.pro2;

import cz.uhk.pro2.gui.MainFrame;
import cz.uhk.pro2.models.ChatClient;
import cz.uhk.pro2.models.InMemoryChatClient;
import cz.uhk.pro2.models.Message;
import cz.uhk.pro2.models.ToFileChatClient;
import cz.uhk.pro2.models.chatFileOperations.ChatFileOperations;
import cz.uhk.pro2.models.chatFileOperations.JsonChatFileOperation;

public class Main {

    public static void main(String[] args) {
        ChatFileOperations chatFileOperations = new JsonChatFileOperation();
        ChatClient chatClient = new ToFileChatClient(chatFileOperations);
    MainFrame mainFrame = new MainFrame(800, 600, chatClient);
    mainFrame.setVisible(true);
    }

    private static void testChat(){
        ChatClient chatClient = new InMemoryChatClient();
        System.out.println("Logged in");
        chatClient.login("Jaroslav");
        System.out.println("Logged: " + chatClient.isAuthenticated());
        System.out.println("currently logged: ");
        for(String user: chatClient.getLoggedUsers()){
            System.out.print(user);
        }
        System.out.println("sendMSG");
        chatClient.sendMessage("Cau");

        System.out.println("messages: ");
        for(Message msg: chatClient.getMessages()){
            System.out.println(msg);
        }
        System.out.println("Logging out");
        chatClient.logout();
        System.out.println("user logged: " + chatClient.isAuthenticated());
    }
}
