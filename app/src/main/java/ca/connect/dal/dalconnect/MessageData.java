package ca.connect.dal.dalconnect;

/**
 * Created by hugokwe on 3/1/2018.
 * This data object class hold the chat interactions between google and user from the listView
 *
 */
public class MessageData {
    public boolean isUser;
    String messageBody;
    //Constructor
    public MessageData (boolean isUSER, String chatMessage, String Sender, String Receiver ) {
        messageBody = chatMessage;
        isUser = isUSER;
    }
}
