# Image

.Synopsis
Include an image.

.Syntax

[source,subs="quotes"]
----
image::_File_[]
image::_File_[_AlternateName_, _Width_, _Height_, link=_URI_]
----

.Types

.Function

.Details

.Description
Describes an image to be included:

* _File_ is the name of the image file.
* _AlternateName_ is the alternate name of the image.
* _Width_ is the required width (in pixels) of the image.
* _Height_ is the rewuired height of the image in pixels.
* When `link` is present, it turns the image in a link to the given _URI_.

For further styling of images, see http://asciidoctor.org/docs/user-manual/#images.

.Examples
##  Example 1 
[source,subs=""]
----
image::dandelion.jpg[]
----
will produce:

image::dandelion.jpg[]

##  Example 2 
[source,subs=""]
----
image::dandelion.jpg[Dandelion, 200, 300, float="right"]
----

image::dandelion.jpg[Dandelion, 200, 300, float="right"]

produces a reduced image floating at the right.

##  Example 3 

And, finally,
[source]
----
image::dandelion.jpg[Dandelion, 200, 300, link="https://flic.kr/p/GknoaW"]
----
produces a clickable image that links back to the source of the image.

image::dandelion.jpg[Dandelion, 200, 300, link="https://flic.kr/p/GknoaW"]

Try it!

.Benefits

.Pitfalls

