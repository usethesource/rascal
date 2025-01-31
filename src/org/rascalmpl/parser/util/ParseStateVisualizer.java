/**
 * Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

package org.rascalmpl.parser.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.AbstractContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.EpsilonNode;
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.result.RecoveredNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.edge.EdgesSet;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.IntegerObjectList;
import org.rascalmpl.parser.gtd.util.Stack;
import org.rascalmpl.util.visualize.dot.CompassPoint;
import org.rascalmpl.util.visualize.dot.DotAttribute;
import org.rascalmpl.util.visualize.dot.DotEdge;
import org.rascalmpl.util.visualize.dot.DotField;
import org.rascalmpl.util.visualize.dot.DotGraph;
import org.rascalmpl.util.visualize.dot.DotNode;
import org.rascalmpl.util.visualize.dot.DotRecord;
import org.rascalmpl.util.visualize.dot.NodeId;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.ProductionAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

/**
 * The parser uses quite complex datastructures.
 * In order to understand what is going on when parsing, this class can generate graphs (as dot files)
 * representing the internal datastructurs of the parser.
 *
 * These graphs are written to files that are relative to a directory specified in the environment
 * variable PARSER_VISUALIZATION_PATH.
 *
 * The parser can generate a large number of snapshots of the parser state during a single parse.
 * The file 'replay.html' contains an simple example of a html file to navigate through these snapshots.
 */
public class ParseStateVisualizer {
    public static final boolean VISUALIZATION_ENABLED = true;
    private static final String VISUALIZATION_URI_PATTERN_ENV = "PARSER_VISUALIZATION_URI_PATTERN";
    private static final String PARSER_VISUALIZATION_PATH_ENV = "PARSER_VISUALIZATION_PATH";
    private static final boolean INCLUDE_PRODUCTIONS = false;

    public static final NodeId PARSER_ID = new NodeId("Parser");
    public static final NodeId TODO_LISTS_ID= new NodeId("todoLists");
    public static final NodeId STACKS_TO_EXPAND_ID = new NodeId("stacksToExpand");
    public static final NodeId TERMINALS_TO_REDUCE_ID = new NodeId("terminalsToReduce");
    public static final NodeId NON_TERMINALS_TO_REDUCE_ID = new NodeId("nonTerminalsToReduce");

    public static final NodeId ERROR_TRACKING_ID = new NodeId("error");
    public static final NodeId UNEXPANDABLE_NODES_ID = new NodeId("unexpandableNodes");
    public static final NodeId UNMATCHABLE_LEAF_NODES_ID = new NodeId("unmatchableLeafNodes");
    public static final NodeId UNMATCHABLE_MID_PRODUCTION_NODES_ID = new NodeId("unmatchableMidProductionNodes");
    public static final NodeId FILTERED_NODES_ID = new NodeId("filteredNodes");

    private static final NodeId RECOVERED_NODES_ID = new NodeId("recoveredNodes");

    public static boolean shouldVisualizeUri(URI inputUri) {
        if (!VISUALIZATION_ENABLED) {
            return false;
        }

        String pattern = System.getenv(VISUALIZATION_URI_PATTERN_ENV);
        if (pattern == null) {
            return false;
        }

        return inputUri.toString().matches(pattern);
    }

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                .forEach(consumer);
        }
    }

    private final String name;
    private final File basePath;
    private final File frameDir;
    private final Map<Integer, DotNode> stackNodeNodes;
    private DotGraph graph;
    private int frame;
    private int nextParseNodeId;


    public ParseStateVisualizer(String name) {
        // In the future we might want to offer some way to control the path from within Rascal.
        String path = System.getenv(PARSER_VISUALIZATION_PATH_ENV);
        if (path == null) {
            throw new RuntimeException("The environment variable '" + PARSER_VISUALIZATION_PATH_ENV + "' is not set.");
        }
        basePath = new File(System.getenv(PARSER_VISUALIZATION_PATH_ENV));

        this.name = name;
        stackNodeNodes = new HashMap<>();

        frameDir = new File(new File(basePath, "frames"), name);
        if (frameDir.exists()) {
            try {
                FileUtils.deleteDirectory(frameDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        frameDir.mkdirs();
    }

    public void visualize(AbstractStackNode<IConstructor> node) {
        if (VISUALIZATION_ENABLED) {
            writeGraph(createGraph(node));
        }
    }

    public void visualizeRecoveryNodes(DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes) {
        writeGraph(createGraph(recoveryNodes));
    }

    public void visualizeProductionTrees(AbstractStackNode<IConstructor>[] nodes) {
        writeGraph(createProductionGraph(nodes));
    }

    public void visualizeNode(AbstractNode node) {
        writeGraph(createGraph(node));
    }

    public int getFrame() {
        return frame;
    }

    private void reset() {
        stackNodeNodes.clear();
        graph = null;
        frame++;
    }

    public void highlight(NodeId id) {
        if (graph != null) {
            graph.highlight(id);
        }
    }

    public void highlightStack(AbstractStackNode<IConstructor> stack) {
        if (graph != null) {
            DotNode dotNode = stackNodeNodes.get(stack.getId());
            highlight(dotNode.getId());
        }
    }

    public void visualizeParseTree(ITree parseTree) {
        writeGraph(createGraph(parseTree));
    }

    private synchronized DotGraph createGraph(AbstractStackNode<IConstructor> stackNode) {
        reset();
        graph = new DotGraph(name, true);
        addStack(graph, stackNode);
        return graph;
    }

    private synchronized DotGraph createGraph(AbstractNode parserNode) {
        reset();
        graph = new DotGraph(name, true);
        addParserNodes(graph, parserNode);
        return graph;
    }

    private DotGraph createGraph(DoubleArrayList<AbstractStackNode<IConstructor>, ArrayList<IConstructor>> recoveryNodes) {
        reset();
        graph = new DotGraph(name, true);
        final NodeId recoveryNodesId = new NodeId("recovery-nodes");

        DotNode arrayNode = DotNode.createArrayNode(recoveryNodesId, recoveryNodes.size());
        graph.addNode(arrayNode);

        for (int i=0; i<recoveryNodes.size(); i++) {
            NodeId pairId = new NodeId("recovery-" + i);
            DotRecord recoveryRecord = new DotRecord();
            recoveryRecord.addEntry(new DotField("Node", "node"));
            recoveryRecord.addEntry(new DotField("Productions", "productions"));
            graph.addRecordNode(pairId, recoveryRecord);

            graph.addEdge(new NodeId(recoveryNodesId, String.valueOf(i)), pairId);

            DotNode node = addStack(graph, recoveryNodes.getFirst(i));

            graph.addEdge(new NodeId(pairId, "node"), node.getId());

            NodeId productionsId = new NodeId("productions-" + i);
            addProductionArray(graph, productionsId, recoveryNodes.getSecond(i));
            graph.addEdge(new NodeId(pairId, "productions"), productionsId);
        }

        return graph;
    }

    private DotGraph createProductionGraph(AbstractStackNode<IConstructor>[] stackNodes) {
        reset();
        graph = new DotGraph(name, true);
        for (AbstractStackNode<IConstructor> stackNode : stackNodes) {
            addProductionNodes(graph, stackNode);
        }
        return graph;
    }

    public DotGraph createGraph(ITree parseTree) {
        reset();
        graph = new DotGraph(name, false);
        addParseTree(graph, parseTree, new HashMap<>());
        return graph;
    }

    private <P> NodeId addProductionNodes(DotGraph graph, AbstractStackNode<P> stackNode) {
        DotNode node = createDotNode(stackNode);
        graph.addNode(node);

        AbstractStackNode<P>[] prods = stackNode.getProduction();
        if (prods != null) {
            NodeId prodArrayId = new NodeId(node.getId() + "-prod");
            graph.addArrayNode(prodArrayId, prods.length);
            for (int i=0; i<prods.length; i++) {
                AbstractStackNode<P> child = prods[i];
                DotNode childNode = createDotNode(child);
                graph.addNode(childNode);
                graph.addEdge(new NodeId(prodArrayId, String.valueOf(i)), childNode.getId());
            }

            graph.addEdge(node.getId(), prodArrayId, "Production");
        }

        return node.getId();
    }

    private void addProductionArray(DotGraph graph, NodeId nodeId, ArrayList<IConstructor> productions) {
        DotNode arrayNode = DotNode.createArrayNode(nodeId, productions.size());
        graph.addNode(arrayNode);
        for (int i=0; i<productions.size(); i++) {
            IConstructor production = productions.get(i);
             NodeId prodId = new NodeId(nodeId.getId() + "-prod-" + i);
             graph.addNode(prodId, DebugUtil.prodToString(production));
             graph.addEdge(new NodeId(nodeId, String.valueOf(i)), prodId);
        }
    }

    public synchronized <P> void addRecoveredNodes(DoubleArrayList<AbstractStackNode<P>, AbstractNode> recoveredNodes) {
        addStackAndNodeDoubleList(graph, RECOVERED_NODES_ID, recoveredNodes);
        graph.addEdge(ERROR_TRACKING_ID, RECOVERED_NODES_ID, "Nodes to revive");
        highlight(RECOVERED_NODES_ID);
    }

    private <P> DotNode addStack(DotGraph graph, AbstractStackNode<P> stackNode) {
        DotNode node = stackNodeNodes.get(stackNode.getId());
        if (node != null) {
            return node;
        }

        node = createDotNode(stackNode);

        stackNodeNodes.put(stackNode.getId(), node);

        graph.addNode(node);

        if (INCLUDE_PRODUCTIONS) {
            addProductionNodes(graph, stackNode);
        }

        IntegerObjectList<EdgesSet<P>> edges = stackNode.getEdges();
        if (edges != null) {
            for (int i = edges.size() - 1; i >= 0; --i) {
                EdgesSet<P> edgesList = edges.getValue(i);
                if (edgesList != null) {
                    for (int j = edgesList.size() - 1; j >= 0; --j) {
                        AbstractStackNode<P> parentStackNode = edgesList.get(j);
                        DotNode parentDotNode = addStack(graph, parentStackNode);
                        graph.addEdge(node.getId(), parentDotNode.getId());
                    }
                }
            }
        }

        return node;
    }

    private <P> DotNode createDotNode(AbstractStackNode<P> stackNode) {
        String type = stackNode.getClass().getSimpleName();
        if (type.endsWith("StackNode")) {
            type = type.substring(0, type.length() - "StackNode".length());
        }

        String nodeName;

        try {
            nodeName = stackNode.getName();
        } catch (UnsupportedOperationException e) {
            nodeName = "";
        }

        if (nodeName.startsWith("layouts_")) {
            nodeName = nodeName.substring("layouts_".length());
        }

        int dot = stackNode.getDot();

        String extraInfo = "";
        if (stackNode.isMatchable()) {
            extraInfo += ",matchable";
        }
        if (stackNode.isSeparator()) {
            extraInfo += ",sep";
        }
        if (stackNode.isExpandable()) {
            extraInfo += ",expandable";
        }
        if (stackNode.isLayout()) {
            extraInfo += ",layout";
        }
        if (stackNode.isEndNode()) {
            extraInfo += ",end";
        }

        DotNode node = new DotNode(getNodeId(stackNode));
        String label = String.format("%s: %s\n.%d@%d %s",
            type, nodeName, dot, stackNode.getStartLocation(), extraInfo);

        String shortString = stackNode.toShortString();
        if (shortString != null) {
            label += "\n" + shortString;
        }

        P parentProduction = stackNode.getParentProduction();
        if (parentProduction instanceof IConstructor) {
            label += "\nin: " + DebugUtil.prodToString((IConstructor) parentProduction);
        } else {
            if (stackNode.getProduction() != null) {
                label += "\nin:";
                for (AbstractStackNode<P> n : stackNode.getProduction()) {
                    String s = n.toShortString();
                    if (!s.startsWith("layouts_")) {
                        label += " " + n.toShortString();
                    }
                }
            }
        }
        node.addAttribute(DotAttribute.ATTR_LABEL, label);

        return node;
    }

    private NodeId addParserNodes(DotGraph graph, AbstractNode parserNode) {
        NodeId id = getNodeId(parserNode);
        if (graph.containsNode(id)) {
            return id;
        }

        addParserNode(graph, parserNode, id);

        if (parserNode instanceof AbstractContainerNode) {
            @SuppressWarnings("unchecked")
            AbstractContainerNode<IConstructor> container = (AbstractContainerNode<IConstructor>) parserNode;
            Link firstAlt = container.getFirstAlternative();
            IConstructor firstProd = container.getFirstProduction();
            if (firstAlt != null) {
                NodeId firstAltId = addLink(graph, firstAlt, "Alt: " + DebugUtil.prodToString(firstProd));
                graph.addEdge(id, firstAltId);

                ArrayList<Link> alternatives = container.getAdditionalAlternatives();
                ArrayList<IConstructor> prods = container.getAdditionalProductions();
                if (alternatives != null) {
                    for (int i=0; i<alternatives.size(); i++) {
                        IConstructor prod = prods.get(i);
                        NodeId altId = addLink(graph, alternatives.get(i), "Alt: " + DebugUtil.prodToString(prod));
                        graph.addEdge(id, altId);
                    }
                }
            }
        }

        return id;
    }

    private NodeId addLink(DotGraph graph, Link link, String label) {
        NodeId linkId = getNodeId(link);
        if (graph.containsNode(linkId)) {
            return linkId;
        }

        DotNode linkNode = new DotNode(linkId);
        linkNode.addAttribute(DotAttribute.ATTR_LABEL, label);

        graph.addNode(linkNode);

        ArrayList<Link> prefixes = link.getPrefixes();
        if (prefixes != null) {
            for (int i=0; i<prefixes.size(); i++) {
                Link prefix = prefixes.get(i);
                if (prefix != null) {
                    NodeId prefixId = addLink(graph, prefix, "Prefix");
                    graph.addEdge(linkId, prefixId);
                }
            }
        }

        NodeId nodeId = addParserNodes(graph, link.getNode());
        graph.addEdge(linkId, nodeId, "node");

        return linkId;
    }

    private NodeId addParserNode(DotGraph graph, AbstractNode parserNode) {
        NodeId id = getNodeId(parserNode);
        addParserNode(graph, parserNode, id);
        return id;
    }

    @SuppressWarnings("unchecked")
    private void addParserNode(DotGraph graph, AbstractNode parserNode, NodeId id) {
        DotNode dotNode = new DotNode(id);
        dotNode.addAttribute(DotAttribute.ATTR_NODE_SHAPE, "octagon");

        String nodeName = parserNode.getClass().getSimpleName();
        if (nodeName.endsWith("Node")) {
            nodeName = nodeName.substring(0, nodeName.length() - "Node".length());
        }

        dotNode.addAttribute(DotAttribute.ATTR_LABEL, nodeName);

        switch (parserNode.getTypeIdentifier()) {
            case EpsilonNode.ID:
                break;
            case CharNode.ID:
                enrichCharNode(dotNode, (CharNode) parserNode);
                break;
            case LiteralNode.ID:
                enrichLiteralNode(dotNode, (LiteralNode) parserNode);
                break;
            case SortContainerNode.ID:
            case RecoveredNode.ID:
            case ExpandableContainerNode.ID:
                enrichContainerNode(dotNode, (AbstractContainerNode<IConstructor>) parserNode);
                break;
            case SkippedNode.ID:
                enrichSkippedNode(dotNode, (SkippedNode) parserNode);
                break;
            default:
                enrichUnknownParserNode(dotNode, parserNode);
                break;
        }

        graph.addNode(dotNode);
    }

    private void enrichCharNode(DotNode dotNode, CharNode charNode) {
        int c = charNode.getCharacter();
        String label = dotNode.getAttributeValue(DotAttribute.ATTR_LABEL) + "\nchar=" + c + "('" + (char) c + "')";
        dotNode.setAttribute(DotAttribute.ATTR_LABEL, label);
    }

    private void enrichLiteralNode(DotNode dotNode, LiteralNode literalNode) {
        int[] content = literalNode.getContent();
        String label = dotNode.getAttributeValue(DotAttribute.ATTR_LABEL) + " \"" + new String(content, 0, content.length) + "\"";
        dotNode.setAttribute(DotAttribute.ATTR_LABEL, label);
    }

    private void enrichSkippedNode(DotNode dotNode, SkippedNode skippedNode) {
        String label = dotNode.getAttributeValue(DotAttribute.ATTR_LABEL);
        int[] skipped = skippedNode.getSkippedChars();
        label += "\n@" + skippedNode.getOffset() + ": " + " \"" + new String(skipped, 0, skipped.length) + "\"";

        dotNode.setAttribute(DotAttribute.ATTR_LABEL, label);
    }

    private void enrichContainerNode(DotNode dotNode, AbstractContainerNode<IConstructor> node) {
        String label = dotNode.getAttributeValue(DotAttribute.ATTR_LABEL);
        label += " " + node.getOffset() + "-" + node.getEndOffset();
        label += "\n" + ProductionAdapter.getSortName(node.getFirstProduction());
        dotNode.setAttribute(DotAttribute.ATTR_LABEL, label);
    }

    private void enrichUnknownParserNode(DotNode dotNode, AbstractNode parserNode) {
        String label = dotNode.getAttributeValue(DotAttribute.ATTR_LABEL);
        label += "\ntype=" + parserNode.getTypeIdentifier();
        dotNode.setAttribute(DotAttribute.ATTR_LABEL, label);
    }

    public static <P> NodeId getNodeId(AbstractStackNode<P> stackNode) {
        return new NodeId(String.valueOf(stackNode.getId()));
    }

    private static NodeId getNodeId(Object node) {
        return new NodeId(String.valueOf(System.identityHashCode(node)));
    }

    public void writeGraph() {
        if (graph != null) {
            writeGraph(graph);
        }
    }

    public <P, T, S> void createGraph(SGTDBF<P, T, S> parser, String step) {
        if (!VISUALIZATION_ENABLED) {
            return;
        }
        reset();

        graph = new DotGraph(name, true);

        int location = parser.getLocation();

        DotNode parserNode = new DotNode(PARSER_ID);

        int[] inputChars = parser.getInput();
        String input = new String(inputChars, 0, inputChars.length);

        char lookahead = (char) parser.getLookAheadChar();
        if (lookahead == '\0') {
            lookahead = '$';
        }

        String label = String.format("Parser\nInput: \"%s\"\nLocation: %d ('%c')\nStep %d: %s",
            input, location, lookahead, frame, step);
        parserNode.setAttribute(DotAttribute.ATTR_LABEL, label);
        graph.addNode(parserNode);

        addTodoLists(parser, graph);
        addStacksToExpand(parser, graph);
        addTerminalsToReduce(parser, graph);
        addNonTerminalsToReduce(parser, graph);

        addErrorNodes(parser, graph);
    }

    private <P, T, S> void addTodoLists(SGTDBF<P, T, S> parser, DotGraph graph) {
        DoubleStack<AbstractStackNode<P>, AbstractNode>[] todoLists = parser.getTodoLists();
        int start = parser.getQueueIndex();

        int todos = Math.min(todoLists.length, 50);

        DotNode todoListsNode = DotNode.createArrayNode(TODO_LISTS_ID, todos);

        for (int tokenLength=1; tokenLength<=todos+1; tokenLength++) {
            int index = (start + tokenLength - 1) % todoLists.length;
            DoubleStack<AbstractStackNode<P>, AbstractNode> todoList = todoLists[index];
            if (todoList != null && !todoList.isEmpty()) {
                NodeId todoListId = new NodeId("todo-" + tokenLength);
                addStackAndNodeDoubleStack(graph, todoListId, todoList);
                graph.addEdge(DotEdge.createArrayEdge(TODO_LISTS_ID, tokenLength, todoListId));
            }
        }

        graph.addNode(todoListsNode);
        graph.addEdge(PARSER_ID, TODO_LISTS_ID, "todo lists");
    }

    private <P, T, S> void addStacksToExpand(SGTDBF<P, T, S> parser, DotGraph graph) {
        Stack<AbstractStackNode<P>> stacksToExpand = parser.getStacksToExpand();
        addStackNodeStack(graph, STACKS_TO_EXPAND_ID, stacksToExpand);
        graph.addEdge(PARSER_ID, STACKS_TO_EXPAND_ID, "stacks to expand");
    }

    private <P, T, S> void addTerminalsToReduce(SGTDBF<P, T, S> parser, DotGraph graph) {
        addStackAndNodeDoubleStack(graph, TERMINALS_TO_REDUCE_ID, parser.getStacksWithTerminalsToReduce());
        graph.addEdge(PARSER_ID, TERMINALS_TO_REDUCE_ID, "terminals to reduce");
    }

    private <P, T, S> void addNonTerminalsToReduce(SGTDBF<P, T, S> parser, DotGraph graph) {
        addStackAndNodeDoubleStack(graph, NON_TERMINALS_TO_REDUCE_ID, parser.getStacksWithNonTerminalsToReduce());
        graph.addEdge(PARSER_ID, NON_TERMINALS_TO_REDUCE_ID, "non-terminals to reduce");
    }

    private <P, T, S> void addErrorNodes(SGTDBF<P, T, S> parser, DotGraph graph) {
        addUnexpandableNodes(parser, graph);
        addUnmatchableLeafNodes(parser, graph);
        addUnmatchableMidProductionNodes(parser, graph);
        addFilteredNodes(parser, graph);

        graph.addNode(ERROR_TRACKING_ID, "Errors");

        graph.addEdge(PARSER_ID, ERROR_TRACKING_ID, "error tracking");
        graph.addEdge(ERROR_TRACKING_ID, UNEXPANDABLE_NODES_ID, "unexpandable");
        graph.addEdge(ERROR_TRACKING_ID, UNMATCHABLE_LEAF_NODES_ID, "unmatchable leafs");
        graph.addEdge(ERROR_TRACKING_ID, UNMATCHABLE_MID_PRODUCTION_NODES_ID, "unmatchable mid-prod");
        graph.addEdge(ERROR_TRACKING_ID, FILTERED_NODES_ID, "filtered");
    }

    private <P, T, S> void addUnexpandableNodes(SGTDBF<P, T, S> parser, DotGraph graph) {
        addStackNodeStack(graph, UNEXPANDABLE_NODES_ID, parser.getUnexpandableNodes());
    }

    private <P, T, S> void addUnmatchableLeafNodes(SGTDBF<P, T, S> parser, DotGraph graph) {
        addStackNodeStack(graph, UNMATCHABLE_LEAF_NODES_ID, parser.getUnmatchableLeafNodes());
    }

    private <P, T, S> void addUnmatchableMidProductionNodes(SGTDBF<P, T, S> parser, DotGraph graph) {
        DoubleStack<DoubleArrayList<AbstractStackNode<P>, AbstractNode>, AbstractStackNode<P>> unmatchableMidProductionNodes = parser.getUnmatchableMidProductionNodes();

        graph.addArrayNode(UNMATCHABLE_MID_PRODUCTION_NODES_ID, unmatchableMidProductionNodes.getSize());
		for (int i = unmatchableMidProductionNodes.getSize() - 1; i >= 0; --i) {
            NodeId failureId = new NodeId("unmatchable-mid-production-" + i);
            DotRecord failureRecord = new DotRecord();
            failureRecord.addEntry(new DotField("Failed Node", "failedNode"));
            failureRecord.addEntry(new DotField("Predecessors", "predecessors"));
            graph.addRecordNode(failureId, failureRecord);
            graph.addEdge(new NodeId(UNMATCHABLE_MID_PRODUCTION_NODES_ID, String.valueOf(i)), failureId);

            DoubleArrayList<AbstractStackNode<P>, AbstractNode> failedNodePredecessors = unmatchableMidProductionNodes.getFirst(i);
            AbstractStackNode<P> failedNode = unmatchableMidProductionNodes.getSecond(i);

            DotNode node = addStack(graph, failedNode);
            NodeId predecessorsId = new NodeId("unmatchable-mid-production-predecessors-" + i);
            addStackAndNodeDoubleList(graph, predecessorsId, failedNodePredecessors);

            graph.addEdge(new NodeId(failureId, "failedNode"), node.getId());
            graph.addEdge(new NodeId(failureId, "predecessors"), predecessorsId);
        }
    }

    private <P, T, S> void addFilteredNodes(SGTDBF<P, T, S> parser, DotGraph graph) {
        addStackAndNodeDoubleStack(graph, FILTERED_NODES_ID, parser.getFilteredNodes());
    }

    private <P, N extends AbstractNode> void addStackAndNodeDoubleStack(DotGraph graph, NodeId nodeId, DoubleStack<AbstractStackNode<P>, N> doubleStack) {
        DotNode arrayNode = DotNode.createArrayNode(nodeId, doubleStack == null ? 0 : doubleStack.getSize());
        graph.addNode(arrayNode);

        if (doubleStack == null) {
            return;
        }

        for (int j=0; j<doubleStack.getSize(); j++) {
            AbstractStackNode<P> stack = doubleStack.getFirst(j);
            DotNode stackDotNode = addStack(graph, stack);
            graph.addEdge(new NodeId(nodeId, String.valueOf(j), CompassPoint.SW), stackDotNode.getId(), "Stack");

            AbstractNode node = doubleStack.getSecond(j);
            addParserNode(graph, node);
            graph.addEdge(new NodeId(nodeId, String.valueOf(j), CompassPoint.SE), getNodeId(node), "Node");
        }
    }

    private <P, N extends AbstractNode> void addStackAndNodeDoubleList(DotGraph graph, NodeId nodeId, DoubleArrayList<AbstractStackNode<P>, N> doubleList) {
        DotNode arrayNode = DotNode.createArrayNode(nodeId, doubleList.size());
        graph.addNode(arrayNode);

        for (int i=0; i<doubleList.size(); i++) {
            NodeId entryId = new NodeId(nodeId.getId() + "-entry" + i);
            DotRecord entryRecord = new DotRecord();
            entryRecord.addEntry(new DotField("Stack", "stack"));
            entryRecord.addEntry(new DotField("Node", "node"));
            graph.addRecordNode(entryId, entryRecord);

            AbstractStackNode<P> stack = doubleList.getFirst(i);
            DotNode stackDotNode = addStack(graph, stack);
            graph.addEdge(new NodeId(entryId, "stack", CompassPoint.SW), stackDotNode.getId(), "Stack");

            AbstractNode node = doubleList.getSecond(i);
            addParserNode(graph, node);
            graph.addEdge(new NodeId(entryId, "node", CompassPoint.SE), getNodeId(node), "Node");


            graph.addEdge(new NodeId(nodeId, String.valueOf(i)), entryId);
        }
    }

    private <P> void addStackNodeStack(DotGraph graph, NodeId nodeId, Stack<AbstractStackNode<P>> stack) {
        if (stack == null) {
            return;
        }

        DotNode arrayNode = DotNode.createArrayNode(nodeId, stack.getSize());

        for (int j=0; j<stack.getSize(); j++) {
            AbstractStackNode<P> stackNode = stack.get(j);
            addStack(graph, stackNode);

            graph.addEdge(DotEdge.createArrayEdge(nodeId, j, getNodeId(stackNode)));
        }

        graph.addNode(arrayNode);
    }

    private void writeGraph(DotGraph graph) {
        try {
            File dotFile =  new File(basePath, name + ".dot");
            File svgFile = new File(basePath, name + ".svg");
            //File frameDir = new File(basePath, BASE_DIR + "/frames/" + name + "/";
            File frameSvgFile = new File(frameDir, String.format("%04d", frame) + ".svg");
            File frameDotFile = new File(frameDir, String.format("%04d", frame) + ".dot");
            FileWriter writer = new FileWriter(dotFile);
            writer.write(graph.toString());
            writer.close();

            String cmd = String.format("dot -Tsvg %s -o %s", dotFile, svgFile);
            Process process = Runtime.getRuntime().exec(cmd);
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<?> future = executorService.submit(streamGobbler);

            process.waitFor();
            future.get(10, TimeUnit.SECONDS);

            Files.copy(svgFile.toPath(), frameSvgFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(dotFile.toPath(), frameDotFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private NodeId addParseTree(DotGraph graph, ITree tree, Map<ITree, NodeId> addedTrees) {
        NodeId id = addedTrees.get(tree);
        if (id == null) {
            Type type = tree.getConstructorType();
            if (type == RascalValueFactory.Tree_Char) {
                id = addChar(graph, tree);
            } else if (type == RascalValueFactory.Tree_Cycle) {
                id = addCycle(graph, tree);
            } else if (type == RascalValueFactory.Tree_Appl) {
                id = addAppl(graph, tree, addedTrees);
            } else if (type == RascalValueFactory.Tree_Amb) {
                id = addAmb(graph, tree, addedTrees);
            } else {
                throw new IllegalStateException("Unsupported type: " + type);
            }

            addedTrees.put(tree, id);
        }

        return id;
    }

    private NodeId addChar(DotGraph graph, ITree charNode) {
        NodeId id = new NodeId("char-" + nextParseNodeId++);
        int value = ((IInteger) charNode.get(0)).intValue();
        graph.addNode(id, "Char: " + (char) value + " (" + value + ")");
        return id;
    }

    private NodeId addCycle(DotGraph graph, ITree cycle) {
        NodeId id = new NodeId("cycle-" + nextParseNodeId++);

        String sym = String.valueOf(cycle.get(0));
        int depth = ((IInteger) cycle.get(1)).intValue();

        graph.addNode(id, "Cycle: " + sym + ", depth=" + depth);

        return id;
    }

    private NodeId addAppl(DotGraph graph, ITree appl, Map<ITree, NodeId> addedTrees) {
        NodeId id = new NodeId("appl-" + nextParseNodeId++);

        IConstructor type = ProductionAdapter.getType(appl);

        graph.addNode(id, DebugUtil.prodToString(type));

        for (IValue arg : TreeAdapter.getArgs(appl)) {
            NodeId argId = addParseTree(graph, (ITree) arg, addedTrees);
            graph.addEdge(id, argId);
        }

        return id;
    }

    private NodeId addAmb(DotGraph graph, ITree amb, Map<ITree, NodeId> addedTrees) {
        NodeId id = new NodeId("amb-" + nextParseNodeId++);

        ISet alts = TreeAdapter.getAlternatives(amb);
        graph.addNode(id, "Amb, size=" + alts.size());

        for (IValue alt : alts) {
            NodeId argId = addParseTree(graph, (ITree) alt, addedTrees);
            graph.addEdge(id, argId);
        }

        return id;
    }

}
