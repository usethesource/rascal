module lang::rascalcore::compile::Examples::Tst1

str a = "";  

str g(str a 
            = a) { 
    return a;
}

//import lang::java::m3::Core;
//
//void foo(M3 model)
//{
//    rel[loc definition, Modifier modifier] mods = model.modifiers;
//    return;
//}