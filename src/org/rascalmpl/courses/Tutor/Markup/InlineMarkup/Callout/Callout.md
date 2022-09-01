# Callout

.Synopsis
Create a numeric callout for the digits 1--9.

.Syntax
```
< _Digit_ >
```

.Types

.Function

.Details

.Description
Callouts are used to attach numeric labels to a source code ((Listing)) for later discussion in the text.

.Examples
`[source]` +
`----` +
`This code contains the callout <1>` +
`----` +
will produce
```
This code contains the callout <1>
```
By including `<1>` in an enumeration or text, for instance, 

`<1> refers to the callout.` 

will give:

<1> refers to the callout.

.Benefits
Callout are most usefull to place markers in code fragments for later reference in explanatory text.

.Pitfalls

