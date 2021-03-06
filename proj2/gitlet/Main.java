package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Bill Hu
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        } else if (!Utils.join(System.getProperty("user.dir"),
                ".gitlet").exists() && !args[0].equals("init")) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        String firstArg = args[0];
        Repository repo = new Repository();
        switch (firstArg) {
            case "init":
                repo.init(args);
                break;
            case "add":
                repo.add(args);
                break;
            case "commit":
                repo.commit(args);
                break;
            case "rm":
                repo.rm(args);
                break;
            case "log":
                repo.log(args);
                break;
            case "global-log":
                repo.globalLog(args);
                break;
            case "find":
                repo.find(args);
                break;
            case "status":
                repo.status(args);
                break;
            case "checkout":
                repo.checkout(args);
                break;
            case "branch":
                repo.branch(args);
                break;
            case "rm-branch":
                repo.rmBranch(args);
                break;
            case "reset":
                repo.reset(args);
                break;
            case "merge":
                repo.merge(args);
                break;
            default:
                System.out.println("No command with that name exists.");
                break;
        }
    }
}
