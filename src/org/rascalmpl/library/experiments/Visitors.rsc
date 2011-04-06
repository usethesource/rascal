@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::Visitors

visitor bottom_up { 
  all { 
    bottom_up;
  }
  yield;
}

visitor top_down { 
  yield;
  all { 
    top_down;
  }
}

visitor top_down_break { 
  try { 
    yield; 
  } 
  fail { 
    all {
      top_down_break;
    }
  }
}

visitor bottom_up_break { 
  try { 
    some {
      bottom_up_break;
    } 
  } 
  fail {
    yield;
  }
}

visitor downup {
  yield;
  all {
    downup;
  }
  yield;
}


visitor innermost {
  all {
    innermost;
  }
  repeat {
    yield; 
  }
}

visitor outermost {
  repeat {
    yield;
  }
  all {
    outermost;
  }
}
