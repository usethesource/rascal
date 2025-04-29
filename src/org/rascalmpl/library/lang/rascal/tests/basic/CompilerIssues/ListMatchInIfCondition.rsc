module lang::rascal::tests::basic::CompilerIssues::ListMatchInIfCondition

int f(list[int] ds){
    if([int xxx]:= ds, xxx > 0){
        return 1;
    } else {
        return 2;
    }
}

test bool ListMatchInIfCondition1() = f([1]) == 1;

test bool ListMatchInIfCondition2() = f([-1]) == 2;