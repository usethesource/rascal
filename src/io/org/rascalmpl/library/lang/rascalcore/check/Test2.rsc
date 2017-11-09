module lang::rascalcore::check::Test2

public set[int] diff(set[int] u1) {
  if ({s1*} := u1) {
    return s1;
  }
  return u1;
}