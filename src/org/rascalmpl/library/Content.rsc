module Content

@doc{
.Synopsis

Content wraps str or loc values with an interpretation for (interactive) content visualization 

.Description

Values wrapped in a `Content` wrapper will be displayed by interactive
Rascal applications such as the IDE, the REPL terminal and the documentation pages. 

For example, a piece of html can be displayed inline in the tutor:
[source,rascal-shell]
html("\<a href=\"http://www.rascal-mpl.org\"\>Rascal homepage\</a\>")
----
}
data Content 
  = html(str val)
  | html(loc src)
  | plain(str val)
  | plain(loc src)
  | png(loc src)
  ;

