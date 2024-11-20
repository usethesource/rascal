module lang::rascal::tests::basic::CompilerIssues::ListMatchInIfCondition

int f(list[int] ds){
    if([int xxx]:= ds, xxx > 0){
        return 1;
    } else {
        return 2;
    }
}

@ignoreCompiler{Generates incorrect code}
test bool ListMatchInIfCondition1() = f([1]) == 1;

@ignoreCompiler{Generates incorrect code}
test bool ListMatchInIfCondition2() = f([-1]) == 2;