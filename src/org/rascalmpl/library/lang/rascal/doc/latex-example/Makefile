.PHONY : FORCE_MAKE
MAINFILE=example # change the main name

# normally just build the latex file without running rascal to highlight the syntax
$(MAINFILE).pdf: FORCE_MAKE
	# offcourse you use latexmk to build latex...
	latexmk -pdf $(MAINFILE) 

# run make final to build the latex file but highlighted with
# make sure you have the rascal.jar console in the same directory
final: clean $(MAINFILE)-final.pdf

$(MAINFILE)-final.pdf: $(MAINFILE)_compiled.pdf
	mv $(MAINFILE)_compiled.pdf $(MAINFILE)-final.pdf

$(MAINFILE)_compiled.pdf: $(MAINFILE)_compiled.tex FORCE_MAKE
	latexmk -pdf -gg $(MAINFILE)_compiled 
	rm $(MAINFILE)_compiled.tex

$(MAINFILE)_compiled.tex: $(MAINFILE).ltx
	mv $(MAINFILE).ltx $(MAINFILE)_compiled.tex

$(MAINFILE).ltx: $(MAINFILE).tex
	java -jar rascal.jar -latex $(MAINFILE).tex

clean:
	rm -f *.log *.pdf *.ltx $(MAINFILE)_compiled.* *_latexmk
