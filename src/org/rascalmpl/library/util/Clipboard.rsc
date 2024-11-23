@synopsis{Programmatic access to the native system clipboard (copy and paste), with some handy (de)-escaping features.}
module util::Clipboard

import Exception;

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
* paste provides the content of the current clipboard, where other processes can always put new content int.
The `paste` function is thread-safe, it will provide the contents of the clipboard as it was at a certain
time, but it does not have to be the latest version.
}
@benefits{
* past never throws exceptions, it just returns "" if something fails and prints an exception. If you
need a less robust version look at `past(str mimetype)` below.
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
* if another application copies something to the clipboard, or content can be overwritten. There 
is always a race for the clipboard, but this function is thread-safe. Right after its execution the
content of the clipboard is guaranteed to contain `content`.
}
@benefits{
* copy/paste allow for interesting and useful user interactions, especially while experimenting on the REPL. 
* copy transfers the pure content of the string, no quotes or escapes.
}
java void copy(str content);

@javaClass{org.rascalmpl.library.util.Clipboard}
@synopsis{Lists the available mimetypes on the current clipboard, restricted to the ones we can serialize to string.}
@description{
The clipboard can contain many kinds of textual data. With this query you retrieve all the supported mimetypes that we can
safely and correctly serialize to a string. 

Most of the mimetypes contain the exact charset encoding parameter, such that we do not have to worry about that here.
Using `paste(str mimetype)` those details will be taken care off. However if you call `paste` with an unavailable
encoding, there will be an appropriate exception.

If this function returns `{}` then there is nothing on the clipboard that can be serialized to a string.
}
@pitfalls{
* after calling this function, the user may have selected another content for the clipboard. There is always a race
for the clipboard. This function is thread-friendly, however and will not crash but just provide outdated information.
}
java rel[str humanReadable, str fullMimetype] availableTextMimetypes();

@javaClass{org.rascalmpl.library.util.Clipboard}
@synopsis{Lists the available mimetypes on the current clipboard, restricted to the ones we can serialize to string and that start with `shortMimetype`.}
@description{
The clipboard can contain many kinds of textual data. With this query you retrieve all the exact supported mimetypes 
for, for example, `text/html`. The function will fill in the specific charset and implementation class parameters of 
the mimetype, for all supported formats.

Most of the mimetypes contain the exact charset encoding parameter, such that we do not have to worry about that here.
Using `paste(str mimetype)` those details will be taken care off. However if you call `paste` with an unavailable
encoding, there will be an appropriate exception.

If this function returns `{}` then there is nothing on the clipboard that can be serialized to a string and matches
the `shortMimetype`.
}
@pitfalls{
* after calling this function, the user may have selected another content for the clipboard. There is always a race
for the clipboard. This function is thread-friendly, however and will not crash but just provide outdated information.
}
java rel[str humanReadable, str fullMimetype] availableTextMimetypesFor(str shortMimetype);

@javaClass{org.rascalmpl.library.util.Clipboard}
@synsopsis{Serializes the current contents of the clipboard that matches the given mimetype to a string}
@description{
This only works for ((availableTextMimetypes)), otherwise an exception is thrown. 

This function behaves as `paste()`, but it can serialize all kinds of other data, as long as the internal
data flavors for the given mimetypes support textual serialization. In principle these are the mimetypes
that list a charset parameter, but there are also some heuristics that enlarge the set a bit. The supported
mimetypes are always listed in ((availableTextMimetypes)).
}
java str paste(str mimetype) throws IO, IllegalArgument;
