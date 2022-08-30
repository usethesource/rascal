@doc{
.Synopsis
Provides the library of stemmers written in the Snowball languages, and compiled to Java, which are
distributed with Lucene as a Rascal module.

.Description

See http://snowball.tartarus.org[the Snowball homepage] for more informations

.Examples

```rascal-shell
import analysis::stemming::Snowball
stem("bikes")
```
}
module analysis::text::stemming::Snowball

data Language
  = armenian()
  | basque()
  | catalan()
  | danish()
  | dutch()
  | english()
  | finnish()
  | french()
  | german()
  | german2()
  | hungarian()
  | irish()
  | italian()
  | lithuanian()
  | norwegian()
  | portugese()
  | romanian()
  | russian()
  | spanish()
  | swedish()
  | turkish()
  ;
  
@doc{
.Synopsis 
Stemming algorithms from the Tartarus Snowball (http://snowball.tartarus.org[the Snowball homepage] for different languages. 

.Description

This library wrapped into a single function supports Armenian, Basque, Catalan, Danish,
Dutch, English, Finnish, French, German, Hungarian, Irish, Italian, Lithuanian, Norwegian, Portugese,
Romanian, Russian, Spanish, Swedish and Turkish.
}  
@javaClass{org.rascalmpl.library.analysis.text.stemming.Snowball}
java str stem(str word, Language lang=english());

@javaClass{org.rascalmpl.library.analysis.text.stemming.Snowball}
@doc{
.Synopsis
Kraaij-Pohlmann is a well-known stemmer for the Dutch language. 

.Description

See http://snowball.tartarus.org/algorithms/kraaij_pohlmann/stemmer.html
}
java str kraaijPohlmannStemmer(str word);

@javaClass{org.rascalmpl.library.analysis.text.stemming.Snowball}
@doc{
.Synopsis
Porter stemming is a "standard" stemming algorithm for English of sorts.

.Description

See http://snowball.tartarus.org/algorithms/porter/stemmer.html for more information. 
}
java str porterStemmer(str word);

@javaClass{org.rascalmpl.library.analysis.text.stemming.Snowball}
@doc{
.Synopsis
Lovins designed the first stemmer according to the Tartarus website.

.Description

See http://snowball.tartarus.org/algorithms/lovins/stemmer.html
}
java str lovinsStemmer(str word);
