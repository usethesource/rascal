module Bubble

fun Integer* sort(Integer* Numbers){
    visit Numbers {
      case <Integer N1> <Integer N2>:
        if( N1 > N2 ) 
            yield [| <Integer N2> <Integer N1> |]
    };
    return Numbers
}