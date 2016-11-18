
/*
 * Prelude for Rascal Tutor
 */

$('.hole').each(visitHole);
$('.code-question').each(visitCodeQuestion);
$('.choice-question').each(visitChoiceQuestion);
$('.click-question').each(visitClickQuestion);

// hole

function visitHole(index){
    var id = $( this ).attr('id');
    var len = $( this ).attr('length');
    var repl = $('<input type="text" name="' + id + '"\ size="' + len + '">');
    $ (this).replaceWith(repl, $(this).children().html());
}

// click

function handleClick(id){
    $("#" + id).attr('clicked', true);
    return false;
}

// CodeQuestion

function submitCode(idGood, idBad, idFeedback){
    return "<input type='submit' value='Submit Answer'>"
           + "<img id ='" + idGood + "' height='25' width='25' src='/images/good.png' style='display:none;'/>"
           + "<img id ='" + idBad  + "' height='25' width='25' src='/images/bad.png' style='display:none;'/>"
           + "<div id ='" + idFeedback + "''> </>";
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
   
    $( this ).html("<form id='" + idForm + "' action='/ValidateCodeQuestion' method='POST'>" 
                     + content
                     + "<input type='hidden' name='question' value='" + id + "'>"
                     + "<input type='hidden' name='listing' value='" + listing + "'>"
                     + submitCode(idGood, idBad, idFeedback)
                     + "</form>");
    $( this ).submit(function(event){
         event.preventDefault();
         $("#" + idBad).hide(100);
         $("#" + idGood).hide(100);
         $("#" + idFeedback).hide(100);
         $("#" + idFeedback).html("");
         $.post("/ValidateCodeQuestion", 
                $( "#" + idForm ).serialize(), 
                function(jsonData,status,jqXHR){
                    var msgs = "";
                    var feedback = jsonData.feedback;
                    if(jsonData.ok == true){
                        if(feedback != null){
                            $("#" + idFeedback).html("<i>" + feedback + "</i>");
                            $("#" + idFeedback).show(100);
                        }
                        $("#" + idGood).show(100);
                    } else {
                        $("#" + idBad).show(100);
                        var failed = jsonData.failed;
                        var exceptions = jsonData.exceptions;
                        var syntax = jsonData.syntax;
                        
                        if(syntax != null){
                          if(syntax.beginLine == syntax.endLine){
                            msgs = "Syntax error near line " + syntax.beginLine + ", column " + syntax.beginColumn;
                          } else {
                            msgs = "Syntax error at lines " + syntax.beginLine + "-" + syntax.endLine;
                          }
                        } else if(failed.length != 0){
                            if(failed.length == 1){
                                msgs = "Test failed (near line " + failed[0].src.beginLine + ")";
                                if(failed[0].msg != ""){
                                   msgs += ": " + failed[0].msg;
                                }
                                msgs += ".";
                            } else {
                              msgs = "Tests failed: <ul>";
                              for(var i = 0; i < failed.length; i++){
                                msgs += "<li>Near line "+ failed[i].src.beginLine;
                                if(failed[i].msg != ""){
                                   msgs += ": " + failed[i].msg + ".";
                                }
                                msgs += "</li>"
                              }  
                              msgs += "</ul>"
                            }
                        } else if(exceptions.length != 0){
                            msgs = "exception occurred: ";
                            for(var i = 0; i < exceptions.length; i++){
                              msgs += " " + exceptions[i];
                           }
                        }
                        if(feedback != null){
                            msgs += "\n<i>" + feedback + "</i>";
                        }
                        $("#" + idFeedback).html("<p>" + msgs + "</p>");
                        $("#" + idFeedback).show(100);
                    }
                }, 
                "json");
         return false;
    });
}

// ChoiceQuestion

function visitChoiceQuestion(index){
    var id = $( this ).attr('id');
    var idGood = id + "-good";
    var idBad = id + "-bad"
    var idForm = id + "-form";
    var idFeedback = id + "-feedback";
    var content = $( this ).html();
    $( this ).html("<form id='" + idForm + "'>" 
                     + content
                     + submitCode(idGood, idBad, idFeedback)
                     + "</form>");
    $( this ).submit(function(event){
        validateChoiceQuestion(id,idGood,idBad,idFeedback)
        return false;
     });
}

function validateChoiceQuestion(id, idGood, idBad, idFeedback){
    event.preventDefault();
    $("#" + idBad).hide(100);
    $("#" + idGood).hide(100);
    $("#" + idFeedback).hide(100);
    var checked = $('input[name=' + id + ']:checked');
   
    if(checked.val() === "yes"){
        $("#" + idGood).show(100);
    } else {
        $("#" + idBad).show(100);
    }
    var fb = checked.attr("feedback");
    $("#" + idFeedback).html("<p><i>" + fb + "</i></p>");
    $("#" + idFeedback).show(100);
}

// ClickQuestion

function visitClickQuestion(index){
    var id = $( this ).attr('id');
    var idGood = id + "-good";
    var idBad = id + "-bad"
    var idForm = id + "-form";
    var idFeedback = id + "-feedback";
    var content = $( this ).html();
    $( this ).html("<form id='" + idForm + "'>" 
                     + content
                     + submitCode(idGood, idBad, idFeedback)
                     + "</form>");
    $( this ).submit(function(event){
        validateClickQuestion(id,idGood,idBad,idFeedback)
        return false;
     });
}

function validateClickQuestion(id, idGood, idBad, idFeedback){
    event.preventDefault();
    $("#" + idBad).hide(100);
    $("#" + idGood).hide(100);
    $("#" + idFeedback).hide(100);
    var missed = 0;
    $("#" + id + " .clickable").each(function(index, object) { if($(object).attr('clicked') != "true") { missed++; }});
   
    if(missed == 0){
        $("#" + idGood).show(100);
    } else {
        $("#" + idBad).show(100);
        var msg = missed == 1 ? "You missed 1 click" : "You missed " + missed + " clicks.";
        $("#" + idFeedback).html("<p><i>" + msg + "</i></p>");
        $("#" + idFeedback).show(100);
    }
}