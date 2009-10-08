module experiments::GrammarTools::LRTable

alias State = int;

data Action = shift(State s) | reduce(Rule r) | accept() | error();


map[tuple[State, Symbol], State] actions;

Action action(State s, Symbol sym){
   return actions[<s,sym>];
}

State goto(State s, Symbol sym){

}
