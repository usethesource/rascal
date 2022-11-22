---
title: Renaming
---

#### Synopsis

How to rename a concept.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

To rename a concept _C_ to _D_:

*  Rename _C_ to _D_ using the commands of the version control system.
*  Rename `D/C.md` to `D/D.md` using the version control system, or keep `D/index.md`
*  Open `D/D.md` in a text editor and change the title `title: Old Display Name` to `title: New Display Name`.
*  Recompile the course.

#### Examples

#### Benefits

* If there are dangling links to the course, the compiler will produce an exact error for each reference.

#### Pitfalls

