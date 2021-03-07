# Gitlet Design Document

**Bill Hu**:

## Classes and Data Structures

### Main

This is the entry point to our program.
It takes in arguments from the command line and based on the command (the first element of the args array) calls the corresponding command in Repository which will actually execute the logic of the command.
It will also make sure that we are in an initialized git repository before using any commands.

#### Fields

This class has no fields and hence no associated state: it simply validates arguments and defers the execution to the Repository class.

### Repository

This is where the main logic of our program will live.
This file will handle all of the actual gitlet commands by reading/writing from/to the correct file, setting up persistence, and additional error checking.

It will also be responsible for setting up all persistence within gitlet.
This includes creating the .gitlet folder as well as the files where we store all git related objects such as blobs.

This class defers all Commit specific logic to the Commit class.


#### Fields

1. GITLET_DIR directory for .gitlet folder
2. HEAD pointer to where HEAD is (in GITLET_DIR folder)
3. COMMITS folder to store commits (in GITLET_DIR folder)
4. STAGED files that have been staged (in GITLET_DIR folder)
5. REMOVED files that have been removed (in GITLET_DIR folder)
6. BRANCHES file that stores each branch in the repository (in GITLET_DIR folder)
7. BLOBS file that stores blobs (in GITLET_DIR folder)

### Commit

This class represents a Commit all of which will be stored in a file.
Because each Commit will have a unique id, we may simply use that as the name of the file that the object is serialized to.

All Commits are serialized within the COMMIT folder which is within the .gitlet folder.
The Commit class has helpful methods that will perform various operations to the Commit object.

#### Fields

1. message: the message for the commit
2. time: the time at which the commit was made
3. parent: the current commits parent
4. merge_parent: the current commits merge parent (for merging)
5. tracked_files: Map of all files tracked by the current commit
6. id: sha1 hash of commit

## Algorithms



## Persistence

The directory structure looks like this:

    CWD
        .gitlet
            blobs
                list of blobs
            branches
                list of branches
            commits
                list of commits
            removed
                list of removed files
            staged
                list of staged files
            head

