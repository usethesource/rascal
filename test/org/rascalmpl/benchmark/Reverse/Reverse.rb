# Program to  reverse alist

def rev(lst)
  if lst.empty?
    []
  else
	rev(lst[1..lst.length]).concat([lst.first]);
  end
end

def measure
  a = Array.new(1000);
  
  for i in 1..1000
      a[i] = i;
  end
      
  start = Time.now.sec;
  for i in 1..10000
      rev(a);
  end
  puts "10000 x rev list of 1000 elements used %f seconds " % (Time.now.sec - start);
end

measure();