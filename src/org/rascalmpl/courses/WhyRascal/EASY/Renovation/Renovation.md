---
title: Renovation
---

#### Synopsis

System renovation.

![]((extract-analyze-view-paradigm.png))

// explicitly separate image from Description 

#### Description



_Charlotte_ is software engineer at a large financial institution in Paris and she is looking for options to connect an old and dusty software system to a web interface. 

She will need to analyze the sources of that system to understand how it can be changed to meet the new requirements. The objects-of-interest are in this case the source files, documentation, test scripts and any other available information. They have to be parsed in some way in order to extract relevant information, say the calls between various parts of the system. The call information can be represented as a binary relation between caller and callee (the internal representation in this example). This relation with 1-step calls is analyzed and further extended with 2-step calls, 3-step calls and so on. In this way call chains of arbitrary length become available. With this new information, we can synthesize results by determining the entry points of the software system, i.e., the points where calls from the outside world enter the system. 

Having completed this first cycle, Charlotte may be interested in which procedures can be called from the entry points and so on and so forth. Results will be typically represented as pictures that display the relationships that were found. In the case of source code analysis, a variation of our workflow scheme is quite common. It is then called the extract-analyze-view paradigm and
 is shown in the figure.

#### Examples

#### Benefits

#### Pitfalls

