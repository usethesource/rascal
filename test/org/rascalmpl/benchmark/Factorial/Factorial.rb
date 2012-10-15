# Program to find the factorial of a number
# Save this as fact.rb

def fact(n)
  if n == 0
    1
  else
    n * fact(n-1)
  end
end


def measure()
  start = Time.now.sec;
  for i in 1..10000
      fact(500);
  end
  puts "10000 x fac(500) used %f seconds " % (Time.now.sec - start);
end

measure();
