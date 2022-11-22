---
title: Doc Annotations
---

#### Synopsis

The doc annotation tags attach an inline concept description to any Rascal declaration. Inside the tag you can use an entire structure of a tutor concept. Previously there was a single `@doc` tag with all the sections
of a concept internally. Now we use different tags for each concept.

* `@synopsis`
* `@description`
* `@examples`
* `@benefits`
* `@pitfalls`

#### Description

All Rascal declarations can be preceeded by annotation of the form `@synopsis{ ... }` where ... may be arbitrary text, 
provided that `{` and `}` characters are balanced and that unbalanced braces are escaped like `\{` or `\}`. 
This text is expanded to a full concept definition when the concept is processed. 

The Tutor supports and expands inline concept descriptions for the following declarations types.

## Module Declaration

The name of this module is extracted.
The header of the concept definition is automatically generated and consists of:

```
---
title: _Module name_
---
#### Usage

_Import declaration needed to use this module_
```

## Function Declaration

The signatures of this function and of all directly following functions with the same name are collected. 
The signatures are placed in an itemized list (unless there is only one).
The header of the concept definition is automatically generated and consists of:

``````
---
title: _Function name_
---

#### Function

```rascal
_Function signature_
```

####
Usage
_Import declaration needed to use this module_
``````

## Data Declaration

The signatures of this data declaration and of all directly following data declarations without their own `@doc` annotation are collected.
The header of the concept definition is automatically generated and consists of:

``````
# _Data declaration name_

#### Type

```rascal
_Data declarations_
```

#### Usage
_Import declaration needed to use this module_
``````

## Annotation Declaration

The signature of this annotation declaration is collected.
The header of the concept definition is automatically generated and consists of:

``````
# _Annotation declaration name_

#### Type

```rascal
_Annotation declarations_
```

#### Usage
_Import declaration needed to use this module_
``````

#### Examples

We only give an example of documenting a simple function. Read the source code of Rascal library files for other ones. 

Consider the source code of the now function in the `DateTime` library.

``````
@synopsis{Get the current datetime.}
@examples{
```rascal-shell
  import DateTime;
  now();
```
}
@javaClass{org.rascalmpl.library.DateTime}
public java datetime now();
``````
  
This will be expanded to

``````
  # now
  #### Function
  
  ```rascal
  datetime now();
  ```
  
  #### Usage
  
  ```rascal
  import DateTime;
  ```

  #### Synopsis
  
  Get the current datetime.

  #### Examples
```rascal-shell
  import DateTime;
  now();
```
``````

The final result is the section of the DateTime module on the now function [here]((Library:DateTime-now)).

#### Benefits

* A (small) part of documentation writing is automated.
* The information about the name of a function, data or annotation declaration, or its signature is always consistent.
* Examples of how to use functions are maintained close to the definitions of those functions
* API documentation for Rascal modules is easily presented online or in an IDE.

#### Pitfalls 

* This approach requires that functions with the same name are grouped together in the source file.
* Editor support for markdown inside @doc tags is usually limited.
