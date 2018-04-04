package ca.connect.dal.dalconnect.Model;

import java.util.UUID;

/**
 * Created by Jesuseyi Fasuyi on 3/18/2018.
 */

public class Todo {
    private String id, title, details;

    public Todo() {
    }

    public Todo(String title, String details) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.details = details;
    }

    public Todo(String id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() { return details; }

    public void setDetails(String details) { this.details = details; }
}
