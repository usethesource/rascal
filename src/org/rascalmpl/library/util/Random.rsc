module util::Random

@doc{
.Synopsis
Get a random value of a certain type
}
@javaClass{org.rascalmpl.library.Prelude}
java &T randomValue(type[&T] ofType, int depth = 5, int width = 5);
    
@doc{
.Synopsis
Get a random value of a certain type
}
@javaClass{org.rascalmpl.library.Prelude}
java &T randomValue(type[&T] ofType, int seed, int depth = 5, int width = 5);
    
