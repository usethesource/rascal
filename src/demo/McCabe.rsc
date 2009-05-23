module demo::McCabe
import Graph;

public int cyclomaticComplexity(graph[&T] PRED){
    return size(PRED) - size(carrier(PRED)) + 2;
}

public bool test(){
  return true;
}