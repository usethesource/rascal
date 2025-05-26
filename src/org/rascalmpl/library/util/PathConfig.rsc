@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Standard intermediate storage format for configuring file-based language processors (such as interpreters, type-checkers and compilers)}
@description{
The module offers the ((PathConfig)) datatype which standardizes the configuration of 
language processors for source code projects.
}
module util::PathConfig

import Message;
import String;

@synopsis{General configuration (via locations) of a file-based language processor.}
@description{
A PathConfig is the result of dependency resolution and other configuration steps. Typically,
IDEs produce the information to fill a PathConfig, such that language tools can consume it
transparantly. A PathConfig is also a log of the configuration process. Typically a single
((pathConfig)) instance configures the language processor for a single source project tree.

* `projectRoot` is the root directory of the source project tree that is being configured.
* `srcs` list of root directories to search for source files; to interpret or to compile.
* `ignores` list of directories and files to not compile or not interpret (these are typically subtracted from the `srcs` tree, and/or skipped when the compiler arrives there.)
* `bin` is the target root directory for the output of a compiler. Typically this directory would be linked into a zip or a jar or some other executable archive later.
* `libs` is a list of binary dependencies (typically jar files or bin folders) on other projects, for checking and linking purposes. Each entry is expected to return `true` for ((isDirectory)).
* `resources` is a list of files or folders that will be copied *by the compiler* to the bin folder, synchronized with its other (binary) output files..
* `messages` is a list of info, warning and error messages informing end-users about the quality of the configuration process. Typically missing dependencies would be reported here, and clashing versions.
}
@benefits{
* `main` functions which have a keyword parameter of type ((PathConfig)) are automatically augmented with commandline parameters for every field of ((PathConfig))
* `messages` can be printed in a standard way using ((mainMessageHandler))
* ((PathConfig)) is a reusable bridge between language processing functions and different execution environments such as VScode, the commandline or Maven.
* ((PathConfig)) makes all configuration processors of file-based language processors explicit and transparent
* ((PathConfig)) is programming language and domain-specific language independent
}
data PathConfig = pathConfig(
    loc projectRoot        = |unknown:///|,
    list[loc] srcs         = [],  
    list[loc] ignores      = [],  
    loc bin                = |unknown:///|,
    list[loc] resources    = [],
    list[loc] libs         = [],          
    list[Message] messages = []
);
