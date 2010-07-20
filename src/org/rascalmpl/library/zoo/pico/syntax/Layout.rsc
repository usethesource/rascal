module zoo::pico::syntax::Layout

layout Layout = [\ \t\n\r]
          | "%" ![%]* "%"
          | "%%" ![\n]* "\n"
          ;