module demo::lang::MissGrant::ShowStateMachine

import demo::lang::MissGrant::AST;
import demo::lang::MissGrant::Step;
import demo::lang::MissGrant::ToRelation;

import vis::Figure;
import List;
import Real;
import util::Math;
import IO;
import util::Resources;
import vis::Render;
import Relation;
import  analysis::graphs::Graph;
import Set;

public Figure stateMachineGraph(TransRel trans,str init,str state){
	str getColor(str s) {
		return (s == state) ? "red" : ((s == init) ? "green" : "lightskyblue");
	} 

	list[Figure] nodes = [box(text(i),fillColor(getColor(i)),grow(1.2),id(i))| i <-[init] + toList(domain(trans) - init)] +
						 [ellipse(text(labelS),grow(1.1),id("<fromS>,<labelS>,<toS>")) | <fromS,labelS,toS> <- trans];
	Edges edges = [ edge(fromS,"<fromS>,<labelS>,<toS>",triangle(10,fillColor("black"))),
						edge("<fromS>,<labelS>,<toS>",toS,triangle(10,fillColor("black")))
				 | <fromS,labelS,toS> <-trans];
	
	return graph(nodes,edges,hint("layered"),width(900),height(1000),top(),gap(70));
}

public void stateMachineVisInterface(TransRel trans, ActionRel commands, str init){
	str cur = init;
	list[str] eventsTokens = [];
	list[str] commandsTokens = [];
	
	void handleToken(str token) {
		if(c <- trans[cur,token]){
			cur = c;
			commandsTokens+=toList(commands[cur]);
		}
	}
	
	void () getAddInputHandler(str tokenName) {
		return void () { eventsTokens+=[tokenName]; handleToken(tokenName); };
	}
	return render(computeFigure(Figure() { return vcat([
		hcat([button(ev,getAddInputHandler(ev)) | ev <- trans<1>],vresizable(false)),
		hcat([text("Events:")] + [box(text(ev),grow(1.1)) | ev <-eventsTokens],resizable(false)),
		hcat([text("Commands:")] + [box(text(comm),grow(1.1)) | comm <-commandsTokens],resizable(false)),
		stateMachineGraph(trans,init,cur)]);
	}));
}
		
