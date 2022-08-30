# Synopsis
This is a test synopsis.
 
# Description
See examples below!!!

# Examples 
 
```rascal-shell
import Content; 
html("this is some \<strong\>HTML\</strong\> output")
file(|https://www.rascal-mpl.org/assets/ico/favicon.png|)
1 + 1 == 2
int count = 1;
content("counter", Response (Request _) { count += 1; return response("count: <count>"); })
count;
count = 66;
content("counter", Response (Request _) { count += 1; return response("count: <count>"); })
count;
```

* _emphasis_
* *bold*
* http:///rascal-mpl.org[Rascal Web site]
* ((CallAnalysis-CallAnalysis)) 
* Table:

  | Module | LOC |
  |--------|-----|
  | A      | 10 |
  | B      | 20 |
   
  
| Operator    | Description |
|------------|------------|
| `$A$ \| $B$` | alternative |
| `\|\|`       | or          |
   
Horizontal rule:

---

* `code`
* `in code: _italics_`

<toc Test 2>

# Benefits

# Pitfalls
