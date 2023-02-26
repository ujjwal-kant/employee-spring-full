function getDailySalesReportUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/reports";
 }
 
 function filterSalesReport() {
     var url = getDailySalesReportUrl()+"/daily-sales";
     $.ajax({
        url: url,
        type: 'GET',
        headers: {
         'Content-Type': 'application/json'
        },
        success: function(response) {
             displayDailySalesReport(response);
             pagination();
        },
        error: handleAjaxError
     });
 }
 
 function displayDailySalesReport(data) {
     var $tbody = $('#daily-sales-table').find('tbody');
     $tbody.empty();
     for(let i=data.length-1; i>=0; i--){
         var b = data[i];
         console.log(b);
         var row = '<tr class="text-center">'
         + '<td>' + convertTimeStampToDateTime(b.date) + '</td>'
         + '<td>' + b.orderCount + '</td>'
         + '<td>' + b.itemCount + '</td>'
         + '<td class="text-right">' + b.totalRevenue.toFixed(2) + '</td>'
         + '</tr>';
         $tbody.append(row);
     }
 }

 function convertTimeStampToDateTime(timestamp) {
    var date = new Date(timestamp);
    return (
      date.getDate() +
      "/" +
      (date.getMonth() + 1) +
      "/" +
      date.getFullYear() +
      " " +
      date.getHours() +
      ":" +
      date.getMinutes() +
      ":" +
      date.getSeconds()
    );
  }

  function downloadcsvfile(){
	var url =getDailySalesReportUrl()+"/daily-sales";
   console.log(url);
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
         // console.log(data);
			helper(data);
		},
		error: function() {
			console.log("Error");
		},
	});
}

function helper(data){
    // var json_pre = '[{"Id":1,"UserName":"Sam Smith"},{"Id":2,"UserName":"Fred Frankly"},{"Id":1,"UserName":"Zachary Zupers"}]';
	// // var json_pre=data;
	var json1 = JSON.stringify(data);
    var json = $.parseJSON(json1);

	var csv = toCSV(json);

    var downloadLink = document.createElement("a");
    var blob = new Blob(["\ufeff", csv]);
    var url = URL.createObjectURL(blob);
    downloadLink.href = url;
    downloadLink.download = "data.csv";

    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
}


function toCSV(json) {
    json = Object.values(json);
    var csv = "";
    var keys = (json[0] && Object.keys(json[0])) || [];
    csv += keys.join(',') + '\n';
    for (var line of json) {
      csv += keys.map(key => line[key]).join(',') + '\n';
    }
    return csv;
}
 
 //INITIALIZATION CODE
 function init(){
    $('#nav-report').addClass('active');
    $('#csv').click(downloadcsvfile)
    $('#get-daily-sales-report').click(filterSalesReport);
    $('#generate-daily-sales-report').click(generateReportForRestDay);
 }

 function generateReportForRestDay(){
    var url = getDailySalesReportUrl()+"/generate-rest-day";
    console.log(url);
    $.ajax({
       url: url,
       type: 'POST',
    //    data: json,
       headers: {
        'Content-Type': 'application/json'
       },
       success: function(response) {
        filterSalesReport(response); 
       },
       error: handleAjaxError
    });
 }
 
 $(document).ready(init);
 $(document).ready(filterSalesReport);