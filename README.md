DECENTRALJ
==========

Decentral Server Java Implementation

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

At the beginning each member will choose a feature to develop.
The feature branch will exitst as long as the feature is in development.
Once the feature has been completed, the code will be merged back into *develop*

A feature branch will exist in your local repo, not in *origin*

To start working on a new feature, branch off from the *develop* branch.


Status
------

The [team](https://github.com/orgs/DecentralBank/people) is currently working on a series of pre-releases on the way to version 1.0. See the [roadmap](https://github.com/DecentralBank/decentralj/wiki/Roadmap) for details.



Staying in Touch
----------------

Contact the team and keep up to date using any of the following:

 - The [#DecentralBank](https://webchat.freenode.net/?channels=DecentralBank) IRC channel on Freenode
 - Our [mailing list](https://groups.google.com/forum/#!forum/DecentralBank)
 - [@decentralteam](https://twitter.com/decentralteam) on Twitter
 - [decentralteam@gmail.com](mailto:decentralteam@gmail.com)
 - The [DecentralBank thread](https://bitcointalk.org/index.php?topic=718112.new#new) on the Bitcointalk forum
 - GitHub [Issues](https://github.com/DecentralBank/decentralj/issues)

If you want to help request to join our Slack Account.


