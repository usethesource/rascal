# Program to  reverse alist

def rev1(lst)
  if lst.empty?
    []
  else
	rev1(lst[1..lst.length]) + lst[0,1]
  end
end

puts rev1(Array.new(size=200))
