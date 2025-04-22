package entity.model;

import java.util.Date;

public class Notification {
    private static int counter = 0;
    private int id;
    private String recipientNric;
    private String message;
    private Date timestamp;
    private boolean read;

    public Notification(String recipientNric, String message) {
        this.id = ++counter;
        this.recipientNric = recipientNric;
        this.message = message;
        this.timestamp = new Date();
        this.read = false;
    }

    public int getId() { return id; }
    public String getRecipientNric() { return recipientNric; }
    public String getMessage() { return message; }
    public Date getTimestamp() { return timestamp; }
    public boolean isRead() { return read; }
    public void markAsRead() { this.read = true; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s",
            read ? " " : "*",
            new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp),
            message);
    }
}