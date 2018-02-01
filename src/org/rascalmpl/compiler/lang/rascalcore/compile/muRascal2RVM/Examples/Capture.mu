module Capture

function f::0::l1[1,i] { return muprim("addition_mint_mint",i,f::0::n); }
function f[0,n] { n = 100; return f::0::l1::1; }

function h::1::k[1,i] { return muprim("addition_mint_mint", muprim("addition_mint_mint",h::1::n1,h::1::n2), i); }
function h[1,n1,n2] { n2 = 50; return h::1::k::1; }

function main[1,args,g,res1,l,res2] {
	g = f();
	res1 = g(11);
	l = h(1);
	res2 = l(2);
	return muprim("addition_mint_mint",res1,res2);
}