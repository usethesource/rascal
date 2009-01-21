module Exception

/* How to write this? */

data Exception Exception(str message);

data OutOfBoundsException OutOfBounds(str message);
alias Exception OutOfBoundsException;

data EmptyListException EmptyList(str message);
alias Exception EmptyListException;

data ArithmeticOperationException ArithmeticOperation(str message);
alias Exception ArithmeticOperationException;

/* or */
data Exception OutOfBounds(str message) |
			   EmptyList(str message) | 
			   ArithmeticOperation(str message);




