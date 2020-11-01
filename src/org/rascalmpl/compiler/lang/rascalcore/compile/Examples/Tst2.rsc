module lang::rascalcore::compile::Examples::Tst2

public &T <: num abs(&T <: num N)
{
    return N >= 0 ? N : -N;
}