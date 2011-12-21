module demo::lang::MissGrant::MissGrant

start syntax Controller = controller: Events events ResetEvents? resets Commands? commands State+ states;

syntax Events = @Foldable "events" Event* "end";
syntax ResetEvents = @Foldable "resetEvents" Id* "end"; 
syntax Commands = @Foldable "commands" Command* "end";

syntax Command = command: Id name Id token;
syntax Event = event: Id name Id token;

syntax State = @Foldable state: "state" Id name Actions? Transition* "end";
syntax Actions = "actions" "{" Id+ "}";

syntax Transition = transition: Id event "=\>" Id state;

lexical Id = ([a-zA-Z][a-zA-Z0-9_]* !>> [a-zA-Z0-9_]) \ Reserved ;

keyword Reserved = "events" | "end" | "resetEvents" | "state" | "actions" ;

lexical LAYOUT = whitespace: [\t-\n\r\ ] | Comment ;

layout LAYOUTLIST = LAYOUT* !>> [\t-\n\r\ ] !>> "/*" ;

lexical Comment = @category="Comment"  "/*" CommentChar* "*/" ;

lexical CommentChar = ![*] | Asterisk ;

lexical Asterisk = [*] !>> [/] ;
 
