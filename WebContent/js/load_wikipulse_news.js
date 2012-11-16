var search_parameter;
search_parameter = getUrlVars()[0];

var wp_service_url = "http://localhost:4567/mostreadarticlesincategory?category=";
var wp_service_parameter = search_parameter;
$.ajax({
    type: 'GET',
    url: 'http://localhost:4567/mostreadarticlesincategory?category=' + wp_service_parameter,
    dataType: 'jsonp',
    jsonpCallback: 'callback',
    success: function (data) {
    	data.sort(function(a,b){ return parseInt(b.relevance*100) - parseInt(a.relevance*100);});
    	$.each(data,function(i,page){
    		if (Number(page.yesterdaysRelevance) > 0.08 ) {
    			var append_str = '<div class="row-fluid"><div class="span6 offset3"><h3>'+ page.title + '</h3>';
				append_str += '<p> relevance: ' + parseInt(page.yesterdaysRelevance*100)  + '</p><div id="_content'+i+'">';
				append_str += '<a href="http://en.wikipedia.org/wiki/' +  page.title + '">' +  page.title + '</a>';
      			append_str += '</div></div></div><hr>';
      			$("#wp_service_results").append(append_str);
      			
				/* Add wiki content
				var wiki_url = "http://en.wikipedia.org/w/api.php?action=parse&format=json&callback=?";
    			var wiki_page = page.title;    			
      			$.getJSON(wiki_url, { 
    			  page: wiki_page, 
    			  prop:"text", 
    			  uselang:"en"
    			}, function(wiki_result) {

    				var w_text = wiki_result['parse']['text']['*'];
    				var sub_str = w_text.substring(w_text.indexOf("Paula Dean Kranz Broadwell"),400);
    				var div_id = '#_content' + i;
    				http://en.wikipedia.org/wiki/
    				$(div_id).append('<p>' + sub_str + '</p>');
    			});
      			*/
    		}
    	});
    },
    jsonp: 'jsonp'
});

function getUrlVars()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('#') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}


/*
var url='http://search.twitter.com/search.json?callback=?&rpp=5&q=';

//alert(query);
$.getJSON(url+search_parameter,function(json){
	$.each(json.results,function(i,tweet){
		$("#twitter_results").append('<div class="row-fluid"><div class="span6 offset3"><img src="'+tweet.profile_image_url+'" widt="48" height="48" /><p>'+tweet.text+'</p></div></div>');
	});
});

setInterval(function () {
	$.getJSON(url+search_parameter,function(json){
		$.each(json.results,function(i,tweet){
		   $("#twitter_results").prepend('<div class="row-fluid"><div class="span6 offset3"><img src="'+tweet.profile_image_url+'" widt="48" height="48" /><p>'+tweet.text+'</p></div></div>');
		});
	});
}, 20000);
*/
