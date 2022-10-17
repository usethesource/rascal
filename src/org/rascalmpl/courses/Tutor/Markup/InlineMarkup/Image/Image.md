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
<img src="url" width="200px"/>
``````



#### Types

#### Function

#### Description

Tutor offers the simple Markdown image syntax plus referencing images in a course. 
If you need more flexibility, use the HTML `<img>` tag.

#### Examples

``````
![alt]((dandelion.jpg))
``````

will produce:

![alt]((dandelion.jpg))

or we could use an `img` tag with a full path to the file in the assets folder:
``````
<img src="/assets/Tutor/Markup/InlineMarkup/Image/dandelion.jpg" width="100px">
``````

which produces this:

<img src="/assets/Tutor/Markup/InlineMarkup/Image/dandelion.jpg" width="100px"/>

Finally, have a look at the ((Library:module:Content)) library module and the listing feature to generate
images using Rascal code at Tutor compile time.

#### Benefits

#### Pitfalls

* Don't forget a sensible alt text. It makes the documentation much more accessible.
