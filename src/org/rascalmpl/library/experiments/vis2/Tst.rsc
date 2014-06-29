module experiments::vis2::Tst

data Figure(int width = 10)
	= box()
	| box(Figure f)
	| empty()
	;