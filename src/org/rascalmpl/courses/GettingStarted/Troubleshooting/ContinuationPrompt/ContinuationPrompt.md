---
title: Continuation prompt?
details:
   - '>>>>>>'
   - continuation
   - prompt
---

##### Synopsis

What is the continuation prompt `>>>>>>`?

#### Description

When Rascal can not recognize a complete [command]((RascalShell:Commands)) yet, it will 
prompt with this `>>>>>>`:

```rascal-shell
x = 1
```

The reason is that it expects a `;` after every ((Statements-Assignment)).
To cancel the entire command, simply provide an empty line:

```rascal-shell,continue

```

But we could also finish the command:

```rascal-shell
x = 1
;
```

#### Benefits

For typing more complex structured commands it is easy to split up a statement over several lines:

```rascal-shell
import IO;
for (int i <- [0..11]) {
    println("Counting <i>");
}
```

As you can see the entire ((Statements-For)) loop only starts when the prompt recognizes the entire command.

#### Pitfalls

If you type an empty line in the middle of a complex command, it is cancelled:

```rascal-shell
import IO;
for (int i <- [0..11]) {

```



