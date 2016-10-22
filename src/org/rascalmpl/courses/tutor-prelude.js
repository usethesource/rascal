
$('.hole').each(visitHole);
$('.codequestion').each(visitCodeQuestion);
//$('.mchoice').each(visitMChoice);

function visitHole(index){
    console.log("transformHole: " + index);
    console.log("transformHole: " + $(this).children().html());
	var id = $( this ).attr('id');
	var len = $( this ).attr('length');
    var repl = $("<input type=\"text\" name=\"" + id + "\ value=\"" + " ".repeat(len) + "\">");
	$ (this).replaceWith(repl, $(this).children().html());
}

function visitCodeQuestion(index){
	var id = $( this ).attr('id');
    var listing = $( this ).attr('listing');
    var content = $( this ).html();
	console.log( "codeQuestion: " + index + ": " + id + " " + listing + "\ncontent:\n" + content);

	$( this ).html("<form action='/validateCodeQuestion' method='POST'>" 
		             + content
		             + "<input type='hidden' name='question' value='" + id + "'>"
		             + "<input type='hidden' name='listing' value='" + listing + "'>"
		             + "<input type='submit' value='Submit Answer'>"
		             + "</form>");
	$( this ).submit(function(){
		 console.log("submit " + id);
		 $.post($(this).attr("action"), $(this).serialize(), function(jsonData){
            console.log(jsonData);
         }, "json");
         return false;
	});
}

// function visitMChoice(index){

// }