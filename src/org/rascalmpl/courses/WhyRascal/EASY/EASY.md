---
title: The Extract-Analyze-SYnthesize (EASY) paradigm
---

.Synopsis
Rascal excels in supporting the Extract-Analyze-SYnthesize (EASY) paradigm.

![]((easy-workflow.png))

// explicitly separate image from Description 

.Description

Many meta-programming problems follow a fixed pattern. 
Starting with some input system (a black box that we usually call _system-of-interest_), 
first relevant information is extracted from it and stored in an internal representation. 
This internal representation is then analyzed and used to synthesize results.
 If the synthesis indicates this, these steps can be repeated over and over again. These steps are shown in the figure.
This is an abstract view on solving meta-programming problems, but is rather common.

Rascal has been designed to fully support problem solving that fits the EASY paradigm.
We will discuss the following use cases represented by different _personas_:

(((TOC)))

