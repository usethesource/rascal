module lang::rascalcore::compile::Examples::Tst0
     
data A = a();
data D = d(list[A] msgs);

void main(){
    x = d([]);
    x.msgs += a();
}