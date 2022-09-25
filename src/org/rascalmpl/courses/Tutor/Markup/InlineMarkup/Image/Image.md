---
title: Image
---

#### Synopsis

Include an image.

#### Syntax

This is the general Markdown syntax:
``````
![alt text](url)
``````

With the double brackets you can search for an image in the tutor index:
``````
![alt text]((Link))
``````

Or, if you need more configurability, like dimensions:
``````
<img src="((Link))" width="200px">
``````

#### Types

#### Function

#### Description

Tutor offers the simple Markdown image syntax plus referencing images in a course. 
If you need more flexibility, simply use the HTML `<img>` tag.
#### Examples


``````
![]((dandelion.jpg))
``````

will produce:

![]((dandelion.jpg))

or we could use an `img` tag:
``````
<img src="((dandelion.jpg))" width="100px">
``````

which produces this:

<img src="((dandelion.jpg))" width="100px">

Finally, have a look at the ((Library:module:Content)) library module and the ((Markup-Listing)) feature to generate
images using Rascal code at Tutor compile time.

#### Benefits

#### Pitfalls

* Don't forget a sensible alt text. It makes the documentation much more accessible.
