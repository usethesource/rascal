module experiments::tutor3::ConvertQuestions

import IO;
import List;
import String;
import util::FileSystem;
import ParseTree;
import util::Reflective;
import lang::rascal::\syntax::Rascal;
/*

Name:               =>  # the_name
the_name           

Synopsis:           => .Synopsis
enz.

<listing>           => ```rascal            PM from file
</listing>          => ```

<screen>            => ```rascal-shell      PM errors en continue
</screen>           => ```

$VarName$           => _VarName_
$VarName_index$     => _VarName~index~_
$VarName^index$     => _VarName^index^_

<warning>           => WARNING:
</warning>          => ""

[ConceptName]       => 
[$ConceptName]      => 
$OtherCourse:ConceptName] =>


In code:

*/

str getAnchor(loc src){
    parts = split("/", src.path);
    return "<parts[-3]>-<parts[-2]>";
}

str convert(loc src){    
    return convert(getAnchor(src), readFileLines(src));
}

str hashes(int n) = "<for(int i <-[0..n]){>#<}>";

str convert(str anchor, list[str] lines){
  result = ""; //"[[<anchor>]]\n";
  int i = 0;
  
  while (i < size(lines)){
    switch(lines[i]){
  
 
    case /^Questions:/: 
    {   i += 1;
        while(i < size(lines)){
          result += lines[i] + "\n";
          i += 1;
        }
        return result;
    }
    // anything else
    default: {
      i += 1;
    }
    }
  }
  return result;
}

value main(){

    for(conceptFile <- find(|file:///Users/paulklint/git/rascal/src/org/rascalmpl/courses/|, "concept")){
        
        lines = readFileLines(conceptFile);
        questions = trim(convert(conceptFile));
        if(questions != ""){
            println("conceptFile: <conceptFile>");
            println(questions);
        }
    }
    
    return true;
}
