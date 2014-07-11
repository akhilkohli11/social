var negative=0;
var postive=0;
var neutral=0;

$(document).ready(function(){
$('#videos').click(function(){
        $('#legend').hide();
d3.select("svg")
       .remove();
        $('#multimedia').text('');
        $('#onemorediv').hide();
            $('#multimedia').show();
  var file=$("#time :selected").text()+"tumblerVideo"+$("#ddlViewBy :selected").text()+".tsv";
        $('#multimedia').append("<h1><p>Video Posts</p></h1>");
  $.get(file, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                  $('#multimedia').append('<div><p>'+item+'</p></div>');
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
  var file=$("#time :selected").text()+"tumbleraudio"+$("#ddlViewBy :selected").text()+".tsv";
        $('#multimedia').append("<h1><p>Audio Posts</p></h1>");
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
  var filenew=$("#time :selected").text()+"tumblerText"+$("#ddlViewBy :selected").text()+".tsv";
        $('#multimedia').append("<h1><p>Text Posts</p></h1>");
  $.get(filenew, function(data) {
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                   $('#multimedia').append('<br><h3><p>Text</p></h3><br/>');
                  $('#multimedia').append('<div><p>'+item+'</p></div>');
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
  var file=$("#time :selected").text()+"tumblerPhoto"+$("#ddlViewBy :selected").text()+".tsv";

        $('#multimedia').append("<h1><p>Photo Posts</p></h1>");
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
showGraph($("#time :selected").text()+"tumblergraph"+$("#ddlViewBy :selected").text()+".tsv");
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



});
