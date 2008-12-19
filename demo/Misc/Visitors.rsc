module Visitors

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