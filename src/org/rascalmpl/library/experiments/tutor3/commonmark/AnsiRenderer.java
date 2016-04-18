package org.rascalmpl.library.experiments.tutor3.commonmark;


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
import org.commonmark.node.Heading;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
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
import org.commonmark.node.ThematicBreak;
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
 * Renders a tree of nodes back to Ansi text.
 * <p>
 * Start with the {@link #builder} method to configure the renderer. Example:
 * <pre><code>
 * AnsiRenderer renderer = AnsiRenderer.builder().build();
 * renderer.render(node);
 * </code></pre>
 */
public class AnsiRenderer {

	private final List<CustomAnsiRenderer> customAnsiRenderers;
    private final List<AttributeProvider> attributeProviders;
    
    private int lineWidth = 80;
	
	private String openEmphasis = "";
	private String closeEmphasis = "";
	
	private String openStrong = "";
	private String closeStrong = "";
	
	private IHighlighter highlighter;

    private AnsiRenderer(Builder builder) {
        this.attributeProviders = builder.attributeProviders;
        
        this.lineWidth = builder.lineWidth;
        
        this.openEmphasis = builder.openEmphasis;
        this.closeEmphasis = builder.closeEmphasis;
        
        this.openStrong = builder.openStrong;
        this.closeStrong = builder.closeStrong;
        
        this.highlighter = builder.highlighter;
        this.customAnsiRenderers = builder.customAnsiRenderers;
    }

    /**
     * Create a new builder for configuring an {@link AnsiRenderer}.
     *
     * @return a builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public void render(Node node, Appendable output) {
        RendererVisitor rendererVisitor = new RendererVisitor(new AnsiWriter(output, lineWidth));
        node.accept(rendererVisitor);
    }

    /**
     * Render the tree of nodes to Ansi.
     * @param node the root node
     * @return the rendered CommonMark as Ansi text
     */
    public String render(Node node) {
        StringBuilder sb = new StringBuilder();
        render(node, sb);
        return sb.toString();
    }

    /**
     * Builder for configuring an {@link AnsiRenderer}. See methods for default configuration.
     */
    public static class Builder {

    	private List<CustomAnsiRenderer> customAnsiRenderers = new ArrayList<>();
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
        public AnsiRenderer build() {
            return new AnsiRenderer(this);
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
        
        public Builder customAnsiRenderer(CustomAnsiRenderer customAnsiRenderer) {
            this.customAnsiRenderers.add(customAnsiRenderer);
            return this;
        }

        /**
         * @param extensions extensions to use on this Ansi renderer
         * @return {@code this}
         */
        public Builder extensions(Iterable<? extends Extension> extensions) {
            for (Extension extension : extensions) {
                if (extension instanceof AnsiRendererExtension) {
                    AnsiRendererExtension ansiRendererExtension = (AnsiRendererExtension) extension;
                    ansiRendererExtension.extend(this);
                }
            }
            return this;
        }
    }

    /**
     * Extension for {@link AnsiRenderer}.
     */
    public interface AnsiRendererExtension extends Extension {
        void extend(Builder rendererBuilder);
    }

    private class RendererVisitor extends AbstractVisitor {

        private final AnsiWriter ansi
        ;

        public RendererVisitor(AnsiWriter ansi) {
            this.ansi = ansi;
        }

        @Override
        public void visit(Document document) {
            visitChildren(document);
        }

        @Override
        public void visit(Heading heading) {
        	ansi.line();
        	if(openStrong.isEmpty()){
        	for(int i = 0; i < heading.getLevel(); i++){
        		ansi.append("#");
        	}
        	ansi.append(" ");
            visitChildren(heading);
            ansi.line();
        	} else {
        		ansi.line();
        		ansi.append(openStrong);
        		visitChildren(heading);
        		ansi.append(closeStrong);
        		ansi.line();
        	}
        }

        @Override
        public void visit(Paragraph paragraph) {
            visitChildren(paragraph);
        }

        @Override
        public void visit(BlockQuote blockQuote) {
        	ansi.setInBlockQuote(true);
        	ansi.line();
        	Node current = blockQuote.getFirstChild();
        	do {
        		current.accept(this);
        		current = current.getNext();
        		if(current == null){
        			ansi.setInBlockQuote(false);
        		} else {
        			ansi.line();
        		}
        	} while (current != null);
        	
        	ansi.setInBlockQuote(false);
        	ansi.line();
        	ansi.line();
        }

        @Override
        public void visit(BulletList bulletList) {
        	ansi.enterBulletList(bulletList.getBulletMarker());
            renderListBlock(bulletList, "ul", getAttrs(bulletList));
            ansi.leaveBulletList();
        }

        @Override
        public void visit(FencedCodeBlock fencedCodeBlock) {
        	String info = fencedCodeBlock.getInfo();
            String literal = fencedCodeBlock.getLiteral();
           
            ansi.line();
            String[] lines;
            if(highlighter == null){
            	ansi.append("```");
            	if (info != null && !info.isEmpty()) {
            		info = info.replaceFirst("-continue", "");
            		info = info.replaceFirst("-errors", "");
            		ansi.append(info);
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
            ansi.line();
            if(!literal.isEmpty()){
            	for(String line : lines){
            		ansi.append(line);
            		ansi.line();
            	}
            }
            if(highlighter == null){
            	ansi.append("```");
            }
            ansi.line();
        }

        @Override
        public void visit(HtmlBlock htmlBlock) {
            ansi.line();
            ansi.append(htmlBlock.getLiteral());
            ansi.line();
        }

        @Override
        public void visit(ThematicBreak thematicBreak) {
            ansi.line();
            ansi.append("---");
            ansi.line();
        }

        @Override
        public void visit(IndentedCodeBlock indentedCodeBlock) {
            renderCodeBlock(indentedCodeBlock.getLiteral(), getAttrs(indentedCodeBlock));
        }

        @Override
        public void visit(Link link) {
        	ansi.append("[");
        	visitChildren(link);
        	ansi.append("](");
        	ansi.append(link.getDestination());
        	if (link.getTitle() != null) {
        		ansi.append(" \"");
        		ansi.append(link.getTitle());
        		ansi.append("\"");
        	}
        	ansi.append(")");
        }

        @Override
        public void visit(ListItem listItem) {
        	ansi.insertListItem();
            visitChildren(listItem);
        }

        @Override
        public void visit(OrderedList orderedList) {
            int start = orderedList.getStartNumber();
            Map<String, String> attrs = new LinkedHashMap<>();
            if (start != 1) {
                attrs.put("start", String.valueOf(start));
            }
            ansi.enterOrderedList(start, orderedList.getDelimiter());
            renderListBlock(orderedList, "ol", getAttrs(orderedList, attrs));
            ansi.leaveOrderedList();
        }

        @Override
        public void visit(Image image) {
        	ansi.append("![");
        	visitChildren(image);
        	ansi.append("](");
        	ansi.append(image.getDestination());
        	if (image.getTitle() != null) {
        		ansi.append(" \"");
        		ansi.append(image.getTitle());
        		ansi.append("\"");
        	}
        	ansi.append(")");
        }

        @Override
        public void visit(Emphasis emphasis) {
        	if(!openEmphasis.isEmpty()){
        		ansi.append(openEmphasis);
        		visitChildren(emphasis);
        		ansi.append(closeEmphasis);
        	} else {
        		ansi.append("_");
        		visitChildren(emphasis);
        		ansi.append("_");
        	}
        }

        @Override
        public void visit(StrongEmphasis strongEmphasis) {
        	if(!openStrong.isEmpty()){
        		ansi.append(openStrong);
        		visitChildren(strongEmphasis);
        		ansi.append(closeStrong);
        	} else {
        		ansi.append("**");
        		visitChildren(strongEmphasis);
        		ansi.append("**");
        	}
        }

        @Override
        public void visit(Text text) {
        	boolean atBoL = true;
        	String literal = text.getLiteral();
        	if(ansi.available() >= literal.length()){
        		ansi.append(literal);
        	} else {
        		String[] literals = literal.split(" ");
        		for(String l : literals){
        			if(ansi.available() > l.length() + 1){
        				if(atBoL){
        					atBoL = false;
        				} else {
        					ansi.append(" ");
        				}
        			} else {
        				ansi.line();
        			}
        			ansi.append(l);
        		}
        	}
        }

        @Override
        public void visit(Code code) {
            ansi.append("`");
            ansi.append(code.getLiteral());
            ansi.append("`");
        }

        @Override
        public void visit(HtmlInline htmlInline) {
        	ansi.append("<");
        	ansi.append(htmlInline.getLiteral());
        	ansi.append(">");
        }

        @Override
        public void visit(SoftLineBreak softLineBreak) {
        	ansi.line();
        }

        @Override
        public void visit(HardLineBreak hardLineBreak) {
            ansi.line();
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
            for (CustomAnsiRenderer customAnsiRenderer : customAnsiRenderers) {
                // TODO: Should we pass attributes here?
                boolean handled = customAnsiRenderer.render(node, ansi, this);
                if (handled) {
                    break;
                }
            }
        }

        private void renderCodeBlock(String literal, Map<String, String> attributes) {
        	String[] lines = literal.split("\n");
        	for(String line : lines){
        		ansi.append("    ");
        		ansi.append(line);
        		ansi.line();
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