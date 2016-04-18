package org.rascalmpl.library.experiments.tutor3.commonmark.ext;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.internal.TableBlockParser;
import org.commonmark.ext.gfm.tables.internal.TableHtmlRenderer;
import org.commonmark.html.HtmlRenderer;
import org.commonmark.html.HtmlRenderer.HtmlRendererExtension;
import org.commonmark.parser.Parser;
import org.commonmark.parser.Parser.ParserExtension;
import org.rascalmpl.library.experiments.tutor3.commonmark.AnsiRenderer;
import org.rascalmpl.library.experiments.tutor3.commonmark.AnsiRenderer.AnsiRendererExtension;

/**
 * Extension for GFM tables using "|" pipes (GitHub Flavored Markdown).
 * <p>
 * Create it with {@link #create()} and then configure it on the builders
 * ({@link org.commonmark.parser.Parser.Builder#extensions(Iterable)},
 * {@link org.commonmark.html.HtmlRenderer.Builder#extensions(Iterable)}).
 * </p>
 * <p>
 * The parsed tables are turned into {@link TableBlock} blocks.
 * </p>
 */
public class TablesWithAnsiExtension implements ParserExtension, HtmlRendererExtension, AnsiRendererExtension {

    private TablesWithAnsiExtension() {
    }

    public static Extension create() {
        return new TablesWithAnsiExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new TableBlockParser.Factory());
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder) {
        rendererBuilder.customHtmlRenderer(new TableHtmlRenderer());
    }
    
    @Override
    public void extend(AnsiRenderer.Builder rendererBuilder) {
        rendererBuilder.customAnsiRenderer(new TablesAnsiRenderer());
    }

}