---
title: Table
---

#### Synopsis

Mark up for tables.

#### Syntax

``````
| _Header_~1~ | _Header_~2~ | ... |
| ---- | --- | --- |
| _Entry_~11~  | _Entry_~12~  | ... |
| _Entry_~21~  | _Entry_~22~  | ... |
``````

The "column specification", that's the line with the dashes, may also contain hints for
left, right and centered aligment:

``````
| _Header_~1~ | _Header_~2~ | ... |
| :---- | :---: | ---: |
| _Entry_~11~  | _Entry_~12~  | ... |
| _Entry_~21~  | _Entry_~22~  | ... |
``````
----

#### Types

#### Function

#### Description

Tables follow the [standard markdown syntax](https://www.markdownguide.org/extended-syntax/#tables) for tables.

#### Examples

##### Example 1 

``````
| A  | B  | C |
| --- | --- | --- |
| 11 | 12 | 13 |
| 21 | 22 | 23 |
``````

gives:

| A  | B  | C |
| --- | --- | --- |
| 11 | 12 | 13 |
| 21 | 22 | 23 |


#####  Example 2 


``````
| A  | B  | C |
|:--- | ---:|:---:|
| 1111 | 1221 | 1331 |
| 21 | 22 | 23 |
``````

gives (with column B centered):

| A  | B  | C |
|:--- | ---:|:---:|
| 1111 | 1221 | 1331 |
| 21 | 22 | 23 |


#####  Example 3 

``````
| Operator   | Description |
| ---        | ---         |
| `A | B`    | Or operator |
| A &#124; B | Or operator |
``````

gives (note the escaped `|` character in one table entry):

| Operator    | Description |
| ---        | --- |
| `A | B`    | Or operator |
| A &#124; B | Or operator |


#### Benefits

* Standard markdown syntax for tables

#### Pitfalls

* Watch out for escaping that `|` character!
