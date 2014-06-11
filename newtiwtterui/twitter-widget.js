var negative=0;
var postive=0;
var neutral=0;

$(document).ready(function(){

//$.getJSON('twit.txt', function(data){
//        $.each(data, function(index, item){
//                $('#tweet').append('<div><p>' + item.text.linkify() + '</p></div>' + '<div id="web_intent">' + '<span class="time">' + relative_time(item.created_at) + '</span>' + '<img src="images/retweet_mini.png" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + item.id_str + '">' + 'Retweet</a>' + '<img src="images/reply_mini.png" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + item.id_str + '">' + 'Reply</a>' + '<img src="images/favorite_mini.png" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + item.id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');
//    });
//},"text");

//var term = $('#term').val();
//var artistURL = "http://localhost:8080/rest/twitter/getTweet?query="+term;
//        var returnData = "";
//
//var result = jQuery.get('http://localhost:8080/rest/twitter/getTweet?query="'+term);
//
//        jQuery.ajax({
//            url: artistURL,
//            success: function(html) {
//                alert(html);
//            }
//            },
//            async:false
//          });
$('#showTweet').click(function() {
  $('#tweet').text('');

$.get('files/'+$("#ddlViewBy :selected").text()+"sentiment", function(data) {
                var lines = data.split("\n");
                var count=0;
                $.each(lines, function(n, item) {
                      count++;
                      if(count==1)
                      {
                          postive=parseFloat(item);
                      }
                      if(count==2)
                                      {
                                                              neutral=parseFloat(item);

                                      }
                                      if(count==3)
                                                      {
                                                                              negative=parseFloat(item);

                                                      }
                          });
                      }, "text")
  //alert(this.id);
  $.get('files/'+$("#ddlViewBy :selected").text(), function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                var jsonText=$.parseJSON(item);
                  $('#tweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');

          });
      }, "text")


});

$('#positive').click(function() {
  $('#tweet').text('');

  //alert(this.id);
  $.get('files/'+$("#ddlViewBy :selected").text()+"positive", function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                var jsonText=$.parseJSON(item);
                  $('#tweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');

          });
      }, "text")


});

$('#negative').click(function() {
  $('#tweet').text('');

  //alert(this.id);
  $.get('files/'+$("#ddlViewBy :selected").text()+"negative", function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                var jsonText=$.parseJSON(item);
                  $('#tweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');

          });
      }, "text")


});


$('#neutral').click(function() {
  $('#tweet').text('');

  //alert(this.id);
  $.get('files/'+$("#ddlViewBy :selected").text()+"neutral", function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                var jsonText=$.parseJSON(item);
                  $('#tweet').append('<div><p>' + jsonText.text.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">' + relative_time(jsonText.created_at) + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + jsonText.id_str + '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + jsonText.id_str + '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + jsonText .id_str + '">' + 'Favorite</a>' + '</div>' + '<hr />');

          });
      }, "text")


});

$('#trends').click(function() {
  $('#tweet').text('');

  //alert(this.id);
  $.get('files/'+"trends", function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {

                  $('#tweet').append('<div><p>' + item + '</p></div>');
          });
      }, "text")


});

$('#tweetsentimental').click(function() {
   var w = 300, //width
  h = 300, //height
  r = 100, //radius
  color = d3.scale.category20c(); //builtin range of colors
    var data = new Array(1000);

if(negative>0)
{
  data = [{"label":"+", "value":postive},
  {"label":"0", "value":neutral},
  {"label":"-", "value":negative}];
  }
  else
  {
     data = [{"label":"+", "value":postive},
      {"label":"0", "value":neutral}
      ];
  }

  var vis = d3.select("body")
  .append("svg:svg") //create the SVG element inside the <body>
  .data([data]) //associate our data with the document
  .attr("width", w) //set the width and height of our visualization (these will be attributes of the <svg> tag
  .attr("height", h)
  .append("svg:g") //make a group to hold our pie chart
  .attr("transform", "translate(" + r + "," + r + ")") //move the center of the pie chart from 0, 0 to radius, radius

  var arc = d3.svg.arc() //this will create <path> elements for us using arc data
  .outerRadius(r);

  var pie = d3.layout.pie() //this will create arc data for us given a list of values
  .value(function(d) { return d.value; }); //we must tell it out to access the value of each element in our data array

  var arcs = vis.selectAll("g.slice") //this selects all <g> elements with class slice (there aren't any yet)
  .data(pie) //associate the generated pie data (an array of arcs, each having startAngle, endAngle and value properties)
  .enter() //this will create <g> elements for every "extra" data element that should be associated with a selection. The result is creating a <g> for every object in the data array
  .append("svg:g") //create a group to hold each slice (we will have a <path> and a <text> element associated with each slice)
  .attr("class", "slice"); //allow us to style things in the slices (like text)

  arcs.append("svg:path")
  .attr("fill", function(d, i) { return color(i); } ) //set the color for each slice to be chosen from the color function defined above
  .attr("d", arc); //this creates the actual SVG path using the associated data (pie) with the arc drawing function

  arcs.append("svg:text") //add a label to each slice
  .attr("transform", function(d) { //set the label's origin to the center of the arc
  //we have to make sure to set these before calling arc.centroid
  d.innerRadius = 0;
  d.outerRadius = r;
  return "translate(" + arc.centroid(d) + ")"; //this gives us a pair of coordinates like [50, 50]
  })
  .attr("text-anchor", "middle") //center the text on it's origin
  .text(function(d, i) { return data[i].label; });
 });


});






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