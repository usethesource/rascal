module demo::Trans

import UnitTest;

// Compute transitive closure: R+ = R + (R o R) + (R o R o R) + ...

public rel[int,int] trans(rel[int,int] R){

  rel[int,int] T = R;
	
  solve (T) {
    T = T + (T o R);
  }

  return T;
}

public bool test()
{
  assertEqual(trans({<1,2>, <2,3>, <3,4>}),
              {<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>}
             );
  return report("Trans");
}

