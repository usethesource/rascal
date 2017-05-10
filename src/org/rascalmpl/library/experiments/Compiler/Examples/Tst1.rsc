module experiments::Compiler::Examples::Tst1

data Node = reference(int anchor);

value fn(list[value] args){

    matching = ();

    bool equalNodesRec(Node x, Node y) {
       switch (<x, y>) {
          case <reference(r1), reference(r2)>: {
             if (r1 in matching, matching[r1] == r2) {  // <== matching[r1] gets type void!
               return true; 
             }
             matching[r1] = r2;
             return true;
          }
        }
    }
    return matching[1];
}