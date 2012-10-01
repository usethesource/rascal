module demo::lang::MissGrant::Plugin

import demo::lang::MissGrant::MissGrant;
import demo::lang::MissGrant::AST;
import demo::lang::MissGrant::Outline;
import demo::lang::MissGrant::Implode;
import demo::lang::MissGrant::CheckController;
import demo::lang::MissGrant::DesugarResetEvents;
import demo::lang::MissGrant::ToSwitch;
import demo::lang::MissGrant::ToMethods;
import demo::lang::MissGrant::Rename;
import demo::lang::MissGrant::ShowStateMachine;
import demo::lang::MissGrant::ToRelation;
import demo::lang::MissGrant::Completion;

import util::IDE;
import util::Prompt;
import vis::Render;
import ParseTree;
import List;
import IO;

private str CONTROLLER_LANG = "Controller";
private str CONTROLLER_EXT = "ctl";

public void main() {
  registerLanguage(CONTROLLER_LANG, CONTROLLER_EXT, demo::lang::MissGrant::MissGrant::Controller(str input, loc org) {
    return parse(#demo::lang::MissGrant::MissGrant::Controller, input, org);
  });
  
  contribs = {
  		outliner(node (demo::lang::MissGrant::MissGrant::Controller input) {
    		return outlineController(implode(input));
  		}),
  		annotator(demo::lang::MissGrant::MissGrant::Controller (demo::lang::MissGrant::MissGrant::Controller input) {
    		msgs = toSet(checkController(implode(input)));
    		return input[@messages=msgs];
  		}),
		popup(
			menu(CONTROLLER_LANG,[
	    		action("Generate Switch", generateSwitch), 
	    		action("Generate Methods", generateMethods),
	    		action("Visualize", visualizeController),
	    		edit("Rename...", rename) 
		    ])
	  	),
	  	proposerContrib
  };
	
  registerContributions(CONTROLLER_LANG, contribs);
}

private void generateSwitch(demo::lang::MissGrant::MissGrant::Controller pt, loc l) {
  name = "ControllerSwitch";
  path = prompt("Output directory: ");
  writeFile(|file://<path>/<name>.java|, controller2switch(name, desugarResetEvents(implode(pt))));
}

private void generateMethods(demo::lang::MissGrant::MissGrant::Controller pt, loc l) {
  name = "ControllerMethods";
  path = prompt("Output directory: ");
  writeFile(|file://<path>/<name>.java|, controller2methods(name, desugarResetEvents(implode(pt))));
}

private void visualizeController(demo::lang::MissGrant::MissGrant::Controller pt, loc l) {
  ast = implode(pt);
  render(stateMachineVisInterface(transRel(ast), commands(ast), ast.states[0].name));
}
