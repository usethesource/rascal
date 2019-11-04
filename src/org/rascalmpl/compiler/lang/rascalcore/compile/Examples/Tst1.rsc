module lang::rascalcore::compile::Examples::Tst1

int size(list[int] L) = 0;
int translateConstantCall(str name, list[value] args) =
    tcc(name, args);

private int tcc("value", []) = 0;
private int tcc("value", list[int] L) = 1 when size(L) == 1;
private int tcc("value", list[int] L) = 2 when size(L) == 2;

private default int tcc(str name, list[value] args) { return -1;}

test bool tcc1() = translateConstantCall("value", []) == 0;
