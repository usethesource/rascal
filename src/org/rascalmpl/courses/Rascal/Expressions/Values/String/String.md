---
title: String
keywords:
  - "\\\""
  - "'"
  - "<"
  - ">"
  - "\\\\"
  - "\\\\n"
  - "\\\\t"
  - "\\\\r"
  - "\\\\b"
  - "\\\\f"
  - "\\\\u"
  - "\\\\U"
  - hexademical
  - escape
  - if
  - else
  - for
  - while
  - do

---

#### Synopsis

String values.

#### Syntax

`"StringChar~1~StringChar~2~..."`
where `_StringChar~i~_ may be one of the following:

*  __Ordinary character__: Any character except `<`, `>`, `"`, `'` or `\`.
*  __Escaped character__: Backslash `\` followed by any of  `<`, `>`, `"`, `'` or `\` represents the escaped character itself.
  Other escape sequences that are supported are:
**  `\n`: newline
**  `\t`: tab
**  `\r`: carriage return
**  `\b`: backspace
**  `\f`: vertical feed
**  `\u hexDigit~1~ hexDigit~2~ hexDigit~3~ hexDigit~4~` : hexadecimal escapes with four digit indexes into UNICODE.
**  `\U hexDigit~1~ hexDigit~2~ hexDigit~3~ hexDigit~4~ hexDigit~5~ hexDigit~6~` : hexadecimal escapes with six digit indexes into UNICODE.
**  `\ ahexDigit~1~ hexDigit~2~`:  hexadecimal escapes with 2 digit indexes into ASCII (0x0 ... 0x7F).


*  __String Interpolation__: 

| Form      | Description |
| --- | --- |
| `<Exp>` | Interpolate the value of the expression as a string |
| `<if(Exp){> ... StringChars ... <}>` | Conditional inclusion of _Text_, where _StringChars_ may use variables introduced in _Exp_ |
| `<if(Exp){> ... StringChars~1~ ... <} else {>  ... StringChars~2~ ... <}>` | Conditional inclusion of either _StringChars~1~_ or _StringChars~2~_ |
| `<for(Exp){>... StringChars ... <}>` | Iterative splicing of _StringChars_ into the result, where _StringChars_ may use variables introduced in _Exp_. |
| `<while(Exp){> ... StringChars ... <}>` | Iterative splicing of _StringChars_ into the result, where _StringChars_ may use variables introduced in _Exp_. |
| `<do {>... StringChars ... <} while (Exp)>` | Iterative splicing of _StringChars_ into the result, where _StringChars_ may use variables introduced in _Exp_. |


*  __Multiline__:

| Form | Description  |
| --- | --- |
| `StringChars~1~\n StringChars~2~ `   | Strings can be multi-line without an escape or continuation marker  |
| `StringChars~2~\n '  StringChars~2~` | A margin character `'` indicates where the next line starts  |



#### Usage

#### Types

`str`

#### Function

#### Description

The string values are represented by the type `str` and consist of character 
sequences surrounded by double quotes, e.g., `"a"` or `"a\nlong\nstring"`.

_Multiline_: Strings may span more than one line. The margin character `'` indicates which part of a line will be ignored. This is useful for indenting a multi-line string with the source code that generates it.

_Interpolation_:  String literals support so-called _string interpolation_: 
inside string constants text between angle brackets (`<` and `>`) is first executed and then replaced by
 its string value.
Various statements (if, for, while, do) also return a value and can be used in this way.
In the interpolation variant of these statements the block or blocks that are part of the statement become arbitrary text 
(that may itself contain interpolations). 

_Auto-indent_: Expressions that get interpolated in a string will be auto-indented. This means that each line that results from the evaluation of the expression is prefixed with the indentation level of the position of the expression in the current string.

The following operators are defined for Strings:
(((TOC)))

There are also [library functions]((Library:module:String)) available for Strings.

#### Examples

```rascal-shell
N = 13;
"The value of N is <N>";
"The value of N*N is <N*N>";
"The value is <(N < 10) ? 10 : N*N>";
```
As you can see the string value of variables and expressions is interpolated in the result as expected. 
<br />
Some examples of more advances string interpolation 
```rascal-shell,continue
"N is <if(N < 10){> small <} else {> large <}>";
"N is <if(N < 10){> small <} else {> large (<N>)<}>";
"before <for(x<-[1..5]){>a <x> b <}>after";
```
multi-line string
```rascal-shell,continue
import IO;
println("hello
this
  is
    new")
```
multi-line string with margin:
```rascal-shell,continue
if (true)
  println("this is
          'what
          '  margins
          'are good for
          ");
```
auto indent:
```rascal-shell,continue
str genMethod(str n) = "int <n>() {
                       '  return 0;
                       '}";
str genClass() = "class myClass {
                 '  <genMethod("myMethod")>
                 '}";
println(genClass());
```


#### Benefits

String interpolation enables very flexible template-based text generation as used in generators for
source code, markup and the like.

#### Pitfalls

