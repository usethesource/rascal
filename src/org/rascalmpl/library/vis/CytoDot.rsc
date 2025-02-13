module vis::CytoDot

import lang::dot::\syntax::Dot;
import vis::Graphs;
import Node;
import String;

private alias DotAttr = tuple[str,str];
private alias DotAttrs = list[DotAttr];

public DOT cytoToDot(Cytoscape cytoscape) {
    AttrList graphAttrList = generateAttrList([]);
    AttrList nodeAttrList = generateAttrList([]);
    AttrList edgeAttrList = generateAttrList([]);

    StatementList initialStats = (StatementList)`graph <AttrList graphAttrList>
    '  node <AttrList nodeAttrList> 
    '  edge <AttrList edgeAttrList>
    ' 1 -\> 2`;


    StatementList stats = (StatementList)``;
    stats = mergeStatements(initialStats, stats);

    return (DOT) `digraph G {
    '  <StatementList stats>
    '}`;

    /*
    for (CytoData elem <- cytoscape.elements) {
        list[CytoStyleOf] styles = gatherElementStyles(cytoscape, elem);
    }*/
}

private DotAttrs styleToAttrs(CytoStyle style) {
    return [
        <"color",style.color>
    ] 
    + dotShape(style.shape);
}

private DotAttrs dotShape(CytoNodeShape shape) {
    // Might need some more work to map shapes here
    str dotShape = getName(shape);
    DotAttrs attrs = [];
    if (startsWith(dotShape, "round-")) {
        dotShape = substring(dotShape, 6);
        attrs = attrs + [<"style", "filled,rounded">];
    }
    return attrs + <"shape", dotShape>;
}

private AttrList generateAttrList(DotAttrs attrs) {
    AttrList result = (AttrList) `[]`;
    for (<str name, str val> <- attrs) {
        result = addAttribute(result, name, val);
    }
    return result;
}

private AttrList addAttribute((AttrList) `[<Attribute* attrs1>]`, str name, str val) {
    Id nameId = [Id] toAttrValue(name);
    Id valId = [Id] toAttrValue(val);
    return (AttrList) `[<Attribute* attrs1> <Id nameId> = <Id valId>]`;
}

private StatementList addStatement((StatementList) `<Statement* stats1>`, Statement stat) {
    return (StatementList) `<Statement* stats1>
    '  <Statement stat>`;
}

private StatementList mergeStatements((StatementList) `<Statement* stats1>`, (StatementList) `<Statement* stats2>`) {
    return (StatementList) `<Statement* stats1> <Statement* stats2>`;
}

private str toAttrValue(str s) = "\"" + escape(s, ("\\": "\\\\", "\"": "\\\"")) + "\"";
