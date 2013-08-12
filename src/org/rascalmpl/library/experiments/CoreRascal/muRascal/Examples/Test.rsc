module experiments::CoreRascal::muRascal::Examples::Test

import experiments::CoreRascal::muRascal::Examples::Fac;
import experiments::CoreRascal::muRascal::Examples::Fib;
import experiments::CoreRascal::muRascal::Examples::Do;


test bool tstAll() = tstFac() && tstFib() && tstDo();