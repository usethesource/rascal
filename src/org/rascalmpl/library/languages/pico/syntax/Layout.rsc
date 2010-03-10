module languages::pico::syntax::Layout

public layout Layout = [\ \t\n\r]
         | "%" ~[%]* "%"
         | "%%" ~[\n]* "\n"
         ;