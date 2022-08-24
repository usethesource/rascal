# Command History

.Synopsis
Use the command history.

.Description

RascalShell provides a history of previously entered commands. This can be accessed as follows:

:arrow-up: &#8593;
:arrow-down: &#8595;
:commandkey: &#8984;
:right-tab: &#8677;

[cols="1,3"]
|===
| kbd:[{arrow-up}]| Show previous command in history; Type kbd:[Return] to execute it.
| kbd:[{arrow-down}] | Next command in history; Type kbd:[Return] to execute it.
| kbd:[Ctrl+r]       |(After search text) Search backward in history.
| kbd:[Ctrl+s]       | (After search text) Forward search in history
|===

.Examples

[source,rascal-shell]
----
a = 1;
b = 2;
c = 3;
----
Typing kbd:[{arrow-up}] will then show the text (not followed by kbd:[Return]!):
[source,rascal,subs="quotes"]
----
rascal>c = 3;
----
Typing kbd:[Return] will reexecute `c = 3`.

Typing the letter `a` (the search text in this example) followed by kbd:[Ctrl+r] will show the text:
[source,rascal,subs="verbatim,quotes"]
----
(reverse-i-search)`a': a = 3;
----
And again, typing kbd:[Return] will reexecute `a = 3`.