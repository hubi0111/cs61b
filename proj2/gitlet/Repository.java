package gitlet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 * TODO: It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author Bill Hu
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /**
     * The .gitlet directory.
     */
    public static final File BLOBS = join(GITLET_DIR, "blobs");

    /**
     * The .gitlet directory.
     */
    public static final File BRANCHES = join(GITLET_DIR, "branches");

    /**
     * The .gitlet directory.
     */
    public static final File COMMITS = join(GITLET_DIR, "commits");

    /**
     * The .gitlet directory.
     */
    public static final File REMOVED = join(GITLET_DIR, "removed");

    /**
     * The .gitlet directory.
     */
    public static final File STAGED = join(GITLET_DIR, "staged");

    /**
     * The .gitlet directory.
     */
    public static final File HEAD = join(GITLET_DIR, "HEAD");


    public void init(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else if (GITLET_DIR.exists()) {
            System.out.println("A gitlet version control system already exists in the current directory.");
            System.exit(0);
        } else {
            GITLET_DIR.mkdir();
            BLOBS.mkdir();
            BRANCHES.mkdir();
            COMMITS.mkdir();
            REMOVED.mkdir();
            STAGED.mkdir();
            Commit initial_commit = new Commit("inital commit", null);
            saveCommit(initial_commit);
            File master = new File(BRANCHES, "master");
            String commit_id = initial_commit.getId();
            writeContents(master, commit_id);
            writeContents(HEAD, "master");
        }

    }

    public void add(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        String file_name = args[1];
        File file = new File(file_name);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            File staged = new File(STAGED, file_name);
            HashMap<String, String> cur_tracked = getHEAD().getTracked_files();
            String id = sha1(readContentsAsString(file));
            if (cur_tracked.keySet().contains(file_name) && cur_tracked.get(file_name).equals(id)) {
                File removed = new File(REMOVED, file_name);
                staged.delete();
                removed.delete();
            } else {
                String s = readContentsAsString(file);
                writeContents(staged, s);
            }
        }
    }

    /**
     * @param args
     * @source https://www.programiz.com/java-programming/examples/convert-array-set convert array to Set
     */
    public void commit(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            String message = args[1];
            HashSet<String> staged = new HashSet<>(Arrays.asList(STAGED.list()));
            HashSet<String> removed = new HashSet<>(Arrays.asList(REMOVED.list()));
            if (message.equals("")) {
                System.out.println("Please enter a commit message.");
                System.exit(0);
            } else if (staged.isEmpty() && removed.isEmpty()) {
                System.out.println("No changes added to the commit.");
                System.exit(0);
            } else {
                Commit head = getHEAD();
                HashMap<String, String> cur_tracked = head.getTracked_files();
                for (String name : cur_tracked.keySet()) {
                    if (staged.contains(name) || removed.contains(name)) {
                        cur_tracked.remove(name);
                    }
                }
                for (String name : staged) {
                    File file = new File(STAGED, name);
                    String id = sha1(readContents(file));
                    cur_tracked.put(name, id);
                    File blob = new File(BLOBS, id);
                    writeContents(blob, readContentsAsString(file));
                }
                Commit commit = new Commit(message, head.getId());
                commit.setTracked_files(cur_tracked);
                saveCommit(commit);
                String branch = readContentsAsString(HEAD);
                File b = new File(BRANCHES, branch);
                writeContents(b, commit.getId());
                for (File file : STAGED.listFiles()) {
                    file.delete();
                }
                for (File file : REMOVED.listFiles()) {
                    file.delete();
                }
            }
        }
    }

    public void log(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            printNextLog(getHEAD());
        }
    }

    public void checkout(String[] args) {
        if (args.length == 3 && args[1].equals("--")) {
            String cur_branch = readContentsAsString(HEAD);
            File file = new File(BRANCHES, cur_branch);
            String head = readContentsAsString(file);
            checkoutFile(args[2], head);
        } else if (args.length == 4 && args[2].equals("--")) {
            String id = args[1];
            String file = args[3];
            if (id.length() == 40) {
                checkoutFile(id, file);
            } else {
                String hashId = sha1(id);
                checkoutFile(hashId, file);
            }
        } else if (args.length == 2) {
            String branch = args[1];
            File file = new File(BRANCHES, branch);
            String head = readContentsAsString(HEAD);
            if (!file.exists()) {
                System.out.println("No such branch exists.");
            } else if (branch.equals(head)) {
                System.out.println("No need to checkout the current branch.");
            } else {
                String id = readContentsAsString(file);
                checkoutBranch(id);
                writeContents(HEAD, branch);
            }
        } else {
            System.out.println("Incorrect operands.");
        }
    }

    private void checkoutFile(String name, String id) {
        File file = new File(COMMITS, id);
        if (!file.exists()) {
            System.out.println("No commit with that id exists.");
        } else {
            Commit commit = getCommit(id);
            HashMap<String, String> tracked = commit.getTracked_files();
            if (tracked.keySet().contains(name)) {
                String blob = tracked.get(name);
                File f = new File(name);
                File blobF = new File(BLOBS, blob);
                writeContents(f, readContentsAsString(blobF));
            } else {
                System.out.println("File does not exist in that commit.");
            }
        }
    }

    private void checkoutBranch(String id) {
        Commit commit = getHEAD();
        Set<String> tracked = commit.getTracked_files().keySet();
        Commit commit2 = getCommit(id);
        Set<String> tracked2 = commit2.getTracked_files().keySet();
        for (String name : new File(".").list()) {
            if ((!tracked.contains(name) && tracked2.contains(name)) || (tracked.contains(name) && !tracked2.contains(name) && new File(REMOVED, name).exists())) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        for (String name : tracked2) {
            checkoutFile(name, id);
        }
        for (String name : tracked) {
            if ((!tracked2.contains(name))) {
                restrictedDelete(name);
            }
        }
        for (File file : STAGED.listFiles()) {
            file.delete();
        }
        for (File file : REMOVED.listFiles()) {
            file.delete();
        }
    }

    private void printNextLog(Commit commit) {
        if (commit == null) {
            return;
        } else {
            printLog(commit);
            Commit parent = getCommit(commit.getParent());
            printNextLog(parent);
        }
    }

    private void printLog(Commit commit) {
        if (commit == null) {
            return;
        }
        System.out.println("===");
        System.out.println("commit " + commit.getId());
        if (commit.getMerge_parent() != null) {
            String parent = commit.getParent();
            String merge_parent = commit.getMerge_parent();
            System.out.println("Merged " + merge_parent + " into " + parent);
        }
        System.out.println("Date: " + commit.getTime());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    private Commit getHEAD() {
        String cur_branch = readContentsAsString(HEAD);
        File f = new File(BRANCHES, cur_branch);
        return getCommit(readContentsAsString(f));
    }

    private void saveCommit(Commit commit) {
        byte[] serialized = serialize(commit);
        String id = sha1(serialized);
        File file = new File(COMMITS, id);
        commit.setId(id);
        writeObject(file, commit);
    }

    private Commit getCommit(String id) {
        if (id == null) {
            return null;
        }
        File file = new File(COMMITS, id);
        return readObject(file, Commit.class);
    }
}
