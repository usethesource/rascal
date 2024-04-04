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
@synopsis{Load the contents of a string value into the system clipboard.}
@description{
This will put the contents of the string value in into the system clipboard.
So this is not the string value with quotes and escapes, but the pure String
data.

If the system's clipboard is not accessible (say we are running in a headless environment),
then copy always has no effect.
}
java void copy(str content);