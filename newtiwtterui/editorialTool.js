var negative=0;
var postive=0;
var neutral=0;
function foobar_cont(){
    console.log("finished.");
};
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
                     if($("#ddlViewBy :selected").text()=="TheCondorHeroes" || $("#ddlViewBy :selected").text()=="BuBuJingQing"
                     ||$("#ddlViewBy :selected").text()=="LeJunKai"||$("#ddlViewBy :selected").text()=="TheLegendofZhenHuan")
                        {
                $('#chinad').show();
                $('#usd').hide();
                $('#indiad').hide();




                        }
                        else if ($("#ddlViewBy :selected").text()=="Mahabharat" ||
                        $("#ddlViewBy :selected").text()=="BalikaVadhu")
                        {
                            $('#usd').hide();
                              $('#chinad').hide();
                                $('#indiad').show();

                        }
                        else
                        {
                $('#usd').show();
                $('#chinad').hide();
                 $('#indiad').hide();



                        }
        }
    if($("#socialType :selected").text()=="TMS")
            {
                        $('#tmsbutton').show();
                         if($("#ddlViewBy :selected").text()=="PhirBhiDilHaiHindustani" || $("#ddlViewBy :selected").text()=="AkeleHumAkeleTum"
                                             )
                                                {
                                                                                                                        $('#watsond').show();

                                                }
                                                else
                                                {
                                                                    $('#watsond').hide();

                                                }
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
                  $('#multimedia').append('<div><p><img   src="'+item+'"</p></div>');
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


$('#watson').click(function(){
        $('#legend').hide();
d3.select("svg")
       .remove();
    $('#multimedia').text('');
                $('#onemorediv').hide();
    $('#multimedia').show();
             $('#multimedia').append("<br/><br/><p class=\"text-center\"><h4> <p class=\"text-primary\">WhatsON INFO</p></h4></p><br/>");

  var file=$("#ddlViewBy :selected").text()+".tsv";
             $.get(file, function(data) {
                              var lines = data.split("\n");
                           $.each(lines, function(n, item) {
                            var jsonText=$.parseJSON(item);
                                $('#multimedia').append("<h6> <p class=\"text-info\">TITLE</p></h6>");
                             $('#multimedia').append('<div class=\"bg-warning\"><p>' + jsonText.title.linkify() + '</p></div><br/>');
                              $('#multimedia').append("<h6> <p class=\"text-info\">WhatsON ID</p></h6>");
                               $('#multimedia').append('<div class=\"bg-warning\"><p>' + jsonText.id.linkify() + '</p></div><br/>');
                                $('#multimedia').append("<h6> <p class=\"text-info\">TMS ID</p></h6>");
                                 $('#multimedia').append('<div class=\"bg-warning\"><p>' + jsonText.tms_id.linkify() + '</p></div><br/>');
                                   $('#multimedia').append("<h6> <p class=\"text-info\">Summary</p></h6>");
                                    $('#multimedia').append('<div class=\"bg-warning\"><p>' + jsonText.desc.linkify() + '</p></div><br/>');
                                      $('#multimedia').append("<h6> <p class=\"text-info\">Description</p></h6>");
                                  $('#multimedia').append('<div class=\"bg-warning\"><p>' + jsonText.long_desc.linkify() + '</p></div><br/>');
                                         $('#multimedia').append("<h6> <p class=\"text-info\">RELEASE YEAR</p></h6>");
                                    $('#multimedia').append('<div class=\"bg-warning\"><p>' + jsonText.prod_year.linkify() + '</p></div><br/>');
                                         $('#multimedia').append("<h6> <p class=\"text-info\">LANGUAGE</p></h6>");
                                         if(jsonText.lang=="hi")
                                              $('#multimedia').append('<div class=\"bg-warning\"><p>Hindi</p></div><br/>');



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
     var bottom=1000;
     var top=10000;
     var count= Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
     var totalCount = count.toString();
      //   requestData={criteria:"ok", maxNumberOfItems:"10",totalCount:count};
       requestData={show:$("#ddlViewBy :selected").text(), bottomTime:$("#datepicker").val(),
       upperTime:$("#enddatepicker").val(),
       id:totalCount};

                   $.ajax('http://localhost:9199/rest/test/tumblr/photo', {
                                               type: 'POST',
                                               headers: {
                                                              Accept : "application/json; charset=utf-8",
                                                              "Content-Type": "application/json; charset=utf-8"

                                                   },
                                               dataType:"jsonp",
                                                               jsonCallback: "jsonp",
                                                               data:requestData,
                                                   async:false,
                                                   success: AjaxSucceeded,
                                                    error: AjaxFailed,

                                    }).done(function(data) {console.log(data);
                                                            });;

sleep(3000, foobar_cont);



//
//                   $.ajax('http://localhost:9999/rest/test/ak', {
//                                               type: 'POST',
//                                               headers: {
//                                                              Accept : "application/json; charset=utf-8",
//                                                              "Content-Type": "application/json; charset=utf-8"
//
//                                                   },
//                                               dataType:"jsonp",
//                                                               jsonCallback: "jsonp",
//                                                               data:requestData,
//                                                   async:false,
//                                                   success: AjaxSucceeded,
//                                                    error: AjaxFailed,
//
//                                    }).done(function(data) {alert("workkkk"+data);
//                                                            });;






alert("does it finish");

});

function sleep(millis, callback) {
    setTimeout(function()
            { callback(); }
    , millis);
}
function jsonp(result)
{
 console.log(result);
        alert("coy");
    var json = $.parseJSON(result);
    alert(json);
}


    function AjaxSucceeded(result) {
        console.log(result);
        alert("boy");
    var json = $.parseJSON(result);
    alert(json);
        }



    function AjaxFailed(result) {
    console.log(result);
//        alert(result.status);
//                alert(result.statusText);

    }

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

          if($("#ddlViewBy :selected").text()=="TheCondorHeroes" || $("#ddlViewBy :selected").text()=="BuBuJingQing"
                               ||$("#ddlViewBy :selected").text()=="LeJunKai"||$("#ddlViewBy :selected").text()=="TheLegendofZhenHuan"
                               || $("#ddlViewBy :selected").text()=="Mahabharat" ||
                                                          $("#ddlViewBy :selected").text()=="BalikaVadhu"||
                                                          $("#ddlViewBy :selected").text()=="AkeleHumAkeleTum" ||
                                                                                                                    $("#ddlViewBy :selected").text()=="PhirBhiDilHaiHindustani")
                                  {
                        var file="jun7to10"+$("#ddlViewBy :selected").text()+"alltext.tsv";
                                            $('#tweet').append("<p class=\"text-center\"><h3> <p class=\"text-primary\">All Tweets</p></h3></p><br/>")
                          $.get(file, function(data) {
                              var lines = data.split("\n");
                                        $.each(lines, function(n, item) {
                                                $('#tweet').append('<div><p>' + item.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">'  + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + 1+ '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + 1+ '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + 1 + '">' + 'Favorite</a>' + '</div>' + '<hr />');

                                  });
                              }, "text")

                                                  $('#positivetweet').append("<p class=\"text-center\"><h3> <p class=\"text-primary\">Positive Tweets</p></h3></p><br/>")

                               var file="jun7to10"+$("#ddlViewBy :selected").text()+"positivetext.tsv";
                                $.get(file, function(data) {
                                    var lines = data.split("\n");
                                              $.each(lines, function(n, item) {
                                                $('#positivetweet').append('<div><p>' + item.linkify() + '</p></div>'+ '<div id="web_intent">' + '<span class="time">'  + '</span>' + '<img src="index.jpg" width="16" height="16" alt="Retweet">' + '<a href="http://twitter.com/intent/retweet?tweet_id=' + 1+ '">' + 'Retweet</a>' + '<img src="index.jpg" width="16" height="16" alt="Reply">' + '<a href="http://twitter.com/intent/tweet?in_reply_to=' + 1+ '">' + 'Reply</a>' + '<img src="index.jpg" width="16" height="16" alt="Favorite">' + '<a href="http://twitter.com/intent/favorite?tweet_id=' + 1 + '">' + 'Favorite</a>' + '</div>' + '<hr />');

                                        });
                                    }, "text")
                                  }
                                  else
                                  {

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
            }
});


$('#usbutton').click(function(){
d3.select("svg")
       .remove();
$('#maindiv').hide();
      $('#newdiv').hide();
         $('#onemorediv').show();

   $('#tweet').text('');
       $('#positivetweet').text('');
       $('#negativeTweet').text('');
         $('#chart_div').text('');
              $('#chart_div').hide();
    countryMapUS();});

$('#geo').click(function(){
d3.select("svg")
       .remove();
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


$('#chinabutton').click(function(){
d3.select("svg")
       .remove();
$('#maindiv').hide();
      $('#newdiv').hide();
                  $('#onemorediv').show();

   $('#tweet').text('');
       $('#positivetweet').text('');
       $('#negativeTweet').text('');
         $('#chart_div').text('');
              $('#chart_div').hide();
                            countryMapIND();});

 $('#indiabutton').click(function(){
 d3.select("svg")
        .remove();
 $('#maindiv').hide();
       $('#newdiv').hide();
                   $('#onemorediv').show();

    $('#tweet').text('');
        $('#positivetweet').text('');
        $('#negativeTweet').text('');
          $('#chart_div').text('');
               $('#chart_div').hide();
                             indiamap();});

                             function indiamap() {
                                   var data = new google.visualization.DataTable();
                                        data.addColumn('string', 'State');
                                        data.addColumn('number', 'POPULARITY');
                             if( $("#ddlViewBy :selected").text()=="Mahabharat")
                             {
                           data.addRow(['IN-AP',10]);data.addRow(['IN-AR',10]);
                           data.addRow(['IN-AS',10]);data.addRow(['IN-BR',200]);data.addRow(['IN-CT',10])
                           ;data.addRow(['IN-GA',5]);data.addRow(['IN-GJ',150]);
                           data.addRow(['IN-HR',15]);data.addRow(['IN-HP',20]);
                           data.addRow(['IN-JK',40]);data.addRow(['IN-JH',50]);
                           data.addRow(['IN-KA',10]);data.addRow(['IN-KL',10]);
                           data.addRow(['IN-MP',160]);data.addRow(['IN-MH',90]);
                           data.addRow(['IN-MN',10]);data.addRow(['IN-ML',40]);
                           data.addRow(['IN-MZ',50]);data.addRow(['IN-NL',10]);
                           data.addRow(['IN-OR',60]);data.addRow(['IN-PB',180]);
                           data.addRow(['IN-RJ',190]);data.addRow(['IN-SK',10]);
                           data.addRow(['IN-TN',10]);data.addRow(['IN-TR',10]);
                           data.addRow(['IN-UT',60]);data.addRow(['IN-UP',300]);
                           data.addRow(['IN-WB',100]);data.addRow(['IN-AN',10]);
                           data.addRow(['IN-CH',10]);data.addRow(['IN-DN',10]);
                           data.addRow(['IN-DD',10]);data.addRow(['IN-DL',10]);
                           data.addRow(['IN-LD',10]);data.addRow(['IN-PY',10]);
                             }
                             else if ($("#ddlViewBy :selected").text()=="BalikaVadhu")
                             {

                              data.addRow(['IN-AP',10]);data.addRow(['IN-AR',10]);
                                                        data.addRow(['IN-AS',10]);data.addRow(['IN-BR',200]);data.addRow(['IN-CT',10])
                                                        ;data.addRow(['IN-GA',5]);data.addRow(['IN-GJ',450]);
                                                        data.addRow(['IN-HR',15]);data.addRow(['IN-HP',20]);
                                                        data.addRow(['IN-JK',40]);data.addRow(['IN-JH',50]);
                                                        data.addRow(['IN-KA',10]);data.addRow(['IN-KL',10]);
                                                        data.addRow(['IN-MP',900]);data.addRow(['IN-MH',800]);
                                                        data.addRow(['IN-MN',10]);data.addRow(['IN-ML',40]);
                                                        data.addRow(['IN-MZ',50]);data.addRow(['IN-NL',10]);
                                                        data.addRow(['IN-OR',60]);data.addRow(['IN-PB',680]);
                                                        data.addRow(['IN-RJ',700]);data.addRow(['IN-SK',10]);
                                                        data.addRow(['IN-TN',10]);data.addRow(['IN-TR',10]);
                                                        data.addRow(['IN-UT',600]);data.addRow(['IN-UP',900]);
                                                        data.addRow(['IN-WB',800]);data.addRow(['IN-AN',10]);
                                                        data.addRow(['IN-CH',10]);data.addRow(['IN-DN',10]);
                                                        data.addRow(['IN-DD',10]);data.addRow(['IN-DL',10]);
                                                        data.addRow(['IN-LD',10]);data.addRow(['IN-PY',10]);
                             }

                                   var formatter = new google.visualization.NumberFormat({prefix: 'Popularity', fractionDigits: 0});
                                         formatter.format(data, 1); // Apply formatter to second column
                                         var stateHeatMap = new google.visualization.GeoChart(document.getElementById('visualization'));
                                         stateHeatMap.draw(data, {width: 960, height: 480, region: 'IN', resolution: 'provinces', legend: {numberFormat: '#,###', textStyle: {color: 'blue', fontSize: 16}} });
                                      }



function countryMapIND() {
      var data = new google.visualization.DataTable();
           data.addColumn('string', 'State');
           data.addColumn('number', 'POPULARITY');
if( $("#ddlViewBy :selected").text()=="BuBuJingQing")
{
data.addRow(['CN-31',19000]);data.addRow(['CN-12',6000]);
data.addRow(['CN-34',7000]);data.addRow(['CN-35',4000]);
data.addRow(['CN-62',1000]);data.addRow(['CN-44',3000]);
data.addRow(['CN-52',1000]);data.addRow(['CN-46',2000]);
data.addRow(['CN-13',3000]);data.addRow(['CN-23',1000]);
data.addRow(['CN-41',2000]);data.addRow(['CN-42',1000]);
data.addRow(['CN-43',1000]);data.addRow(['CN-32',200]);
data.addRow(['CN-36',1000]);data.addRow(['CN-22',1000]);
data.addRow(['CN-21',5000]);data.addRow(['CN-63',7000]);
data.addRow(['CN-61',1000]);
data.addRow(['CN-37',1000]);data.addRow(['CN-14',1000]);data.addRow(['CN-51',1000]);data.addRow(['CN-71',1000]);
data.addRow(['CN-53',8000]);data.addRow(['CN-33',1000]);
data.addRow(['CN-45',1000]);data.addRow(['CN-15',2000]);data.addRow(['CN-64',1000]);data.addRow(['CN-65',1000]);
data.addRow(['CN-54',1000]);data.addRow(['CN-91',1000]);data.addRow(['CN-92',1000]);
 data.addRow(['CN-71',1000]);data.addRow(['CN-91',1000]);
data.addRow(['CN-92',1000]);
}
else if ($("#ddlViewBy :selected").text()=="TheLegendofZhenHuan")
{
data.addRow(['CN-31',10000]);data.addRow(['CN-12',6000]);
data.addRow(['CN-34',3000]);data.addRow(['CN-35',4000]);
data.addRow(['CN-62',1000]);data.addRow(['CN-44',3000]);
data.addRow(['CN-52',5000]);data.addRow(['CN-46',2000]);
data.addRow(['CN-13',3000]);data.addRow(['CN-23',1000]);
data.addRow(['CN-41',2000]);data.addRow(['CN-42',1000]);
data.addRow(['CN-43',100]);data.addRow(['CN-32',200]);
data.addRow(['CN-36',1000]);data.addRow(['CN-22',1000]);
data.addRow(['CN-21',600]);data.addRow(['CN-63',7000]);
data.addRow(['CN-61',1000]);
data.addRow(['CN-37',1000]);data.addRow(['CN-14',1000]);data.addRow(['CN-51',1000]);data.addRow(['CN-71',1000]);
data.addRow(['CN-53',700]);data.addRow(['CN-33',1000]);
data.addRow(['CN-45',1000]);data.addRow(['CN-15',800]);data.addRow(['CN-64',1000]);data.addRow(['CN-65',1000]);
data.addRow(['CN-54',1000]);data.addRow(['CN-91',1000]);data.addRow(['CN-92',1000]);
 data.addRow(['CN-71',1000]);data.addRow(['CN-91',1000]);
data.addRow(['CN-92',1000]);

}
else if ($("#ddlViewBy :selected").text()=="LeJunKai")
{
data.addRow(['CN-31',7000]);data.addRow(['CN-12',6000]);
data.addRow(['CN-34',7000]);data.addRow(['CN-35',4000]);
data.addRow(['CN-62',1000]);data.addRow(['CN-44',400]);
data.addRow(['CN-52',1000]);data.addRow(['CN-46',2000]);
data.addRow(['CN-13',3000]);data.addRow(['CN-23',1000]);
data.addRow(['CN-41',2000]);data.addRow(['CN-42',1000]);
data.addRow(['CN-43',1000]);data.addRow(['CN-32',200]);
data.addRow(['CN-36',1000]);data.addRow(['CN-22',1000]);
data.addRow(['CN-21',5000]);data.addRow(['CN-63',7000]);
data.addRow(['CN-61',1000]);
data.addRow(['CN-37',1000]);data.addRow(['CN-14',1000]);data.addRow(['CN-51',1000]);data.addRow(['CN-71',1000]);
data.addRow(['CN-53',8000]);data.addRow(['CN-33',1000]);
data.addRow(['CN-45',1000]);data.addRow(['CN-15',600]);data.addRow(['CN-64',1000]);data.addRow(['CN-65',1000]);
data.addRow(['CN-54',1000]);data.addRow(['CN-91',1000]);data.addRow(['CN-92',1000]);
 data.addRow(['CN-71',1000]);data.addRow(['CN-91',1000]);
data.addRow(['CN-92',1000]);
}
      var formatter = new google.visualization.NumberFormat({prefix: 'Popularity', fractionDigits: 0});
            formatter.format(data, 1); // Apply formatter to second column
            var stateHeatMap = new google.visualization.GeoChart(document.getElementById('visualization'));
            stateHeatMap.draw(data, {width: 960, height: 480, region: 'CN', resolution: 'provinces', legend: {numberFormat: '#,###', textStyle: {color: 'blue', fontSize: 16}} });
         }



function countryMapUS() {
      var data = new google.visualization.DataTable();
           data.addColumn('string', 'State');
           data.addColumn('number', 'Popularity');
            data.addRow(['US-AL',Math.floor(Math.random() * 100)]);
           data.addRow(['US-AK',Math.floor(Math.random() * 100)]);
           data.addRow(['US-AZ',Math.floor(Math.random() * 100)]);
           data.addRow(['US-AR',Math.floor(Math.random() * 100)]);
           data.addRow(['US-CA',Math.floor(Math.random() * 100)]);
           data.addRow(['US-CO',Math.floor(Math.random() * 100)]);
           data.addRow(['US-CT',Math.floor(Math.random() * 100)]);
           data.addRow(['US-DE',Math.floor(Math.random() * 100)]);
           data.addRow(['US-DC',Math.floor(Math.random() * 100)]);
           data.addRow(['US-FL',Math.floor(Math.random() * 100)]);
           data.addRow(['US-GA',Math.floor(Math.random() * 100)]);
           data.addRow(['US-HI',Math.floor(Math.random() * 100)]);
           data.addRow(['US-ID',Math.floor(Math.random() * 100)]);
           data.addRow(['US-IL',Math.floor(Math.random() * 100)]);
           data.addRow(['US-IN',Math.floor(Math.random() * 100)]);
           data.addRow(['US-IA',Math.floor(Math.random() * 100)]);
           data.addRow(['US-KS',Math.floor(Math.random() * 100)]);
           data.addRow(['US-KY',Math.floor(Math.random() * 100)]);
           data.addRow(['US-LA',Math.floor(Math.random() * 100)]);
           data.addRow(['US-ME',Math.floor(Math.random() * 100)]);
           data.addRow(['US-MD',Math.floor(Math.random() * 100)]);
           data.addRow(['US-MA',Math.floor(Math.random() * 100)]);
           data.addRow(['US-MI',Math.floor(Math.random() * 100)]);
           data.addRow(['US-MN',Math.floor(Math.random() * 100)]);
           data.addRow(['US-MS',Math.floor(Math.random() * 100)]);
           data.addRow(['US-MO',Math.floor(Math.random() * 100)]);
           data.addRow(['US-MT',Math.floor(Math.random() * 100)]);
           data.addRow(['US-NE',Math.floor(Math.random() * 100)]);
           data.addRow(['US-NV',Math.floor(Math.random() * 100)]);
           data.addRow(['US-NH',Math.floor(Math.random() * 100)]);
           data.addRow(['US-NJ',Math.floor(Math.random() * 100)]);
           data.addRow(['US-NM',Math.floor(Math.random() * 100)]);
           data.addRow(['US-NY',Math.floor(Math.random() * 100)]);
           data.addRow(['US-NC',Math.floor(Math.random() * 100)]);
           data.addRow(['US-ND',Math.floor(Math.random() * 100)]);
           data.addRow(['US-OH',Math.floor(Math.random() * 100)]);
           data.addRow(['US-OK',Math.floor(Math.random() * 100)]);
           data.addRow(['US-OR',Math.floor(Math.random() * 100)]);
           data.addRow(['US-PA',Math.floor(Math.random() * 100)]);
           data.addRow(['US-RI',Math.floor(Math.random() * 100)]);
           data.addRow(['US-SC',Math.floor(Math.random() * 100)]);
           data.addRow(['US-SD',Math.floor(Math.random() * 100)]);
           data.addRow(['US-TN',Math.floor(Math.random() * 100)]);
           data.addRow(['US-TX',Math.floor(Math.random() * 100)]);
           data.addRow(['US-UT',Math.floor(Math.random() * 100)]);
           data.addRow(['US-VT',Math.floor(Math.random() * 100)]);
           data.addRow(['US-VA',Math.floor(Math.random() * 100)]);
           data.addRow(['US-WA',Math.floor(Math.random() * 100)]);
           data.addRow(['US-WV',Math.floor(Math.random() * 100)]);
           data.addRow(['US-WI',Math.floor(Math.random() * 100)]);
           data.addRow(['US-WY',200]);
           data.addRow(['US-AS',440]);
           data.addRow(['US-GU',330]);
           data.addRow(['US-MP',880]);
           data.addRow(['US-PR',900]);
           data.addRow(['US-VI',100]);

            var formatter = new google.visualization.NumberFormat({prefix: 'Popularity', fractionDigits: 0});
            formatter.format(data, 1); // Apply formatter to second column
            var stateHeatMap = new google.visualization.GeoChart(document.getElementById('visualization'));
            stateHeatMap.draw(data, {width: 960, height: 500, region: 'US', resolution: 'provinces', legend: {numberFormat: '#,###', textStyle: {color: 'blue', fontSize: 16}} });
         }





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

                      if($("#ddlViewBy :selected").text()=="Mahabharat")
                                      {
                                            var data = google.visualization.arrayToDataTable([
                                              ['Country', 'Popularity'],
                                              ['Germany', 200],
                                              ['United States', 2000],
                                              ['Brazil', 100],
                                              ['Canada', 10],
                                              ['France', 10],
                                              ['RU', 500],
                                              ['INDIA', 10000]

                                            ]);
                                            }

                                                      if($("#ddlViewBy :selected").text()=="BalikaVadhu")
                                                      {
                                                            var data = google.visualization.arrayToDataTable([
                                                              ['Country', 'Popularity'],
                                                              ['Germany', 10],
                                                              ['United States', 200],
                                                              ['Brazil', 5],
                                                              ['Canada', 10],
                                                              ['France', 10],
                                                              ['RU', 5],
                                                              ['INDIA', 10000]

                                                            ]);
                                                            }

 if($("#ddlViewBy :selected").text()=="TheCondorHeroes")
                                                      {
                                                            var data = google.visualization.arrayToDataTable([
                                                              ['Country', 'Popularity'],
                                                              ['Germany', 10],
                                                              ['United States', 200],
                                                              ['Brazil', 5],
                                                              ['Canada', 10],
                                                              ['France', 10],
                                                              ['RU', 5],
                                                              ['China', 10000]

                                                            ]);
                                                            }
                         if($("#ddlViewBy :selected").text()=="BuBuJingQing")
                                                                            {
                                                                                  var data = google.visualization.arrayToDataTable([
                                                                                    ['Country', 'Popularity'],
                                                                                   ['Germany', 10],
                                                                                    ['United States', 100],
                                                                                    ['Brazil', 5],
                                                                                    ['Canada', 10],
                                                                                    ['France', 10],
                                                                                    ['RU', 5],
                                                                                    ['China', 10000]

                                                                                  ]);
                                                                                  }
                          if($("#ddlViewBy :selected").text()=="LeJunKai")
                                                                             {
                                                                                   var data = google.visualization.arrayToDataTable([
                                                                                     ['Country', 'Popularity'],
                                                                                     ['Germany', 10],
                                                                                     ['United States', 80],
                                                                                     ['Brazil', 5],
                                                                                     ['Canada', 10],
                                                                                     ['France', 10],
                                                                                     ['RU', 5],
                                                                                     ['China', 10000]

                                                                                   ]);
                                                                                   }


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
