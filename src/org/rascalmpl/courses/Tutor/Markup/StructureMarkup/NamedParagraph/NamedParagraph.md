---
title: NamedParagraph
---

#### Synopsis

Mark up for a named paragraph.

#### Syntax

*  `## ParagraphName`
*  `\### ParagraphName`
*  ...

#### Types

#### Function

#### Description

A NamedParagraph produces a named paragraph of a level that is determined by the number of `#` characters preceding the _ParagraphName_.

#### Examples

The input:

`## Paragraph`

will give

## Paragraph

#### Benefits

#### Pitfalls

* NamedParagraphs have at least two hash signs (`##`). A single hash is reserved for the Concept level.
* A NamedParagraph should start at the start of a line.

