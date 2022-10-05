---
title: Getting Started with Rascal
sidebar_position: 1
keywords:
  - help
  - start
  - download
  - install
---

#### Download and installation

Rascal is deployed as one of four easy-to-use packages:
1. **A standalone "jar" file**, which can be downloaded [here](https://update.rascal-mpl.org/console/rascal-shell-stable.jar)
2. A **Visual Studio Code extension**, which can be found [here](https://marketplace.visualstudio.com/items?itemName=usethesource.rascalmpl) or search for "Rascal" in the "Extension" view in VScode itself.
3. An **Eclipse plugin**, for which the update site is <https://update.rascal-mpl.org/stable/>.
4. A set of **Maven MOJOs**, for which the plugin repository is <https://releases.usethesource.io/maven/>

For developers on Rascal itself or experimental libraries, who want to work with on continuously integrated and tests releases please have a look at the ((Developers)) course.

#### Running Rascal: starting a terminal with a read-eval-print-loop

1. On the Unix or Windows commandline, simply start a ((RascalShell)) by: `java -jar rascal-<version>.jar`
2. In VScode, in the command palette type `Rascal` and select `Create Rascal Terminal`
3. In Eclipse, from the button bar select the button with the Rascal logo.
4. With Maven, create a pom.xml which includes the above plugin and type: `mvn rascal:console`

You will be prompted for input right after the version is printed and information about the current search paths. 

Here we show how to quit the terminal:
```rascal
:quit
```

Similarly you could type `CTRL+D` which indicates end-of-file to terminate the process. More information 
about the features of the shell can be found [here]((RascalShell)). At the prompt you can type ((Rascal:Expressions)), ((Rascal:Statements)) and ((Rascal:Declarations)).

```rascal-shell
1 + 1
myList = [ i | i <- [1..11], i % 2 == 0];
import Prelude;
println("Hello <myList> is <size(myList)> long");
```

#### Troubleshooting

In case of trouble, there is no need to panic. [Help]((GettingHelp)) is nearby.

* Common troubles with installation and first runs are documented [here]((Troubleshooting))
* If you have a question that probably has Rascal _source code_ as an answer, go to [StackOverflow](http://stackoverflow.com/questions/tagged/rascal). Either find your answer directly, or you are welcome to post a new question using the tag `[rascal]`.
* If you think you've run into a bug, we are very happy to receive your report on [Github](http://github.com/usethesource/rascal/issues). 
* If any documentation is unclear, incomplete or ambiguous you are also invited to submit a report there, but you may also use the `edit` button below to propose a fix.

