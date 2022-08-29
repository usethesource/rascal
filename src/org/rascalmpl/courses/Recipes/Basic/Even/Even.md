# Even

.Synopsis
Produce a list of even numbers.

.Syntax

.Types

.Function

.Details

.Description

Let's write a function that generates all the even numbers in a list up to a certain maximum. We will do it in a few alternative 
ways: from very imperative to very declarative and some steps in between.

```rascal-shell
list[int] even0(int max) {
  list[int] result = [];
  for (int i <- [0..max])
    if (i % 2 == 0)
      result += i;
  return result;
}
even0(25);
```
Now lets remove the temporary type declarations:
```rascal-shell,continue
list[int] even1(int max) {
  result = [];
  for (i <- [0..max])
    if (i % 2 == 0)
      result += i;
  return result;
}
even1(25);
```
To make the code shorter, we can inline the condition in the for loop:
```rascal-shell,continue
list[int] even2(int max) {
  result = [];
  for (i <- [0..max], i % 2 == 0)
    result += i;
  return result;
}
even2(25);
```
In fact, for loops may produce lists as values, using the append statement:
```rascal-shell,continue
list[int] even3(int max) {
  result = for (i <- [0..max], i % 2 == 0)
    append i;
  return result;
}
even3(25);
```
So now, the result temporary is not necessary anymore:
```rascal-shell,continue
list[int] even4(int max) {
  return for (i <- [0..max], i % 2 == 0)
           append i;
}
even4(25);
```
This code is actually very close to a list comprehension already:
```rascal-shell,continue
list[int] even5(int max) {
  return [ i | i <- [0..max], i % 2 == 0];
}
even5(25);
```
And now we can just define `even` using an expression only:
```rascal-shell,continue
list[int] even6(int max) = [i | i <- [0..max], i % 2 == 0];
even6(25);
```
Or, perhaps we prefer creating a set instead of a list:
```rascal-shell,continue
set[int] even7(int max) = {i | i <- [0..max], i % 2 == 0};
even7(25);
```

.Examples

.Benefits

*  You can program in for loops and use temporary variables if you like.
*  Comprehensions are shorter and more powerful.
*  There are comprehensions for lists, sets, and maps

.Pitfalls

*  Trainwreck alert: if you start putting too many conditions in a single for loop or comprehension the code may become unreadable.

