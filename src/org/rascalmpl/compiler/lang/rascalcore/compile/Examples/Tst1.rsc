module lang::rascalcore::compile::Examples::Tst1
     

//import Type;

data Symbol
     = \int()  
     | \num()
     | \str() 
     | \void()   
     | \label(str name, Symbol s)       
     ;      

bool subtype(\int(), \num()) = true;
bool subtype(Symbol s, s) = true; 
default bool subtype(Symbol s, Symbol t) = false;
public bool subtype(Symbol::\void(), Symbol _) = true; 
 

//private list[Symbol] stripLabels(list[Symbol] l) = [ (Symbol::\label(_,v) := li) ? v : li | li <- l ]; 

bool main() {  
 return subtype(\str(), \str());     
}