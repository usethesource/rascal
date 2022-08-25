# My First Box

.Synopsis
Drawing a box in many variations.

.Syntax

.Types

.Function

.Details

.Description

.Examples

Drawing a red [box]((Libraries:Figure-box)) is as simple as this:
[source,rascal-figure,width=,height=,file=b1.png]
----
import vis::Figure;
import vis::Render;
b = box(fillColor("red"));
render(b);
----
and it will look like this:

image:b1.png[alt="Box 1"]
 or rather, it will look like this:



image:Screenshot1.png[alt="Screenshot 1"]


Wow, the box fills the whole window! So lets give our box a [size]((Libraries:Properties-size)):
[source,rascal-figure,width=,height=,file=b2.png]
----
import vis::Figure;
import vis::Render;
b = box(fillColor("red"), size(200,100));
render(b);
----
and it will look like this:

image::b2.png[alt="Box 2"]


On screen however, it still fills the whole window as shown above.
The lesson here is that size is to be taken as *minimum size* (and probably we should
rename `size` to `minSize` to emphasize this).

So how can we produce a box that does _not_ fill the whole window? The answer is to define the size of the box
_relative_ to its surroundings by using [shrink]((Libraries:Properties-shrink)):

[source,rascal-figure,width=,height=,file=b3.png]
----
import vis::Figure;
import vis::Render;
b = box(fillColor("red"), shrink(0.5));
render(b);
----
which says: _I am a red box and I want to occupy 50% of the available space._ The result is:


image::Screenshot2.png[alt="Screen shot 2"]


Shrinking can also be limited to one dimension using [hshrink]((Libraries:Properties-hshrink)) 
or [vshrink]((Libraries:Properties-vshrink)):
[source,rascal-figure,width=,height=,file=b4.png]
----
import vis::Figure;
import vis::Render;
b = box(fillColor("red"), hshrink(0.5));
render(b);
----
which says:_ I am a red box and I want to occupy 50% of the available space in the horizontal direction and 100% of the available space in the vertical direction._ The result is:


image::Screenshot3.png[alt="Screen shot 3"]


Relative sizes can also be used when figures are nested.

[source,rascal-figure,width=,height=,file=b5.png]
----
import vis::Figure;
import vis::Render;
b1 = box(fillColor("red"), hshrink(0.5));
b2 = box(b1, fillColor("yellow"), size(200,100));
render(b2);
----

image::b5.png[alt="Box 5"]     


.Benefits

.Pitfalls

In the above examples we have consistently added the two imports:
[source,rascal]
----
import vis::Figure;
import vis::Render;
----
In other recipes and the Rascal documentation we omit these two imports to avoid cluttering our examples with irrelevant details.
Be aware that you will always need them when creating a visualisation.

