module demo::StateMachine::CanReach

import demo::StateMachine:Syntax;
import IO;

alias state = str;

public rel[state,state] getTransitions(str fileName){
  return {<From, To> | trans <str From>, <str To> <- parseFSM(fileName)}+;
}

public void printCanReach(){
  transitions = getTransitions(fileName);
   
  for(state s <- carrier(transitions){
     canReach = transitions[state];
     println("<s> can reach <canReach>");
   }
}

public bool test(){
   printCanReach("src/demo/StateMachine/SM1");
   return true;
}