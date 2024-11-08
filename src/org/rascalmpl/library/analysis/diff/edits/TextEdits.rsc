@license{
Copyright (c) 2022, NWO-I Centrum Wiskunde & Informatica (CWI) 
All rights reserved. 
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
  
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
  
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
  
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
}
@synopsis{Intermediate representation for file creation, removal and changing, including textual (string) rewriting.}
@description{
((DocumentEdit))s can be produced by source-to-source transformation tools, and then executed 
via ((executeDocumentEdits)) in the REPL or ((applyDocumentsEdits)) by the IDE.
}
@benefits{
* Document edits can be attached to ((data:CodeAction))s and error ((util::IDEServices-Message))s, to achieve interactive 
source code rewriting utilities. 
* Document edits can be tested via ((executeDocumentEdits))
* Document edits can be "high fidelity", avoiding unnecessary damage to a source text.   
}
@pitfalls{
* Code edits depend on a specific state of the source file that may be transient while editing. Use the ((CodeAction)) interface
to avoid racing for the state of the source file.
}
module analysis::diff::edits::TextEdits

@synopsis{File changing operations}
data DocumentEdit
    = removed(loc file)
    | created(loc file)
    | renamed(loc from, loc to)
    | changed(loc file, list[TextEdit] edits)
    ;

@synopsis{Shorthand for file changes.}
DocumentEdit changed(list[TextEdit] edits:[replace(loc l, str _), *_])
    = changed(l.top, edits);

@synopsis{String rewriting operations}
@description{
The core operation is to replace a substring with another.
The replace operator uses a `loc` value to point to a range inside a string,
and a `str` as its replacement.
}
data TextEdit
    = replace(loc range, str replacement);

@synopsis{Deletion is replacement with an empty string.}
TextEdit delete(loc range) 
    = replace(range, "");

@synopsis{Inserting before a given range.}
TextEdit insertBefore(loc range, str insertion, str separator=" ")
    = (range.begin?)
        ? replace(range.top(range.offset, 0, range.begin, range.begin), "<insertion><separator>")
        : replace(range.top(range.offset, 0), "<insertion><separator>");

@synopsis{Inserting after a given range.}
TextEdit insertAfter(loc range, str insertion, str separator=" ")
    = (range.end?)
        ? replace(range.top(range.offset + range.length, 0, range.end, range.end), "<separator><insertion>")
        : replace(range.top(range.offset + range.length, 0), "<separator><insertion>");
 
