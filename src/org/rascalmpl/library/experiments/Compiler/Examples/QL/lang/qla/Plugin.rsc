module experiments::Compiler::Examples::QL::lang::qla::Plugin

import experiments::Compiler::Examples::QL::lang::qla::Load;
import experiments::Compiler::Examples::QL::lang::qla::Parse;
import experiments::Compiler::Examples::QL::lang::qla::Resolve;
import experiments::Compiler::Examples::QL::lang::qla::Check;
import experiments::Compiler::Examples::QL::lang::qla::Outline;
import experiments::Compiler::Examples::QL::lang::qla::Compile;


import ParseTree;
import util::IDE;
import Message;
import IO;

private str QLA ="QL";

anno rel[loc,loc, str] Tree@hyperlinks;

rel[loc,loc,str] computeXRef(Info i) 
  = { <u, d, "<l>"> | u <- i.refs.use, d <- i.refs.use[u], 
                      l <- i.labels, d in i.labels[l] }; 

public void setupQL() {
  registerLanguage(QLA, "dql", Tree(str src, loc l) {
    return parseQL(src, l);
  });
  
  
  contribs = {
    outliner(node(Tree pt) {
      return outline(implodeQL(pt));
    }),
    
    annotator(Tree(Tree pt) {
      ast = implodeQL(pt);
      inf = resolve(ast);
      msgs = checkForm(ast, inf);
      return pt[@messages=msgs][@hyperlinks=computeXRef(inf)];
    }),
    
    builder(set[Message] (Tree pt) {
      ast = implodeQL(pt);
      msgs = checkForm(ast, resolve(ast));
      if (msgs == {}) {
        js = pt@\loc[extension="js"];
        writeFile(js, form2js(ast));
        html = pt@\loc[extension="html"];
        writeFile(html, form2html(ast, js));
      }
      return msgs;
    })
  };
  
  registerContributions(QLA, contribs);
}


