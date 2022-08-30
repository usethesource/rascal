module util::UUID

@javaClass{org.rascalmpl.library.Prelude}
@doc{
.Synopsis
generates a unique identifier shaped as a `loc`

.Description

This function generates a UUID, see http://en.wikipedia.org/wiki/Universally_unique_identifier.
Since UUIDs are useful to assign an opaque and unique identity to data, the function returns
a ((Location)) (which is the preferred representation for encoding identities in Rascal)

.Examples

```rascal-shell
import util::UUID;
```

The uuid() function generates a location with the authority showing the literal canonical UUID string
```rascal-shell,continue
uuid()
```

Use it to relate identies to data objects, as in this example which adds a field to a relation:

```rascal-shell,continue
myData = { <i,i*i> | i <- [1..11] }; 
rel[int n, int square, loc id] myUniqueData = { <i,j,uuid()> | <i,j> <- myData };
map[tuple[int i, int j], loc id] myUniqueMap = (<i,j>:uuid() | <i,j> <- myData );
```
Note how uuid() should always generate a fresh value:
```rascal-shell,continue
assert uuid() != uuid(); 
```

.Benefits

*  [Location]s are used for identifying program elements or model elements in Rascal. The uuid() function provides
an quick-and-easy way of acquiring such an identity without having to design a naming scheme.

.Pitfalls

*  UUIDs are a quick and dirty way of identifying data which may lead to hard to debug code. A naming scheme for [Location]s is better because it generates human readable
((Location))s which carry meaning. For example consider the difference in readability between these two values:
`|uuid://47fdcd64-4fd0-41a1-8aa3-61c5b272c3fc|` and `|java+class:///java/lang/Object|`. Both may lead to the same 
results in your computation, but if we print either of them out, one of them is opaque and the other is transparent. A transparent naming scheme is preferable for
debugging purposes.
}
java loc uuid();

@javaClass{org.rascalmpl.library.Prelude}
@doc{
.Synopsis
see [uuid], this function does the same except return the UUID as an int.

.Pitfalls

*  beware that this integer is almost guaranteed to use 128 bits, so communicating it outside of
Rascal should not be done via a Java 32-bit integer.
}
java int uuidi();
