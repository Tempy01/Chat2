package cz.uhk.pro2.models.chatFileOperations;

import cz.uhk.pro2.models.Message;

import java.util.List;

public class JsonChatFileOperation implements ChatFileOperations{
    @Override
    public List<Message> loadMessages() {
        return null;
    }

    @Override
    public void writemessagesToFile(List<Message> messages) {

    }

    @Override
    public List<Message> loadLoggedUsers() {
        return null;
    }

    @Override
    public void writeLoggedUsersToFile(List<String> users) {

    }
}
