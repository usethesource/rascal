# Resources

.Synopsis
A `Resource` datatype and related functions on Eclipse resources.


.Usage
`import util::Resources;`

.Types
[source,rascal]
----
data Resource = root(set[Resource] projects) 
              | project(loc id, set[Resource] contents)
              | folder(loc id, set[Resource] contents)
              | file(loc id);
----

.Function

.Details

.Description
The `Resource` library provides direct access to Eclipse projects and the resources they contain. 
A `Resource` is the Rascal representation of an Eclipse project, or a folder or a file in an Eclipse project. 
In combination with the ((Prelude-IO)) library module, users of the Resources library gain access to the contents 
of any file that is in an Eclipse project.

Resource is a recursive data-type, where recursion indicates *containment*, i.e., 
a folder contains many other resources, a project also contains other resources. 
The root of an Eclipse workspace also contains other resources, in particular `projects`.

Each Resource, but the root, has an `id` field that explains the exact location of the resource.

The schema `project` that is supported by source locations (see ((Values-Location))) gives direct access to Eclipse projects.

The `Resource` library provides the following:
loctoc::[1]

.Examples
A location that points to a project in the Eclipse workspace  named "myProject":
[source,rascal]
----
|project://myProject| 
----

A location that points to a file named `HelloWorld.java` in the `src` 
folder of the `example-project` project in the workspace:
[source,rascal]
----
|project://example-project/src/HelloWorld.java| 
----

A location that points to a part of the previous file, 
namely the first 10 characters on the first line:
[source,rascal]
----
|project://example-project/src/HelloWorld.java|(0,10,1,0,1,10) 
----

Assuming that the project `|project://example-project|` exists in the current workspace, we can get the following:

[source,rascal-shell]
----
import util::Resources;
getProject(|project://example-project|);
----

[source,rascal-shell,error]
----
import util::Resources;
getProject(|project://example-project-which-does-not-exist|);
----


.Benefits

.Pitfalls
This library is only available for the Eclipse version of Rascal.

