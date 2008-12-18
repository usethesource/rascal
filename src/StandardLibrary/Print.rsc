module Print

public void java print(value V)
@java-imports{import java.io.File;}
{
   System.out.println(V.toString());
   return;
}

public void java print(value V1, value V2)
@java-imports{import java.io.File;}
{
   System.out.println(V1.toString() + V2.toString());
   return;
}