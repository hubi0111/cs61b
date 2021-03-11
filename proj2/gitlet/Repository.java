package gitlet;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static gitlet.Utils.*;

/**
 * Represents a gitlet repository.
 *
 * @author Bill Hu
 */
public class Repository {
    /**
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
     * The blobs directory.
     */
    public static final File BLOBS = join(GITLET_DIR, "blobs");

    /**
     * The branches directory.
     */
    public static final File BRANCHES = join(GITLET_DIR, "branches");

    /**
     * The commits directory.
     */
    public static final File COMMITS = join(GITLET_DIR, "commits");

    /**
     * The removed directory.
     */
    public static final File REMOVED = join(GITLET_DIR, "removed");

    /**
     * The staged directory.
     */
    public static final File STAGED = join(GITLET_DIR, "staged");

    /**
     * The .gitlet directory.
     */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    /**
     * sets up persistance and creates inital commit
     *
     * @param args
     */
    public void init(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else if (GITLET_DIR.exists()) {
            System.out.println("A gitlet version control "
                    + "system already exists in the current directory.");
            System.exit(0);
        } else {
            GITLET_DIR.mkdir();
            BLOBS.mkdir();
            BRANCHES.mkdir();
            COMMITS.mkdir();
            REMOVED.mkdir();
            STAGED.mkdir();
            Commit intialCommit = new Commit("initial commit", null);
            intialCommit.setTime("Thu Jan 1 00:00:00 1970 -0800");
            saveCommit(intialCommit);
            File master = new File(BRANCHES, "master");
            String commitId = intialCommit.getId();
            writeContents(master, commitId);
            writeContents(HEAD, "master");
        }

    }

    /**
     * adds the specified file to staged folder if it is altered
     *
     * @param args
     */
    public void add(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        String fileName = args[1];
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            File staged = new File(STAGED, fileName);
            HashMap<String, String> curTracked = getHEAD().getTrackedFiles();
            String id = sha1(readContentsAsString(file));
            if (curTracked.keySet().contains(fileName) && curTracked.get(fileName).equals(id)) {
                File removed = new File(REMOVED, fileName);
                staged.delete();
                removed.delete();
            } else {
                String s = readContentsAsString(file);
                writeContents(staged, s);
            }
        }
    }

    /**
     * updates all currently tracked files accoding to the following
     * 1. gets the previous commits currently tracked files
     * 2. or add files in either staged or removed, remove them from the hashmap
     * 3. for add files in staged, add them to the hashmap
     * this is because we have altered the file, and need to replace the old tracked with the new one
     * 4. for all altered files, create a new blob
     * 5. writes the commit with the new tracked files
     * 6. delete everything in staged and removed
     *
     * @param args
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
                HashMap<String, String> curTracked = head.getTrackedFiles();
                HashMap<String, String> newTracked = curTracked;
                for (String name : curTracked.keySet()) {
                    if (!staged.contains(name) && !removed.contains(name)) {
                        newTracked.put(name, curTracked.get(name));
                    }
                }
                for (String name : staged) {
                    File file = new File(STAGED, name);
                    String id = sha1(readContents(file));
                    newTracked.put(name, id);
                    File blob = new File(BLOBS, id);
                    writeContents(blob, readContentsAsString(file));
                }
                Commit commit = new Commit(message, head.getId());
                commit.setTrackedFiles(newTracked);
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

    /**
     * removes the file from either staged or the currently tracked files
     * first checks if we can remove from tracked
     * if not, then remove from directory
     *
     * @param args
     */
    public void rm(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            String name = args[1];
            File file = new File(name);
            File staged = new File(STAGED, name);
            Set<String> tracked = getHEAD().getTrackedFiles().keySet();
            if (tracked.contains(name)) {
                File remove = new File(REMOVED, name);
                remove.mkdir();
                restrictedDelete(file);
            }
            if (!staged.delete() && !tracked.contains(name)) {
                System.out.println("No reason to remove the file.");
            }
        }
    }

    /**
     * prints a commit history
     * does this recursively by starting at head until commit parent is null
     *
     * @param args
     */
    public void log(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            printNextLog(getHEAD());
        }
    }

    /**
     * log but for all commits
     *
     * @param args
     */
    public void globalLog(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            for (File commit : COMMITS.listFiles()) {
                Commit c = readObject(commit, Commit.class);
                printLog(c);
            }
        }
    }

    /**
     * finds all commits of the given message
     * does this by looping through commits and checking the message is equal to the specified message
     *
     * @param args
     */
    public void find(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            boolean found = false;
            for (File commit : COMMITS.listFiles()) {
                Commit c = readObject(commit, Commit.class);
                if (c.getMessage().equals(args[1])) {
                    System.out.println(c.getId());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Found no commit with that message.");
            }
        }
    }

    /**
     * prints a status log of the current repository
     *
     * @param args
     */
    public void status(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            String[] branches = BRANCHES.list();
            String[] staged = STAGED.list();
            String[] removed = REMOVED.list();
            String[] modifications = null; //TODO
            String[] untracked = null; //TODO
            String curBranch = readContentsAsString(HEAD);
            Arrays.sort(branches);
            Arrays.sort(staged);
            Arrays.sort(removed);
//            Arrays.sort(modifications);
//            Arrays.sort(untracked);
            System.out.println("=== Branches ===");
            for (String b : branches) {
                if (b.equals(curBranch)) {
                    System.out.print("*");
                }
                System.out.println(b);
            }
            System.out.println();
            System.out.println("=== Staged Files ===");
            for (String s : staged) {
                System.out.println(s);
            }
            System.out.println();
            System.out.println("=== Removed Files ===");
            for (String r : removed) {
                System.out.println(r);
            }
            System.out.println();
            System.out.println("=== Modifications Not Staged For Commit ===");
//            for (String m : modifications) {
//                System.out.println(m);
//            }
            System.out.println();
            System.out.println("=== Untracked Files ===");
//            for (String u : untracked) {
//                System.out.println(u);
//            }
            System.out.println();
        }
    }

    /**
     * depending on the arguments, perform a checkout method
     *
     * @param args
     */
    public void checkout(String[] args) {
        if (args.length == 3 && args[1].equals("--")) {
            String curBranch = readContentsAsString(HEAD);
            File file = new File(BRANCHES, curBranch);
            String head = readContentsAsString(file);
            checkoutFile(args[2], head);
        } else if (args.length == 4 && args[2].equals("--")) {
            String id = args[1];
            String file = args[3];
            if (id.length() != 40) {
                id = getLongId(id);
            }
            checkoutFile(file, id);
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

    /**
     * creates a new branch in the branches folder of the given name and copies the current branch
     *
     * @param args
     */
    public void branch(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            String branch = args[1];
            File newBranch = new File(BRANCHES, branch);
            if (newBranch.exists()) {
                System.out.println("A branch with that name already exists.");
            } else {
                String head = readContentsAsString(HEAD);
                File file = new File(BRANCHES, head);
                writeContents(newBranch, readContentsAsString(file));
            }
        }
    }

    /**
     * removes the specified branch from the branches folder.
     *
     * @param args
     */
    public void rmBranch(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            String branch = args[1];
            File remove = new File(BRANCHES, branch);
            if (!remove.exists()) {
                System.out.println("A branch with that name does not exist.");
            } else if (branch.equals(readContentsAsString(HEAD))) {
                System.out.println("Cannot remove the current branch.");
            } else {
                remove.delete();
            }
        }
    }

    /**
     * resets all files to a given commit
     * checks out to the specified branch
     * then reads head to get the current branch
     * finally goes into branch folder and updates current branch to most commit
     *
     * @param args
     */
    public void reset(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            String id = args[1];
            if (id.length() != 40) {
                id = getLongId(id);
            }
            File file = new File(COMMITS, id);
            if (!file.exists()) {
                System.out.println("No commit with that id exists.");
            } else {
                checkoutBranch(id);
                String curHEAD = readContentsAsString(HEAD);
                File branch = new File(BRANCHES, curHEAD);
                writeContents(branch, id);
            }
        }
    }

    /**
     * merges all files from the given branch to the current one
     *
     * @param args
     */
    public void merge(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else {
            //do merge
        }
    }

    /**
     * checks out the file name to the commit with id id
     * will look at the tracked files of the specified commit and retrieve the sha1 hash of the requested file
     * then looks in blobs and finds the correct file with the sha1 hash
     * replaces the file with the one from blobs
     *
     * @param name file name
     * @param id   commit id
     */
    private void checkoutFile(String name, String id) {
        File file = new File(COMMITS, id);
        if (id == null || !file.exists()) {
            System.out.println("No commit with that id exists.");
        } else {
            Commit commit = getCommit(id);
            HashMap<String, String> tracked = commit.getTrackedFiles();
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

    /**
     * Helper method to checkout to a branch
     * Will try to replace all files in directory with those that are traced by the most recent commit the specified branch
     * Identical files will not be affected
     * Non-existent files will be added
     * Files that no longer exist will be deleted
     *
     * @param id branch to checkout to
     */
    private void checkoutBranch(String id) {
        Commit commit = getHEAD();
        Set<String> tracked = commit.getTrackedFiles().keySet();
        Commit commit2 = getCommit(id);
        Set<String> tracked2 = commit2.getTrackedFiles().keySet();
        for (String name : new File(".").list()) {
            if ((!tracked.contains(name) && tracked2.contains(name)) || (tracked.contains(name)
                    && !tracked2.contains(name) && new File(REMOVED, name).exists())) {
                System.out.println("There is an untracked file in the way"
                        + "; delete it, or add and commit it first.");
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

    /**
     * gets the next commit if there is any by looking at parent
     *
     * @param commit current commit
     */
    private void printNextLog(Commit commit) {
        if (commit == null) {
            return;
        } else {
            printLog(commit);
            Commit parent = getCommit(commit.getParent());
            printNextLog(parent);
        }
    }

    /**
     * Prints a single log of the specified commit
     *
     * @param commit commit to be printed
     */
    private void printLog(Commit commit) {
        if (commit == null) {
            return;
        }
        System.out.println("===");
        System.out.println("commit " + commit.getId());
        if (commit.getMergeParent() != null) {
            String parent = commit.getParent();
            String mergeParent = commit.getMergeParent();
            System.out.println("Merged " + mergeParent + " into " + parent);
        }
        System.out.println("Date: " + commit.getTime());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    /**
     * gets the head pointer
     * first looks at contents of HEAD file and get's the branch
     * then goes into branches folder and returns the commit that the file is pointing towards.
     *
     * @return HEAD commit
     */
    private Commit getHEAD() {
        String curBranch = readContentsAsString(HEAD);
        File f = new File(BRANCHES, curBranch);
        return getCommit(readContentsAsString(f));
    }

    /**
     * Writes the commit to the commit folder in .gitlet
     *
     * @param commit commit object to add
     */
    private void saveCommit(Commit commit) {
        byte[] serialized = serialize(commit);
        String id = sha1(serialized);
        File file = new File(COMMITS, id);
        commit.setId(id);
        writeObject(file, commit);
    }

    /**
     * Takes in a string corresponding to the commit id and returns the Commit object correspondingly
     *
     * @param id id fo the commit wanted
     * @return the Commit wanted
     */
    private Commit getCommit(String id) {
        if (id == null) {
            return null;
        }
        File file = new File(COMMITS, id);
        return readObject(file, Commit.class);
    }

    /**
     * Takes in the short id and iterates through commit folder to find commit with that prefix
     * Could be improved taking inspiration from actual git and dividing commits into folder by prefix of id
     *
     * @param shortId prefix of commit id
     * @return returns the full commit id
     */
    private String getLongId(String shortId) {
        for (String id : COMMITS.list()) {
            if (id.substring(0, shortId.length()).equals(shortId)) {
                return id;
            }
        }
        return null;
    }
}
