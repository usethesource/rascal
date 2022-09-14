---
title: Ad Hoc Data Exploration
---

.Synopsis
Using Rascal to explore an interesting data space.

.Syntax

.Types

.Function

.Description
The problem we will look at comes from mathematics, and has a precise analytical solution, but
let's use Rascal to explore the state space, and see how it can help us to build intuition.

As you know, Rascal supports arbitrarily large numbers cleanly and simply, unlike more traditional 
languages like C or Java.  For example, if you want to compute 1000!, then it's a simple matter of
calling `fact(1000)` at the command line.  Let's use this definition of factorial:

```rascal
public int fact (int n) {
    if (n <= 1) {
	    return 1;
    } else {
	    return n * fact (n-1);
    }
}
```

If you compute `fact(1000)` at the Rascal command line, you get a large number, on the order of 4.02 x 10^2567^. This is much, much bigger than, say a google, which is a mere 10<^>100^.  (If Rascal runs out stack space, try computing 100!, then 200!, then ... then 1000!; the run-time will allocate more stack space incrementally and automatically if you sneak up to where you want to go).

```rascal
rascal> fact(1000);
int: 402387260077093773543702433923003985719374864210714632543799910429938512398629020592044208486969404800479988610197196058631666872994808558901323829669944590997424504087073759918823627727188732519779505950995276120874975462497043601418278094646496291056393887437886487337119181045825783647849977012476632889835955735432513185323958463075557409114262417474349347553428646576611667797396668820291207379143853719588249808126867838374559731746136085379534524221586593201928090878297308431392844403281231558611036976801357304216168747609675871348312025478589320767169132448426236131412508780208000261683151027341827977704784635868170164365024153691398281264810213092761244896359928705114964975419909342221566832572080821333186116811553615836546984046708975602900950537616475847728421889679646244945160765353408198901385442487984959953319101723355556602139450399736280750137837615307127761926849034352625200015888535147331611702103968175921510907788019393178114194545257223865541461062892187960223838971476088506276862967146674697562911234082439208160153780889893964518263243671616762179168909779911903754031274622289988005195444414282012187361745992642956581746628302955570299024324153181617210465832036786906117260158783520751516284225540265170483304226143974286933061690897968482590125458327168226458066526769958652682272807075781391858178889652208164348344825993266043367660176999612831860788386150279465955131156552036093988180612138558600301435694527224206344631797460594682573103790084024432438465657245014402821885252470935190620929023136493273497565513958720559654228749774011413346962715422845862377387538230483865688976461927383814900140767310446640259899490222221765904339901886018566526485061799702356193897017860040811889729918311021171229845901641921068884387121855646124960798722908519296819372388642614839657382291123125024186649353143970137428531926649875337218940694281434118520158014123344828015051399694290153483077644569099073152433278288269864602789864321139083506217095002597389863554277196742822248757586765752344220207573630569498825087968928162753848863396909959826280956121450994871701244516461260379029309120889086942028510640182154399457156805941872748998094254742173582401063677404595741785160829230135358081840096996372524230560855903700624271243416909004153690105933983835777939410970027753472000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
```

Now copy the numerical result above and paste it into an edit window to have a good look at it.  Notice anything interesting?  The last 249 digits are all zeros.  How did this happen and what does it mean?

To be honest, when I did this calculation for the first time, I thought I'd found a bug.  So I looked at the values of N! for N in the range 900 to 1000 and discovered that the zeros accumulate on the end of N! as N gets bigger.  Let's think about it for a bit:  N! is a cumulative product, so once a zero has appeared on the end there is no way to get rid of it by multiplying by a positive integer.

How do the zeros appear?  Well, this isn't to hard to figure out.  Obviously, each time you reach a multiple of 10, you will add (at least) one more zero to the cumulative product.  But what about multiples of 5?  Well, you would add one more zero if you can match the 5 to a 2 within the factors, and there are lots of lonely 2s in that list.  So, to summarize, each time N is a multiple of 5, you add at least one zero onto the cumulative product N!.

So here's the question we're going to solve:  For an arbitrary N, can you predict exactly how many trailing zeros there will be in N!?  

Again, this can be solved analytically (and if you go looking on the web, you will discover that this is an old chestnut of a math problem that's sometimes used in job interviews to test analytical ability), but what I want to do here is to show how we can use Rascal to play around with the problem space a bit to help us build up our intuition.  This is very much like what we do in empirical software engineering, when we have lots of data to analyze, and we're trying to look for patterns that might explain behaviours, such as why some functions are more likely to be associated with bugs than others.  In that kind of situation, we typically go through two stages:  first, we wade through the data, exploring relationships, looking for unusual lumps or recognizable patters; second, we build theories of how the world works, and test them out using the data.  In both stages, we not only look at the data, we play with it.  We build little tools to help answer our questions, see where our hunches lead us.  We use this "play" to improve our understanding of the problem space, and build intuition about how it works as testable theories.  In empirical software engineering, as in most other sciences, we usually don't get concrete proof of a theory; rather, we gather evidence towards ultimately accepting or rejecting the theories (tho often, we may choose to use this evidence to refine the theories and try again).

In this case, however, there is a precise analytical solution, a proof, a "ground truth".  But that doesn't mean that we can't use the empirical approach to help build our intuition about the problem space, and ultimately devise a theory about how to calculate the number of trailing zeros in N!.  Solving analytical problems is about having enough intuition to see possible solutions.  And using this empirical approach is one way to build intuition.

So let's define a few helper functions and see where that leads us:
```rascal
public int countZeros (int n) {
    if (n < 10) {
	    return 0;
    } else if (n % 10 == 0) {
        return 1 + countZeros (n / 10);
    } else {
	    return countZeros (n / 10);
    }   
}
rascal> int i = fact(1000);
int: 402387260077093773543702433923003985719374864210714632543799910429938512398629020592044208486969404800479988610197196058631666872994808558901323829669944590997424504087073759918823627727188732519779505950995276120874975462497043601418278094646496291056393887437886487337119181045825783647849977012476632889835955735432513185323958463075557409114262417474349347553428646576611667797396668820291207379143853719588249808126867838374559731746136085379534524221586593201928090878297308431392844403281231558611036976801357304216168747609675871348312025478589320767169132448426236131412508780208000261683151027341827977704784635868170164365024153691398281264810213092761244896359928705114964975419909342221566832572080821333186116811553615836546984046708975602900950537616475847728421889679646244945160765353408198901385442487984959953319101723355556602139450399736280750137837615307127761926849034352625200015888535147331611702103968175921510907788019393178114194545257223865541461062892187960223838971476088506276862967146674697562911234082439208160153780889893964518263243671616762179168909779911903754031274622289988005195444414282012187361745992642956581746628302955570299024324153181617210465832036786906117260158783520751516284225540265170483304226143974286933061690897968482590125458327168226458066526769958652682272807075781391858178889652208164348344825993266043367660176999612831860788386150279465955131156552036093988180612138558600301435694527224206344631797460594682573103790084024432438465657245014402821885252470935190620929023136493273497565513958720559654228749774011413346962715422845862377387538230483865688976461927383814900140767310446640259899490222221765904339901886018566526485061799702356193897017860040811889729918311021171229845901641921068884387121855646124960798722908519296819372388642614839657382291123125024186649353143970137428531926649875337218940694281434118520158014123344828015051399694290153483077644569099073152433278288269864602789864321139083506217095002597389863554277196742822248757586765752344220207573630569498825087968928162753848863396909959826280956121450994871701244516461260379029309120889086942028510640182154399457156805941872748998094254742173582401063677404595741785160829230135358081840096996372524230560855903700624271243416909004153690105933983835777939410970027753472000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
rascal> countZeros(i);
int: 472
```

This was my first try at the solution (really!), and there's a problem: 1000! has exactly 249 trailing zeros, not 472.  

What did I do wrong?  Oh, right, _trailing_ zeros, and the above function counts _all_ of the zeros.  Let's try again:

```rascal
public int countTrailingZeros (int n) {
    if (n < 10) {
	    return 0;
    } else if (n % 10 == 0) {
        return 1 + countTrailingZeros (n / 10);
    } else {
	    return 0 ;
    }   
}

rascal> countTrailingZeros(i);
int: 249
```

OK, so we're making progress.  Let's define another function to help us explore the data space:

```rascal
public void printLastTwenty (int n){
    for(int i <- [n-19..n+1]) {
        println ("<i>! has <countTrailingZeros(fact(i))> trailing zeros.");
    }
}

rascal>printLastTwenty(1000);
981! has 243 trailing zeros.
982! has 243 trailing zeros.
983! has 243 trailing zeros.
984! has 243 trailing zeros.
985! has 244 trailing zeros.
986! has 244 trailing zeros.
987! has 244 trailing zeros.
988! has 244 trailing zeros.
989! has 244 trailing zeros.
990! has 245 trailing zeros.
991! has 245 trailing zeros.
992! has 245 trailing zeros.
993! has 245 trailing zeros.
994! has 245 trailing zeros.
995! has 246 trailing zeros.
996! has 246 trailing zeros.
997! has 246 trailing zeros.
998! has 246 trailing zeros.
999! has 246 trailing zeros.
1000! has 249 trailing zeros.
ok
```
So the pattern I see arising (confirmed by more playing that I won't show you) is that you add a zero every time N is divisible by 5.  But sometimes you add more than one zero: 1000! adds three zeros.  

We defined one function above to help us look at the data more compactly; now let's create another function to look for lumps in the data:
```rascal
// Printout all i in [0..n] where i! has more trailing zeros than (i-1)!
public void findLumps (int n) {
    int iMinusOneFactZeros = 0;
    for (int i <- [1..n+1]) {
        int iFactZeros = countTrailingZeros(fact(i));
        int diff = iFactZeros - iMinusOneFactZeros ;
        if (diff >= 1) {
            println ("<diff> more zeros at <i>!");
        }
        iMinusOneFactZeros = iFactZeros;
    }
}

rascal>findLumps(1000);
1 more zeros at 5!
1 more zeros at 10!
1 more zeros at 15!
1 more zeros at 20!
2 more zeros at 25!
1 more zeros at 30!
1 more zeros at 35!
1 more zeros at 40!
1 more zeros at 45!
2 more zeros at 50!
1 more zeros at 55!
1 more zeros at 60!
1 more zeros at 65!
1 more zeros at 70!
2 more zeros at 75!
1 more zeros at 80!
1 more zeros at 85!
1 more zeros at 90!
1 more zeros at 95!
2 more zeros at 100!
1 more zeros at 105!
1 more zeros at 110!
1 more zeros at 115!
1 more zeros at 120!
3 more zeros at 125!
1 more zeros at 130!
...
1 more zeros at 245!
3 more zeros at 250!
1 more zeros at 255!
1 more zeros at 495!
3 more zeros at 500!
1 more zeros at 505!
...
1 more zeros at 620!
4 more zeros at 625!
1 more zeros at 630!
...
1 more zeros at 985!
1 more zeros at 990!
1 more zeros at 995!
3 more zeros at 1000!
ok
```

So probably we're noticing some patterns here already, and maybe forming some intuition.  But let's first revise our lump-finding function to produce even more concise output:

```rascal
// We can parameterize the threshold to look for jumps of 2, 3, or 4 zeros
public void findLumps2 (int n, int tao) {
    int iMinusOneFactZeros = 0;
    for (int i <- [1..n+1]) {
        int iFactZeros = countTrailingZeros(fact(i));
        int diff = iFactZeros - iMinusOneFactZeros ;
        if (diff >= tao) {
            println ("<diff> more zeros at <i>!");
        }
        iMinusOneFactZeros = iFactZeros;
    }
}

rascal>findLumps2(1000,2);
2 more zeros at 25!
2 more zeros at 50!
2 more zeros at 75!
2 more zeros at 100!
3 more zeros at 125!
2 more zeros at 150!
2 more zeros at 175!
2 more zeros at 200!
2 more zeros at 225!
3 more zeros at 250!
2 more zeros at 275!
...
2 more zeros at 950!
2 more zeros at 975!
3 more zeros at 1000!
ok

rascal>findLumps2(1000,3);
3 more zeros at 125!
3 more zeros at 250!
3 more zeros at 375!
3 more zeros at 500!
4 more zeros at 625!
3 more zeros at 750!
3 more zeros at 875!
3 more zeros at 1000!
ok

rascal>findLumps2(1000,4);
4 more zeros at 625!
ok
```

Notice anything yet?  Here are some fun math facts to consider:

*  5^0^ = 1
*  5^1^ = 5
*  5 ^2^ = 25
*  5^3^ = 125
*  5^4^ = 625
*  5^5^ = 3125

So here's the solution:

Let N be a positive integer.  

Let k = floor (log~5~ N)

Start a counter at zero, call it nz

We want to examine i <- [1..N+1]

*  If i  is not divisible by 5, ignore it
*  If  i  is divisible by 5, add 1 to nz
*  If  i  is also divisible by 25, add 1 more
*  ...
*  If  i is also divisible by 2k, add 1 more

We can write this in Rascal as:
```rascal
public int predictZeros (int N) {
    int k = floorLogBase(N, 5);  // I wrote this
    int nz = 0;
    for (int i <- [1..N+1] ) {
	    int p5 = 1;
        for (int j <- [1..k+1]) {
            p5 *= 5;
    	    if (i % p5 == 0) {
		        nz += 1;
	        } else {
                break;
	        }
	    }
    }
    return nz; 
}
```

Now a little hand validation might convince you that this should work, but let's write a little verifier function to be sure:

```rascal
public void verifyTheory (int N) {
    int checkInterval = 100; // for printing
    bool failed = false;
    for (int i <- [1..N+1]) {
        ifact=fact(i);
    	int p = predictZeros(i);
    	int c = countTrailingZeros(ifact);
    	if (p != c) {
	        failed = true;
	        println ("Found a counter example at i=<i>");
	        break;
    	} else {
	        if (i % checkInterval == 0) {
		        println ("<i>! has <p> trailing zeros");
	        }
	    }
    }
    if (!failed) {
	    println ("The theory works for i: 1..<N>");
    }
}

rascal>verifyTheory(10);
The theory works for i: 1..10
ok

rascal>verifyTheory(100);
100! has 24 trailing zeros
The theory works for i: 1..100
ok

rascal>verifyTheory(1000);
100! has 24 trailing zeros
200! has 49 trailing zeros
300! has 74 trailing zeros
400! has 99 trailing zeros
500! has 124 trailing zeros
600! has 148 trailing zeros
Found a counter example at i=625
    predicted zeros = 155
    observed zeros  = 156
ok
```

Yikes, what do we do?  Well, first let's look under the hood at the engine.  The function `predictZeros` _is_ actually correct, assuming that the functions is calls are correct.  So let's look at the auxiliary functions I wrote (but haven't shown you yet):

```rascal
// Log for an arbitrary base
public real logB(real a, real base) {
    return log(a) / log(base);
}

public real floor (real a) {
    return toReal(round (a - 0.5));
}

public int floorLogBase (int a, int b) {
    return toInt(floor(logB(toReal(a), toReal(b))));
}

rascal>floorLogBase(625,5);
int: 3
rascal>logB(625.0,5.0);
real: 3.9999999999999998757330130880776320985295476764801684...
```

Oh right, real numbers are prone to round off error.  What should we do?

Well, here's a bad solution (that "works"):

```rascal
public real floor (real a) {
    return toReal(round (a - 0.5 + 0.00001));
}
```
But how can I be sure that that's enought decimal places?  What if someone likes my `floor` function and sticks it into the Rascal library, where it is subsequently used by the Eurpoean Space Agency for its next generation of flight control software?

Sometimes, the answer is to do a lot of homework.  Lucky for us, here there is a fairly efficient exact solution using repeated integer division:
```rascal
// Also change predictZeros to call this version
public int floorLogBase2 (int a, int b) {
    int remaining = a;
    int ans = 0;
    while (remaining >= b) {
	    ans += 1;
	    remaining /= b;
    }
    return ans;		
}

rascal>verifyTheory(1000);
100! has 24 trailing zeros
200! has 49 trailing zeros
300! has 74 trailing zeros
400! has 99 trailing zeros
500! has 124 trailing zeros
600! has 148 trailing zeros
700! has 174 trailing zeros
800! has 199 trailing zeros
900! has 224 trailing zeros
1000! has 249 trailing zeros
The theory works for i: 1..1000
ok
```

And we're done.  But what did we learn here?  Here's what I think:

*  Explore the terrain, take notes, build intuition, develop theories, test them.
**  Refine and repeat
**  Double check!

*  Build infrastructure with natural "break points"
**  Understandable is better than fast, esp. in the beginning
**  The correct way is better than the easy way
***  The correct way may be pretty easy too

*  Document and later challenge your assumptions 
**  Are you measuring what you think you are measuring?  How do you know?



.Examples

.Benefits

.Pitfalls

