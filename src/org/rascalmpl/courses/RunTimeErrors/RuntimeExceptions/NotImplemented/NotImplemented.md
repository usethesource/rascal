---
title: Not Implemented
---

#### Synopsis

Attempt to execute an unimplemented feature.

#### Types

`data RuntimeException = NotImplemented(str msg), loc src);`
       
#### Usage

`import Exception;` (only needed when `NotImplemented` is used in `catch`)

#### Description

Thrown when a not (yet) implemented feature is executed.
This exception is mostly used by the Rascal implementors.
