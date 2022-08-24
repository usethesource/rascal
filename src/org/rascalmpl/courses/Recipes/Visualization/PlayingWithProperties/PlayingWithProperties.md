# Playing With Properties

.Synopsis
Illustrate the effect of various figure properties.

.Syntax

.Types

.Function

.Details

.Description

.Examples
Here is an ellipse with minimum size 200x300 that occupies 80% of the available space:
[source,rascal-figure,width=,height=,file=e1.png]
----
                e = ellipse(size(200,100), shrink(0.8));
render(e);
----

image:e1.png[alt="e1"]

(we add the shrink to leave some space for thick lines and shadows below).

Change the style of its border using link:/Libraries#Properties-lineStyle[lineStyle]:
[source,rascal-figure,width=,height=,file=e2.png]
----
                e = ellipse(size(200,100), shrink(0.8), lineStyle("dot"));
render(e);
----

image:e2.png[alt="e2"]


Change the thickness of its border using link:/Libraries#Properties-lineWidth[lineWidth]:
[source,rascal-figure,width=,height=,file=e3.png]
----
                e = ellipse(size(200,100), shrink(0.8), lineWidth(5));
render(e);
----

image:e3.png[alt="e3"]


Change the color of its border using link:/Libraries#Properties-lineColor[lineColor]:
[source,rascal-figure,width=,height=,file=e4.png]
----
                e = ellipse(size(200,100), shrink(0.8), lineColor("blue"));
render(e);
----

image:e4.png[alt="e4"]


Change the color of its area using link:/Libraries#Properties-fillColor[fillColor]:
[source,rascal-figure,width=,height=,file=e5.png]
----
                e = ellipse(size(200,100), shrink(0.8), fillColor("yellow"));
render(e);
----

image:e5.png[alt="e5"]


Add a shadow using link:/Libraries#Properties-shadow[shadow]:
[source,rascal-figure,width=,height=,file=e6.png]
----
                e = ellipse(size(200,100), shrink(0.8), shadow(true));
render(e);
----

image:e6.png[alt="e6"]


Add the color of the shadow using link:/Libraries#Properties-shadowColor[shadowColor]:
[source,rascal-figure,width=,height=,file=e7.png]
----
                e = ellipse(size(200,100), shrink(0.8), shadow(true), shadowColor("grey"));
render(e);
----

image:e7.png[alt="e7"]


Finally, enjoy the grande finale:
[source,rascal-figure,width=,height=,file=e8.png]
----
                e = ellipse(size(200,100), shrink(0.8), lineStyle("dot"), lineWidth(5), lineColor("blue"), fillColor("yellow"), shadow(true), shadowColor("grey"));
render(e);
----

image:e8.png[alt="e8"]




.Benefits

.Pitfalls

