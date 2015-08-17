module util::REPL


data REPLFeature
  = inputProcessing(tuple[str result, str prompt] (str line) cmd)
  | fragmentCompletion(tuple[int fragmentOffset, int fragmentLength, list[str] suggestions] (str line, int cursorOffset) completion)
  | fileChanges(set[loc] () monitor, tuple[str result, str error] (loc changed) changed)
  | style(bool prettyPrompt = true, bool allowColors = true, bool useVT102Features = true)
  | title(str name)
  ;

java void startREPL(str headerText, str initialPrompt, set[REPLFeature] features);