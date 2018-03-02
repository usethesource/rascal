module lang::rascalcore::check::Test4

data DefInfo(bool isStart = false);

data Define = def(DefInfo defInfo);

value main(){
    Define def;
    
    return defInfo.isStart;
}