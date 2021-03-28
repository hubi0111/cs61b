package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents a gitlet commit object.
 *
 * @author Bill Hu
 */
public class Commit implements Serializable {
    /**
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
    private String mergeParent;

    /**
     * The files this Commit tracks.
     */
    private HashMap<String, String> trackedFiles;

    /**
     * The id of this Commit.
     */
    private String id;

    Commit(String message, String parent, HashMap<String, String>tracked, String mergeParent) {
        this.message = message;
        this.parent = parent;
        this.trackedFiles = tracked;
        this.mergeParent = mergeParent;
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

    public void setTime(String time) {
        this.time = time;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getMergeParent() {
        return mergeParent;
    }

    public void setMergeParent(String mergeParent) {
        this.mergeParent = mergeParent;
    }

    public HashMap<String, String> getTrackedFiles() {
        return trackedFiles;
    }

    public void setTrackedFiles(HashMap<String, String> trackedFiles) {
        this.trackedFiles = trackedFiles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
