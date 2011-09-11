var navigation_loaded = false;
var rootConcept = "Test";

$.setRootConcept = function (c){ rootConcept = c; };

$(document).ready(function(){

  if(navigation_loaded != true){
  	 $("#navPane").load("/Courses/" + rootConcept + "/navigate.html", null, function(a,b,c){initNavigation();});
  }
  attachHandlers();
});

function initNavigation(){
//alert("initNavigation");
 
 
 $("#navPane").bind("select_node.jstree", function(event, data) {
         var url = $(data.args[0]).attr("href") + " #conceptPane";
         $("#conceptPane").load(url, null, function (a,b,c) {attachHandlers();});
      }).jstree({
		"core" : {
			"animation" : 100
		},
		"ui" : {
			"select_limit": 1
		},
		"plugins" : [
			"themes", "html_data", "ui"
		],
		"themes":  {
            "theme" : "default",
            "dots"  : true,
            "icons" : true
        }
    });
    navigation_loaded = true;
    $("#navPane").show(500);
    attachHandlers();
 //   alert("initNavigation ... done");
}

function attachHandlers(){

//alert("attachHandlers");

  $('#searchField').keyup(searchSuggest);
  $('#searchForm').submit(handleSearch);
  
  $('.answerForm').submit(handleAnswer);
  $('.cheatForm').submit(handleCheat);
  $('.anotherForm').submit(handleAnother);

  $('.answerStatus').hide();
  $('.answerFeedback').hide();
  
  $('#editErrors').hide();
  $('#editForm').submit(handleSave);
  
 //alert("alertHandlers ... done");
}

function reload(data){
  $('body').html(data);
  attachHandlers();
}

// ------------ Show a concept ------------------------------------------

function show(fromConcept, toConcept){
  for(var i = 0; i < conceptNames.length; i++){
     if(toConcept == conceptNames[i]){
      var url = 'show?concept=/' + toConcept;    
      $(location).attr('href',url);
      return;
     }
  }
  back = '<a href="show?concept=/' + fromConcept + '">' +
         '<img width="30" height="30" src="/images/back.png"></a>';
  
  var options = new Array();
  for(var i = 0; i < conceptNames.length; i++){
     if(endsWith(conceptNames[i], '/' + toConcept))
       options.push(conceptNames[i]);
  }
  if(options.length == 0){
     $('title').html('Unknown concept "' + toConcept + '"');
     $('body').html(back + '<h1>Concept "' + toConcept + '" does not exist, please add it or correct link!</h1>' + back);
     return;
  }
   if(options.length == 1){
     var url = 'show?concept=/' + options[0];    
     $(location).attr('href',url);
    return;
  }
   $('title').html('Ambiguous concept "' + toConcept + '"');
   html_code = '<h1>Concept "' + toConcept + '" is ambiguous, select one of (or disambiguate in source):</h1>\n<ul>';
      for(var i = 0; i < options.length; i++){
        html_code += '<li>' + makeConceptURL(options[i]) + '</li>\n';
      }
      html_code += '\n</ul>';

   $('body').html(back + html_code + back);
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

function handleSearch(evt){
   evt.preventDefault();
   
   var term = $('input#searchField').val();
   var lcterm = term.toLowerCase();
   var concept = $('input[name=concept]').val();
   
   //alert('term = "' + term + '"; concept = ' + concept + '; ' + conceptNames);
   
   var results = new Array();
   for(var i=0; i<conceptNames.length; i++){
      var conceptName = conceptNames[i];
      if(match(conceptName, lcterm)){
        results.push(conceptName);
      }
   }
  
//   alert("leave handleSearch: " + results);
   showSearchResults(concept, results, term);
   return false;
}

function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function startsWith(str, prefix){
    return str.substring(0, prefix.length) == prefix;
}

function match(conceptName, term){
   lcConceptName = conceptName.toLowerCase();
   
   if(startsWith(lcConceptName, term) ||
      endsWith(lcConceptName, "/" + term) ||
      lcConceptName.indexOf("/" + term) !== -1){
      return true;
    }
   
   terms = searchTerms[conceptName]
   if(terms){
     for(var i = 0; i < terms.length; i++){
       //alert('terms[' + i + '] = ' + terms[i]);
       if(term == terms[i]){
          return true;
       }
     }
   }
   //alert('match: ' + conceptName + ' and ' + term + ' ===> false');
   return false;
}

function showSearchResults(concept, results, term){
   back = '<a href="show?concept=' + concept + '">' +
          '<img width="30" height="30" src="/images/back.png"></a>';
   if(results.length == 0)
      html_code = '<h1>No results found for "' + term + '"</h1>';
   else if(results.length == 1){
      //html_code = '<h1>Search result</h1><p>' + makeConceptURL(results[0]) + '</p>';
      var url = 'show?concept=' + results[0];    
     $(location).attr('href',url);
     return;
   } else {
      html_code = '<h1>' + results.length + ' search results for "' + term + '"</h1>\n<ul>';
      for(var i = 0; i < results.length; i++){
        html_code += '<li>' + makeConceptURL(results[i]) + '</li>\n';
      }
      html_code += '\n</ul>';
   }
   $('title').html('Search results for "' + term + '"');
   $('body').html(back + html_code + back);
}


function makeConceptURL(conceptName){
   return '<a href="show?concept=' + conceptName + '">' + conceptName + '</a>';
}


// ------------ Handler for making a choice from the suggestions for the searchBox 

function makeChoice() {
   	$('input#searchField').val($(this).text());
	$('#popups').html("");
    $('#searchForm').submit();
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
  alert("handleSave: " + formData);
  evt.preventDefault();
  $.get("save", formData, 
    function processSaveFeedback(data, textStatus){
     var c = $('#concept', data).text();
     var e = $('#error', data).text();
     var r = $('#replacement', data).text();
     //alert("c = " + c + "; e = " + e);
     if(e != ""){
        $('#editErrors').html("<img height=\"25\" width=\"25\" src=\"/images/bad.png\">Correct error: " + e);
        $('#editErrors').fadeIn(500);
     } else
        reload(r);
    });
  return false;
}

