# Edit Command

.Synopsis
Open an editor for a Rascal module

.Syntax
* `:edit ModuleName`  

.Description

This opens an editor for the given module name. For every context of running Rascal, this could
mean different things:

* On the Unix commandline the `${EDITOR}` environment variable will be used to open a file. If that file is present in the local file system, it is opened by running `${EDITOR} /absolute/path/to/module.rsc`, but if the file is hidden behind an opaque ((Values-Location)) scheme, then it is first copied to a temporary file on the local file system, and then opened.
* In Eclipse, the eclipse editor framework is used to open an editor for the file. If it is a so called "resource" in the eclipse file system, and editable file is opened with all the Rascal language support. If the file is from an embedded library (inside a jar) then the contents of the file are shown in a similar editor, but read-only.
* In VScode a similar experience is provided as in Eclipse, but the editor for library files does not know it is read-only.
