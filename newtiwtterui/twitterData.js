var negative=0;
var postive=0;
var neutral=0;

$(document).ready(function(){

//$('#showTweet').click(function() {
// $('#maindiv').hide();
//   $('#tweet').text('');
//showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+".tsv");
//
//});
//
//$('#positive').click(function() {
// $('#maindiv').hide();
//   $('#tweet').text('');
//showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+"positive.tsv");
//
//});
//
//$('#negative').click(function() {
// $('#maindiv').hide();
//   $('#tweet').text('');
//showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+"negative.tsv");
//
//});
//
//$('#neutral').click(function() {
// $('#maindiv').hide();
//   $('#tweet').text('');
//showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+"neutral.tsv");
//
//});

$('#all').click(function() {
 $('#maindiv').hide();
       $('#newdiv').hide();
     $('#positivetweet').text('');
   $('#tweet').text('');
showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+"all.tsv");

});

$('#top').click(function() {
 $('#maindiv').hide();
       $('#newdiv').hide();

   $('#tweet').text('');
       $('#positivetweet').text('');
showGraph($("#time :selected").text()+"combinationtweet.tsv");

});

//
//('#negativeTweet').click(function() {
// $('#maindiv').hide();
//showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+"negative.tsv");
//
//});
//
//('#neutralTweet').click(function() {
// $('#maindiv').hide();
//showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+"neutral.tsv");
//
//});
//


//('#positiveTweet').click(function() {
////$('#maindiv').hide();
//alert($("#time :selected").text()+$("#ddlViewBy :selected").text()+"positive.tsv");
//showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+"positive.tsv");
//
//});

$('#trending').click(function() {
d3.select("svg")
       .remove();
             $('#newdiv').hide();

         $('#tweet').text('');
             $('#positivetweet').text('');
        $('#maindiv').show();

});

//$('#pos').click(function(){
//d3.select("svg")
//       .remove();
//   $('#maindiv').hide();
//  $('#tweet').text('')  ;
//    var file=$("#time :selected").text()+$("#ddlViewBy :selected").text()+"positivetext.tsv";
//  $.get(file, function(data) {
//      var lines = data.split("\n");
//                $.each(lines, function(n, item) {
//                var jsonText=$.parseJSON(item);
//                  $('#tweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');
//
//          });
//      }, "text")
//});


$('#sentiment').click(function(){
$('#maindiv').hide();
      $('#newdiv').hide();

   $('#tweet').text('');
       $('#positivetweet').text('');
showGraph($("#time :selected").text()+$("#ddlViewBy :selected").text()+"all.tsv");
});

$('#all').click(function(){
d3.select("svg")
       .remove();
   $('#maindiv').hide();
      $('#newdiv').show();

  $('#tweet').text('');
   $('#positivetweet').text('');
  var file=$("#time :selected").text()+$("#ddlViewBy :selected").text()+"alltext.tsv";
                    $('#tweet').append("<h1><p>Tweets</p></h1>")
  $.get(file, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                var jsonText=$.parseJSON(item);
                  $('#tweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');

          });
      }, "text")

                          $('#positivetweet').append("<h1><p>Positive Tweets</p></h1>")

       var file=$("#time :selected").text()+$("#ddlViewBy :selected").text()+"positivetext.tsv";
        $.get(file, function(data) {
            var lines = data.split("\n");
                      $.each(lines, function(n, item) {
                      var jsonText=$.parseJSON(item);
                        $('#positivetweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');

                });
            }, "text")
});





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
      .text("Tweets");

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
