
$('.hole').each(visitHole);
$('.codequestion').each(visitCodeQuestion);
//$('.mchoice').each(visitMChoice);

function visitHole(index){
    console.log("transformHole: " + index);
    console.log("transformHole: " + $(this).children().html());
	var id = $( this ).attr('id');
	var len = $( this ).attr('length');
    var repl = $('<input type="text" name="' + id + '"\ value="' + ' '.repeat(len) + '">');
	$ (this).replaceWith(repl, $(this).children().html());
}

function visitCodeQuestion(index){
	var id = $( this ).attr('id');
	var idGood = id + "-good";
	var idBad = id + "-bad"
	var idForm = id + "-form";
	var idFeedback = id + "-feedback";
    var listing = $( this ).attr('listing');
    var content = $( this ).html();
    var $this = $( this );
	console.log( "codeQuestion: " + index + ": " + id + " " + listing + "\ncontent:\n" + content);
   
	$( this ).html("<form id='" + idForm + "' action='/ValidateCodeQuestion' method='POST'>" 
		             + content
		             + "<input type='hidden' name='question' value='" + id + "'>"
		             + "<input type='hidden' name='listing' value='" + listing + "'>"
		             + "<input type='submit' value='Submit Answer'>"
		             + "<img id ='" + idGood + "' height='25' width='25' src='/images/good.png' style='display:none;'/>"
		             + "<img id ='" + idBad  + "' height='25' width='25' src='/images/bad.png' style='display:none;'/>"
		             + "<div id ='" + idFeedback + "'/>"
		             + "</form>");
	$( this ).submit(function(event){
		 event.preventDefault();
		 $("#" + idBad).hide(100);
		 $("#" + idGood).hide(100);
		 $("#" + idFeedback).hide(100);
		 $("#" + idFeedback).html();
		 $.post("/ValidateCodeQuestion", 
		 	    $( "#" + idForm ).serialize(), 
		 	    function(jsonData,status,jqXHR){
            		if(jsonData.ok == true){
            			$("#" + idGood).show(100);
            		} else {
						$("#" + idBad).show(100);
						var failed = jsonData.failed;
						var exceptions = jsonData.exceptions;
						var syntax = jsonData.syntax;
						var feedback = "";

						if(syntax != null){
						  if(syntax.beginLine == syntax.endLine){
						  	feedback = "Syntax error at line " + syntax.beginLine + ", column " + syntax.beginColumn;
						  } else {
                            feedback = "Syntax error at lines " + syntax.beginLine + "-" + syntax.endLine;
                          }
						} else if(failed.length != 0){
							if(failed.length == 1){
								feedback = "Test failed (line " + failed[0].src.beginLine + ")";
								if(failed[0].msg != ""){
						   		   feedback += ": " + failed[0].msg;
						   		}
						   		feedback += ".";
							} else {
						      feedback = "Tests failed: <ul>";
						      for(var i = 0; i < failed.length; i++){
						   	    feedback += "<li>At line "+ failed[i].src.beginLine;
						   	    if(failed[i].msg != ""){
						   		   feedback += ": " + failed[i].msg + ".";
						   	    }
						   	    feedback += "</li>"
						      }  
						      feedback += "</ul>"
						    }
						} else if(exceptions.length != 0){
							feedback = "exception occurred: ";
							for(var i = 0; i < exceptions.length; i++){
						   	  feedback += " " + exceptions[i];
						   }
						}
						$("#" + idFeedback).html("<p>" + feedback + "</p>");
						$("#" + idFeedback).show(100);
            		}
         		}, 
         		"json");
         return false;
	});
}

// function visitMChoice(index){

// }