package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;

/**
 * Represents a gitlet commit object.
 * TODO: It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author Bill Hu
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private String message;

    /**
     * The time of this Commit.
     */
    private String time;

    /**
     * The parent of this Commit.
     */
    private String parent;

    /**
     * The merge parent of this Commit.
     */
    private String merge_parent;

    /**
     * The files this Commit tracks.
     */
    private HashMap<String, String> tracked_files;

    /**
     * The id of this Commit.
     */
    private String id;

    Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        this.tracked_files = new HashMap<>();
        setTime();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    /**
     * @source http://tutorials.jenkov.com/java-internationalization/simpledateformat.html
     */
    public void setTime() {
        SimpleDateFormat d = new SimpleDateFormat("EEE MMM d hh:mm:ss YYYY");
        Date date = new Date();
        this.time = d.format(date) + " -0800";
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getMerge_parent() {
        return merge_parent;
    }

    public void setMerge_parent(String merge_parent) {
        this.merge_parent = merge_parent;
    }

    public HashMap<String, String> getTracked_files() {
        return tracked_files;
    }

    public void setTracked_files(HashMap<String, String> tracked_files) {
        this.tracked_files = tracked_files;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /* TODO: fill in the rest of this class. */
}
