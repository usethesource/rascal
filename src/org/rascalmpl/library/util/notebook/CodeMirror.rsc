module util::notebook::CodeMirror

import IO;
import String;
import util::notebook::Mode;


void createCodeMirrorModeFile(Mode mode, loc path){
	str content = parseMode(mode);
	writeFile(path, content);
}

str parseMode(Mode mode) =
  "(function(mod) {
  '	if (typeof exports == \'object\' && typeof module == \'object\') // CommonJS
  '		mod(require(\'../../lib/codemirror\'), require(\'../../addon/mode/simple\'));
  '	else if (typeof define == \'function\' && define.amd) // AMD
  '		define([\'../../lib/codemirror\', \'../../addon/mode/simple\'], mod);
  '	else // Plain browser env
  '		mod(CodeMirror);
  '})(function(CodeMirror) {
  '	\'use strict\';
  '	CodeMirror.defineSimpleMode(\'<toLowerCase(mode.name)>\', {
  '		<eval(mode.states)>
  '	});
  '});"
  ;

str eval(list[State] states) =
  "<for(state <- states){> <eval(state)> <}>"
  ;
  
str eval(State state)=
  "<state.name>: [
  '	<eval(state.rules)>
  ']"
  ;	
  
str eval(list[Rule] rules) =
  "<for(rule <- rules){>
  '{
  '	<eval(rule)>
  '}, <}>"
  ;
  
str eval(Rule rule) = 
  "regex: /<rule.regex>/,
  'token: <"<rule.tokens>">"
  ;	
