function report(m, s) {
/*
if(s == null)
   alert(m + ": null");
else
   alert(m + ": " + s.substring(0, 200));
   */
}

$(document).ready(function () {
	// first retrieve navpane
	if ($('#tdnav').length > 0) {
		$('#tdnav').load(navigationPaneSource + ' #navPane', function () {
			if ($('#navPane #navItialized').length > 0) {
				attachHandlers();
			} else {
				initNavigation();
			}
		});
		addGoogleTracker();
	}
	else if ($('#conceptPane').length > 0) {
		// edit page
    	var concept = $("input[name=concept]").val();
		attachDisqus(translateConceptToURL(concept));
		$('#editErrors').hide();
		$('#editForm').submit(handleSave);
	}
	else {
		// on the main page
		addGoogleTracker();
		return;
	}
	currentSubCourse = window.location.pathname.match(/\/[^\/]+\//);
	if (currentSubCourse && currentSubCourse.length > 0) {
		$('#conceptPane a[href*="' + currentSubCourse + '"]').live("click", function(e){
			if ($(this).attr('id') != 'tutorAction' && ($(this).parent('#editMenu').size() == 0)) {
				// make sure any local links do not cause a actual page reload
				var url = $(this).attr('href');
				e.preventDefault();
				var treeNode = $('#navPane a[href=' + url + ']');
				if (treeNode) {
					$('#navPane').jstree('deselect_all');
					$('#navPane').jstree('select_node', treeNode);
				}
				else {
					loadConceptURL(url);
				}
			}
		});
	}
});

var _gaq = _gaq || [];

function addGoogleTracker() {
	if (window.location.hostname !== 'tutor.rascal-mpl.org') {
		_gaq.push(['_setAccount', 'UA-560596-8']); // inside eclipse
	}
	else {
		_gaq.push(['_setAccount', 'UA-560596-7']); // from website
	}
	_gaq.push(['_setDomainName', 'none']);
	_gaq.push(['_setAllowLinker', true]);
	//_gaq.push(['_trackPageview']);

	(function() {
		var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	})();
}


function addLightboxToImages() {
	$('#tdconcept img').each(function () {
		if (($(this).parent('a').length > 0)) return;
		var img = this; 
		$("<img/>") // Make in memory copy of image to avoid css issues
			.attr("src", $(img).attr("src"))
			.load(function() {
				if (img.width != this.width || img.height != this.height ) {
					$(img).css('cursor', 'pointer')
						.colorbox({ href: $(img).attr('src'), maxWidth: '90%', maxHeight: '90%' });
				}
			});
	});
}

var skipNextNodeClick = 0;

function initNavigation() {


    $("#navPane").bind("select_node.jstree", function (event, data) {
		if (skipNextNodeClick > 0) {
			skipNextNodeClick -= 1;
			return;
		}
        var url = $(data.args[0]).attr("href");
        loadConceptURL(url);
        return false;
    }).jstree({
        "core": {
            "animation": 100
        },
        "ui": {
            "select_limit": 1
        },
        "plugins": ["themes", "html_data", "ui", "cookies"],
        "themes": {
            "theme": "default",
            "dots": true,
            "icons": true
        },
        "cookies": {
            "auto_save": true

        }
    });
	setTimeout(function() {
		$('#navPane').jstree('open_node', $('#navPane a:first'));
	}, 0);
	$.History.bind(function(state) {
		if (state == '' || state[0] != '/') {
			state = window.location.pathname;
		}

    	$("#conceptPane").load(state + " div#conceptPane", function() {
			newTitle = state.match(/\/(.+)\/[^\/]+\.html/);// second group
			if (newTitle.length == 2) {
				$('title').html(newTitle[1]);
			}
			_gaq.push(['_trackPageview', state]);
			attachDisqus(state);
			var treeNode = $('#navPane a[href=' + state + ']');
			if (treeNode && !($(treeNode).hasClass('jstree-clicked'))) {
				// we have to update the tree selection
				$('#navPane').jstree('deselect_all');
				skipNextNodeClick += 1;
				$('#navPane').jstree('select_node', treeNode);
			}
			finishLoad();
		});
	});
	if (window.location.hash == '') {
		setTimeout(function() {
			var treeNode = $('#navPane a[href=' + window.location.pathname + ']');
			if (treeNode && !($(treeNode).hasClass('jstree-clicked'))) {
				$('#navPane').jstree('deselect_all');
				skipNextNodeClick += 1;
				$('#navPane').jstree('select_node', treeNode);
			}
		}, 5);
		// no initial state will fire, so we have to attach the Disqus manually
		attachDisqus(window.location.pathname);
	}

    $('<div id="navInitialized"></div>').insertAfter("#navPane");
    $('#navPane').bind("open_node.jstree close_node.jstree", function (e) {
        // this is a  simple fix to work around a resizing issues caused 
        // by the mouse-over on the longest leaf of the tree
        // causing the width of the navPane to expand .
        // After a leaf open / close we retrieve the new width of the navPane
        // and set the navInitialized just a bit wider
        $('#navInitialized').width(0); // first reset the below div to to make the navPane the largest again
        // since the browser does not apply the width directly we have to update
        // the navInitialized width using a callback
        setTimeout(function () {
            $('#navInitialized').width($('#navPane').width() + 5);
        }, 0);
    });
    //    navigation_initialized = true;
    attachHandlers();
    //   alert("initNavigation ... done");
}

var disqus_shortname = 'rascalmpltutor'; 
var disqus_identifier = '';
var disqus_url = '';

function attachDisqus(page) {
	$('#disqus_thread').remove();
	disqus_identifier = page;
	disqus_url = "http://tutor.rascal-mpl.org" + page;
	$("#conceptPane").after("<div id=\"disqus_thread\" style=\"clear:both\"></div>"); // insert disqus div
	(function() {
		var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
		dsq.src = 'http://' + disqus_shortname + '.disqus.com/embed.js';
		(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
	})();
}

function finishLoad() {

    $('.answerForm').submit(handleAnswer);
    $('.cheatForm').submit(handleCheat);
    $('.anotherForm').submit(handleAnother);
    
    $('.examAnswerForm').submit(handleExam);

    $('.answerStatus').hide();
    $('.answerFeedback').hide();

    if (enableEditing == false) $('#editMenu').hide();
    if (enableQuestions == false) $('#questions').hide();

    $('pre').css('max-width', $(window).width() * 0.7);
    $(window).resize(function() {
        $('pre').css('max-width', $(window).width() * 0.7);
    });
}

function attachHandlers() {

    report("attachHandlers", $("#navPane").html());

	$('#searchField').autocomplete({
		data : baseConcepts,
		onItemSelect: function () { $('#searchForm').submit(); },  // could also change the handleSearch method to support a non event calling it..
		selectFirst : true
	});
	$('#searchForm').submit(handleSearch);


	addLightboxToImages();

    report("attachHandlers ... done", $("#navPane").html());

	finishLoad();
    return false;
}

var rbdata;

function loadConceptURL(url) {
	$.History.go(url); // store history in url, such that the back button could work
}

function loadConcept(cn) {
	loadConceptURL(translateConceptToURL(cn));
}

function translateConceptToURL(concept) {
	return "/" + concept + "/" + basename(concept) + ".html";
}


// ------------ Handler for suggestions for searchBox -------------------

function handleSearch(evt) {
	evt.preventDefault();

    var term = $('input#searchField').val();
    var lcterm = term.toLowerCase();
    var concept = $('input[name=concept]').val();

    //alert('term = "' + term + '"; concept = ' + concept + '; ' + conceptNames);
    var results = [];
    for (var i = 0; i < conceptNames.length; i++) {
        var conceptName = conceptNames[i];
        if (match(conceptName, lcterm)) {
            results.push(conceptName);
        }
    }
    
    var concepts = searchTerms[term];
    if(concepts != null){
       //alert('concepts = ' + concepts);
       for(var i = 0; i < concepts.length; i++)
           results.push(concepts[i]);
    }

    //alert("leave handleSearch: " + results);
    showSearchResults(concept, results, term);
    return false;
}

function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function startsWith(str, prefix) {
    return str.substring(0, prefix.length) == prefix;
}

function basename(cn) {
    var si = cn.lastIndexOf("/");
    if (si >= 0) {
        return cn.substring(si + 1);
    } else {
        return cn;
    }
}

function match(conceptName, term) {
    lcConceptName = conceptName.toLowerCase();

    if (startsWith(lcConceptName, term) || endsWith(lcConceptName, "/" + term) || lcConceptName.indexOf("/" + term) !== -1) {
        return true;
    }
    return false;
}

function showSearchResults(concept, results, term) {
    backarrow = back(concept);
    if (results.length == 0) html_code = '<h1>No results found for "' + term + '"</h1>';
    else if (results.length == 1) {
        loadConcept(results[0]);
        return;
    } else {
        html_code = '<h1>' + results.length + ' search results for "' + term + '"</h1>\n<ul>';
        for (var i = 0; i < results.length; i++) {
            html_code += '<li>' + makeConceptURL(results[i]) + '</li>\n';
        }
        html_code += '\n</ul>';
    }
	html_code = html_code + '<br /><a href="http://www.google.com/search?q=' + term +'+site%3Atutor.rascal-mpl.org"><img src="/images/google_icon.png" /> Use google to search for this term.</a><br />';
    $('title').html('Search results for "' + term + '"');
    $('div#conceptPane').html(backarrow + html_code + backarrow);
	$('#disqus_thread').remove();
}


function makeConceptURL(toConcept) {
    return '<a href="' +  translateConceptToURL(toConcept) +'">' + toConcept +'</a>';
}

function back(fromConcept) {
    return '<a href="' +  translateConceptToURL(fromConcept) +'"><img width="30" height="30" src="/images/back.png"></a>';
}


// ------------ Handler for making a choice from the suggestions for the searchBox 

function makeChoice() {
    $('input#searchField').val($(this).text());
    $('#popups').html("");
    $('#searchForm').submit();
}

// ------------ Handler for answers to exercises

function handleAnswer(evt) {
    var formData = $(this).serialize();
    evt.preventDefault();
    $.get("/validate", formData, function processValidationResult(data, textStatus) {

        //alert("processValidationResult: " + data);
        var v = $('#validation', data).text();
        var c = $('#concept', data).text();
        var e = $('#exercise', data).text();
        var fb = $('#feedback', data).text();
        //alert("v = " + v + "; c = " + c  + "; e = " + e + "; fb = " + fb);
        var sep = "_";
        var c_e = c + sep + e;
        var good = "#good" + sep + c_e;
        var bad = "#bad" + sep + c_e;
        
        //alert("good = " + good + "; bad = " + bad);
        $(good).fadeOut(1000);
        $(bad).fadeOut(1000);

        if (v == "true") {
            $(good).fadeIn();
        } else {
            $(bad).fadeIn();
        }
        var afb = "#answerFeedback" + sep + c_e;
        $(afb).fadeOut(1000, function () {
            if (fb != "") {
                $(afb).html(fb);
                $(afb).fadeIn(1000);
            }
        });
    });

    return false;
}

// ------------ Handler for answers to exercises

function validName(name){
   return name.split(' ').join('').length > 3;
}

function validEmail(address){
   var iat = address.indexOf('@') ;
   var idot = address.lastIndexOf('.');
   return iat >= 1 && idot > iat && idot < address.length - 1;
}

function validStudentNumber(number){
   return number.length >= 5;
}

function handleExam(evt) {
    var formData = $(this).serialize();
    //evt.preventDefault();

    var studentName = $("#studentName").val();
    var studentMail = $("#studentMail").val();
    var studentNumber = $("#studentNumber").val();
    
    var msgs = [];
    if(!validName(studentName)){
        msgs.push("Name too short");
    }
    
    if(!validEmail(studentMail)){
        msgs.push("Invalid email address");
    }
  
    if(!validStudentNumber(studentNumber)){
        msgs.push("Invalid student number");
    }
    if(msgs.length > 0){
        alert(msgs.join("\n"));
        return false;
    }

    return true;
}


// ------------ Handler for "cheat" requests

function handleCheat(evt) {
    var formData = $(this).serialize();
    evt.preventDefault();
    $.get("/validate", formData, function processCheatResult(data, textStatus) {
        var c = $('#concept', data).text();
        var e = $('#exercise', data).text();
        var cheat = $('#feedback', data).text();
        var sep = "_";
        var c_e = c + sep + e;
        var afb = "#answerFeedback" + sep + c_e;
        $(afb).html(cheat);
        $(afb).fadeIn(1000);
    });

    return false;
}

// ------------ Handler for "another" requests

function handleAnother(evt) {
    //alert("handleAnother");
    var formData = $(this).serialize();
    evt.preventDefault();
    $.get("/validate", formData, function processAnotherResult(data, textStatus) {
        //alert("processAnotherResult: " + data);
        var c = $('#concept', data).text();
        var e = $('#exercise', data).text();
        var another = $('#another', data).text();
        //alert("c = " + c  + "; e = " + e + "; another=" + another.substring(0,20));
        
        var sep = "_";
        var c_e = "#" + c + sep + e;
        var good = "#good" + sep + c_e;
        var bad = "#bad" + sep + c_e;
        
        $(c_e).fadeOut(1000, function () {
            $(c_e).html(another);
            $(c_e + " .answerStatus").hide();
            $(c_e + " .answerFeedback").hide();

            $(c_e + ' .answerForm').submit(handleAnswer);
            $(c_e + ' .cheatForm').submit(handleCheat);
            $(c_e + ' .anotherForm').submit(handleAnother);
            $(c_e).show();
        });

    });

    return false;
}

// ------------ Handle "save" request while editing

function handleSave(evt) {
    var formData = $(this).serialize();
    var concept = $("input[name=concept]").val();
    report("handleSave", formData);
    evt.preventDefault();
	$('#saveButton').attr('disabled','disabled');
	$('#pleaseWaitMessage').css('display', 'inline');
	$.get("save", formData, function processSaveFeedback(data, textStatus) {
		$('#pleaseWaitMessage').hide();
		$('#saveButton').removeAttr('disabled');
        var c = $('#concept', data).text();
        var e = $('#error', data).text();
        var r = $('#replacement', data).text();
        //alert("c = " + c + "; e = " + e);
        if (e != "") {
            $('#editErrors').html("<img height=\"25\" width=\"25\" src=\"/images/bad.png\">Correct error: " + e);
            $('#editErrors').fadeIn(500);
        } else {
            window.location = translateConceptToURL(concept); 
        }
    });
    return false;
}

function reload(data) {
    report("reload", data);
    $('div#conceptPane').html(data);
    attachHandlers();
}
