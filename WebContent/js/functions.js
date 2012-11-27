var wp_service_url = "http://localhost:4567";
var wp_service_url_current_events = wp_service_url + "/mostreadarticlesincategory?category=Current_events";
var wp_service_url_get_articles_from_category = wp_service_url + "/mostreadarticlesincategory?category=";
var wp_service_url_free_text_search = wp_service_url + "/FreeTextSearch?&srsearch=";

$(document).ready(function() {
	var dt = new GetDateTime();
	$("#date").text(dt.formats.pretty.b);
	load_s('home.html');
	
	setInterval(function () {
		var dt = new GetDateTime();
		$("#date").text(dt.formats.pretty.b);
		//alert(dt.formats.pretty.b);
    }, 30000);
	
	load_current_Events();
	
	
});

// catch click event coming from TOP (Home) and BOTTOM navigation (Home AboutUs,ContactUs)
$('.bottom_nav_link').click(function(){
	var value = $(this).attr("href");
	$('.nav>li.active').removeClass('active');
	load_s(value.replace('#','') + '.html');
	$(this).parent().addClass('active');
});

//catch click event coming from top navigation bar (category)
$('.nav_link').click(function(){
	var value = $(this).attr("href");
	$(this).parent().siblings().removeClass('active');
	load_s('news.html?content=' + value.replace('#',''));
	$(this).parent().addClass('active');
});

// catch enter event
$('.search-query').keyup(function (e) {
	e.preventDefault();
    if (e.keyCode == 13) {
    	//var value = $(this).val();
    	//load_s('search.html?content=' + value);
    }    
});

//catch enter event
$('#search_form').submit(function (e) {
	e.preventDefault();
    var value = $('#search_input').val();
    load_s('search.html?content=' + value);
});


// load desired document into main_div
function load_s(source) {
    $.get(source, function(data) {
    	$('#main_div').html(data);
    });
}

//load current events
function load_current_Events(){
	$.ajax({
	    type: 'GET',
	    url: wp_service_url_current_events,
	    dataType: 'jsonp',
	    jsonpCallback: 'callback',
	    success: function (data) {
	    	data.sort(function(a,b){ return parseInt(b.yesterdaysRelevance*100) - parseInt(a.yesterdaysRelevance*100);});
	    	$.each(data,function(i,page){
	    		if (Number(page.yesterdaysRelevance) > 0.08 ) {
	    			var append_str = '<li><a href="http://en.wikipedia.org/wiki/' +  page.title + '">' + page.title + '</a></li>';
	      			$("#left_navigation ul").append(append_str);
	    		}
	    	});
	    },
	    jsonp: 'jsonp'
	});	
}

//load category values
function load_wikipulse_news(url_parameter){
	$.ajax({
	    type: 'GET',
	    url: wp_service_url_get_articles_from_category + url_parameter,
	    dataType: 'jsonp',
	    jsonpCallback: 'callback',
	    success: function (data) {
	    	data.sort(function(a,b){ return parseInt(b.yesterdaysRelevance*100) - parseInt(a.yesterdaysRelevance*100);});
	    	$.each(data,function(i,page){
	    		if (Number(page.yesterdaysRelevance) > 0.08 ) {
	    			var append_str = '<div class="row-fluid"><div class="span6 offset3"><h3>'+ page.title + '</h3>';
					append_str += '<p> relevance: ' + parseInt(page.yesterdaysRelevance*100)  + '</p><div id="_content'+i+'">';
					append_str += '<a href="http://en.wikipedia.org/wiki/' +  page.title + '">' +  page.title + '</a>';
	      			append_str += '</div></div></div><hr>';
	      			$("#wp_service_news_results").append(append_str);
	      			
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
}

//search functionality on wikipulse
function search_wikipulse_service(){
	var search_input =$('#search_input').val();
	//alert(wp_service_url_free_text_search + search_input);
	$.ajax({
	    type: 'GET',
	    url: wp_service_url_free_text_search + search_input,
	    dataType: 'jsonp',
	    jsonpCallback: 'callback',
	    success: function (data) {
	    	$.each(data,function(i,page_snippet){
    			var append_str = '<div class="row-fluid"><div class="span6 offset3">';
				append_str += '<div id="_content'+i+'">';
				append_str += '<a href="' +  page_snippet.urlToFullPage + '">' +  page_snippet.title + '</a>';
				append_str += '<p class="p-small">' + page_snippet.snippet + '</p>';
      			append_str += '</div></div></div><hr>';
      			$("#wp_service_search_results").append(append_str);
	    	});
	    },
	    jsonp: 'jsonp'
	});
}


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

//Set DateTime - Value on Website
function GetDateTime() {
    function getDaySuffix(a) {
        var b = "" + a,
            c = b.length,
            d = parseInt(b.substring(c-2, c-1)),
            e = parseInt(b.substring(c-1));
        if (c == 2 && d == 1) return "th";
        switch(e) {
            case 1:
                return "st";
                break;
            case 2:
                return "nd";
                break;
            case 3:
                return "rd";
                break;
            default:
                return "th";
                break;
        };
    };

    this.getDoY = function(a) {
        var b = new Date(a.getFullYear(),0,1);
    return Math.ceil((a - b) / 86400000);
    };

    this.date = arguments.length == 0 ? new Date() : new Date(arguments);

    this.weekdays = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday');
    this.months = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');
    this.daySuf = new Array( "st", "nd", "rd", "th" );

    this.day = {
        index: {
            week: "0" + this.date.getDay(),
            month: (this.date.getDate() < 10) ? "0" + this.date.getDate() : this.date.getDate()
        },
        name: this.weekdays[this.date.getDay()],
        of: {
            week: ((this.date.getDay() < 10) ? "0" + this.date.getDay() : this.date.getDay()) + getDaySuffix(this.date.getDay()),
            month: ((this.date.getDate() < 10) ? "0" + this.date.getDate() : this.date.getDate()) + getDaySuffix(this.date.getDate())
        }
    };

    this.month = {
        index: (this.date.getMonth() + 1) < 10 ? "0" + (this.date.getMonth() + 1) : this.date.getMonth() + 1,
        name: this.months[this.date.getMonth()]
    };

    this.year = this.date.getFullYear();

    this.time = {
        hour: {
            meridiem: (this.date.getHours() > 12) ? (this.date.getHours() - 12) < 10 ? "0" + (this.date.getHours() - 12) : this.date.getHours() - 12 : (this.date.getHours() < 10) ? "0" + this.date.getHours() : this.date.getHours(),
            military: (this.date.getHours() < 10) ? "0" + this.date.getHours() : this.date.getHours(),
            noLeadZero: {
                meridiem: (this.date.getHours() > 12) ? this.date.getHours() - 12 : this.date.getHours(),
                military: this.date.getHours()
            }
        },
        minute: (this.date.getMinutes() < 10) ? "0" + this.date.getMinutes() : this.date.getMinutes(),
        seconds: (this.date.getSeconds() < 10) ? "0" + this.date.getSeconds() : this.date.getSeconds(),
        milliseconds: (this.date.getMilliseconds() < 100) ? (this.date.getMilliseconds() < 10) ? "00" + this.date.getMilliseconds() : "0" + this.date.getMilliseconds() : this.date.getMilliseconds(),
        meridiem: (this.date.getHours() > 12) ? "PM" : "AM"
    };

    this.sym = {
        d: {
            d: this.date.getDate(),
            dd: (this.date.getDate() < 10) ? "0" + this.date.getDate() : this.date.getDate(),
            ddd: this.weekdays[this.date.getDay()].substring(0, 3),
            dddd: this.weekdays[this.date.getDay()],
            ddddd: ((this.date.getDate() < 10) ? "0" + this.date.getDate() : this.date.getDate()) + getDaySuffix(this.date.getDate()),
            m: this.date.getMonth() + 1,
            mm: (this.date.getMonth() + 1) < 10 ? "0" + (this.date.getMonth() + 1) : this.date.getMonth() + 1,
            mmm: this.months[this.date.getMonth()].substring(0, 3),
            mmmm: this.months[this.date.getMonth()],
            yy: (""+this.date.getFullYear()).substr(2, 2),
            yyyy: this.date.getFullYear()
        },
        t: {
            h: (this.date.getHours() > 12) ? this.date.getHours() - 12 : this.date.getHours(),
            hh: (this.date.getHours() > 12) ? (this.date.getHours() - 12) < 10 ? "0" + (this.date.getHours() - 12) : this.date.getHours() - 12 : (this.date.getHours() < 10) ? "0" + this.date.getHours() : this.date.getHours(),
            hhh: this.date.getHours(),
            m: this.date.getMinutes(),
            mm: (this.date.getMinutes() < 10) ? "0" + this.date.getMinutes() : this.date.getMinutes(),
            s: this.date.getSeconds(),
            ss: (this.date.getSeconds() < 10) ? "0" + this.date.getSeconds() : this.date.getSeconds(),
            ms: this.date.getMilliseconds(),
            mss: Math.round(this.date.getMilliseconds()/10) < 10 ? "0" + Math.round(this.date.getMilliseconds()/10) : Math.round(this.date.getMilliseconds()/10),
            msss: (this.date.getMilliseconds() < 100) ? (this.date.getMilliseconds() < 10) ? "00" + this.date.getMilliseconds() : "0" + this.date.getMilliseconds() : this.date.getMilliseconds()
        }
    };

    this.formats = {
        compound: {
            commonLogFormat: this.sym.d.dd + "/" + this.sym.d.mmm + "/" + this.sym.d.yyyy + ":" + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            exif: this.sym.d.yyyy + ":" + this.sym.d.mm + ":" + this.sym.d.dd + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            /*iso1: "",
            iso2: "",*/
            mySQL: this.sym.d.yyyy + "-" + this.sym.d.mm + "-" + this.sym.d.dd + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            postgreSQL1: this.sym.d.yyyy + "." + this.getDoY(this.date),
            postgreSQL2: this.sym.d.yyyy + "" + this.getDoY(this.date),
            soap: this.sym.d.yyyy + "-" + this.sym.d.mm + "-" + this.sym.d.dd + "T" + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss + "." + this.sym.t.mss,
            //unix: "",
            xmlrpc: this.sym.d.yyyy + "" + this.sym.d.mm + "" + this.sym.d.dd + "T" + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            xmlrpcCompact: this.sym.d.yyyy + "" + this.sym.d.mm + "" + this.sym.d.dd + "T" + this.sym.t.hhh + "" + this.sym.t.mm + "" + this.sym.t.ss,
            wddx: this.sym.d.yyyy + "-" + this.sym.d.m + "-" + this.sym.d.d + "T" + this.sym.t.h + ":" + this.sym.t.m + ":" + this.sym.t.s
        },
        constants: {
            atom: this.sym.d.yyyy + "-" + this.sym.d.mm + "-" + this.sym.d.dd + "T" + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            cookie: this.sym.d.dddd + ", " + this.sym.d.dd + "-" + this.sym.d.mmm + "-" + this.sym.d.yy + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            iso8601: this.sym.d.yyyy + "-" + this.sym.d.mm + "-" + this.sym.d.dd + "T" + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            rfc822: this.sym.d.ddd + ", " + this.sym.d.dd + " " + this.sym.d.mmm + " " + this.sym.d.yy + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            rfc850: this.sym.d.dddd + ", " + this.sym.d.dd + "-" + this.sym.d.mmm + "-" + this.sym.d.yy + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            rfc1036: this.sym.d.ddd + ", " + this.sym.d.dd + " " + this.sym.d.mmm + " " + this.sym.d.yy + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            rfc1123: this.sym.d.ddd + ", " + this.sym.d.dd + " " + this.sym.d.mmm + " " + this.sym.d.yyyy + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            rfc2822: this.sym.d.ddd + ", " + this.sym.d.dd + " " + this.sym.d.mmm + " " + this.sym.d.yyyy + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            rfc3339: this.sym.d.yyyy + "-" + this.sym.d.mm + "-" + this.sym.d.dd + "T" + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            rss: this.sym.d.ddd + ", " + this.sym.d.dd + " " + this.sym.d.mmm + " " + this.sym.d.yy + " " + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss,
            w3c: this.sym.d.yyyy + "-" + this.sym.d.mm + "-" + this.sym.d.dd + "T" + this.sym.t.hhh + ":" + this.sym.t.mm + ":" + this.sym.t.ss
        },
        pretty: {
            a: this.sym.t.hh + ":" + this.sym.t.mm + "." + this.sym.t.ss + this.time.meridiem + " " + this.sym.d.dddd + " " + this.sym.d.ddddd + " of " + this.sym.d.mmmm + ", " + this.sym.d.yyyy,
            b: this.sym.t.hhh + ":" + this.sym.t.mm + " " + this.sym.d.dddd + " " + this.sym.d.ddddd + " of " + this.sym.d.mmmm + ", " + this.sym.d.yyyy
        }
    };
};
