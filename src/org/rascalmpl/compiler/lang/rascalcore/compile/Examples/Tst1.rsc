module lang::rascalcore::compile::Examples::Tst1

@javaClass{org.rascalmpl.library.Prelude}
public java int size(str s);

//public list[&T] sort(list[&T] lst) =
//    sort(lst, bool (&T a, &T b) { return a < b; } );
    
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] sort(list[&T] l, bool (&T a, &T b) less) ;

str substitute(str src, map[loc,str] s) { 
    int shift = 0;
    str subst1(str src, loc x, str y) {
        delta = size(y) - x.length;
        src = src[0..x.offset+shift] + y + src[x.offset+x.length+shift..];
        shift += delta;
        return src; 
    }
    order = sort([ k | k <- s ], bool(loc a, loc b) { return a.offset < b.offset; });
    return ( src | subst1(it, x, s[x]) | x <- order );
}