/* get current domain and create URLs to REST - routes */
var wp_service_url = window.location.toString().substring(0, window.location.toString().lastIndexOf("/"));
var wp_service_route_news = wp_service_url + "/news";
var wp_service_route_categories = wp_service_url + "/categories";
var wp_service_route_changes = wp_service_url + "/changes";
var wp_service_url_free_text_search = wp_service_url + "/FreeTextSearch?&srsearch=";
var category_news_limit = "15";
var category_no_limit = "4";
var homepage_limit = "35";
var mostreadstories_limit = "6";
/* set wikipedia URL */
var wiki_url = "http://en.wikipedia.org/wiki/";

/* functionality that is called after page has been loaded and the full DOM is available on client */
$(document).ready(function() {
	/* set current DateTime on website */
	var dt = new GetDateTime();
	$("#date").text(dt.formats.pretty.b);
	
	/* update DateTime on website (every 30sec) */
	setInterval(function () {
		var dt = new GetDateTime();
		$("#date").text(dt.formats.pretty.b);
    }, 30000);
	
	/* load and create category structure */
	loadCategories();
	
	/* load and create recent news */
	loadRecentChanges(10);
	
	/* load and create most read stories */
	loadMostReadNews();
	
	/* update recent news and most read stories (every 20 seconds) */
	var i = 1;
	setInterval(function () {
		loadRecentChanges(10);
		loadMostReadNews();
		i += 1;		
    }, 20000);		
});

/* catch click event of BOTTOM navigation (AboutUs,ContactUs) */
$(".bottom_nav_link").click(function(){
	/* retrieve link-information, remove active-class from TOP navigation, load .html - file */
	var value = $(this).attr("href");
	$(".nav>li.active").removeClass("active");
	loadDoc(value.replace("#","") + ".html");
});

/* catch click event of TOP navigation (dynamic categories) */
$("#navigation").on("click",".nav_link", function(){
	/* retrieve link-information, remove active-class, load either home.html or dynamic content, set new active-class*/
	var value = $(this).attr("href");
	value = value.replace("#cat=","");
	$(this).parent().siblings().removeClass("active");
	if (value == "home" ) loadDoc("home.html");
	else loadDoc("news.html");
	$(this).parent().addClass("active");
});

/* catch ENTER key (search form)  not used currently*/
//$(".search-query").keyup(function (e) {
	/* prevent default which would cause a full page reload */
//	e.preventDefault();
//    if (e.keyCode == 13) {    	
//    }    
//});

/* catch search form event */
//$("#search_form").submit(function (e) {
	/* prevent page reload and load dynamic content based on search inpute value*/
//	e.preventDefault();
//    var value = $("#search_input").val();
//    loadDoc("search.html?content=" + value);
//});

/* add click event to all news records in order, increase user interaction (counter) and load news detail*/
$("#main_div").on("click",".show_detail", function(){
    var news_id = $(this).attr("newsid");
    increaseViewCounter(news_id);
    loadDoc("news_detail.html");
});

$("#most_read_stories").on("click",".show_detail", function(){
    var news_id = $(this).attr("newsid");
    increaseViewCounter(news_id);
    loadDoc("news_detail.html");
});

/* set class=active for navigation item based on attribute href */
function setActiveClass(href){
	/* remove active from all items and then set it */
	$(".top_nav li").each(function(){
		$(this).removeClass("active");		
	    if( ($(this).children(".nav_link").attr("href") !== undefined) &&
    		($(this).children(".nav_link").attr("href").toLowerCase() == (("#cat=" + href).toLowerCase())) && 
    		(href != "")){
	    		$(this).addClass("active");
	    }
	});
}

/* load desired document into main_div */
function loadDoc(source) {
    $.get(source, function(data) {
    	$("#main_div").html(data);
    });
}

/* load recent changes and display */
function loadRecentChanges(minchanges){
	$.ajax({
	    type: "GET",
	    url: wp_service_route_changes + "?minchanges=" + minchanges.toString(),
	    dataType: "json",
	    success: function (data) {
	    	$("#wait_recent_changes").html("");
	    	$("#recent_changes ul").html("");
	    	$("#recent_changes ul").append("<li class='nav-header'>Recent Changes</li>");
	    	$.each(data,function(i,changes){
    			if(changes.title.indexOf(":") < 0)
    				{
	    				var append_str = "<li><a href='http://" +  changes.url + "'>" + changes.title + "&nbsp;(&nbsp;" + changes.count + "&nbsp;edits)&nbsp;" +"</a></li>";
		      			$("#recent_changes ul").append(append_str);	    				
    				}	    			
	    	});
	    }
	});	
}

/* get news detail from service and display */
function loadNewsDetail(url_parameter){
	var news_template = "<h3>news_title</h3>" + "<div id='news_id'>img_slide_show</div>" +
	"<p>news_summary</p><a href='wikipedia_article'>see more on wikipedia</a><p class='p-small'>edited by:<br/> editor_list</p>";
    $.ajax({
	    type: "GET",
	    url: wp_service_route_news + "/" + url_parameter,
	    dataType: "json",
	    success: function (news) {
    		$("#wait").html("");
	    	if (news !== null){	 							
				append_str = news_template;				
				append_str = append_str.replace(/news_id/g, "'" + news.id + "'");
				append_str = append_str.replace(/news_title/g,news.pageTitle);
				append_str = append_str.replace(/img_slide_show/g,createImageSlideshow(news.id,0,news.imageUrlList));
				append_str = append_str.replace(/news_summary/g,news.news);
				append_str = append_str.replace(/wikipedia_article/g, news.BASE_URL + news.pageTitle);
				var editor_list = "";
				if (news.editors !== undefined){ 
					if (news.editors.length > 1) alert("editors: " + news.editors.length);
					for ( var i = 0; i < news.editors.length; i++) {
						editor_list += news.editors[i].user;
						if ((i+1)<news.editors.length) editor_list += ", "; 
					}			
				}
				append_str = append_str.replace(/editor_list/g,editor_list);
				
	    		$("#wp_service_news_detail_results").html(append_str);	  
	    	}
	    	else {
	    		$("#wp_service_news_detail_results").html("sorry, news not found");	  
	    	}
	    },
	    error: function(){
	    	$("#wait").html("");
	    	$("#wp_service_news_detail_results").html("sorry, news not found");	
	    }
	});
}

/* load news for specific category or home-page */
function loadNews(type,url_parameter){
	var url = "";
	if (type=="home") url = wp_service_route_news + url_parameter;
	else if (type=="category") url = wp_service_route_categories + "/" + url_parameter + "?limit=" + category_news_limit;
	loadNewsImplementation(url);
}

/* load news for a category */
function loadNewsImplementation(url){
	var news_template_big = "<h3><a href='#newsid=news_id' class='show_detail' newsid='news_id'>news_title</a></h3>" +
							"<div id='news_id'>img_slide_show</div>" +
							"<p>news_shortsummary<a href='#newsid=news_id' class='show_detail' newsid='news_id'>&nbsp;more information</a></p>";
	var news_template_small = "<h6><a href='#newsid=news_id' class='show_detail' news_id='news_id'>news_title</a></h6>" +
							"<div id='news_id'>img_slide_show</div>" +
							"<p class='p-small'>news_shortsummary<a href='#newsid=news_id' class='show_detail' newsid='news_id'>&nbsp;more information</a></p>";
	
	$.ajax({
	    type: "GET",
	    url: url,
	    dataType: "json",
	    success: function (data) {
	    	$("#wait").html("");
	    	data.sort(function(a,b){ return parseInt(b.yesterdaysRelevance*100) - parseInt(a.yesterdaysRelevance*100);});
	    	var append_str = "";
	    	var counter = 0;
	    	var subcounter = 0;
	    	var news_rows = "";
	    	$.each(data,function(i,news){	    		
    			found = false;
    			append_str = (counter == 0 ) ? news_template_big : news_template_small;
    			append_str = append_str.replace(/news_id/g, news.id);
				append_str = append_str.replace(/news_title/g,news.pageTitle);
				append_str = append_str.replace(/img_slide_show/g,createImageSlideshow(news.id,counter,news.imageUrlList));				
				append_str = append_str.replace(/news_shortsummary/g,news.shortNews);

    			if(counter == 0 && (found == false)){
    				$("#news_1").html(append_str);
	    			counter += 1;
	    			found = true;
    			}
    			if(counter==1 && (found == false)){						
    				$("#news_2").html(append_str);
	    			counter += 1;
	    			found = true;
    			}
    			if(counter==2 && (found == false)){
    				$("#news_3").html(append_str);
	    			counter += 1;
	    			found = true;
    			}
    			if(counter>2 && (found == false) && (subcounter==0)){
    				news_rows += "<div class='row-fluid'><div class='span3'>" + append_str +"</div>"; // only for first news item in row
    				subcounter += 1;
	    			counter += 1;
	    			found = true;
				}
    			if(counter>2 && (found == false) && (subcounter==1)){
    				news_rows += "<div class='span3'>" + append_str +"</div>";
    				subcounter += 1;
	    			counter += 1;
	    			found = true;
				}	
    			if(counter>2 && (found == false) && (subcounter==2)){
    				news_rows += "<div class='span3'>" + append_str + "</div></div>";
    				subcounter = 0; // only 3rd news item
	    			counter += 1;
	    			found = true;
				}
	    	}); // close for-each loop
	    	if(subcounter != 0){
	    		news_rows += "</div>";	    		
	    	}
	    	$("#row2").append(news_rows);
	    },
	    error: function(){
	    	$("#wait").html("sorry, category not available");
	    	//alert("sorry, category not available");
	    } 
	});
}

/* search functionality on wikipulse - currently not used */
//function search_wikipulse_service(){
//	var search_input =$("#search_input").val();
	//alert(wp_service_url_free_text_search + search_input);
//	$.ajax({
//	    type: "GET",
//	    url: wp_service_url_free_text_search + search_input,
//	    dataType: "json",
//	    success: function (data) {
//	    	$.each(data,function(i,page_snippet){
//   			var append_str = "<div class='row-fluid'><div class='span6 offset3'>";
//				append_str += "<div id='_content"+i+"'>";
//				append_str += "<a href='" +  page_snippet.urlToFullPage + "'>" +  page_snippet.title + "</a>";
//				append_str += "<p class='p-small'>" + page_snippet.snippet + "</p>";
//     			append_str += "</div></div></div><hr>";
//      			$("#wp_service_search_results").append(append_str);
//	    	});
//	    }
//	});
//}

/* get most read news functionality on wikipulse and display them */
function loadMostReadNews(){
	$.ajax({
	    type: "GET",
	    url: wp_service_route_news + "?sort=views&limit=" + mostreadstories_limit,
	    dataType: "json",
	    success: function (data) {	    	
	    	var append_str = "<div class='nav-header p-small'>Most Read Stories</div>"+
	    					"<div class='row-fluid'><div class='span12' style='text-align:center;'>"+
	    						"<table class='table table-condensed' style='margin-bottom:0px'><tr>";
	    	$.each(data,function(i,news){
    			if(news.pageTitle.indexOf(":") < 0)
    				{
    					append_str += "<td><a href='#newsid=" + news.id + "' class='show_detail' newsid='" + news.id + "'>" + news.pageTitle + '&nbsp;(&nbsp;' + news.viewCount + '&nbsp;click(s))&nbsp;' +'</a></td>';
	    				//append_str += '<td><a href="http://' +  news.url + '">' + decodeURIComponent(news.pageTitle) + '&nbsp;(&nbsp;' + news.count + '&nbsp;click(s))&nbsp;' +'</a></td>';		
    				}	    			
	    	});
	    	append_str += "</tr></table></div></div>";	    	
	    	$("#most_read_stories").html(append_str);
	    }
	});	
}
/* get category structure and load it if desired, e.g. bookmarks */
function loadCategories(){
	$.ajax({
	    type: "GET",
	    url: wp_service_route_categories + "?limit=" + category_no_limit,
	    dataType: "json",
	    success: function (data) {	    	
	    	$.each(data,function(i,category){
	    		$(".top_nav").append($("<li/>").append($("<a/>", {
	    		    "href": "#cat=" + category.id,
	    		    "class": "nav_link",
	    		    "text": category.title
	    		})));
	    		$(".top_nav").append($("<li/>", { "class": "divider-vertical" }));
	    	});
	    	if (window.location.toString().indexOf("#cat=", 0) > 0){
	    		var current_doc = "";
	    		current_doc = window.location.toString().substring(window.location.toString().lastIndexOf("#cat=")+5,window.location.toString().length).toLowerCase();
	    		//alert(current_doc);
	    		switch(current_doc)
	    		{
	    			case "":
	    				loadDoc("home.html");
	    				setActiveClass("home");
	    				break;
	    			case "home":
	    				loadDoc("home.html");
	    				setActiveClass("home");
	    				break;
	    			case "aboutus":
	    				loadDoc("aboutus.html");
	    				setActiveClass("");
	    				break;
	    			case "contactus":
	    				loadDoc("contactus.html");
	    				setActiveClass("");
	    				break;
	    			default:
	    				loadDoc("news.html");
	    				setActiveClass(current_doc);
	    				break;
	    		}
	    	}
	    	else if (window.location.toString().indexOf("#newsid=", 0) > 0){
	    		//var news_id = "";
	    		//news_id = window.location.toString().substring(window.location.toString().lastIndexOf("#newsid=")+9,window.location.toString().length).toLowerCase();
	    		//alert(news_id);
	    		loadDoc("news_detail.html");
	    	}
	    	else {
	    		//alert("load home, no parameter passed");
	    		loadDoc("home.html");
	    		setActiveClass("home");
	    	} 
	    },
	    error: function(){
	    	//alert("error, no result");
			loadDoc("home.html");
			setActiveClass("home");
	    }
	});
}

/* increase view counter for news */
function increaseViewCounter(newsID){	
	$.ajax({
	    type: "PUT",
	    url: wp_service_route_news + "/" + newsID,
	    dataType: "json",
	    success: function (data) {
	    }
	});
}

/* create HTML-code for image slide for news */
function createImageSlideshow(newsId,news_counter,IMGurlList){
	if ((jQuery.isEmptyObject(IMGurlList)) || (IMGurlList == "") || (IMGurlList == undefined) ){
		IMGurlList[0] = wp_service_url + "/img/img_not_found.jpg";
	}	
	var div_slideshow = "<div id='myCarousel_" + newsId
	+ "' class='carousel slide'><div class='carousel-inner'>";
	
	for(var j=0;j<IMGurlList.length;j++){
		if(j==0){
			div_slideshow += "<div class='item active'>";
		}
		else {
			div_slideshow += "<div class='item'>";
		} 
		if (news_counter >= 3)
			div_slideshow += "<img class='img-rounded img_size_news_small' src='"+ IMGurlList[j] +"' alt=''>";
		else if (news_counter >= 1)
			div_slideshow += "<img class='img-rounded img_size_news_small' src='"+ IMGurlList[j] +"' alt=''>";
		else 
			div_slideshow += "<img class='img-rounded img_size_news_big' src='"+ IMGurlList[j] +"' alt=''>";
		
		div_slideshow += "</div>";
    };
    div_slideshow += "</div>";
    div_slideshow += "<a class='left carousel-control' href='#myCarousel_"+ newsId + "' data-slide='prev'>&lsaquo;</a>";
    div_slideshow += "<a class='right carousel-control' href='#myCarousel_" + newsId + "' data-slide='next'>&rsaquo;</a>";
    div_slideshow += "</div>";
    return div_slideshow;
}

/* returns url parameter as an array, whole parameter string must start with a "#" */
function getUrlVars()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf("#") + 1).split("&");
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split("=");
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

/* Set DateTime - value on website */
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

    this.weekdays = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    this.months = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
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
