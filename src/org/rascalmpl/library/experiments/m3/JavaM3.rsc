module experiments::m3::JavaM3

extend experiments::m3::Core;

anno rel[loc from, loc to] M3@inheritance;
anno rel[loc from, loc to] M3@invocation;
anno rel[loc from, loc to] M3@access;
anno rel[loc from, loc to] M3@reference;
anno rel[loc from, loc to] M3@imports;
anno map[loc definition, Type typ] M3@types;
anno map[loc definition, loc comments] M3@documentation;
anno rel[loc definition, ExtendedModifiers modifiers] M3@modifiers;