package cz.uhk.pro2.models;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.awt.event.ActionListener;
import java.util.List;

public interface ChatClient {
    Boolean isAuthenticated();
    void login(String userName);
    void logout();
    void sendMessage(String text);
    List<String> getLoggedUsers();
    List<Message> getMessages();

    void addActionListenerLoggedUsersChanged(ActionListener toAdd);
}
