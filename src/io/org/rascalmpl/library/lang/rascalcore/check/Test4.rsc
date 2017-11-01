module lang::rascalcore::check::Test4
data S;
alias SU = map[S, set[S]] ;
SU f(SU su) { return su;}