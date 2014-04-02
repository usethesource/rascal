@doc{
Synopsis: a callback framework for externally registering M3 extractors.

Description:

This module facilitates extension for different kinds of M3 extractors (e.g. for different languages)  
}
module analysis::m3::Extractors

extend analysis::m3::Core;
import util::FileSystem;
import String;

alias Extractor = tuple[set[M3] models, set[loc] rest] (loc project, set[loc] files); 

private set[Extractor] registry = {};

void registerExtractor(Extractor extractor) {
  registry += {extractor};
}

set[M3] extractM3(loc project) = extractM3(project, {project});

@doc{
Synopsis: runs all extractors on a project to return one M3 model per file in the project
}
set[M3] extractM3(loc project, set[loc] roots) {
  todo = { *files(r) | r <- roots };
  
  result = for (e <- registry) {
    <models, todo> = e(project, todo);
    append models;
  }
  
  return {*m | m <- result} + {genericM3(f) | f <- todo}; 
}

M3 genericM3(loc file) {
  m = m3(file);
  
  m@declarations = { };
  m@uses = { };
  m@containment = { };
  m@documentation = { };
  m@modifiers = { };
  m@messages = [ ];
  m@names = { };
  m@types = { };
  
  try {
    content = readFile(file);
    chs = size(content);
    lines = chs == 0 ? 1 : (1 | it + 1 | /\n/ := content);
    lastline = size(readFileLines(file)[-1]);
    m@declarations = { <file[scheme="m3+unit"], file(0,chs,<1,0>,<lines - 1,lastline>)> }; 
  }
  catch IO(str msg) : {
    m@messages += [error(msg, file)];
  }
  
  return m;
}