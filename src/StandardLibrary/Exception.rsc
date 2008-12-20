module Exception

/* How to write this? */

data Exception Exception(str message);

data OutOfBoundsException OutOfBounds(str message);
type Exception OutOfBoundsException;

data EmptyListException EmptyList(str message);
type Exception EmptyListException;

data ArithmeticOperationException ArithmeticOperation(str message);
type Exception ArithmeticOperationException;

/* or */
data Exception OutOfBounds(str message) |
			   EmptyList(str message) | 
			   ArithmeticOperation(str message);




