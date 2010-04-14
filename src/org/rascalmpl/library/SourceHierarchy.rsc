module SourceHierarchy

@doc{Labels are used to refer to source code entities in the UI of the IDE}
data Label = string(str description)
           | icon(loc image)
           | composite(set[Label] labels);

@doc{
Source hierarchies are tree shaped abstract models of the source code of programs.
This model offers a generic representation format, for use in communication with IDE
features such as "outline".
}
data SourceHierarchy = group(Label label, loc ref, list[SourceHierarchy] members)
                     | group(Label label, loc ref, set[SourceHierarchy] elements)
                     | item(Label label, loc ref);
