@synopsis{Programmatic access to the native system clipboard (copy and paste), with some handy (de)-escaping features.}
module util::Clipboard

@javaClass{org.rascalmpl.library.util.Clipboard}
@synopsis{If there is textual content in the current clipboard, then return it as a string.}
@description{
This will transfer any plaintext content that is currently on the system clipboard, no matter
the source to a string value.

If the system's clipboard is not accessible (say we are running in a headless environment),
then paste always returns the empty string.
}
@pitfalls{
* the first time paste or copy are called the UI toolkit must boot up; it can take a few seconds.
}
@benefits{
* copy/paste allow for interesting and useful user interactions, especially while experimenting on the REPL.
* paste encodes and escapes all kinds of wild characters automatically.
}
java str paste();

@javaClass{org.rascalmpl.library.util.Clipboard}
@synopsis{Load the contents of a string value into the system clipboard.}
@description{
This will put the contents of the string value in into the system clipboard.
So this is not the string value with quotes and escapes, but the pure string
data.

If the system's clipboard is not accessible (say we are running in a headless environment),
then copy always has no effect.
}
@pitfalls{
* the first time paste or copy are called the UI toolkit must boot up; it can take a few seconds.
}
@benefits{
* copy/paste allow for interesting and useful user interactions, especially while experimenting on the REPL. 
* copy transfers the pure content of the string, no quotes or escapes.
}
java void copy(str content);