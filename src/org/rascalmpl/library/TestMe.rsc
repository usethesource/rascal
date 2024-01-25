module TestMe

import IO;
import util::Benchmark;
import util::Math;

void main() {
    results = for (i <- [2,3,4,5]) {
        println("2^<i> chains");
        append cpuTimeOf(() {
            R = {<j, j+1> | j <- [0..toInt(pow(10,i))], j % 100 != 0};
            R+;
        });
    }

    iprintln(results);
}
