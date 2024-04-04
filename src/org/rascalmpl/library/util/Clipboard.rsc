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
java str paste();

@javaClass{org.rascalmpl.library.util.Clipboard}
@synopsis{Load the pretty printed value as a string into the system clipboard.}
@description{
This will pretty print a value to a string and then load it into the clipboard.
* top-level strings are unquoted and de-escaped first
* top-level parse trees are yielded
* structured values are pretty-printed with each indentation level `level` spaces deep.

If the system's clipboard is not accessible (say we are running in a headless environment),
then copy always has no effect.
}
java void copy(str content, bool indent=true, int level=2);