module experiments::Compiler::Examples::Stat
import util::Math;
import List ;

real mean(list[int] s) {
		int sum = 0 ;
		for ( int el <- s ) {
			sum = sum + el ;
		}
		return  (sum/1.0) / (size(s)/1.0) ;
}
	
real var(list[int] a) {
		real avg = mean(a);
		real sum = 0.0 ;
		for (int el <- a) {
			sum = sum + ( ((el/1.0) - avg ) * ((el/1.0) - avg ) ) ; 
		}
		return sum / size(a) ;
}

real stdev(real var) = sqrt(var) ;
	
value main(list[value] args) {
	// List of 100 
	list[int] llist = [1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,
					   1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,
					   1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,
					   1,2,3,4,5,6,7,8,9,10] ;
	real avg  = mean(llist) ;
	real vari = 0.0 ;
	
	//for ( int i <- [1..100000] ) {
	//	vari =  mean(llist) ;
	//}
	
	for ( int i <- [1..13000] ) {  // At least a run od 2 sec
		vari =  var(llist) ;
	}
	return <avg,stdev(vari),vari> ;
}