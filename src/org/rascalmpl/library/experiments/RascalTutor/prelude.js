$(document).ready(function(){
  attachHandlers();
});

function attachHandlers(){
  $('#searchField').keyup(searchSuggest);
  $('.answerForm').submit(handleAnswer);
  $('.cheatForm').submit(handleCheat);
  $('.anotherForm').submit(handleAnother);
  $('.categoryButton').click(categoryClick);

  $('.answerStatus').hide();
  $('.answerFeedback').hide();
  
  $('#editErrors').hide();
  $('#editForm').submit(handleSave);
}

function reload(data){
  $('body').html(data);
  attachHandlers();
}

// ------------ Handler for suggestions for searchBox -------------------

function searchSuggest() {
	var str = $(this).val();

	if (str != "") {
		$('#popups').html("");
		for (var i=0; i<baseConcepts.length; i++) {
			var thisConcept = baseConcepts[i];
	
			if (thisConcept.toLowerCase().indexOf(str.toLowerCase()) == 0) {
				$('#popups').append('<div class="suggestions">' + thisConcept + '</div>');
			}
		}
        var foundCt = $('#popups').children().length;
		if (foundCt == 0) {
			$(this).addClass("error");
		}
		/*if (foundCt == 1) {
			$(this).text($('#popups')[0]);
			$('#popups').html("");
            $('#searchForm').submit();
		} else { */
            $('#popups').children().each(function(){$(this).click(makeChoice);});
        /*}*/
	}
}

// ------------ Handler for making a choice from the suggestions for the searchBox 

function makeChoice() {
   	$('#searchField').val($(this).text());
	$('#popups').html("");
    $('#searchForm').submit();
}

// ------------ Handler for category selection

function categoryClick(){
  var formData="";
  
  $(".categoryButton").each(function(){
    var button = $(this);
    formData += (button.attr("name") + "=" + button.is(":checked") + "&");
   });
   
  var cn = $('#categoryForm input[name=concept]').attr("value");
  formData += "concept=" + cn;
  $.get("category", formData,
    function processCategorySelectionResult(data, textStatus){
     reload(data);
   });
}

// ------------ Handler for answers to exercises

function handleAnswer(evt){
  var formData = $(this).serialize();
  evt.preventDefault();
  $.get("validate", formData, 
    function processValidationResult(data, textStatus){
  
         //alert("processValidationResult: " + data);
         var v = $('#validation', data).text();
         var c = $('#concept', data).text();
         var e = $('#exercise', data).text();
         var fb = $('#feedback', data).text();
         //alert("v = " + v + "; c = " + c  + "; e = " + e + "; fb = " + fb);
         
         $("#" + e + "bad").fadeOut(1000);
         $("#" + e + "good").fadeOut(1000); 
             
         if(v == "true"){
            $("#" + e + "good").fadeIn();
         } else {
            $("#" + e + "bad").fadeIn();
         }
         $("#answerFeedback" + e).fadeOut(1000, function(){
            if(fb != ""){
                $("#answerFeedback" + e).html(fb);
                $("#answerFeedback" + e).fadeIn(1000);
                }
            });
    });

 return false;
}

// ------------ Handler for "cheat" requests

function handleCheat(evt){
  var formData = $(this).serialize();
  evt.preventDefault();
  $.get("validate", formData, 
    function processCheatResult(data, textStatus){
         var c = $('#concept', data).text();
         var e = $('#exercise', data).text();
         var cheat = $('#feedback', data).text();
         $("#answerFeedback" + e).html(cheat);
         $("#answerFeedback" + e).fadeIn(1000);
    });

 return false;
}

// ------------ Handler for "another" requests

function handleAnother(evt){
  //alert("handleAnother");
  var formData = $(this).serialize();
  evt.preventDefault();
  $.get("validate", formData, 
    function processAnotherResult(data, textStatus){
         //alert("processAnotherResult: " + data);
         var c = $('#concept', data).text();
         var e = $('#exercise', data).text();
         var another = $('#another', data).text();
         //alert("c = " + c  + "; e = " + e + "; another=" + another.substring(0,20));
         $("#" + e).fadeOut(1000, function(){
            $("#" + e).html(another);
            $("#" + e + ' .answerStatus').hide();
            $("#" + e + ' .answerFeedback').hide();
         
            $("#" + e + ' .answerForm').submit(handleAnswer);
            $("#" + e + ' .cheatForm').submit(handleCheat);
            $("#" + e + ' .anotherForm').submit(handleAnother);
             $("#" + e).show();
         });
        
    });

 return false;
}

// ------------ Handle "save" request while editing

function handleSave(evt){
  var formData = $(this).serialize();
  //alert("handleSave: " + formData);
  evt.preventDefault();
  $.get("save", formData, 
    function processSaveFeedback(data, textStatus){
     var c = $('#concept', data).text();
     var e = $('#error', data).text();
     var r = $('#replacement', data).text();
     //alert("c = " + c + "; e = " + e);
     if(e != ""){
        $('#editErrors').html("<img height=\"25\" width=\"25\" src=\"images/bad.png\">Correct error: " + e);
        $('#editErrors').fadeIn(500);
     } else
        reload(r);
    });
  return false;
}

