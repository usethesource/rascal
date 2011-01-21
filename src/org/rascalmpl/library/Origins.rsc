module Origins

data Origin = literal(loc location)
		    | expression(loc location)
		    | none();

