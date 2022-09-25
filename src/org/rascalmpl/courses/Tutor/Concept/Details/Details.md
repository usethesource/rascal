---
title: Details
---

#### Synopsis

Explicitly list and _order_ the subconcepts of the current concept.

#### Syntax

Details are provided in the YAML header of the markdown file.

```
---
details:
  - Detail~1~
  - Detail~2~
  - ...
---
```

#### Description

To fully describe a concept, subconcepts (or details) are needed.
When the `details` meta-data header is empty, subconcepts will be listed
alphabetically in the (derived) outline of the documentation.

This alphabetical order can be influenced by explicitly stating concepts in the details meta-data header..
The effect is that the concepts listed there will be shown first, followed by the unmentioned subconcepts
in alphabetical order.

The details declaration also influences the order and contents of the ((TableOfContents)) markup.

#### Examples

In ((Concept)) we want to order the details in the order as they appear in the concept description.
Its `Details` meta-data is therefore:

``````
---
title: Concept
details:
  - Name
  - Details
  - Syntax
  - Types
  - Function
  - Synopsis
  - Description
  - Examples
  - Benefits
  - Pitfalls
  - Questions
---
``````

#### Benefits

* With details you can choose an order for the subconcepts
* The Tutor compiler will warn about missing or additional elements in the details list.

#### Pitfalls

* You can forget to add a new concept to the list, or remove an old one from it. 

