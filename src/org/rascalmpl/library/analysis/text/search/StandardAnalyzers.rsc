module analysis::text::search::StandardAnalyzers

extend analysis::text::search::Lucene;

Analyzer arabic() = analyzerClass("org.apache.lucene.analysis.ar.ArabicAnalyzer");
Analyzer armenian() = analyzerClass("org.apache.lucene.analysis.hy.ArmenianAnalyzer");
Analyzer basque() = analyzerClass("org.apache.lucene.analysis.eu.BasqueAnalyzer");
Analyzer bengali() = analyzerClass("org.apache.lucene.analysis.bn.BengaliAnalyzer");
Analyzer brazilian() = analyzerClass("org.apache.lucene.analysis.br.BrazilianAnalyzer");
Analyzer bulgarian() = analyzerClass("org.apache.lucene.analysis.bg.BulgarianAnalyzer");
Analyzer catalan() = analyzerClass("org.apache.lucene.analysis.ca.CatalanAnalyzer");
Analyzer czech() = analyzerClass("org.apache.lucene.analysis.cz.CzechAnalyzer");
Analyzer danish() = analyzerClass("org.apache.lucene.analysis.da.DanishAnalyzer");
Analyzer dutch() = analyzerClass("org.apache.lucene.analysis.nl.DutchAnalyzer");
Analyzer english() = analyzerClass("org.apache.lucene.analysis.en.EnglishAnalyzer");
Analyzer finnish() = analyzerClass("org.apache.lucene.analysis.fi.FinnishAnalyzer");
Analyzer galician() = analyzerClass("org.apache.lucene.analysis.gl.GalicianAnalyzer");
Analyzer german() = analyzerClass("org.apache.lucene.analysis.de.GermanAnalyzer");
Analyzer greek() = analyzerClass("org.apache.lucene.analysis.el.GreekAnalyzer");
Analyzer hindi() = analyzerClass("org.apache.lucene.analysis.hi.HindiAnalyzer");
Analyzer hungarian() = analyzerClass("org.apache.lucene.analysis.hu.HungarianAnalyzer");
Analyzer indonesian() = analyzerClass("org.apache.lucene.analysis.id.IndonesianAnalyzer");
Analyzer irish() = analyzerClass("org.apache.lucene.analysis.ga.IrishAnalyzer");
Analyzer italian() = analyzerClass("org.apache.lucene.analysis.it.ItalianAnalyzer");
Analyzer latvian() = analyzerClass("org.apache.lucene.analysis.lv.LatvianAnalyzer");
Analyzer lithuanian() = analyzerClass("org.apache.lucene.analysis.lt.LithuanianAnalyzer");
Analyzer norwegian() = analyzerClass("org.apache.lucene.analysis.no.NorwegianAnalyzer");
Analyzer persian() = analyzerClass("org.apache.lucene.analysis.fa.PersianAnalyzer");
Analyzer portugese() = analyzerClass("org.apache.lucene.analysis.pt.PortugeseAnalyzer");
Analyzer romanian() = analyzerClass("org.apache.lucene.analysis.ro.RomanianAnalyzer");
Analyzer russian() = analyzerClass("org.apache.lucene.analysis.ru.RussianAnalyzer");
Analyzer sorani() = analyzerClass("org.apache.lucene.analysis.chb.SoraniAnalyzer");
Analyzer spanish() = analyzerClass("org.apache.lucene.analysis.es.SpanishAnalyzer");
Analyzer swedish() = analyzerClass("org.apache.lucene.analysis.sv.SwedishAnalyzer");
Analyzer thai() = analyzerClass("org.apache.lucene.analysis.th.ThaiAnalyzer");
Analyzer turkish() = analyzerClass("org.apache.lucene.analysis.tr.TurkishAnalyzer");

Analyzer cjkAnalyzer() = analyzerClass("org.apache.lucene.analysis.cjk.CJKAnalyzer");
Analyzer stopAnalyzer() = analyzerClass("org.apache.lucene.analysis.core.StopAnalyzer");
Analyzer keywordAnalyzer() = analyzerClass("org.apache.lucene.analysis.core.KeywordAnalyzer");
Analyzer unicodeWhitespaceAnalyzer() = analyzerClass("org.apache.lucene.analysis.core.UnicodeWhitespaceAnalyzer");
Analyzer emailAnalyzer() = analyzerClass("org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer");
