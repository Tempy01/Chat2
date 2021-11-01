package cz.uhk.pro2.models.chatFileOperations;

import cz.uhk.pro2.models.Message;

import java.util.List;

public interface ChatFileOperations {
    List<Message> loadMessages();
    void writemessagesToFile(List<Message> messages);

    List<Message> loadLoggedUsers();
    void writeLoggedUsersToFile(List<String> users);
}
