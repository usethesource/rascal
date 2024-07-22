@license{
  Copyright (c) 2019 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Davy Landman}
@contributor{Jurgen J. Vinju}
module util::Random

@synopsis{Get a random value of a certain type}
@javaClass{org.rascalmpl.library.Prelude}
java &T randomValue(type[&T] ofType, int depth = 5, int width = 5);
    
@synopsis{Get a random value of a certain type}
@javaClass{org.rascalmpl.library.Prelude}
java &T randomValue(type[&T] ofType, int seed, int depth = 5, int width = 5);
    
