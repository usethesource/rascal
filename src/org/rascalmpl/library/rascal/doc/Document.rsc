module rascal::doc::Document

start syntax Document
	= Chunk*
	;

layout WS
	=
	;

syntax Chunk
	= Snippet
	| Water
	;

syntax Water
	= ...
	;

syntax Block
	= Begin Content End
	;

syntax Inline
	= IBegin Content IEnd
	;

syntax Snippet
	= Block
	| Inline
	;


public str expand(Document doc, loc l, str(Tree, loc) formatBlock, str(Tree, loc) formatInline) {
	result = "";
	top-down-break visit (doc) {
		case Block b: result += formatBlock(b, l);
		case Inline i: result += formatInline(i, l);
		case Water w: result += "<w>";
	}
	return result;
}

