
/*
 * Prelude for Rascal Tutor
 */

$('.hole').each(visitHole);
$('.code-question').each(visitCodeQuestion);
$('.choice-question').each(visitChoiceQuestion);
$('.click-question').each(visitClickQuestion);
$('.move-question').each(visitMoveQuestion);
$('.fact-question').each(visitFactQuestion);

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
           + "<div id ='" + idFeedback + "''> </div>"
           + "<br>";
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
                     + "</form><br>");
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
                     + "</form><br>");
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
                     + "</form><br>");
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

// MoveQuestion

var deltax = 20;
var deltay  = 10;

function visitMoveQuestion(index){
    var id = $( this ).attr('id');
    var idGood = id + "-good";
    var idBad = id + "-bad"
    var idForm = id + "-form";
    var idFeedback = id + "-feedback";
    var content = $( this ).html();

    $( this ).html("<form id='" + idForm + "' class='movable-code-form'>" 
                     + content
                     + "<br>"
                     + submitCode(idGood, idBad, idFeedback)
                     + "</form><br>");
    $( this ).submit(function(event){
        validateMoveQuestion(id,idGood,idBad,idFeedback)
        return false;
     });

    // Custom grid
    $("#" + id + " .movable-code").draggable({
        drag: function( event, ui ) {
            var snapTolerance = $(this).draggable('option', 'snapTolerance');
            var topRemainder = ui.position.top % deltay;
            var leftRemainder = ui.position.left % deltax;
            
            if (topRemainder <= snapTolerance) {
                ui.position.top = ui.position.top - topRemainder;
            }
            
            if (leftRemainder <= snapTolerance) {
                ui.position.left = ui.position.left - leftRemainder;
            }
        },
        containment: "parent"
      });

    

    // $(".movable-code-form").submit(function(event){
    //         validateMovedCode(event);
    //         return false;
    //      });
}

function inside(inner, outer){
  return inner.left > outer.left &&
         inner.top > outer.top &&
         inner.right < outer.right &&
         inner.bottom < outer.bottom;
}

function above(upper, lower){
  return upper.bottom < lower.top;
}

function near(y1, y2){
    return Math.abs(y1 - y2) < 3;
}

function compare(r1, r2){
  return r1.top == r2.top ? 0 : (r1.bottom < r2.top) ? -1 : 1;
}

function insideTarget(target, elem){
  var targetRect = target.getBoundingClientRect();
  var elemRect = elem.getBoundingClientRect();

  return inside(elemRect, targetRect);
}

function validateIndent(b1, b2){
  var b1Rect = b1.getBoundingClientRect();
  var b2Rect = b2.getBoundingClientRect();
  var indent1 = parseInt($(b1).attr("indent"));
  var indent2 = parseInt($(b2).attr("indent"));
  return b1Rect.left - b2Rect.left == (indent1 - indent2) * deltax;
}

function validateMoveQuestion(id, idGood, idBad, idFeedback){

  event.preventDefault();
  $("#" + idBad).hide(100);
  $("#" + idGood).hide(100);
  $("#" + idFeedback).hide(100);
  var mq = event.target.closest(".move-question");

  var boxes = $(mq).find(".movable-code");
  boxes.each(function(index,box){ $(box).attr("placement", "none"); });

  target = $(mq).find(".movable-code-target").get(0);
  var insideboxes = boxes.filter(function(index, elem) { return insideTarget(target, elem); });
  
  insideboxes = insideboxes.sort(function(x, y) { 
    return compare(x.getBoundingClientRect(), 
                   y.getBoundingClientRect());
  });
 
  var decoy = 0;
  var wrong_placement = 0;
  var wrong_indent = 0;
  insideboxes.each(function(index, box){

    var indexBox = parseInt($(box).attr("index"));
    console.log("indexBox", indexBox, box);
    
    if(indexBox >= 0){
      var indentOK =  index == 0 || validateIndent(insideboxes.get(index-1), box);
      var indexOK = index == indexBox;
      if(!indentOK){
        wrong_indent += 1;
      }
      if(!indexOK){
        wrong_placement += 1;
      }
      $(box).attr("placement", indexOK ? (indentOK ? "correct" : "wrong-indent") : "wrong");
    } else {
      $(box).attr("placement", "wrong");
      decoy += 1;
    }
  });
  var missing = 0;
  boxes.each(function(index, box){
    if($(box).attr("placement") === "none" && parseInt($(box).attr("index")) >= 0){
       missing += 1;
    }
  });
  if(decoy > 0 || wrong_placement > 0 || wrong_indent > 0 || missing > 0 ){
    $("#" + idBad).show(100);
    var msg = "";
    if(decoy > 0){
        msg += incorrect_fragments(decoy, "decoy");
    } else
    if(missing){
        msg += incorrect_fragments(missing, "missing");
    } else 
    if(wrong_placement > 0){
        msg += incorrect_fragments(wrong_placement, "incorrectly placed");
    } else
    if(wrong_indent > 0){
        msg += incorrect_fragments(wrong_indent, "incorrectly indented");
    }
    
    $("#" + idFeedback).html("<p><i>" + msg + "</i></p>");
    $("#" + idFeedback).show(100);
  } else {
     $("#" + idGood).show(100);
  } 
}

function incorrect_fragments(n, msg){
    return n + " " + msg + " fragment" + (n == 1 ? "" : "s");
}

// ---- FactQuestion

function visitFactQuestion(index){
    var id = $( this ).attr('id');
    var idGood = id + "-good";
    var idBad = id + "-bad"
    var idForm = id + "-form";
    var idFeedback = id + "-feedback";
    var content = $( this ).html();
    var $this = $( this );

    $( this ).html("<form id='" + idForm + "'>" 
                     + content
                     + "<br>"
                     + submitCode(idGood, idBad, idFeedback)
                     + "</form><br>");

    $( ".sortableLeft" ).sortable({
      containment: "parent"
    }).disableSelection();
    $( ".sortableRight" ).sortable({
      containment: "parent"
    }).disableSelection();

    $(this).find("li").addClass("ui-state-default");
    $( this ).submit(function(event){
        validateFactQuestion(id,idGood,idBad,idFeedback)
        return false;
     });
}

function validateFactQuestion(id, idGood, idBad, idFeedback){
  event.preventDefault();
  $("#" + idBad).hide(100);
  $("#" + idGood).hide(100);
  $("#" + idFeedback).hide(100);
  var fq = event.target.closest(".fact-question");

  var leftItems = $(fq).find(".sortableLeft li");
  var rightItems = $(fq).find(".sortableRight li");
  var wrong = 0;
  if(leftItems.length == rightItems.length){
    leftItems.each(function(index, item){
         var indexLeft = parseInt($(item).attr("index"));
         var indexRight = parseInt($(rightItems.get(index)).attr("index"));
         if(indexLeft == indexRight){
            $(item).attr("placement", "correct");
            $(rightItems.get(index)).attr("placement", "correct");
         } else {
            $(item).attr("placement", "wrong");
            $(rightItems.get(index)).attr("placement", "wrong");
            wrong += 1;
         }
    });
  }
  if(wrong > 0){
    $("#" + idBad).show(100);
  } else {
     $("#" + idGood).show(100);
  }

}
