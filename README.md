decentralj
==========

Decentral Java Implementation

# Environment
IntelliJ (JDK1.8)

###Libraries
Zeromq
BitcoinJ

##Running Zeromq on IntelliJ
Set VM options to run -Djava.library.path=/usr/local/lib  or wherever your bindings are placed.

# Committing code
Do not commit .idea files
Always pull before pushing

# Deployment Flow

## Main branches

### Dev - *develop* branch
Use  [git branch](http://git-scm.com/book/ch3-2.html) to create a *develop* branch of the codebase rather than making edits directly to the *master* branch
> 
>

### Staging
Once there's a realease candidate all all the code will be merged into a *staging* branch.
Use [git tag](http://git-scm.com/book/en/Git-Basics-Tagging) to tag a release candidate.
If last minute code/commits or code changes need to be made, do so in the *develop* branch.

### Production - *master* branch
The last train stop
Once testing has been performed in *staging*, and we have not been able to break the application, merge the code from *staging* into *master*.

## Supporting branches
These are the limited-life time branches, that will be removed

### Feature branches
### Release branches
### Hotfix branches
