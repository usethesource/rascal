Name: TestSoftwareEvolution

Details:

Syntax:

Types:

Function:

Synopsis:

Description:
This test assesses basic Rascal skills. In each exercise you have to select a choice or fill in a text field. 
Push the __Give answer__ button when you have completed your answer.

When you make an error, you can push the __I want another question__ button for a similar question (if available).
You can repeat this until you have the right answer or you give up.

You pass when you have more than 60% of the questions right.

Examples:

Benefits:

Pitfalls:

This assessment software is still experimental; please bear with us and take the following into account:

* Answers are not recorded: show at the end of the session all your answers to the instructor.
* _Do not go to another page of the tutor since all your answers will be erased_ (precaution: record your answers yourself)
* Always push the __Give answer__ button when you are ready (a newline in the text box will not work).

__Be aware: in the web version, there are no questions visible: use the Eclipse version instead__

Questions:

QChoice: Sets can be used to represent a sequence of values when
b: The values have duplicates.
g: The values have no duplicates and no order.
b: The values are unordered.

QChoice: The type of a list is determined by:
b: The type of the first element that was first added to the list.
b: The upperbound of the type of two arbitrary elements.
g: The upperbound of the type of all elements.

QType: <A:set[arb[int,real,str,loc]]>

QType: <A:list[arb[int,real,str,loc]]>

QType: <A:map[str,arb]>


QType: 
make: A = int
type: set[int]
test: {<A>, <?> } 
hint: one or more integer values separated by commas

QType: 
make: A = str
type: map[str,int]
test: (<A>: <?>)
hint: a map from strings to integers

QType: <A:set[arb[int,real,num,str,loc]]>

QType: {<A:int>, <B:str>, <C:int>}

QType: <A:rel[str,int,loc]>

QType: <A:rel[int[0,20],int]>

QValue:
desc: Determine the number of elements in a list
list:
import List;
text = ["abc", "def", "ghi"];
test: <?>(text) == 3;

QValue:
desc: Determine the number of strings that contain "a".
list:
text = ["andra", "moi", "ennepe", "Mousa", "polutropon"];
public int count(list[str] text){
  n = 0;
  for(s <- text)
    if(<?> := s)
      n +=1;
  return n;
}

test: count(text) == 2;

QValue:
desc: Return the strings that contain "o".
list:
text = ["andra", "moi", "ennepe", "Mousa", "polutropon"];
public list[str] find(list[str] text){
  return 
    for(s <- text)
      if(/o/ := s)
        <?>;
}
test: find(text) == ["moi", "Mousa", "polutropon"];

QValue:
desc: Complete this function that finds duplicates in a list of strings
list:
text = ["the", "jaws", "that", "bite", "the", "claws", "that", "catch"];
public list[str] duplicates(list[str] text){
    m = {};
    return 
      for(s <- text)
        if(<?>)
           append s;
        else
           m += s;
}
test: duplicates(text) == ["the", "that"];

QValue:
desc: Complete this function that tests that a list of words forms a palindrome. A palindrome is a word that is symmetrical 
and can be read
from left to right and from right to left.
list:
import List;
public bool isPalindrome(list[str] words){
  return words == <?>;
}
test: isPalindrome(["a", "b", "b", "a"]) == true;








