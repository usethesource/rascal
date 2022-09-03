# Set

.Synopsis
An unordered collection of values without duplicates.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

A set is a collection of values with the following properties:

*  The set maybe empty.
*  The values in the list are _unordered_.
*  A value can only occur once.
*  The set has a size that is equal to the number of values in the set.


In Rascal, sets are surrounded by braces `{` and `}` and the elements are separated by commas.
Each set has a type of the form `set[T]`, where _T_ is the smallest common type of all set elements.
Read the description of [sets and their operators]((Rascal:Values-Set))
and of [library functions on sets]((Library:Set)).

.Examples

## Sets in Daily Life

*  A cutlery set consisting of knife, fork and the like.
   ![]((cutlery-set.jpg))
   http://www.ikea.com/gb/en/catalog/products/50087185/[credit]
*  A crowd of people.
*  A stamp collection (but be aware that the duplicates will disappear!)
   ![]((stamp-collecting.jpg))
   http://www.life123.com/hobbies/antiques-collectibles/stamps/stamp-collecting-2.shtml[credit]

## Sets in Computer Science

*  The files in a directory. Of course, when you order them (by name, modification date) you need a ((List)) to represent them.
*  The set of moves an opponent can play in a game.
*  The set of nodes in a network.

## Sets in Rascal

*  The empty set: `{}`. Its type is `set[void]`.
*  A set of integers: `{3, 1, 4}`. Its type is `set[int]`.
*  A set of mixed-type values: `{3, "a", 4}`. Its type is `set[value]`.

.Benefits

.Pitfalls

