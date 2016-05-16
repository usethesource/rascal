module experiments::Compiler::Examples::ExtendedBreakit

bool f(0) = true;
default bool f(int i) = false;

value main() = f(0);