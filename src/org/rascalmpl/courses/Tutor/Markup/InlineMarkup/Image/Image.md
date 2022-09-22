---
title: Image
---

#### Synopsis

Include an image.

#### Syntax

(((TODO-image syntax has changed)))

#### Types

#### Function

#### Description

Describes an image to be included:

* _File_ is the name of the image file.
* _AlternateName_ is the alternate name of the image.
* _Width_ is the required width (in pixels) of the image.
* _Height_ is the rewuired height of the image in pixels.
* When `link` is present, it turns the image in a link to the given _URI_.

    
(((TODO:For further styling of images, see http://asciidoctor.org/docs/user-manual/#images.)))

#### Examples

##  Example 1 
```
![]((dandelion.jpg))
```
will produce:

![]((dandelion.jpg))

##  Example 2 
```
![]((dandelion.jpg))
```

![]((dandelion.jpg))

produces a reduced image floating at the right.

##  Example 3 

And, finally,
```
![]((dandelion.jpg))
```
produces a clickable image that links back to the source of the image.

![]((dandelion.jpg))

Try it!

#### Benefits

#### Pitfalls
