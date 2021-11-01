package cz.uhk.pro2.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cz.uhk.pro2.models.chatFileOperations.ChatFileOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ToFileChatClient implements ChatClient {
    private String loggedUser;
    private List<Message> messages;
    private List<String> loggedUsers;
    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();
    private static final String MESSAGES_FILE = "./messages.json";

    Gson gson;
    ChatFileOperations chatFileOperations;

    public ToFileChatClient(ChatFileOperations chatFileOperations){
        gson = new GsonBuilder().setPrettyPrinting().create();
        this.chatFileOperations = chatFileOperations;
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();
        readMessagesFromFile();
    }

    @Override
    public Boolean isAuthenticated() {
        return loggedUser!=null;
    }

    @Override
    public void login(String userName) {
        messages.add(new Message(Message.USER_LOGGED_IN, userName));
        loggedUser = userName;
        loggedUsers.add(userName);
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        messages.add(new Message(Message.USER_LOGGED_OUT, loggedUser));
        loggedUsers.remove(loggedUser);
        loggedUser=null;
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionListenerLoggedUsersChanged(ActionListener toAdd) {
        listenersLoggedUsersChanged.add(toAdd);
    }
    private void raiseEventLoggedUsersChanged(){
        for(ActionListener al:listenersLoggedUsersChanged){
            al.actionPerformed(new ActionEvent(this, 1, "listenersLoggedUsersChanged"));
        }
    }

    private void addMessage(Message message){
        messages.add(message);
        writeMessagesToFile();
    }
    private void writeMessagesToFile(){
        String jsonText = gson.toJson(messages);
        try {
            FileWriter writer = new FileWriter(MESSAGES_FILE);
            writer.write(jsonText);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readMessagesFromFile(){
        try{
            FileReader reader = new FileReader(MESSAGES_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String jsonText = "";
            String line;
            while((line = bufferedReader.readLine()) != null){
                jsonText += jsonText;
            }

            Type targetTyp = new TypeToken<ArrayList<Message>>(){}.getType();
            messages = gson.fromJson(jsonText, targetTyp);
        }catch (IOException e){

        }
    }
}
