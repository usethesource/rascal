module PicoAnalysis

import PicoAbstractSyntax;

public data ProgramPoint = pp(EXP exp, int label) | pp(STATEMENT stat, int label);

alias CFG = rel[ProgramPoint, ProgramPoint];

alias Entry = set[ProgramPoint];

alias Exit = set[ProgramPoint];

public data BLOCK = block(Entry entry, CFG graph, Exit exit);

int labelCnt = 0;

public void resetLabelGen(){
	labelCnt = 0;
}

private int labelGen(){
	labelCnt = labelCnt + 1;
	return labelCnt;
}

public ProgramPoint pp(Exp exp){
	return pp(exp, labelGen());
}

public ProgramPoint pp(STATEMENT stat){
	return pp(stat, labelGen());
}