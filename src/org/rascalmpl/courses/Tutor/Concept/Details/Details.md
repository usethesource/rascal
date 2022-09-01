# Details

.Synopsis
Explcitly order the subconcepts of the current concept.

.Syntax
```
.Details
_ConceptNames_
```

.Types

.Function

.Details

.Description
To fully describe a concept, subconcepts (or details) are needed.
When the `Details` section is empty, subconcepts will be listed
alphabetically in the details section of the generated HTML file.

This alphabetical order can be influenced by explicitly stating concepts in the details sections.
The effect is that the concepts listed there will be shown first, followed by the unmentioned subconcepts
in alphabetical order.

.Examples
In ((Concept)) we want to order the details in the order as they appear in the concept description.
Its `Details` section is therefore:

```
.Details
Name Details Syntax Types Function Synopsis Description Examples Benefits Pitfalls Questions
```

.Benefits

.Pitfalls

