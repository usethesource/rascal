module lang::rascal::scrap::Patch

import lang::rascal::\syntax::Rascal;
import util::Reflective;
import ParseTree;
import List;
import String;

@synopsis{Convert a sequence of commands to a textual patch value to be applied to the editor
containing the commands. The patch is based on the results of evaluating the commands
and comparing the outputs with what is in the source (pt) itself. Differences in command
output are reconciled through the patch. 

A patch is list of tuples from loc to str. Loc to "" represents removal.
A loc with length=0 to x represents insertion of x.}
lrel[loc, str] commands2patch(start[Commands] pt) {
  
  // don't evaluate commands that represent output
  cmds = [ "<c>" | c <- pt.top.commands, !(c is output) ];
  results = evalCommands(cmds, pt@\loc);
  
  patch = [];
  
  args = pt.top.args[0].args; // the list of Commands (EvalCommand+)
  delta = 0; // maintain where we are in the results list.
  addedSpace = false; // whether a leading space was added as part of output.
  
  change = false;
  for (i <- [0..size(args)]) {
    
    if (i mod 2 == 0) { // a non-layout node
      if (!(args[i] is output)) { // a proper command
        <addedSpace, new> = resultSource(results[(i / 2) - delta]);
        
        old = "";
        j = i + 2;
        
        // collect all subsequent layout and outputs following
        // the current command to determine whether the computed
        // output is different from the output in the previous run.
        while (j < size(args), args[j] is output) {
          old += "<args[j - 1]><args[j]>";
          j += 2;
        }

        // if there's a change in output, add a tuple
        // to the patch.        
        if (new != "" && trim(old) != trim(new)) {
          org = args[i]@\loc; 
          at = org.offset + org.length;
          l = org[offset=at][length=0]; // insert
          patch += [<l, new>];
          change = true;
        }
        else {
          // signal to following iterations that there was no change.
          change = false;
        }
      }
      else {
        if (change) {
          // only remove previous output nodes if there was a change
          patch += [<args[i]@\loc, "">];
        }
        
        // output commands are not evaluated by evalCommands above;
        // delta maintains the difference for indexing.
        delta += 1;
      }
    }
    else {
        l = "<args[i]>";
        if (addedSpace && change && startsWith(l, " ")) {
          // if a leading space was added in the case of changed output,
          // remove it here. Otherwise leave the layout unchanged.
          org = args[i]@\loc; 
          patch += [<org[length=1], "">]; 
          addedSpace = false;
        }
    }
  }

  return patch;
}

private tuple[bool, str] resultSource(tuple[str val, str out, str err] output) {
  code = "";
  addedSpace = false;
  if (output.val == "", output.out == "", output.err == "") {
    return <addedSpace, code>;
  }
  if (output.val != "") {
    x = output.val;
    if (contains(x, "origin=")) {
      x = substring(x, 0, findFirst(x, ":"));
    }
    code += " ⇨ <x>\n";
    addedSpace = true;
  }
  if (output.out != "") {
    if (!endsWith(code, "\n")) {
      code += "\n";
    }
    code += "≫ <replaceLast(replaceAll(output.out, "\n", "\n≫ "), "≫ ", "")>";
  }
  if (output.err != "") {
    if (!endsWith(code, "\n")) {
      code += "\n";
    }
    err = output.err;
    if (contains(err, "")) {
      err = substring(err, 0, findFirst(err, ""));
    }
    code += "⚠ <replaceAll(trim(err), "\n", "\n⚠ ")>";
  }
  return <addedSpace, code>;
}
