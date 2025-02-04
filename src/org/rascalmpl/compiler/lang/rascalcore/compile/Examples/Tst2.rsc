//@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

import List;
data Shape = rect(int w, int h) | square(int n);

int area(rect(int w, int h)) = w * h;
int area(square(int n)) = n * n;

bool less(Shape a, Shape b) {
  return area(a) < area(b);
}

value main() {
  s1 = square(2);
  s2 = rect(1, 4);
  s3 = square(3);
  s4 = rect (3,3);
  return sort([s1, s2, s3, s4], less);
}