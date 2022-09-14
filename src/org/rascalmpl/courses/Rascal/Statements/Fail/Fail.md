---
title: "Fail"
keywords: "fail"
---

.Synopsis
Let the current alternative of a pattern match fail.

.Syntax
`fail;`

.Types

.Function

.Details

.Description
A `fail` statement is only allowed in statements that are controlled by the outcome of a pattern match:

*  The ((Patterns)) in a ((Pattern with Action)) in ((Switch)) or ((Statements-Visit)) statement controls the statements in the action part.
*  The test (expression) of a ((While)) or ((Do)) statement controls the statements in the body part.
*  The test (expressions) of a ((For)) statement control the statements in the body part.
*  The formal parameter declaration of a ((Function Declarations)).


The `fail` statement is associated with the innermost pattern match by which it is controlled.

When `fail` is executed:

*  If the associated pattern has more alternatives, the next alternative is explored,
*  otherwise the pattern as a whole fails. 
   ** In the case of switch or visit this means that the next case will be tried.
   ** For while, do and for, this implies that any bindings caused by the pattern are undone and that the next 
      alternative in the test is tried; otherwise the loop is terminated.
   ** For a function call it means that the next function declaration (or the default one) is tried.

.Examples

Here is an example taken from [Bubble]((Recipes:Bubble)). 
It uses a `fail` for the case that no unsorted element can be found in the list of numbers.
As a result, the whole case fails and the default case is used.
```rascal-shell
import IO;
public list[int] sort(list[int] numbers){
  switch(numbers){
    case [*int nums1, int p, int q, *int nums2]:
       if(p > q){
          return sort(nums1 + [q, p] + nums2);
       } else {
       	  fail;
       }
     default: return numbers;
   }
}
sort([10, 1, 5, 3]);
```

.Benefits

.Pitfalls

