---
title: Admonition
---

#### Synopsis

Mark up for admonitions.

#### Syntax

The general scheme is:
* `:::label␤ MarkedText ␤:::`, where `␤` represents a newline character and label is one of:
   * `note`
   * `tip`
   * `info`
   * `caution`
   * `danger`


#### Types

#### Function

#### Description

An admonition is remark that should draw the reader's attention.

#### Examples

``````
:::note 
This is a note
:::
``````

:::note
This is a note
:::

``````
:::tip 
MarkedText
:::
``````

:::tip 
what a great tip!
:::

``````
:::info 
MarkedText
:::
``````

:::info 
Some more information here.
:::

``````
:::caution 
MarkedText
:::
``````

:::caution 
Careful now..
:::


``````
:::danger 
MarkedText
:::
``````

``````
:::danger 
alarm!
:::
``````

#### Benefits

* If used sparingly admonitions can draw the user's attention to important parts in your documentation.
* The tutor compiler uses admonitions to mark problems (errors and warnings) with the links or the code contents of the documentations.

#### Pitfalls

* Too many admonitions clutters the documentation and inspires "alarm fatique"
* Better use the `#### Pitfalls` and `#### Benefits` sections to group information that is meant to alert the reader to positive/negative information.
