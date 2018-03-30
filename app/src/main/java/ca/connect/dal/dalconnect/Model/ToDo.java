package ca.connect.dal.dalconnect.Model;

/**
 * Created by Jesuseyi Fasuyi on 3/19/2018.
 */

public class ToDo {
    private String id, title, details;
/**
    public Todo(String id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }
*/
    public  ToDo(String id, String title, String details){
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

    public String getDetailsT() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
