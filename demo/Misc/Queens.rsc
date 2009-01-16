module Queens

int abs(int x, int y)
{
	int diff = x - y;
	return diff >= 0 ? diff : - diff;
}

public void queens(int N)
{
   list[int] R = [1 .. N];
   
   all(int i : [1 .. N]){
      all(int j : [1 .. N]){
         if(i == j || abs(i - j) == abs(R[i] - R[j])){
         	fail;
         }
         R[i] = j;
      }
   }
   println(R);
   fail;
}