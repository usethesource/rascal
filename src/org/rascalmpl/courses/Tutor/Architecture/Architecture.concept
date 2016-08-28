# Architecture

.Synopsis
The global architeture of the Rascal Tutor

.Description

At the highest level, a course consists of concept files, which are translated to intermediate 
http://asciidoctor.org[AsciiDoc] files,
which in their turn, can be translated to html, pdf, epub and other output formats:

image::Global.png[Global Translation]

The compilation and runtime use of a single course are organized as follows:

image::CompileUse.png[Compilation of a course]

A course is a directory with concept files. Each concept file yields:

* An AsciiDoc file for further processing
* Onthology: the hierarchical structure of the concepts.
* Lucene-based Index-data extracted from the various sections of the concept.

External concepts refer to Rascal source code that contains embedded concepts in `@doc{ ... }`.
Similar information is extracted from them as for a standard concept.

Once all concepts have been processed their extracted information is combined:

* All AsciiDoc files are included in a single AsciiDoc file for the whole course. 
  This will be converted to the desired output format.
* The index-data are included in a single index.

The end results of processing a complete course are:

* A single html (or other) file for the whole course.
* A single index file for the whole course.

At runtime, the generated Hhtml can be viewed via a browser and search queries
can be answered using https://lucene.apache.org[Lucene] and the single index.

Another way to use a compiled course is directly from the REPL.

.Benefits

* Index creation is modular: an index is created per course and these indices are combined at runtime.

.Pitfalls

* Courses are compiled on a per-course basis. 
  Support for incremental courses compilation is not yet available.

