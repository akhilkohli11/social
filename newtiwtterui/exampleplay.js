
//var d1 = [[1401906600, 12], [1402041969, 10], [1402041985, 8]];

//Rome, Italy
var d1 = [['Jan'   , 12], ['Feb', 13],  ['Mar', 18]];
//var d2 = [[1262304000000, 6], [1264982400000, 7], [1267401600000, 12], [1270080000000, 16], [1272672000000, 20], [1275350400000, 23], [1277942400000, 25], [1280620800000, 24], [1283299200000, 21], [1285891200000, 16], [1288569600000, 10], [1291161600000, 7]];
//// Madrid, Spain
//var d3 = [[1262304000000, 11], [1264982400000, 13], [1267401600000, 16], [1270080000000, 18], [1272672000000, 22], [1275350400000, 28], [1277942400000, 33], [1280620800000, 32], [1283299200000, 28], [1285891200000, 21], [1288569600000, 15], [1291161600000, 11]];
//// London, UK
//var d4 = [[1262304000000, 7], [1264982400000, 7], [1267401600000, 10], [1270080000000, 13], [1272672000000, 16], [1275350400000, 20], [1277942400000, 22], [1280620800000, 21], [1283299200000, 19], [1285891200000, 15], [1288569600000, 10], [1291161600000, 8]];
//
var data1 = [
    {label: "Rome, Italy",  data: d1, points: { symbol: "circle", fillColor: "#058DC7" }, color: '#058DC7'},
//    {label: "Paris, France",  data: d2, points: { symbol: "diamond", fillColor: "#AA4643" }, color: '#AA4643'},
//    {label: "Madrid, Spain",  data: d3, points: { symbol: "square", fillColor: "#50B432" }, color: '#50B432'},
//    {label: "London, UK",  data: d4, points: { symbol: "triangle", fillColor: "#ED561B" }, color: '#ED561B'}
];

$(document).ready(function () {
    $.plot($("#placeholder"), data1, {
        xaxis: {

                       tickSize: [1, "month"],
                       monthNames: ["Jan", "Feb", "Mar"],
                       axisLabel: 'Month',
                       axisLabelUseCanvas: true,
                       axisLabelFontSizePixels: 12,
                       axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
                       axisLabelPadding: 5
        },
        yaxis: {
            axisLabel: 'Temperature (C)',
            axisLabelUseCanvas: true,
            axisLabelFontSizePixels: 12,
            axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
            axisLabelPadding: 5
        },
        series: {
            lines: { show: true },
            points: {
                radius: 3,
                show: true,
                fill: true
            },
        },
        grid: {
            hoverable: true,
            borderWidth: 1
        },
        legend: {
            labelBoxBorderColor: "none",
                position: "right"
        }
    });

    function showTooltip(x, y, contents, z) {
        $('<div id="flot-tooltip">' + contents + '</div>').css({
            top: y - 30,
            left: x - 135,
            'border-color': z,
        }).appendTo("body").fadeIn(200);
    }

    function getMonthName(numericMonth) {
        var monthArray = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        var alphaMonth = monthArray[numericMonth];

        return alphaMonth;
    }

    function convertToDate(timestamp) {
        var newDate = new Date(timestamp);
        var dateString = newDate.getMonth();
        var monthName = getMonthName(dateString);

        return monthName;
    }

    var previousPoint = null;

    $("#placeholder").bind("plothover", function (event, pos, item) {
        if (item) {
            if ((previousPoint != item.dataIndex) || (previousLabel != item.series.label)) {
                previousPoint = item.dataIndex;
                previousLabel = item.series.label;

                $("#flot-tooltip").remove();

               // var x = convertToDate(item.datapoint[0]),
               var x = item.datapoint[0],
                y = item.datapoint[1];
                z = item.series.color;

                showTooltip(item.pageX, item.pageY,
                    "<b>" + item.series.label + "</b><br /> " + x + " = " + y + "mm",
                    z);
            }
        } else {
            $("#flot-tooltip").remove();
            previousPoint = null;
        }
    });
});