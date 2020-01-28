module lang::rascalcore::compile::Examples::Tst2
  
import List;
    
public int ModVar42 = 42;
public int ModVar44 = 44;
public list[int] ModVarList_41_42_43 = [41, 42, 43];

test bool matchModuleVar1() = ModVar42 := 42;
test bool matchModuleVar2() = ModVarList_41_42_43 := ModVarList_41_42_43;

// matchList1
        
test bool matchList1() = !([] := [2]);
test bool matchList2() = !([1] := []);
  
test bool matchList3() = [] := [];
test bool matchList4() = [1] := [1];
test bool matchList5() = [1,2] := [1,2];
        
test bool matchList6() = !([1] := [2]);
test bool matchList7() = !([1,2] := [1,2,3]);
  
test bool matchList8() = ([int N] := [1]) && (N == 1);
test bool matchList9() = [ _ ] := [1];
        
test bool matchList10() = ([int N, 2, int M] := [1,2,3]) && (N == 1) && (M==3);
test bool matchList11() = [ _, 2, _] := [1,2,3];
        
test bool matchList12() = ([int N, 2, N] := [1,2,1]) && (N == 1);
        
test bool matchList13() = !(([int N, 2, N] := [1,2,3]));
test bool matchList14() = !([int N, 2, N] := [1,2,"a"]);
        
test bool matchList15() {int N = 1; return ([N, 2, int M] := [1,2,3]) && (N == 1) && (M==3);}
test bool matchList16() {int N = 1; return !([N, 2, int M] := [4,2,3]);}
        
test bool matchList17() {list[int] L = [3]; return [1,2,*L] := [1,2,3];}
test bool matchList18() {list[int] L = [2, 3]; return [1, *L] := [1,2,3];}
  
test bool matchList19() = [1, [2, 3], 4] := [1, [2, 3], 4];
test bool matchList20() = !([1, [2, 3], 4] := [1, [2, 3, 4], 4]);
        
test bool matchList21() = ([list[int] L] := [[]]) && (L == []);
test bool matchList22() = ([1, list[int] L] := [1, [2]]) && (L == [2]);
test bool matchList23() = ([1, list[int] L, 10] := [1,[],10]) && (L == []);
test bool matchList24() = ([1, list[int] L, 10] := [1,[2],10]) && (L == [2]);
test bool matchList25() = ([*list[int] L] := []) && (L == []);
test bool matchList26() { list[int] X = []; return ([*int L] := X) && (L == []);} 
test bool matchList27() = ([*int L] := ([1] - [1])) && (L == []);
test bool matchList28() = ([*int L] := [1]) && (L == [1]);
test bool matchList29() = ([*int L] := [1,2]) && (L == [1,2]);
  
test bool matchList30() = ([1, *int L] := [1]) && (L == []);
test bool matchList31() = ([1, *int L] := [1, 2]) && (L == [2]);
test bool matchList32() = ([1, *int L] := [1, 2, 3]) && (L == [2, 3]);
  
test bool matchList33() = ([*int L, 10] := [10]) && (L == []);
test bool matchList34() = ([*int L, 10] := [1,10]) && (L == [1]);
test bool matchList35() = ([*int L, 10] := [1,2,10]) && (L == [1,2]);
  
test bool matchList36() = ([1, *int L, 10] := [1,10]) && (L == []);
test bool matchList37() = ([1, *int L, 10] := [1,2,10]) && (L == [2]);
test bool matchList38() = ([1, *int L, 10, *int M, 20] := [1,10,20]) && (L == []) && (M == []);
test bool matchList39() = ([1, *int L, 10, *int M, 20] := [1,2,10,20]) && (L == [2]) && (M == []);
test bool matchList40() = ([1, *int L, 10, *int M, 20] := [1,2,10,3,20]) && (L == [2]) && (M==[3]);
test bool matchList41() = ([1, *int L, 10, *int M, 20] := [1,2,3,10,4,5,20]) && (L == [2,3]) && (M==[4,5]);
        
test bool matchList42() = ([1, *int L, 10, *L, 20] := [1,2,3,10,2,3,20]) && (L == [2,3]);
test bool matchList43() = !(([1,*int L, 10, *L, 20] := [1,2,3,10,2,4,20]));
        
test bool matchList44() = [*int _] := [];
test bool matchList45() = [*int _] := [1];
test bool matchList46() = [*int _] := [1,2];
test bool matchList47() = ([1, *int _, 10, *int _, 20] := [1,2,10,20]);

test bool matchList48() {
    res = [];
    for([*int a, *int b, *int c, *int d] := [1,2,3,4,5,6]) { res = res + [[a,b,c,d]]; }
    return res ==
        [
          [
            [],
            [],
            [],
            [1,2,3,4,5,6]
          ],
          [
            [],
            [],
            [1],
            [2,3,4,5,6]
          ],
          [
            [],
            [],
            [1,2],
            [3,4,5,6]
          ],
          [
            [],
            [],
            [1,2,3],
            [4,5,6]
          ],
          [
            [],
            [],
            [1,2,3,4],
            [5,6]
          ],
          [
            [],
            [],
            [1,2,3,4,5],
            [6]
          ],
          [
            [],
            [],
            [1,2,3,4,5,6],
            []
          ],
          [
            [],
            [1],
            [],
            [2,3,4,5,6]
          ],
          [
            [],
            [1],
            [2],
            [3,4,5,6]
          ],
          [
            [],
            [1],
            [2,3],
            [4,5,6]
          ],
          [
            [],
            [1],
            [2,3,4],
            [5,6]
          ],
          [
            [],
            [1],
            [2,3,4,5],
            [6]
          ],
          [
            [],
            [1],
            [2,3,4,5,6],
            []
          ],
          [
            [],
            [1,2],
            [],
            [3,4,5,6]
          ],
          [
            [],
            [1,2],
            [3],
            [4,5,6]
          ],
          [
            [],
            [1,2],
            [3,4],
            [5,6]
          ],
          [
            [],
            [1,2],
            [3,4,5],
            [6]
          ],
          [
            [],
            [1,2],
            [3,4,5,6],
            []
          ],
          [
            [],
            [1,2,3],
            [],
            [4,5,6]
          ],
          [
            [],
            [1,2,3],
            [4],
            [5,6]
          ],
          [
            [],
            [1,2,3],
            [4,5],
            [6]
          ],
          [
            [],
            [1,2,3],
            [4,5,6],
            []
          ],
          [
            [],
            [1,2,3,4],
            [],
            [5,6]
          ],
          [
            [],
            [1,2,3,4],
            [5],
            [6]
          ],
          [
            [],
            [1,2,3,4],
            [5,6],
            []
          ],
          [
            [],
            [1,2,3,4,5],
            [],
            [6]
          ],
          [
            [],
            [1,2,3,4,5],
            [6],
            []
          ],
          [
            [],
            [1,2,3,4,5,6],
            [],
            []
          ],
          [
            [1],
            [],
            [],
            [2,3,4,5,6]
          ],
          [
            [1],
            [],
            [2],
            [3,4,5,6]
          ],
          [
            [1],
            [],
            [2,3],
            [4,5,6]
          ],
          [
            [1],
            [],
            [2,3,4],
            [5,6]
          ],
          [
            [1],
            [],
            [2,3,4,5],
            [6]
          ],
          [
            [1],
            [],
            [2,3,4,5,6],
            []
          ],
          [
            [1],
            [2],
            [],
            [3,4,5,6]
          ],
          [
            [1],
            [2],
            [3],
            [4,5,6]
          ],
          [
            [1],
            [2],
            [3,4],
            [5,6]
          ],
          [
            [1],
            [2],
            [3,4,5],
            [6]
          ],
          [
            [1],
            [2],
            [3,4,5,6],
            []
          ],
          [
            [1],
            [2,3],
            [],
            [4,5,6]
          ],
          [
            [1],
            [2,3],
            [4],
            [5,6]
          ],
          [
            [1],
            [2,3],
            [4,5],
            [6]
          ],
          [
            [1],
            [2,3],
            [4,5,6],
            []
          ],
          [
            [1],
            [2,3,4],
            [],
            [5,6]
          ],
          [
            [1],
            [2,3,4],
            [5],
            [6]
          ],
          [
            [1],
            [2,3,4],
            [5,6],
            []
          ],
          [
            [1],
            [2,3,4,5],
            [],
            [6]
          ],
          [
            [1],
            [2,3,4,5],
            [6],
            []
          ],
          [
            [1],
            [2,3,4,5,6],
            [],
            []
          ],
          [
            [1,2],
            [],
            [],
            [3,4,5,6]
          ],
          [
            [1,2],
            [],
            [3],
            [4,5,6]
          ],
          [
            [1,2],
            [],
            [3,4],
            [5,6]
          ],
          [
            [1,2],
            [],
            [3,4,5],
            [6]
          ],
          [
            [1,2],
            [],
            [3,4,5,6],
            []
          ],
          [
            [1,2],
            [3],
            [],
            [4,5,6]
          ],
          [
            [1,2],
            [3],
            [4],
            [5,6]
          ],
          [
            [1,2],
            [3],
            [4,5],
            [6]
          ],
          [
            [1,2],
            [3],
            [4,5,6],
            []
          ],
          [
            [1,2],
            [3,4],
            [],
            [5,6]
          ],
          [
            [1,2],
            [3,4],
            [5],
            [6]
          ],
          [
            [1,2],
            [3,4],
            [5,6],
            []
          ],
          [
            [1,2],
            [3,4,5],
            [],
            [6]
          ],
          [
            [1,2],
            [3,4,5],
            [6],
            []
          ],
          [
            [1,2],
            [3,4,5,6],
            [],
            []
          ],
          [
            [1,2,3],
            [],
            [],
            [4,5,6]
          ],
          [
            [1,2,3],
            [],
            [4],
            [5,6]
          ],
          [
            [1,2,3],
            [],
            [4,5],
            [6]
          ],
          [
            [1,2,3],
            [],
            [4,5,6],
            []
          ],
          [
            [1,2,3],
            [4],
            [],
            [5,6]
          ],
          [
            [1,2,3],
            [4],
            [5],
            [6]
          ],
          [
            [1,2,3],
            [4],
            [5,6],
            []
          ],
          [
            [1,2,3],
            [4,5],
            [],
            [6]
          ],
          [
            [1,2,3],
            [4,5],
            [6],
            []
          ],
          [
            [1,2,3],
            [4,5,6],
            [],
            []
          ],
          [
            [1,2,3,4],
            [],
            [],
            [5,6]
          ],
          [
            [1,2,3,4],
            [],
            [5],
            [6]
          ],
          [
            [1,2,3,4],
            [],
            [5,6],
            []
          ],
          [
            [1,2,3,4],
            [5],
            [],
            [6]
          ],
          [
            [1,2,3,4],
            [5],
            [6],
            []
          ],
          [
            [1,2,3,4],
            [5,6],
            [],
            []
          ],
          [
            [1,2,3,4,5],
            [],
            [],
            [6]
          ],
          [
            [1,2,3,4,5],
            [],
            [6],
            []
          ],
          [
            [1,2,3,4,5],
            [6],
            [],
            []
          ],
          [
            [1,2,3,4,5,6],
            [],
            [],
            []
          ]
        ];
}       
        
@ignore{investigate} test bool matchList() {([1, list[int] L, [10, list[int] M, 100], list[int] N, 1000] := [1, [10,100],1000]);}
        
test bool matchListFalse1() {list[value] l = [1,2,3]; return [1, str S, 2] !:= l; }

test bool matchListModuleVar1() = [ModVar42] := [42];
test bool matchListModuleVar2() = [*ModVarList_41_42_43] := ModVarList_41_42_43;
test bool matchListModuleVar3() = [ModVar42, *ModVarList_41_42_43] := [ModVar42, *ModVarList_41_42_43];
@ignoreInterpreter{Seems to be a bug in the interpreter}
test bool matchListModuleVar4() = [ModVar42, ModVarList_41_42_43] := [ModVar42, ModVarList_41_42_43];

//  matchNestedList

test bool matchNestedList1() = !([] := [[2]]);
  
test bool matchNestedList3() = [] := [];
test bool matchNestedList4() = [[1]] := [[1]];
test bool matchNestedList5() = [[1,2]] := [[1,2]];
        
test bool matchNestedList6() = !([[1]] := [[2]]);
test bool matchNestedList7() = !([[1,2]] := [[1,2,3]]);
        
test bool matchNestedList8() = [*list[int] L] := [];
        
test bool matchNestedList9() = [*list[int] L] := [[1]];
test bool matchNestedList10() = [*list[int] L] := [[1,2]];
        
test bool matchNestedList11() = ([[1], *list[int] L, [6,7,8]] := [[1],[2,3],[4,5],[6,7,8]]) && (L == [[2,3],[4,5]]);
test bool matchNestedList12() = !(([[1], *list[int] L, [6,7,8]] := [[1],[2,3],[4,5],[8]]) && (L == [[2,3],[4,5]]));
        
test bool matchNestedList13() = ([[1], *list[int] L, [6,7,8], *L] := [[1],[2,3],[4,5],[6,7,8],[2,3],[4,5]]) && (L == [[2,3],[4,5]]);

// matchExternalListVars

test bool matchExternalListVars1() {int n;  return n := 3 && n == 3; }

/*TODO:fails*/
@Ignore
test bool matchExternalListVars2() {list[int] L; return ([1, *L, 4, 5] := [1, 2, 3, 4, 5] && L == [2, 3]);}

//  matchListMultiVars

test bool matchListMultiVars1() = [1, L*, 4, 5] := [1, 2, 3, 4, 5] && L == [2, 3];
test bool matchListMultiVars2() = [1, _*, 4, 5] := [1, 2, 3, 4, 5];
test bool matchListMultiVars3() = [1, L*, 4, *L, 5] := [1, 2, 3, 4, 2, 3, 5] && L == [2, 3];
    
//  matchListSpliceVars

test bool matchListSpliceVars1() = [1, *L, 4, 5] := [1, 2, 3, 4, 5] && L == [2, 3];
test bool matchListSpliceVars2() = [1, * int L, 4, 5] := [1, 2, 3, 4, 5] && L == [2, 3];
test bool matchListSpliceVars3() = [1, *_, 4, 5] := [1, 2, 3, 4, 5];
test bool matchListSpliceVars4() = [1, * int _, 4, 5] := [1, 2, 3, 4, 5];
test bool matchListSpliceVars5() = [1, *L, 4, *L, 5] := [1, 2, 3, 4, 2, 3, 5] && L == [2, 3];
test bool matchListSpliceVars6() = [1, * int L, 4, *L, 5] := [1, 2, 3, 4, 2, 3, 5] && L == [2, 3];
  
//  matchListHasOrderedElement

test bool matchListHasOrderedElement1() = hasOrderedElement([]) == false;
test bool matchListHasOrderedElement2() = hasOrderedElement([1]) == false;
test bool matchListHasOrderedElement3() = hasOrderedElement([1,2]) == false;
test bool matchListHasOrderedElement4() = hasOrderedElement([1,2,1]) == true;
test bool matchListHasOrderedElement5() = hasOrderedElement([1,2,3,4,3,2,1]) == true;
    
//  matchListHasDuplicateElement

test bool matchListHasDuplicateElement1() = hasDuplicateElement([]) == false;
test bool matchListHasDuplicateElement2() = hasDuplicateElement([1]) == false;
test bool matchListHasDuplicateElement3() = hasDuplicateElement([1,2]) == false;
test bool matchListHasDuplicateElement4() = hasDuplicateElement([1,1]) == true;
test bool matchListHasDuplicateElement5() = hasDuplicateElement([1,2,3]) == false;
test bool matchListHasDuplicateElement6() = hasDuplicateElement([1,2,3,1]) == true;
test bool matchListHasDuplicateElement7() = hasDuplicateElement([1,2,3,2]) == true;
test bool matchListHasDuplicateElement8() = hasDuplicateElement([1,2,3,3]) == true;
  
//  matchListIsDuo1

test bool matchListIsDuo1() = isDuo1([]) == true;
test bool matchListIsDuo2() = isDuo1([1]) == false;
test bool matchListIsDuo3() = isDuo1([1,1]) == true;
test bool matchListIsDuo4() = isDuo1([1,2]) == false;
test bool matchListIsDuo5() = isDuo1([1,2, 1]) == false;
test bool matchListIsDuo6() = isDuo1([1,2, 1,2]) == true;
test bool matchListIsDuo7() = isDuo1([1,2,3, 1,2]) == false;
test bool matchListIsDuo8() = isDuo1([1,2,3, 1,2, 3]) == true;
        
//  matchListIsDuo2

test bool matchListIsDuo9() = isDuo2([]) == true;
test bool matchListIsDuo10() = isDuo2([1]) == false;
test bool matchListIsDuo11() = isDuo2([1,1]) == true;
test bool matchListIsDuo12() = isDuo2([1,2]) == false;
test bool matchListIsDuo13() = isDuo2([1,2, 1]) == false;
test bool matchListIsDuo14() = isDuo2([1,2, 1,2]) == true;
test bool matchListIsDuo15() = isDuo2([1,2,3, 1,2]) == false;
test bool matchListIsDuo16() = isDuo2([1,2,3, 1,2, 3]) == true;
    
//  matchListIsDuo3

test bool matchListIsDuo17() = isDuo3([]) == true;
test bool matchListIsDuo18() = isDuo3([1]) == false;
test bool matchListIsDuo19() = isDuo3([1,1]) == true;
test bool matchListIsDuo20() = isDuo3([1,2]) == false;
test bool matchListIsDuo21() = isDuo3([1,2, 1]) == false;
test bool matchListIsDuo22() = isDuo3([1,2, 1,2]) == true;
test bool matchListIsDuo23() = isDuo3([1,2,3, 1,2]) == false;
test bool matchListIsDuo24() = isDuo3([1,2,3, 1,2, 3]) == true;
  
// matchListIsTrio1

test bool matchListIsTrio1() = isTrio1([]) == true;
test bool matchListIsTrio2() = isTrio1([1]) == false;
test bool matchListIsTrio3() = isTrio1([1,1]) == false;
test bool matchListIsTrio4() = isTrio1([1,1,1]) == true;
test bool matchListIsTrio5() = isTrio1([2,1,1]) == false;
test bool matchListIsTrio6() = isTrio1([1,2,1]) == false;
test bool matchListIsTrio7() = isTrio1([1,1,2]) == false;
test bool matchListIsTrio8() = isTrio1([1,2, 1,2, 1,2]) == true;
    
//  matchListIsTrio2

test bool matchListIsTrio9() = isTrio2([]) == true;
test bool matchListIsTrio10() = isTrio2([1]) == false;
test bool matchListIsTrio11() = isTrio2([1,1]) == false;
test bool matchListIsTrio12() = isTrio2([1,1,1]) == true;
test bool matchListIsTrio13() = isTrio2([2,1,1]) == false;
test bool matchListIsTrio14() = isTrio2([1,2,1]) == false;
test bool matchListIsTrio15() = isTrio2([1,1,2]) == false;
test bool matchListIsTrio16() = isTrio2([1,2, 1,2, 1,2]) == true;
    
//  matchListIsTrio3

test bool matchListIsTrio17() = isTrio3([]) == true;
test bool matchListIsTrio18() = isTrio3([1]) == false;
test bool matchListIsTrio19() = isTrio3([1,1]) == false;
test bool matchListIsTrio20() = isTrio3([1,1,1]) == true;
test bool matchListIsTrio21() = isTrio3([2,1,1]) == false;
test bool matchListIsTrio22() = isTrio3([1,2,1]) == false;
test bool matchListIsTrio23() = isTrio3([1,1,2]) == false;
test bool matchListIsTrio24() = isTrio3([1,2, 1,2, 1,2]) == true;

// listCount1
 
test bool listCount1(list[int] L){
   int cnt(list[int] L){
    int count = 0;
    while ([int N, *int Ns] := L) { 
           count = count + 1;
           L = tail(L);
    }
    return count;
  }
  return cnt(L) == size(L);
}
  
 // listCount2
 
test bool listCount2(list[int] L){
  int cnt(list[int] L){
    int count = 0;
    while ([int N, *int _] := L) {
           count = count + 1;
           L = tail(L);
    }
    return count;
  }
  return  cnt(L) == size(L);
}

// listCount3
  
//test bool listCount3(list[int] L){      
//  int cnt(list[int] L){
//   int count = 0;
//    while ([_, *int _] := L) {
//           count = count + 1;
//           L = tail(L);
//    }
//    return count;
//  }
//
//  return cnt(L) == size(L);
//}
//  
public bool hasOrderedElement(list[int] L)
{
     switch(L){
     
     case [*int L1, int I, *int L2, int J, *int L3]: {
          if(I > J){
            return true;
          } else {
            fail;
          }
          }
     }
     return false;
}
  
  
public bool hasDuplicateElement(list[int] L)
{
    switch(L){
    
    case [*int L1, int I, *int L2, int J, *int L3]:
        if(I == J){
            return true;
        } else {
            fail;
        }
    default:
        return false;
      }
}
  
public bool isDuo1(list[int] L)
{
    switch(L){
    case [*int L1, *int L2]:
        if(L1 == L2){
            return true;
        } else {
            fail;
        }
    default:
        return false;
      }
}
  
public bool isDuo2(list[int] L)
{
    switch(L){
    case [*int L1, *L1]:
            return true;
    default:
        return false;
      }
}
  
public bool isDuo3(list[int] L)
{
    return [*int L1, *L1] := L;
}
  
public bool isTrio1(list[int] L)
{
    switch(L){
    case [*int L1, *int L2, *int L3]:
        if((L1 == L2) && (L2 == L3)){
            return true;
        } else {
            fail;
        }
    default:
        return false;
      }
}
  
public bool isTrio2(list[int] L)
{
    switch(L){
    case [*int L1, *L1, *L1]:
        return true;
    default:
        return false;
      }
}
  
public bool isTrio3(list[int] L)
{
    return [*int L1, *L1, *L1] := L;
}

test bool matchTypedListVarBecomes1() = list[int] L1 : [int N, *int L2, int M] := [1,2,3] && L1 == [1,2,3] && N == 1 && L2 == [2] && M == 3;
test bool matchTypedListVarBecomes2() = [1, list[int] L: [int N], 2] := [1,[2],2] && L == [2];
test bool matchTypedListVarBecomes3() = [1, list[int] L1: [*int L2, int N], 5] := [1,[2,3,4],5] && L1 == [2,3,4] && L2 == [2,3] && N == 4;
test bool matchTypedListVarBecomes4() = [1, list[int] L1: [*int L2, int N], L1] := [1,[2,3,4],[2,3,4]] && L1 == [2,3,4] && L2 == [2,3] && N == 4;
