
module Tijs

import ParseTree;
extend lang::std::Layout;
extend lang::std::Id;
import String;
import IO;

start syntax Machine = "machine" Id name State* states;

syntax State = "state" Id name Trans* trans "end";

syntax Trans = "on" Id event "=\>" Id target;

alias Canvas = tuple[
    void(Tree, str /* name + extends/implements or method sig */, void()) decl,
    void(Tree, str /* prefix */, void()) block,
    void(Tree, str /* stat */ ) stat,
    tuple[str, map[loc, loc]]() output
];

Canvas newCanvas(loc out) {
    str src = "";
    map[loc, loc] srcMap = ();

    void decl(Tree t, str h, void() b) = block(t, h, b);

    int ind = 0;

    void indent() {
        src += ( "" | it + " " | _ <- [0..ind]);
    }

    void block(Tree t, str h, void() b) {
        int currentOffset = size(src);
        indent();
        src += h + " {\n";
        ind += 2;
        b();
        ind -= 2;
        indent();
        src += "}\n";
        int len = size(src) - currentOffset;
        loc l = out[offset=currentOffset][length=len];
        srcMap[l] = t.src;
    }

    void stat(Tree t, str s) {
        int currentOffset = size(src);
        indent();
        src += s + ";\n";
        int len = size(src) - currentOffset;
        loc l = out[offset=currentOffset][length=len];
        srcMap[l] = t.src;
    }

    tuple[str, map[loc,loc]] output() = <src, srcMap>;

    return <decl, block, stat, output>;
}

void tryIt() {
    start[Machine] m = 
    (start[Machine])`machine Doors
                    'state closed 
                    '   on open =\> opened
                    'end
                    'state opened
                    '   on close =\> closed
                    'end`;

    loc l = |home:///Doors.java|;
    Canvas j = newCanvas(l);
    toJava(m, j);
    <src, srcMap> = j.output();
    println("Generated code: ");
    println(src);
    for (loc k <- srcMap) {
        println("<k> ===\> <srcMap[k]>");
    }
    writeFile(l, src);
}

void toJava(start[Machine] m, Canvas j) {
    j.decl(m, "class <m.top.name>", () {
        int i = 0;
        for (State s <- m.top.states) {
            j.stat(s, "final static int <s.name> = <i>");
            i += 1;
        }
        j.stat(m, "private int $current = 0");
        j.decl(m, "public void run(Iterator\<String\> $input)", () {
            j.block(m, "while ($input.hasNext())", () {
                j.stat(m, "String $token = $input.getNext()");
                j.block(m, "switch ($current)", () {
                    for (State s <- m.top.states) {
                        j.block(s, "case <s.name>: ", () {
                            for (Trans t <- s.trans) {
                                j.block(t, "if ($token.equals(\"<t.event>\"))", () {
                                    j.stat(t, "$current = <t.target>");
                                    j.stat(t, "break");
                                });
                            }
                        });
                    }
                });
            });
        });
    });
}
