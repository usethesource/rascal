package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.commonmark;


import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.commonmark.Extension;
import org.commonmark.html.AttributeProvider;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.BlockQuote;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.CustomNode;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Header;
import org.commonmark.node.HorizontalRule;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlTag;
import org.commonmark.node.Image;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Link;
import org.commonmark.node.ListBlock;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.highlighter.IHighlighter;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * Renders a tree of nodes back CommonMark to HTML.
 * <p>
 * Start with the {@link #builder} method to configure the renderer. Example:
 * <pre><code>
 * CommonMarkRenderer renderer = CommonMarkRenderer.builder().escapeHtml(true).build();
 * renderer.render(node);
 * </code></pre>
 */
public class CommonMarkRenderer {

    private final List<AttributeProvider> attributeProviders;
    
    private int lineWidth = 80;
	
	private String openEmphasis = "";
	private String closeEmphasis = "";
	
	private String openStrong = "";
	private String closeStrong = "";
	
	private IHighlighter highlighter;

    private CommonMarkRenderer(Builder builder) {
        this.attributeProviders = builder.attributeProviders;
        
        this.lineWidth = builder.lineWidth;
        
        this.openEmphasis = builder.openEmphasis;
        this.closeEmphasis = builder.closeEmphasis;
        
        this.openStrong = builder.openStrong;
        this.closeStrong = builder.closeStrong;
        
        this.highlighter = builder.highlighter;
    }

    /**
     * Create a new builder for configuring an {@link CommonMarkRenderer}.
     *
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public void render(Node node, Appendable output) {
        RendererVisitor rendererVisitor = new RendererVisitor(new CommonMarkWriter(output, lineWidth));
        node.accept(rendererVisitor);
    }

    /**
     * Render the tree of nodes to CommonMark.
     * @param node the root node
     * @return the rendered CommonMark as text
     */
    public String render(Node node) {
        StringBuilder sb = new StringBuilder();
        render(node, sb);
        return sb.toString();
    }

    /**
     * Builder for configuring an {@link CommonMarkRenderer}. See methods for default configuration.
     */
    public static class Builder {

        private List<AttributeProvider> attributeProviders = new ArrayList<>();
        private int lineWidth = 80;
    	
    	private String openEmphasis = "";
    	private String closeEmphasis = "";
    	
    	private String openStrong = "";
    	private String closeStrong = "";
    	
    	private IHighlighter highlighter;

        /**
         * @return the configured {@link HtmlRenderer}
         */
        public CommonMarkRenderer build() {
            return new CommonMarkRenderer(this);
        }
        
        public Builder setLineWidth(int n){
        	this.lineWidth = n;
        	return this;
        }
        
        public Builder setEmphasisMarkup(String open, String close){
        	this.openEmphasis = open;
        	this.closeEmphasis = close;
        	return this;
        }
        
        public Builder setStrongMarkup(String open, String close){
        	this.openStrong = open;
        	this.closeStrong = close;
        	return this;
        }
        
        public Builder setHighlighter(IHighlighter highlighter){
        	this.highlighter = highlighter;
        	return this;
        }

        /**
         * @param extensions extensions to use on this HTML renderer
         * @return {@code this}
         */
        public Builder extensions(Iterable<? extends Extension> extensions) {
            for (Extension extension : extensions) {
                if (extension instanceof CommonMarkRendererExtension) {
                    CommonMarkRendererExtension commonMarkRendererExtension = (CommonMarkRendererExtension) extension;
                    commonMarkRendererExtension.extend(this);
                }
            }
            return this;
        }
    }

    /**
     * Extension for {@link CommonMarkRenderer}.
     */
    public interface CommonMarkRendererExtension extends Extension {
        void extend(Builder rendererBuilder);
    }

    private class RendererVisitor extends AbstractVisitor {

        private final CommonMarkWriter cm;

        public RendererVisitor(CommonMarkWriter cm) {
            this.cm = cm;
        }

        @Override
        public void visit(Document document) {
            visitChildren(document);
        }

        @Override
        public void visit(Header header) {
        	cm.line();
        	if(openStrong.isEmpty()){
        	for(int i = 0; i < header.getLevel(); i++){
        		cm.append("#");
        	}
        	cm.append(" ");
            visitChildren(header);
            cm.line();
        	} else {
        		cm.line();
        		cm.append(openStrong);
        		visitChildren(header);
        		cm.append(closeStrong);
        		cm.line();
        	}
        }

        @Override
        public void visit(Paragraph paragraph) {
            visitChildren(paragraph);
        }

        @Override
        public void visit(BlockQuote blockQuote) {
        	cm.setInBlockQuote(true);
        	cm.line();
        	Node current = blockQuote.getFirstChild();
        	do {
        		current.accept(this);
        		current = current.getNext();
        		if(current == null){
        			cm.setInBlockQuote(false);
        		} else {
        			cm.line();
        		}
        	} while (current != null);
        	
        	cm.setInBlockQuote(false);
        	cm.line();
        }

        @Override
        public void visit(BulletList bulletList) {
        	cm.enterBulletList(bulletList.getBulletMarker());
            renderListBlock(bulletList, "ul", getAttrs(bulletList));
            cm.leaveBulletList();
        }

        @Override
        public void visit(FencedCodeBlock fencedCodeBlock) {
        	String info = fencedCodeBlock.getInfo();
            String literal = fencedCodeBlock.getLiteral();
           
            cm.line();
            String[] lines;
            if(highlighter == null){
            	cm.append("```");
            	if (info != null && !info.isEmpty()) {
            		cm.append(info);
            	}
            	lines = literal.split("\n");
            } else {
            	ISourceLocation src = ValueFactoryFactory.getValueFactory().sourceLocation("");
    			ITree parseTree = new RascalParser().parse(Parser.START_COMMANDS, src.getURI(), literal.toCharArray(), new NoActionExecutor() , new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));
    			
    			StringWriter sw = new StringWriter();
    			try {
					TreeAdapter.unparse(parseTree, true, sw);
				} catch (FactTypeUseException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			lines = sw.toString().split("\n");
            	//lines = highlighter.highlight(literal).split("\n");
            }
            cm.line();
            if(!literal.isEmpty()){
            	for(String line : lines){
            		cm.append(line);
            		cm.line();
            	}
            }
            if(highlighter == null){
            	cm.append("```");
            }
            cm.line();
        }

        @Override
        public void visit(HtmlBlock htmlBlock) {
            cm.line();
            cm.append(htmlBlock.getLiteral());
            cm.line();
        }

        @Override
        public void visit(HorizontalRule horizontalRule) {
            cm.line();
            cm.append("---");
            cm.line();
        }

        @Override
        public void visit(IndentedCodeBlock indentedCodeBlock) {
            renderCodeBlock(indentedCodeBlock.getLiteral(), getAttrs(indentedCodeBlock));
        }

        @Override
        public void visit(Link link) {
        	cm.line();
        	cm.append("[");
        	visitChildren(link);
        	cm.append("](");
        	cm.append(link.getDestination());
        	if (link.getTitle() != null) {
        		cm.append(" \"");
        		cm.append(link.getTitle());
        		cm.append("\"");
        	}
        	cm.append(")");
        }

        @Override
        public void visit(ListItem listItem) {
        	cm.insertListItem();
            visitChildren(listItem);
        }

        @Override
        public void visit(OrderedList orderedList) {
            int start = orderedList.getStartNumber();
            Map<String, String> attrs = new LinkedHashMap<>();
            if (start != 1) {
                attrs.put("start", String.valueOf(start));
            }
            cm.enterOrderedList(start,orderedList.getDelimiter());
            renderListBlock(orderedList, "ol", getAttrs(orderedList, attrs));
            cm.leaveOrderedList();
        }

        @Override
        public void visit(Image image) {
        	cm.append("![");
        	visitChildren(image);
        	cm.append("](");
        	cm.append(image.getDestination());
        	if (image.getTitle() != null) {
        		cm.append(" \"");
        		cm.append(image.getTitle());
        		cm.append("\"");
        	}
        	cm.append(")");
        }

        @Override
        public void visit(Emphasis emphasis) {
        	if(!openEmphasis.isEmpty()){
        		cm.append(openEmphasis);
        		visitChildren(emphasis);
        		cm.append(closeEmphasis);
        	} else {
        		cm.append("_");
        		visitChildren(emphasis);
        		cm.append("_");
        	}
        }

        @Override
        public void visit(StrongEmphasis strongEmphasis) {
        	if(!openStrong.isEmpty()){
        		cm.append(openStrong);
        		visitChildren(strongEmphasis);
        		cm.append(closeStrong);
        	} else {
        		cm.append("**");
        		visitChildren(strongEmphasis);
        		cm.append("**");
        	}
        }

        @Override
        public void visit(Text text) {
        	boolean atBoL = true;
        	String literal = text.getLiteral();
        	if(cm.available() >= literal.length()){
        		cm.append(literal);
        	} else {
        		String[] literals = literal.split(" ");
        		for(String l : literals){
        			if(cm.available() > l.length() + 1){
        				if(atBoL){
        					atBoL = false;
        				} else {
        					cm.append(" ");
        				}
        			} else {
        				cm.line();
        			}
        			cm.append(l);
        		}
        	}
        }

        @Override
        public void visit(Code code) {
            cm.append("`");
            cm.append(code.getLiteral());
            cm.append("`");
        }

        @Override
        public void visit(HtmlTag htmlTag) {
        	//cm.append("<");
        	cm.append(htmlTag.getLiteral());
        	//cm.append(">");
        }

        @Override
        public void visit(SoftLineBreak softLineBreak) {
        	cm.line();
        }

        @Override
        public void visit(HardLineBreak hardLineBreak) {
            cm.line();
        }

        @Override
        public void visit(CustomBlock customBlock) {
            renderCustom(customBlock);
        }

        @Override
        public void visit(CustomNode customNode) {
            renderCustom(customNode);
        }

        private void renderCustom(Node node) {
//            for (CustomCommonMarkRenderer customHtmlRenderer : customCommonMarkRenderers) {
//                // TODO: Should we pass attributes here?
//                boolean handled = customHtmlRenderer.render(node, cm, this);
//                if (handled) {
//                    break;
//                }
//            }
        }

        private void renderCodeBlock(String literal, Map<String, String> attributes) {
        	String[] lines = literal.split("\n");
        	for(String line : lines){
        		cm.append("    ");
        		cm.append(line);
        		cm.line();
        	}
        }

        private void renderListBlock(ListBlock listBlock, String tagName, Map<String, String> attributes) {
            visitChildren(listBlock);
        }

        private Map<String, String> getAttrs(Node node) {
            return getAttrs(node, Collections.<String, String>emptyMap());
        }

        private Map<String, String> getAttrs(Node node, Map<String, String> defaultAttributes) {
            Map<String, String> attrs = new LinkedHashMap<>(defaultAttributes);
            setCustomAttributes(node, attrs);
            return attrs;
        }

        private void setCustomAttributes(Node node, Map<String, String> attrs) {
            for (AttributeProvider attributeProvider : attributeProviders) {
                attributeProvider.setAttributes(node, attrs);
            }
        }
    }
}