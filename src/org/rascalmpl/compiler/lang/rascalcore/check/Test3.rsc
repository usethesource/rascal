module lang::rascalcore::check::Test3

public int ModVar42 = 42;
public list[int] ModVarList_41_42_43 = [41, 42, 43];
//public set[int] ModVarSet_41_42_43 = {41, 42, 43};

test bool matchListModuleVar4() = [ModVar42,*ModVarList_41_42_43] := [ModVar42, *ModVarList_41_42_43];

//test bool matchSetModuleVar2() = {*ModVarSet_41_42_43} := ModVarSet_41_42_43;
//test bool matchSetModuleVar3() = {ModVar44, *ModVarSet_41_42_43} := {ModVar44, *ModVarSet_41_42_43};
