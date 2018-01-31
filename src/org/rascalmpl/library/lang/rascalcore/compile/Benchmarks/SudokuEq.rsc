module lang::rascalcore::compile::Benchmarks::SudokuEq

import util::Math;
import Exception;
import IO;
import List;
import lang::logic::\syntax::Propositions;
import lang::dimacs::\syntax::Dimacs;
import lang::dimacs::IO;

public int s(int x, int y, int z) {return x*81+y*9+z+1;}

public tuple[int x,int y, int v] s_inv(int d) { 
     int p = abs(d)-1;
     return  (<p/81, (p%81)/9, d>0?(d-1)%9+1:-((-d-1)%9)>);
}

public list[tuple[int x,int y, int v]] fieldValues(list[int] g) {
   return [<x,y,v>|int p<-g, <int x, int y, int v>:=s_inv(p), v>0]; 
}

list[list[int]] full() {
    list[list[int]] r = [[s(x,y,z)|z<-[0..9]]| int x<-[0..9], int y<-[0..9]];
    return r;
    }
    
list[list[int]] rowFull() {
    list[list[int]] r = [[-s(x,y,z), -s(i, y, z)]|int y<-[0..9], int z<-[0..9], 
       int x<-[0..8], int i<-[(x+1)..9]];
    return r;
    }
    
list[list[int]] colFull() {
    list[list[int]] r = [[-s(x,y,z), -s(x, i, z)]|int x<-[0..9], int z<-[0..9], 
       int y<-[0..8], int i<-[(y+1)..9]];
    return r;
    }
 
list[list[int]] subgridFull1() {   
    list[list[int]] r = [[-s(3*i+x,3*j+y,z), -s(3*i+x, 3*j+k, z)]|
    int z<-[0..9],  int i<-[0..3], int j<-[0..3],int x<-[0..3],
    int y<-[0..3], int k <- [y+1..3]];
    return r;
    }
    
list[list[int]] subgridFull2() {   
    list[list[int]] r = [[-s(3*i+x,3*j+y,z), -s(3*i+k, 3*j+l, z)]|
    int z<-[0..9],  int i<-[0..3], int j<-[0..3],int x<-[0..3],
    int y<-[0..3], int k <- [x+1..3], int l<-[0..3]];
    return r;
    }   



public void main() {
    list[list[int]] r = full()+rowFull()+colFull()
           +subgridFull1()+subgridFull2();
    println(size(r));
    // writeDimacsCnf(|project://aap/src/u.cnf|, r, 729);
}