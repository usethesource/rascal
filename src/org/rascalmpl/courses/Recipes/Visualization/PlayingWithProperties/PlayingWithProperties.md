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
```rascal-figure,width=,height=,file=e1.png
                e = ellipse(size(200,100), shrink(0.8));
render(e);
```

![]((e1.png))

(we add the shrink to leave some space for thick lines and shadows below).

Change the style of its border using [lineStyle]((Library:Properties-lineStyle)):
```rascal-figure,width=,height=,file=e2.png
                e = ellipse(size(200,100), shrink(0.8), lineStyle("dot"));
render(e);
```

![]((e2.png))


Change the thickness of its border using [lineWidth]((Library:Properties-lineWidth)):
```rascal-figure,width=,height=,file=e3.png
                e = ellipse(size(200,100), shrink(0.8), lineWidth(5));
render(e);
```

![]((e3.png))


Change the color of its border using [lineColor]((Library:Properties-lineColor)):
```rascal-figure,width=,height=,file=e4.png
                e = ellipse(size(200,100), shrink(0.8), lineColor("blue"));
render(e);
```

![]((e4.png))


Change the color of its area using [fillColor]((Library:Properties-fillColor)):
```rascal-figure,width=,height=,file=e5.png
                e = ellipse(size(200,100), shrink(0.8), fillColor("yellow"));
render(e);
```

![]((e5.png))


Add a shadow using [shadow]((Library:Properties-shadow)):
```rascal-figure,width=,height=,file=e6.png
                e = ellipse(size(200,100), shrink(0.8), shadow(true));
render(e);
```

![]((e6.png))


Add the color of the shadow using [shadowColor]((Library:Properties-shadowColor)):
```rascal-figure,width=,height=,file=e7.png
                e = ellipse(size(200,100), shrink(0.8), shadow(true), shadowColor("grey"));
render(e);
```

![]((e7.png))


Finally, enjoy the grande finale:
```rascal-figure,width=,height=,file=e8.png
                e = ellipse(size(200,100), shrink(0.8), lineStyle("dot"), lineWidth(5), lineColor("blue"), fillColor("yellow"), shadow(true), shadowColor("grey"));
render(e);
```

![]((e8.png))




.Benefits

.Pitfalls

