module lang::rascalcore::compile::Benchmarks::BSudoku

//import lang::rascalcore::compile::Benchmarks::SudokuEq;
import Exception;
import IO;
import List;
import Set;
import util::Math;
import String;

str lab = "";
// int siz = 3*198;
int siz = 3*100;

list[int] val, sol;

public list[int] readSudoku(loc f) {
   list[str] s = [stringChar(x)|x<-readFileBytes(f)];
   list[int] w = [c=="."?0:-toInt(c)|c<-s];
   list[int] r = [w[(i/27)*27+((i%27)/9)*3+((i%9)/3)*9+i%3]|i<-[0..81]];
   return r;
}

public void updateVal(int i, int j, int v) {
   int k = 9*j+i;
   sol[(k/27)*27+((k%27)/9)*3+((k%9)/3)*9+k%3] = 
   (sol[(k/27)*27+((k%27)/9)*3+((k%9)/3)*9+k%3]>=0)?v:-v;
   }


// list[int] val=[0|int i<-[0..81]], sol=[0|int i<-[0..81]];

list[int] getFreeForRow(int p) {
    int k = p/27;
    int s = (p%9)/3;
    int low=27*k+3*s;
    return ([1..10]-[abs(val[i])|int i<-
    [low..low+3]+[low+9..low+12]+[low+18..low+21],
      val[i]!=0]);
    }
    
list[int] getFreeForColumn(int p) {
    int low=(p%3)+((p%27)/9)*9;
    return([1..10]-[abs(val[i])|int i<-
        [low,low+3,low+6]+[low+27,low+30,low+33]+
      [low+54,low+57,low+60], val[i]!=0]);
   }
   
list[int] getFreeForSubgrid(int p) {
   int low = (p/9)*9;
   return ([1..10]-[abs(val[i])|int i<-[low..low+9], val[i]!=0]);
   }
   
list[int] getFree(int p) {return 
  getFreeForColumn(p)&getFreeForRow(p)&getFreeForSubgrid(p);
  }

   
bool isSubgridInjective(int p) {
   int low = (p/9)*9;
   list[int] row =  [abs(val[i])|int i<-[low..low+9], val[i]!=0];
   if (size(toSet(row))==size(row)) return true;
   return false;
}

bool isRowInjective(int p) {
    int k = p/27;
    int s = (p%9)/3;
    int low=27*k+3*s;
    list[int] row =  [abs(val[i])|int i<-
    [low..low+3]+[low+9..low+12]+[low+18..low+21],
      val[i]!=0];
    if (size(toSet(row))==size(row)) return true;
    return false;
    }
    
bool isColumnInjective(int p) {
    int low=(p%3)+((p%27)/9)*9;
    list[int] row =  [abs(val[i])|int i<-
    [low,low+3,low+6]+[low+27,low+30,low+33]+
      [low+54,low+57,low+60], val[i]!=0];
    if (size(toSet(row))==size(row)) return true;
    return false;
    }

public bool solv(int p) {
   list[int] free = getFree(p);
   if (isEmpty(free)) return false;
   for (int d <- free) {
      // println("p = <p> d=<d>");
      val[p] = d;
      if (solv()) return true;
      }
    val[p] = 0;
    return false;
}

public bool solv() {
   int p = 0;
   bool cond = true;
   while (p<81 && cond) {
       if(val[p]!=0) {
           p = p+1;
       } else {
           cond = false;
       }
   }
   if (p==81) return true;
   return solv(p);
}
    

/* ----------------------------------------- */

public value main() { 
  for(int i <- [0 .. 5]){
   	list[int] sdku=readSudoku(|std:///experiments/Compiler/Benchmarks/example1.txt|);
   	val = sdku;
   	sol = sdku;
   	solv();
   }
   return 0;
}