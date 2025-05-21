@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module util::PathConfig

import Message;

data RascalConfigMode
    = compiler()
    | interpreter()
    ;

@synopsis{General configuration (via path references) of a compiler or interpreter.}
@description{
A PathConfig is the result of dependency resolution and other configuration steps. Typically,
IDEs produce the information to fill a PathConfig, such that language tools can consume it
transparantly. A PathConfig is also a log of the configuration process. 

* `srcs` list of root directories to search for source files; to interpret or to compile.
* `ignores` list of directories and files to not compile or not interpret (these are typically subtracted from the `srcs` tree, or skipped when the compiler arives there.)
* `bin` is the target root directory for the output of a compiler. Typically this directory would be linked into a zip or a jar or an executable file later.
* `libs` is a list of binary dependency files (typically jar files or target folders) on other projects, for checking and linking purposes.
* `resources` is a list of files or folders that must be copied to the bin folder, syncronized with the binary output of a compiler.
* `messages` is a list of info, warning and error messages informing end-users about the quality of the configuration process. Typically missing dependencies would be reported here, and clashing versions.
}
data PathConfig 
  = pathConfig(
        loc projectRoot        = |unknown:///|,
        list[loc] srcs         = [],  
        list[loc] ignores      = [],  
        loc bin                = |unknown:///|,
        list[loc] resources    = [],
        list[loc] libs         = [],          
        list[Message] messages = []
    );
