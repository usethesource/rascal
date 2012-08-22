module demo::lang::MissGrant::Completion

import demo::lang::MissGrant::MissGrant;
import demo::lang::MissGrant::AST;
import demo::lang::MissGrant::Implode;

import util::IDE;
import util::ContentCompletion;
import ParseTree;
import List;

str symbol_type_event = "Event";
str symbol_type_command = "Command";
str symbol_type_state = "State";
str symbol_attr_token = "Token";

public list[CompletionProposal] makeProposals(demo::lang::MissGrant::MissGrant::Controller input, str prefix, int requestOffset) {
	Controller AST = implode(input);
	SymbolTree symbolTree = makeSymbolTree(AST);
	
	list[str] filterTypes = getFilterTypes(input, requestOffset);
	list[SymbolTree] symbols = flattenTree(symbolTree);
	symbols = filterSymbolsByType(symbols, filterTypes);
	
	list[CompletionProposal] proposals = createProposalsFromLabels(symbols);
	proposals = sort(proposals, lessThanOrEqual);
	proposals = filterPrefix(proposals, prefix);
	return proposals;
}

list[str] getFilterTypes(demo::lang::MissGrant::MissGrant::Controller input, int offset) {	
	bottom-up-break visit(input) {
		case ResetEvents events: if (isWithin(events, offset)) return [symbol_type_event];
		case Actions actions: if (isWithin(actions, offset)) return [symbol_type_command];
		case demo::lang::MissGrant::MissGrant::State state: if (isWithin(state, offset)) return [symbol_type_event, symbol_type_state];
	}
	
	return [];
}

SymbolTree makeSymbolTree(Controller ast) {
	list[SymbolTree] symbols = [];	
	visit(ast) {
		case command(str name, str token): symbols += symbol(name, symbol_type_command, (symbol_attr_token:token))[@label = "<name> (<token>) - Command"];
		case event(str name, str token): symbols += symbol(name, symbol_type_event, (symbol_attr_token:token))[@label = "<name> (<token>) - Event"];
		case state(str name, _, _): symbols += symbol(name, symbol_type_state)[@label = "<name> - state"];
	}	
	return scope(symbols)[@label=""];
}

public Contribution proposerContrib = proposer(makeProposals, alphaNumeric + "_");