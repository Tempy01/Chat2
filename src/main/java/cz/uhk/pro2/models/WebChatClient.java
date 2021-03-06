package cz.uhk.pro2.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.uhk.pro2.models.api.MessageRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebChatClient implements ChatClient{

    private String loggedUser;
    private String token;
    private List<String> loggedUsers;
    private List<Message> messages;
    private Gson gson;
    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz";
    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();

    public WebChatClient(){
        gson = new Gson();
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();

        Runnable refreshLoggedUsersRun = () -> {
            Thread.currentThread().setName("refreshLoggedUsersThread");
            while(true){
                if(isAuthenticated()){
                    refreshLoggedUsers();
                }
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(refreshLoggedUsersRun);
        thread.start();
    }

    @Override
    public Boolean isAuthenticated() {
        return token!=null;
    }

    @Override
    public void login(String userName) {
        try{
            String url = BASE_URL + "/api/chat/login";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\""+userName+"\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 200 ){
                token = EntityUtils.toString(response.getEntity());
                token = token.replaceAll("\"", "");
                loggedUser = userName;
                addMessages(new Message(Message.USER_LOGGED_IN, loggedUser));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        try{
            String url = BASE_URL + "/api/chat/logout";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\""+token+"\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204 ){
                addMessages(new Message(Message.USER_LOGGED_OUT, loggedUser));
                token = null;
                loggedUser = null;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String text) {
        addMessages(new Message(loggedUser, text));
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

    private void addMessages(Message message){
        try{
            MessageRequest messageRequest = new MessageRequest(token,message);
            String url = BASE_URL + "/api/chat/sendMessage";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(gson.toJson(messageRequest), "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204 ) {
                refreshMessages();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshLoggedUsers(){
        try{
            String url = BASE_URL + "/api/chat/getLoggedUsers";
            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                String jsonResult = EntityUtils.toString(entity);

                loggedUsers = gson.fromJson(jsonResult, new TypeToken<ArrayList<String>>(){}.getType());
                raiseEventLoggedUsersChanged();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
        private void refreshMessages(){
            //zprovoznit na????t??n?? zpr??v z api -> t????da messageresponse , ve form??tu author,text a created, rozparsovat automaticky na message??????????????????????????
        }
}
