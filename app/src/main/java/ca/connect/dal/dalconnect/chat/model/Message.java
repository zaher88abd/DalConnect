package ca.connect.dal.dalconnect.chat.model;



public class Message {
    private String idSender;
    private String idReceiver;
    private String text;
    private long timestamp;

    private String senderPortrait;
    private String receiverPortrait;

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderPortrait() {
        return senderPortrait;
    }

    public void setSenderPortrait(String senderPortrait) {
        this.senderPortrait = senderPortrait;
    }

    public String getReceiverPortrait() {
        return receiverPortrait;
    }

    public void setReceiverPortrait(String receiverPortrait) {
        this.receiverPortrait = receiverPortrait;
    }
}