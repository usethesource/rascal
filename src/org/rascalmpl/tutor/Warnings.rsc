module Warnings

// Maintain a list of warnings found for current concept;

private list[str] warnings = [];

// Get previous list of warnings and clear for next job.

public list[str] getAndClearWarnings(){
  w = warnings;
  warnings = [];
  return w;
}

// Add a warning.

public void addWarning(str txt){
  warnings += txt;
}