---
title: No Main Function
---

#### Synopsis

Rascal program does not provide a `main` function.


#### Types

`data RuntimeException = NoMainFunction(str message);`
       
#### Usage

`import Exception;` (only needed when `NoMainFunction` is used in `catch`)

#### Description

Thrown when executing a Rascal program that does not declare a main function.
