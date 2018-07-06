module lang::rascalcore::check::Test3
           
data Message = 
       error(str msg, loc at)
       ;

str error(value src, str msg, value args...) = "abc";


value main(){
    error("val", "msg", "a");
}