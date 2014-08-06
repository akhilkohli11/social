var negative=0;
var postive=0;
var neutral=0;
       var url='http://ec2-54-187-52-149.us-west-2.compute.amazonaws.com:8080/rest/test/';
        //      var url='http://localhost:9999/rest/test/';
    //    var url='http://localhost:9199/rest/test/';

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
                                                $("#multiple").hide();
                 d3.select("svg")
                        .remove();


    if($("#socialType :selected").text()=="TUMBLR")
    {
    $("#multiple").hide();
       $("#ddlViewBy").show();
                $('#tumblrButton').show();
    }
     if($("#socialType :selected").text()=="TWITTER")
        {
        $("#multiple").hide();
                              $("#ddlViewBy").show();
                $('#twitterButton').show();

        }
    if($("#socialType :selected").text()=="COMPARE")
            {
                      $("#multiple").show();
                      $("#ddlViewBy").hide();
                         $('#tmsbutton').show();
            }

}


$(document).ready(function(){
$('#videos').click(function(){
        $('#img').show();
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
          var finalUrl=url+'tumblr/video';
                         $.ajax(finalUrl, {
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

      setTimeout(function(){
                 $('#img').hide();
      var file="videotumblr"+totalCount+$("#ddlViewBy :selected").text()+".tsv";

              $('#multimedia').append("<h3> <p class=\"text-primary\">Tabular Video Post Data</p></h3>");
        $.get(file, function(data) {
            var lines = data.split("\n");
            var image=-1;
      //                                      $('#multimedia').append("<tr><th class=\"active\">"+res[0]+"</td><tdclass=\"success\">"+res[1]+
      //                                                                "</td><tdclass=\"warning\">"+res[2]+"</td><tdclass=\"danger\">"+res[3]+"</td>");

                      $.each(lines, function(n, item) {
                      if(image==-1)
                      {
                                                   $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                      $('#one').append("<tr><th class=\"active\">Blog Name</th>"+
                                                                                                      "<th class=\"danger\">Note Count</th>"+

                                                      "<th class=\"success\">Followers</th>"+
                                                      "<th class=\"warning\">Post URL</th>"+
                                                      "</tr>");

                          image=0;
                      }

                      if(image==1)
                      {
                            $('#multimedia').append('<div> <p class=\"text-primary\">'+item+'</p></div>');
                       }
                      if(item=="photos")
                                      {
                                                $('#multimedia').append('</table>');
                                           $('#multimedia').append("<h3> <p class=\"text-primary\">Video Posts</p></h3>");
                                           image=1;
                                      }
                      if(image==0)
                      {
                                  var res = item.split("break");
                                $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"danger\">"+res[1]+"</td><td class=\"success\">"+res[2]+
                                "<td class=\"warning\"><a href=\""+res[3]+"\">"+res[3]+"</a></td></tr>");
                      }

                    //   $('#slideshow_1').append("<div class=\"slideshow_item\"><div class=\"image\"><a href=\"#\"><img  alt=\"photo 1\" width=\"900\" height=\"400\" src=\""+item+"\""
                     //   +"/></a></div></div>");

                });
            }, "text")
              $('#multimedia').append("</div>");
              },
       2000);



});

$('#audio').click(function(){
$('#img').show();
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
          var finalUrl=url+'tumblr/audio';
                         $.ajax(finalUrl, {
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

      setTimeout(function(){
                 $('#img').hide();
      var file="audiotumblr"+totalCount+$("#ddlViewBy :selected").text()+".tsv";

              $('#multimedia').append("<h3> <p class=\"text-primary\">Tabular Video Post Data</p></h3>");
        $.get(file, function(data) {
            var lines = data.split("\n");
            var image=-1;
      //                                      $('#multimedia').append("<tr><th class=\"active\">"+res[0]+"</td><tdclass=\"success\">"+res[1]+
      //                                                                "</td><tdclass=\"warning\">"+res[2]+"</td><tdclass=\"danger\">"+res[3]+"</td>");

                      $.each(lines, function(n, item) {
                      if(image==-1)
                      {
                                                   $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                      $('#one').append("<tr><th class=\"active\">Blog Name</th>"+
                                                                                                                                                            "<th class=\"danger\">Note Count</th>"+

                                                      "<th class=\"success\">Followers</th>"+
                                                      "<th class=\"warning\">Post URL</th>"+
                                                      "</tr>");

                          image=0;
                      }
                      if(image==1)
                                            {
                                                  $('#multimedia').append('<div> <p class=\"text-primary\">'+item+'</p></div>');
                                             }
                      if(item=="photos")
                                      {
                                                $('#multimedia').append('</table>');
                                           $('#multimedia').append("<h3> <p class=\"text-primary\">Audio Posts</p></h3>");
                                           image=1;
                                      }
                      if(image==0)
                      {
                                  var res = item.split("break");
                                $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"danger\">"+res[1]+"</td></td><td class=\"success\">"+res[2]+
                                "<td class=\"warning\"><a href=\""+res[3]+"\">"+res[3]+"</a></td></tr>");
                      }


                    //   $('#slideshow_1').append("<div class=\"slideshow_item\"><div class=\"image\"><a href=\"#\"><img  alt=\"photo 1\" width=\"900\" height=\"400\" src=\""+item+"\""
                     //   +"/></a></div></div>");

                });
            }, "text")
              $('#multimedia').append("</div>");
              },
       2000);


});


$('#text').click(function(){
$('#img').show();
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
          var finalUrl=url+'tumblr/text';
                         $.ajax(finalUrl, {
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

      setTimeout(function(){
                 $('#img').hide();
      var file="texttumblr"+totalCount+$("#ddlViewBy :selected").text()+".tsv";

              $('#multimedia').append("<h3> <p class=\"text-primary\">Tabular Text Post Data</p></h3>");
        $.get(file, function(data) {
            var lines = data.split("\n");
            var image=-1;
      //                                      $('#multimedia').append("<tr><th class=\"active\">"+res[0]+"</td><tdclass=\"success\">"+res[1]+
      //                                                                "</td><tdclass=\"warning\">"+res[2]+"</td><tdclass=\"danger\">"+res[3]+"</td>");

                      $.each(lines, function(n, item) {
                      if(image==-1)
                      {
                                                   $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                      $('#one').append("<tr><th class=\"active\">Blog Name</th>"+
                                                                                                                                                                                                                  "<th class=\"danger\">Note Count</th>"+
                                                      "<th class=\"success\">Followers</th>"+
                                                      "<th class=\"warning\">Post URL</th>"+
                                                      "</tr>");

                          image=0;
                      }
                       if(image==1)
                                            {
                                                $('#multimedia').append('<br><br/>');
                                                  $('#multimedia').append('<div class=\"bg-warning\"><p >'+item+'</p></div>');
                                             }
                      if(item=="photos")
                                      {
                                                $('#multimedia').append('</table>');
                                           $('#multimedia').append("<h3> <p class=\"text-primary\">Text Posts</p></h3>");
                                           image=1;
                                      }
                      if(image==0)
                      {
                                  var res = item.split("break");
                                $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"danger\">"+res[1]+"</td></td><td class=\"success\">"+res[2]+
                                "<td class=\"warning\"><a href=\""+res[3]+"\">"+res[3]+"</a></td></tr>");

                      }


                    //   $('#slideshow_1').append("<div class=\"slideshow_item\"><div class=\"image\"><a href=\"#\"><img  alt=\"photo 1\" width=\"900\" height=\"400\" src=\""+item+"\""
                     //   +"/></a></div></div>");

                });
            }, "text")
              $('#multimedia').append("</div>");
              },
       2000);


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



$('#photo').click(function(){
      $('#img').show();
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
    var finalUrl=url+'tumblr/photo';
                   $.ajax(finalUrl, {
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

setTimeout(function(){
           $('#img').hide();
var file=totalCount+"phototumblr"+$("#ddlViewBy :selected").text()+".tsv";

        $('#multimedia').append("<h3> <p class=\"text-primary\">Tabular Photo Post Data</p></h3>");
  $.get(file, function(data) {
      var lines = data.split("\n");
      var image=-1;
//                                      $('#multimedia').append("<tr><th class=\"active\">"+res[0]+"</td><tdclass=\"success\">"+res[1]+
//                                                                "</td><tdclass=\"warning\">"+res[2]+"</td><tdclass=\"danger\">"+res[3]+"</td>");

                $.each(lines, function(n, item) {
                if(image==-1)
                {
                                             $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                $('#one').append("<tr><th class=\"active\">Blog Name</th>"+
                                                "<th class=\"danger\">Note Count</th>"+
                                                "<th class=\"success\">Followers</th>"+
                                                "<th class=\"warning\">Post URL</th>"+
                                                "<th class=\"danger\">Photo Link</th></tr>");

                    image=0;
                }
                 if(image==1)
                                {
                                 $('#multimedia').append('<div><div class="image"><a href="#"><p><img    src="'+item+'"</p></a></div></div>');
                                 }
                if(item=="photos")
                                {
                                          $('#multimedia').append('</table>');
                                     $('#multimedia').append("<h3> <p class=\"text-primary\">Photo Posts</p></h3>");
                                     image=1;
                                }
                if(image==0)
                {
                            var res = item.split("break");
                          $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"danger\">"+res[1]+"</td><td class=\"success\">"+res[2]+
                          "<td class=\"warning\"><a href=\""+res[3]+"\">"+res[3]+"</a></td><td class=\"danger\"><a href=\""+res[4]+"\">"+res[4]+"</a></td></tr>");
                }


              //   $('#slideshow_1').append("<div class=\"slideshow_item\"><div class=\"image\"><a href=\"#\"><img  alt=\"photo 1\" width=\"900\" height=\"400\" src=\""+item+"\""
               //   +"/></a></div></div>");

          });
      }, "text")
        $('#multimedia').append("</div>");
        },
 2000);



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


});


$('#usbutton').click(function(){
      $('#img').show();
d3.select("svg")
       .remove();
$('#maindiv').hide();
      $('#newdiv').hide();
         $('#onemorediv').show();
    $('#multimedia').text('');
        $('#multimedia').show();
   $('#tweet').text('');
       $('#positivetweet').text('');
       $('#negativeTweet').text('');
         $('#chart_div').text('');
              $('#chart_div').hide();
               var bottom=1000;
                   var top=10000;
                   var count= Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
                   var totalCount = count.toString();
                    //   requestData={criteria:"ok", maxNumberOfItems:"10",totalCount:count};
                     requestData={show:$("#ddlViewBy :selected").text(), bottomTime:$("#datepicker").val(),
                     upperTime:$("#enddatepicker").val(),
                     id:totalCount};
                  var finalUrl=url+'twitter/geo';
                                 $.ajax(finalUrl, {
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

setTimeout(function(){
           $('#img').hide();
var file="geotwitter"+totalCount+$("#ddlViewBy :selected").text()+".tsv";

  $('#multimedia').append("<h3> <p class=\"text-primary\">US GEO Data</p></h3>");
   $.get(file, function(data) {
       var lines = data.split("\n");
       var image=-1;

                 $.each(lines, function(n, item) {
                 if(image==-1)
                 {
                                              $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                 $('#one').append("<tr><th class=\"active\">US State</th>"+
                                                 "<th class=\"success\">Count</th></tr>");

                     image=0;
                 }
                 if(image==0)
                 {
                             var res = item.split("newvalue");
                           $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"success\">"+res[1]+"</td></tr>");
                 }


           });
           }, "text")



  $.get(file, function(data) {
   var newdata = new google.visualization.DataTable();
                                            newdata.addColumn('string', 'State');
                                            newdata.addColumn('number', 'Popularity');
      var lines = data.split("\n");
                $.each(lines, function(n, item) {
                  var res = item.split("newvalue");
                  newdata.addRow([res[0],parseInt(res[1])]);
          });
                              countryMapUS(newdata);
      }, "text")
        },
 3000);

});





function countryMapUS(data) {
//      var data = new google.visualization.DataTable();
//           data.addColumn('string', 'State');
//           data.addColumn('number', 'Popularity');
//            data.addRow(['US-AL',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-AK',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-AZ',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-AR',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-CA',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-CO',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-CT',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-DE',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-DC',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-FL',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-GA',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-HI',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-ID',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-IL',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-IN',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-IA',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-KS',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-KY',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-LA',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-ME',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-MD',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-MA',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-MI',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-MN',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-MS',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-MO',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-MT',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-NE',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-NV',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-NH',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-NJ',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-NM',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-NY',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-NC',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-ND',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-OH',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-OK',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-OR',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-PA',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-RI',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-SC',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-SD',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-TN',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-TX',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-UT',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-VT',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-VA',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-WA',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-WV',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-WI',Math.floor(Math.random() * 100)]);
//           data.addRow(['US-WY',200]);
//           data.addRow(['US-AS',440]);
//           data.addRow(['US-GU',330]);
//           data.addRow(['US-MP',880]);
//           data.addRow(['US-PR',900]);
//           data.addRow(['US-VI',100]);

          var formatter = new google.visualization.NumberFormat({prefix: 'Popularity', fractionDigits: 0});
                      formatter.format(data, 1); // Apply formatter to second column
                      var stateHeatMap = new google.visualization.GeoChart(document.getElementById('visualization'));
                      stateHeatMap.draw(data, {width: 1260, height: 500, region: 'US', resolution: 'provinces', legend: {numberFormat: '#,###', textStyle: {color: 'blue', fontSize: 16}} });
                    }



function jsonp(result)
{
 console.log(result);
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

 $('#compare').click(function(){
 var foo = "";
 $('#multiple :selected').each(function(i, selected){
   foo+=$(selected).text()+"split";
 });
 $('#multimedia').text('');
     $('#multimedia').show();
  $('#img').show();

       var bottom=1000;
       var top=10000;
       var count= Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
       var totalCount = count.toString();
        //   requestData={criteria:"ok", maxNumberOfItems:"10",totalCount:count};
         requestData={show:foo, bottomTime:$("#datepicker").val(),
         upperTime:$("#enddatepicker").val(),
         id:totalCount};
      var finalUrl=url+'compare';
                     $.ajax(finalUrl, {
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


  setTimeout(function(){
             $('#img').hide();
 var file="comparetumblr"+totalCount+".tsv";
     $.get(file, function(data) {
         var lines = data.split("\n");
         var image=10;

                   $.each(lines, function(n, item) {


                   if(image==-1)
                   {
                                                   $('#one').append("<tr><th class=\"active\">Date</th>"+
                                                   "<th class=\"success\">Total</th>"+
                                                   "<th class=\"warning\">Video</th >"+
                                                    "<th class=\"info\">Audio</th>"+
                                                      "<th class=\"active\">Text</th>"+
                                                   "<th class=\"danger\">Photo</th></tr>");

                       image=0;
                   }
                    if(image==100)
                                                                            {
                                                                             $('#one').append("<tr><td></td><td></td><td></td><td></td></tr>");

                                                                              $('#one').append("<tr><th class=\"active\">"+item+"</th></tr>");
                                                                             image=-1;
                                                                            }
                   if(item=="newline")
                                      {
                                     $('#multimedia').append('<table id="one" class="table table-condensed">');
                                     image=100;
                                      }

                   if(image==0)
                   {
                               var res = item.split("break");
                             $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"success\">"+res[1]+"</td>"+
                             "<td class=\"warning\">"+res[2]+"</td><td class=\"info\">"+res[3]+"</td>"+
                             "<td class=\"active\">"+res[4]+"</td><td class=\"danger\">"+res[5]+"</td>");
                   }



             });

         }, "text")


           },
   4000);
 });

 $('#tumblrtrend').click(function(){

 $('#img').show();
 d3.select("svg")
        .remove();
 $('#maindiv').hide();
       $('#newdiv').hide();
          $('#onemorediv').show();
     $('#multimedia').text('');
         $('#multimedia').show();
    $('#tweet').text('');
        $('#positivetweet').text('');
        $('#negativeTweet').text('');
          $('#chart_div').text('');
               $('#chart_div').hide();
                var bottom=1000;
                    var top=10000;
                    var count= Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
                    var totalCount = count.toString();
                     //   requestData={criteria:"ok", maxNumberOfItems:"10",totalCount:count};
                      requestData={bottomTime:$("#datepicker").val(),
                      upperTime:$("#enddatepicker").val(),
                      id:totalCount};
                   var finalUrl=url+'tumblr/trends';
                                  $.ajax(finalUrl, {
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

 setTimeout(function(){
            $('#img').hide();
 var file="trendstumblr"+totalCount+".tsv";

   $('#multimedia').append("<h3> <p class=\"text-primary\">TRENDS</p></h3>");
    $.get(file, function(data) {
        var lines = data.split("\n");
        var image=-1;

                  $.each(lines, function(n, item) {
                  if(image==-1)
                  {
                                               $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                  $('#one').append("<tr><th class=\"active\">SHOW</th>"+
                                                  "<th class=\"success\">Count</th></tr>");

                      image=0;
                  }
                  if(image==0)
                  {
                              var res = item.split("newvalue");
                            $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"success\">"+res[1]+"</td></tr>");
                  }


            });
            }, "text")

           },
   3000);
 });



$('#twittertrend').click(function(){

 $('#img').show();
 d3.select("svg")
        .remove();
 $('#maindiv').hide();
       $('#newdiv').hide();
          $('#onemorediv').show();
     $('#multimedia').text('');
         $('#multimedia').show();
    $('#tweet').text('');
        $('#positivetweet').text('');
        $('#negativeTweet').text('');
          $('#chart_div').text('');
               $('#chart_div').hide();
                var bottom=1000;
                    var top=10000;
                    var count= Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
                    var totalCount = count.toString();
                     //   requestData={criteria:"ok", maxNumberOfItems:"10",totalCount:count};
                      requestData={bottomTime:$("#datepicker").val(),
                      upperTime:$("#enddatepicker").val(),
                      id:totalCount};
                   var finalUrl=url+'twitter/trends';
                                  $.ajax(finalUrl, {
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

 setTimeout(function(){
            $('#img').hide();
 var file="trendstwitter"+totalCount+".tsv";

   $('#multimedia').append("<h3> <p class=\"text-primary\">TRENDS</p></h3>");
    $.get(file, function(data) {
        var lines = data.split("\n");
        var image=-1;

                  $.each(lines, function(n, item) {
                  if(image==-1)
                  {
                                               $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                  $('#one').append("<tr><th class=\"active\">SHOW</th>"+
                                                  "<th class=\"success\">Count</th></tr>");

                      image=0;
                  }
                  if(image==0)
                  {
                              var res = item.split("newvalue");
                            $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"success\">"+res[1]+"</td></tr>");
                  }


            });
            }, "text")

           },
   3000);
 });


 $('#twittercompare').click(function(){
  var foo = "";
  var totcount=0;
  $('#multiple :selected').each(function(i, selected){
    foo+=$(selected).text()+"split";
    totcount++;
  });
  if(totcount>5)
  {
    alert("Maximum 5 shows can be compared");
    return;
  }
  $('#multimedia').text('');
      $('#multimedia').show();
   $('#img').show();

        var bottom=1000;
        var top=10000;
        var count= Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
        var totalCount = count.toString();
         //   requestData={criteria:"ok", maxNumberOfItems:"10",totalCount:count};
          requestData={show:foo, bottomTime:$("#datepicker").val(),
          upperTime:$("#enddatepicker").val(),
          id:totalCount};
       var finalUrl=url+'compare/twitter';
                      $.ajax(finalUrl, {
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


   setTimeout(function(){
              $('#img').hide();
  var file="comparetwitter"+totalCount+".tsv";
      $.get(file, function(data) {
          var lines = data.split("\n");
          var image=10;

                    $.each(lines, function(n, item) {


                    if(image==-1)
                    {
                                                                                           $('#one').append("<tr><th class=\"active\">Date</th>"+
                                                                                              "<th class=\"success\">Total</th>"+
                                                                                               "<th class=\"warning\">Photo</th>"+
                                                                                                      "<th class=\"info\">Text</th>"+
                                                                                                                           "<th class=\"active\">Link</th></tr>");

                        image=0;
                    }
                     if(image==100)
                                                                             {
                                                                              $('#one').append("<tr><td></td><td></td><td></td><td></td></tr>");

                                                                               $('#one').append("<tr><th class=\"active\">"+item+"</th></tr>");
                                                                              image=-1;
                                                                             }
                    if(item=="newline")
                                       {
                                      $('#multimedia').append('<table id="one" class="table table-condensed">');
                                      image=100;
                                       }

                    if(image==0)
                    {
                                var res = item.split("break");
                              $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"success\">"+res[1]+"</td>"+
                              "<td class=\"warning\">"+res[2]+"</td><td class=\"info\">"+res[3]+"</td>"+
                              "<td class=\"active\">"+res[4]+"</td></tr>");
                    }



              });
                 $('#multimedia').append("<br/><br/></div>");
                                 $('#multimedia').append('<div id="newboy" class="newgraph-svg-component">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
                                 +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
                                                    +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
                                                    var count=0;
                                $('#multiple :selected').each(function(i, selected){
                                count++;
                               if(count==1)
                               {
                                    $('#newboy').append('<span class="foo" style="background-color:#82CAFA;">'+$(selected).text()
                                    +'</span>&nbsp;&nbsp;');
                                    }
                                     if(count==2)
                                   {
                                           $('#newboy').append('<span class="foo" style="background-color:#FF8C00;">'+$(selected).text()
                                            +'</span>&nbsp;&nbsp;');
                                   }
                              if(count==3)
                                                                {
                                                                        $('#newboy').append('<span class="foo" style="background-color:#7FE817;">'+$(selected).text()
                                                                         +'</span>&nbsp;&nbsp;');
                                                                }
                                if(count==4)
                                                                  {
                                                                          $('#newboy').append('<span class="foo" style="background-color:#DC143C;">'+$(selected).text()
                                                                           +'</span>&nbsp;&nbsp;');
                                                                  }
                               if(count==5)
                                                                 {
                                                                         $('#newboy').append('<span class="foo" style="background-color:#B19CD9;">'+$(selected).text()
                                                                          +'</span>&nbsp;&nbsp;');
                                                                 }
                                  });

          }, "text")

        var file="comparetwittergraph"+totalCount+".tsv";
                 showGraph(file);
            },
    4000);
  });



$('#graph').click(function(){
 $('#multimedia').text('');
    $('#multimedia').show();
 $('#img').show();

      var bottom=1000;
      var top=10000;
      var count= Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
      var totalCount = count.toString();
       //   requestData={criteria:"ok", maxNumberOfItems:"10",totalCount:count};
        requestData={show:$("#ddlViewBy :selected").text(), bottomTime:$("#datepicker").val(),
        upperTime:$("#enddatepicker").val(),
        id:totalCount};
     var finalUrl=url+'tumblr/graphs';
                    $.ajax(finalUrl, {
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

 setTimeout(function(){
            $('#img').hide();
var file="statstumblr"+totalCount+$("#ddlViewBy :selected").text()+".tsv";

         $('#multimedia').append("<h3> <p class=\"text-primary\">Tabular Graph Post Data</p></h3>");
   $.get(file, function(data) {
       var lines = data.split("\n");
       var image=-1;

                 $.each(lines, function(n, item) {
                 if(image==-1)
                 {
                                              $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                 $('#one').append("<tr><th class=\"active\">Date</th>"+
                                                 "<th class=\"success\">Total</th>"+
                                                 "<th class=\"warning\">Video</th>"+
                                                  "<th class=\"info\">Audio</th>"+
                                                    "<th class=\"active\">Text</th>"+
                                                 "<th class=\"danger\">Photo</th></tr>");

                     image=0;
                 }
                 if(image==0)
                 {
                             var res = item.split("break");
                           $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"success\">"+res[1]+"</td>"+
                           "<td class=\"warning\">"+res[2]+"</td><td class=\"info\">"+res[3]+"</td>"+
                           "<td class=\"active\">"+res[4]+"</td><td class=\"danger\">"+res[5]+"</td>");
                 }



           });
            $('#multimedia').append("<br/><br/></div>");
                   $('#multimedia').append('<div id="newboy" class="newgraph-svg-component">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
                   +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
                                      +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
                   +'<span class="foo" style="background-color:#82CAFA;">Total</span>&nbsp;&nbsp;'+
                    '<span class="foo" style="background-color:#B19CD9;">Photo</span>&nbsp;&nbsp;<span class="foo" style="background-color:#DC143C;">Text</span>'+
                     '&nbsp;&nbsp;<span class="foo" style="background-color:#7FE817;">Audio</span>&nbsp;&nbsp;<span class="foo" style="background-color:#FF8C00;">Video</span>');
       }, "text")

var file="graphtumblr"+totalCount+$("#ddlViewBy :selected").text()+".tsv"
         showGraph(file);
         },
  3000);
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
$('#multimedia').text('');
     $('#multimedia').show();
                 $('#onemorediv').hide();

  $('#img').show();

       var bottom=1000;
       var top=10000;
       var count= Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
       var totalCount = count.toString();
        //   requestData={criteria:"ok", maxNumberOfItems:"10",totalCount:count};
         requestData={show:$("#ddlViewBy :selected").text(), bottomTime:$("#datepicker").val(),
         upperTime:$("#enddatepicker").val(),
         id:totalCount};
      var finalUrl=url+'twitter/graphs';
                     $.ajax(finalUrl, {
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

  setTimeout(function(){
             $('#img').hide();
 var file="statstwitter"+totalCount+$("#ddlViewBy :selected").text()+".tsv";

          $('#multimedia').append("<h3> <p class=\"text-primary\">Tabular Graph Post Data</p></h3>");
    $.get(file, function(data) {
        var lines = data.split("\n");
        var image=-1;

                  $.each(lines, function(n, item) {
                  if(image==-1)
                  {
                                               $('#multimedia').append('<table id="one" class="table table-condensed">');
                                                  $('#one').append("<tr><th class=\"active\">Date</th>"+
                                                  "<th class=\"success\">Total</th>"+
                                                  "<th class=\"warning\">Photo</th>"+
                                                   "<th class=\"info\">Text</th>"+
                                                     "<th class=\"active\">Link</th></tr>");

                      image=0;
                  }
                  if(image==0)
                  {
                              var res = item.split("break");
                            $('#one').append("<tr><td class=\"active\">"+res[0]+"</td><td class=\"success\">"+res[1]+"</td>"+
                            "<td class=\"warning\">"+res[2]+"</td><td class=\"info\">"+res[3]+"</td>"+
                            "<td class=\"active\">"+res[4]+"</td></tr>");
                  }


            });
             $('#multimedia').append("<br/><br/></div>");
                    $('#multimedia').append('<div id="newboy" ><div class="foo" style="background-color:#82CAFA;">Total</div><br>'+
                     '<div class="foo" style="background-color:#DC143C;">Link</div><br>'+
                      '<div class="foo" style="background-color:#7FE817;">Text</div><br><div class="foo" style="background-color:#FF8C00;">Photo</div><br>');
        }, "text")

 var file="graphtwitter"+totalCount+$("#ddlViewBy :selected").text()+".tsv"
          showGraph(file);
          },
   3000);
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





function showGraph(fileNme)
{
d3.select("svg")
       .remove();
       var margin = {top: 20, right: 80, bottom: 30, left: 50},
    width = 1500 - margin.left - margin.right,
    height = 700 - margin.top - margin.bottom;

var parseDate = d3.time.format("%Y%m%d").parse;

var x = d3.time.scale()
    .range([0, width]);

var y = d3.scale.linear()
    .range([height, 0]);

var color = d3.scale.category10();

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
             .ticks(5);

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
                 .ticks(5);

var line = d3.svg.line()
    .interpolate("basis")
    .x(function(d) { return x(d.date); })
    .y(function(d) { return y(d.temperature); });

var svg = d3.select("body").append("tweet").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
     .attr("class", "graph-svg-component")
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

      legend = svg.append("g")
          .attr("class","legend")
          .attr("transform","translate(50,30)")
          .style("font-size","12px")
          .call(d3.legend)


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
