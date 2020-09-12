module lang::rascalcore::compile::Examples::Tst1

    
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] sort(list[&T] l, bool (&T a, &T b) less) ;

@javaClass{org.rascalmpl.library.Prelude}
public java int size(list[&T] lst);

public list[&T] takeWhile(list[&T] lst, bool (&T a) take) {
    i = 0;
    return while(i < size(lst) && take(lst[i])) {
        append lst[i];
        i+=1;
    }
}

value main() = takeWhile([1,3,2, 0, 10, 20], bool (&T a) { return a == 0; });
//value main() = sort([1,3,2], bool (&T a, &T b) { return a < b; });
