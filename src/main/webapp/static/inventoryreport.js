
function getUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/inventory";
}


function getReportList(){
	var url = getUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			displayInventoryReportLis(data); 
			pagination();  
		},
		error: function() {
			console.log("Error");
		},
	});
}


function displayInventoryReportLis(data){
	var $tbody = $('#inventoryreport-table').find('tbody');
	$tbody.empty();
	console.log(data);
	for(var i in data){
		var e = data[i];
		
		var row = "<tr>"
		+ "<td class='text-center'>" + e.brand + "</td>"
		+ "<td class='text-center'>" + e.category + "</td>"
		+ "<td class='text-center'>"  + e.quantity + "</td>"
		+ "</tr>";
		$tbody.append(row);
	}
}

function downloadcsvfile(){
	var url = getUrl();
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
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

function init(){
	$('#csv').click(downloadcsvfile)
}

$(document).ready(init);
$(document).ready(getReportList);








