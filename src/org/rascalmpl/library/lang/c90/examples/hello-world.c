
int print(const char *text); 

void printHello(char *name) {
  print("Hello ");
  print(name);
  print("!");
}

int main(int argc, char *argv[]) {                                                        
  char *name;
  if (argc > 1) {
    name = argv[1];
  } else {
    name = "World";
  }

  printHello(name);
}
