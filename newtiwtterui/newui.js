var negative=0;
var postive=0;
var neutral=0;

function changeFunction()
{
                $('#tumblrButton').hide();
                $('#twitterButton').hide();
                  $('#tmsbutton').hide();
                          $('#multimedia').text('');
                          $('#maindiv').hide();
                                $('#newdiv').hide();
                                      $('#onemorediv').hide();
                 $('#legend').hide();
                 d3.select("svg")
                        .remove();


    if($("#socialType :selected").text()=="TUMBLR")
    {
                $('#tumblrButton').show();
    }
     if($("#socialType :selected").text()=="TWITTER")
        {
                    $('#twitterButton').show();
        }
    if($("#socialType :selected").text()=="TMS")
            {
                        $('#tmsbutton').show();
            }

}


$(document).ready(function(){
$('#videos').click(function(){
        $('#legend').hide();
d3.select("svg")
       .remove();
        $('#multimedia').text('');
        $('#onemorediv').hide();
            $('#multimedia').show();
  var file="jun"+"tumblerVideo"+$("#ddlViewBy :selected").text()+".tsv";
        $('#multimedia').append("<h3> <p class=\"text-primary\">Video Posts</p></h3>");
  $.get(file, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                  $('#multimedia').append('<div> <p class=\"text-primary\">'+item+'</p></div>');
          });
      }, "text")

});

$('#audio').click(function(){
        $('#legend').hide();
d3.select("svg")
       .remove();
        $('#multimedia').text('');
        $('#onemorediv').hide();
            $('#multimedia').show();
  var file="jun"+"tumbleraudio"+$("#ddlViewBy :selected").text()+".tsv";
        $('#multimedia').append("<h3> <p class=\"text-primary\">Audio Posts</p></h3>");
  $.get(file, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                  $('#multimedia').append('<div><p>'+item+'</p></div>');
          });
      }, "text")

});


$('#text').click(function(){
        $('#legend').hide();
d3.select("svg")
       .remove();
        $('#multimedia').text('');
        $('#onemorediv').hide();
            $('#multimedia').show();
  var filenew="jun"+"tumblerText"+$("#ddlViewBy :selected").text()+".tsv";
        $('#multimedia').append("<h3> <p class=\"text-primary\">Text Posts</p></h3>");
  $.get(filenew, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                   $('#multimedia').append('<br><br/>');
                  $('#multimedia').append('<div class=\"bg-warning\"><p >'+item+'</p></div>');
          });
      }, "text")

});


$('#tmsphoto').click(function(){
        $('#legend').hide();
d3.select("svg")
       .remove();
    $('#multimedia').text('');
                $('#onemorediv').hide();
    $('#multimedia').show();
  var file="tmsphoto"+$("#ddlViewBy :selected").text()+".tsv";

        $('#multimedia').append("<h3> <p class=\"text-primary\">TMS Photos</p></h3>");
  $.get(file, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                  $('#multimedia').append('<div><p><img src="'+item+'"</p></div>');
          });
      }, "text")

});

$('#info').click(function(){
        $('#legend').hide();
d3.select("svg")
       .remove();
    $('#multimedia').text('');
                $('#onemorediv').hide();
    $('#multimedia').show();
             $('#multimedia').append("<br/><br/><p class=\"text-center\"><h4> <p class=\"text-primary\">TMS INFO</p></h4></p><br/>");

  var file="tmstitle"+$("#ddlViewBy :selected").text()+".tsv";
             $.get(file, function(data) {
                 var lines = data.split("\n");
                          $('#multimedia').append("<h6> <p class=\"text-info\">TITLE</p></h6>");
                           $.each(lines, function(n, item) {
                             $('#multimedia').append('<div class=\"bg-warning\"><p>' + item.linkify() + '</p></div><br/>');
                     });
                 }, "text")

 var file="tmsdesc"+$("#ddlViewBy :selected").text()+".tsv";
         $.get(file, function(data) {
             var lines = data.split("\n");
                                       $('#multimedia').append("<h6> <p class=\"text-info\">DESCRIPTION</p></h6>");
                       $.each(lines, function(n, item) {
                         $('#multimedia').append('<div class=\"bg-warning\"><p>' + item.linkify() + '</p></div><br/>');
                 });
             }, "text")
   var file="tmsgenre"+$("#ddlViewBy :selected").text()+".tsv";
           $.get(file, function(data) {
               var lines = data.split("\n");
                                         $('#multimedia').append("<h6> <p class=\"text-info\">GENRE</p></h6>");
                         $.each(lines, function(n, item) {
                           $('#multimedia').append('<div class=\"bg-warning\"><p>' + item.linkify() + '</p></div>');
                   });
               }, "text")


});


$('#photo').click(function(){
        $('#legend').hide();
d3.select("svg")
       .remove();
    $('#multimedia').text('');
                $('#onemorediv').hide();
    $('#multimedia').show();
  var file="jun"+"tumblerPhoto"+$("#ddlViewBy :selected").text()+".tsv";

        $('#multimedia').append("<h3> <p class=\"text-primary\">Photo Posts</p></h3>");
  $.get(file, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                  $('#multimedia').append('<div><p><img src="'+item+'"</p></div>');
          });
      }, "text")

});

$('#graph').click(function(){
$('#multimedia').hide();
        $('#legend').show();
showGraph("jun"+"tumblergraph"+$("#ddlViewBy :selected").text()+".tsv");
});





function showGraph(fileNme)
{
d3.select("svg")
       .remove();
       var margin = {top: 20, right: 80, bottom: 30, left: 50},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

var parseDate = d3.time.format("%Y%m%d").parse;

var x = d3.time.scale()
    .range([0, width]);

var y = d3.scale.linear()
    .range([height, 0]);

var color = d3.scale.category10();

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");

var line = d3.svg.line()
    .interpolate("basis")
    .x(function(d) { return x(d.date); })
    .y(function(d) { return y(d.temperature); });

var svg = d3.select("body").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
d3.tsv(fileNme, function(error, data) {
  color.domain(d3.keys(data[0]).filter(function(key) { return key !== "date"; }));

  data.forEach(function(d) {
    d.date = parseDate(d.date);
  });

  var cities = color.domain().map(function(name) {
    return {
      name: name,
      values: data.map(function(d) {
        return {date: d.date, temperature: +d[name]};
      })
    };
  });

  x.domain(d3.extent(data, function(d) { return d.date; }));

  y.domain([
    d3.min(cities, function(c) { return d3.min(c.values, function(v) { return v.temperature; }); }),
    d3.max(cities, function(c) { return d3.max(c.values, function(v) { return v.temperature; }); })
  ]);

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "start")
      .text("Post");

  var city = svg.selectAll(".city")
      .data(cities)
    .enter().append("g")
      .attr("class", "city");

  city.append("path")
      .attr("class", "line")
      .attr("d", function(d) { return line(d.values); })
      .style("stroke", function(d) { return color(d.name); });

  city.append("text")
      .datum(function(d) { return {name: d.name, value: d.values[d.values.length - 1]}; })
      .attr("transform", function(d) { return "translate(" + x(d.value.date) + "," + y(d.value.temperature) + ")"; })
      .attr("x", 3)
      .attr("dy", ".35em")
      .attr("text-anchor", "start")
      .text(function(d) { return d.name; });

      legend = svg.append("g")
          .attr("class","legend")
          .attr("transform","translate(50,30)")
          .style("font-size","12px")
          .call(d3.legend)

        setTimeout(function() {
          legend
            .style("font-size","20px")
            .attr("data-style-padding",10)
            .call(d3.legend)
        },1000)

});
}
//
//$('#all').click(function() {
// $('#maindiv').hide();
//       $('#newdiv').hide();
//     $('#positivetweet').text('');
//   $('#tweet').text('');
//showGraph("jun7to10"+$("#ddlViewBy :selected").text()+"all.tsv");
//
//});

$('#top').click(function() {
 $('#maindiv').hide();
       $('#newdiv').hide();
                    $('#onemorediv').hide();
   $('#tweet').text('');
       $('#positivetweet').text('');
              $('#negativeTweet').text('');

showGraph("jun7to10"+"combinationtweet.tsv");

});


$('#trending').click(function() {
d3.select("svg")
       .remove();
             $('#newdiv').hide();

         $('#tweet').text('');
             $('#positivetweet').text('');
                    $('#negativeTweet').text('');
                    $('#onemorediv').hide();

        $('#maindiv').show();

});

$('#sentiment').click(function(){
$('#maindiv').hide();
      $('#newdiv').hide();
            $('#onemorediv').hide();


   $('#tweet').text('');
       $('#positivetweet').text('');
              $('#negativeTweet').text('');

showGraph("jun7to10"+$("#ddlViewBy :selected").text()+"all.tsv");
});

$('#all').click(function(){
d3.select("svg")
       .remove();
   $('#maindiv').hide();
      $('#newdiv').show();
                $('#onemorediv').hide();

  $('#tweet').text('');
   $('#positivetweet').text('');
          $('#negativeTweet').text('');

  var file="jun7to10"+$("#ddlViewBy :selected").text()+"alltext.tsv";
                    $('#tweet').append("<p class=\"text-center\"><h3> <p class=\"text-primary\">All Tweets</p></h3></p><br/>")
  $.get(file, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                var jsonText=$.parseJSON(item);
                  $('#tweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');

          });
      }, "text")

                          $('#positivetweet').append("<p class=\"text-center\"><h3> <p class=\"text-primary\">Positive Tweets</p></h3></p><br/>")

       var file="jun7to10"+$("#ddlViewBy :selected").text()+"positivetext.tsv";
        $.get(file, function(data) {
            var lines = data.split("\n");
                      $.each(lines, function(n, item) {
                      var jsonText=$.parseJSON(item);
                        $('#positivetweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');

                });
            }, "text")
});


$('#geo').click(function(){

$('#maindiv').hide();
      $('#newdiv').hide();
                  $('#onemorediv').show();

   $('#tweet').text('');
       $('#positivetweet').text('');
       $('#negativeTweet').text('');
         $('#chart_div').text('');
              $('#chart_div').show();
              drawRegionsMap();

});

function drawRegionsMap() {

            var randomNum = Math.floor(Math.random() * 100);

                      var data = google.visualization.arrayToDataTable([
                        ['Country', 'Popularity'],
                        ['Germany', Math.floor(Math.random() * 100)],
                        ['United States', Math.floor(Math.random() * 1000)],
                        ['Brazil', Math.floor(Math.random() * 300)],
                        ['Canada', Math.floor(Math.random() * 500)],
                        ['France', Math.floor(Math.random() * 100)],
                        ['RU', Math.floor(Math.random() * 600)],
                        ['INDIA', Math.floor(Math.random() * 400)]

                      ]);

                      var options = {};

                      var chart = new google.visualization.GeoChart(document.getElementById('chart_div'));
                      chart.draw(data, options);



}

				$("#wordcloud2").awesomeCloud({
					"size" : {
						"grid" : 9,
						"factor" : 1
					},
					"options" : {
						"color" : "random-dark",
						"rotationRatio" : 0.35
					},
					"font" : "'Times New Roman', Times, serif",
					"shape" : "circle"
				});


function showGraph(fileNme)
{
d3.select("svg")
       .remove();
       var margin = {top: 20, right: 80, bottom: 30, left: 50},
    width = 960 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

var parseDate = d3.time.format("%Y%m%d").parse;

var x = d3.time.scale()
    .range([0, width]);

var y = d3.scale.linear()
    .range([height, 0]);

var color = d3.scale.category10();

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");

var line = d3.svg.line()
    .interpolate("basis")
    .x(function(d) { return x(d.date); })
    .y(function(d) { return y(d.temperature); });

var svg = d3.select("body").append("tweet").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//var fileNme="jun7to14KeepingUpWiththeKardashians.tsv";
d3.tsv(fileNme, function(error, data) {
  color.domain(d3.keys(data[0]).filter(function(key) { return key !== "date"; }));

  data.forEach(function(d) {
    d.date = parseDate(d.date);
  });

  var cities = color.domain().map(function(name) {
    return {
      name: name,
      values: data.map(function(d) {
        return {date: d.date, temperature: +d[name]};
      })
    };
  });

  x.domain(d3.extent(data, function(d) { return d.date; }));

  y.domain([
    d3.min(cities, function(c) { return d3.min(c.values, function(v) { return v.temperature; }); }),
    d3.max(cities, function(c) { return d3.max(c.values, function(v) { return v.temperature; }); })
  ]);

  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "start")
      .text("Post");

  var city = svg.selectAll(".city")
      .data(cities)
    .enter().append("g")
      .attr("class", "city");

  city.append("path")
      .attr("class", "line")
      .attr("d", function(d) { return line(d.values); })
      .style("stroke", function(d) { return color(d.name); });

  city.append("text")
      .datum(function(d) { return {name: d.name, value: d.values[d.values.length - 1]}; })
      .attr("transform", function(d) { return "translate(" + x(d.value.date) + "," + y(d.value.temperature) + ")"; })
      .attr("x", 3)
      .attr("dy", ".35em")
      .attr("text-anchor", "start")
      .text(function(d) { return d.name; });

});
}

function relative_time(time_value) {
  var values = time_value.split(" ");
  time_value = values[1] + " " + values[2] + ", " + values[5] + " " + values[3];
  var parsed_date = Date.parse(time_value);
  var relative_to = (arguments.length > 1) ? arguments[1] : new Date();
  var delta = parseInt((relative_to.getTime() - parsed_date) / 1000);
  delta = delta + (relative_to.getTimezoneOffset() * 60);

  var r = '';
  if (delta < 60) {
        r = 'a minute ago';
  } else if(delta < 120) {
        r = 'couple of minutes ago';
  } else if(delta < (45*60)) {
        r = (parseInt(delta / 60)).toString() + ' minutes ago';
  } else if(delta < (90*60)) {
        r = 'an hour ago';
  } else if(delta < (24*60*60)) {
        r = '' + (parseInt(delta / 3600)).toString() + ' hours ago';
  } else if(delta < (48*60*60)) {
        r = '1 day ago';
  } else {
        r = (parseInt(delta / 86400)).toString() + ' days ago';
  }

      return r;
  }

  String.prototype.linkify = function() {
         return this.replace(/[A-Za-z]+:\/\/[A-Za-z0-9-_]+\.[A-Za-z0-9-_:%&\?\/.=]+/, function(m) {
                return m.link(m);
        });
  };
});
