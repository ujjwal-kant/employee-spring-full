function getSalesReportUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/reports/sales";
 }
 
 function getBrandCategoryUrl(){
     var baseUrl = $("meta[name=baseUrl]").attr("content")
     return baseUrl + "/api/brands";
 }
 brandCategoryData = []
 brandSet = new Set()
 categorySet = new Set()
 function getBrandCategory(){
      $.ajax({
            url: getBrandCategoryUrl(),
            type: 'GET',
            success: function(response) {
                 brandCategoryData = response
                 for(let i in response){
                     brandSet.add(response[i].brand)
                     categorySet.add(response[i].category)
                 }
 
                 populateBrandCategoryDropDown(brandSet,categorySet)
 
 
            },
            error: handleAjaxError
         });
 }
 
 function populateBrandCategoryDropDown(brandSet,categorySet){
     $('#inputBrand').empty()
     $('#inputCategory').empty()
     $('#inputBrand').append('<option selected="" value="">Select Brand</option>')
     $('#inputCategory').append('<option selected="" value="">Select Category</option>')
     brandSet.forEach(function(brand){
         let brandOptionHTML = '<option value="'+ brand +'">'+ brand+'</option>'
         $('#inputBrand').append(brandOptionHTML)
     })
     categorySet.forEach(function(category){
          let categoryOptionHTML = '<option value="'+ category +'">'+ category+'</option>'
          $('#inputCategory').append(categoryOptionHTML)
     })
 
 }
 
 function filterSalesReport() {
     var $form = $("#sales-form");
     var json = toJson($form);
     let data = JSON.parse(json);
     if(startDateGreaterThanEndDate(data.startDate,data.endDate)){
         $.notify('Start Date should not be greater than End Date!',"error");
         return;
     }
     var url = getSalesReportUrl();
     $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
         'Content-Type': 'application/json'
        },
        success: function(response) {
             displaySalesReport(response);
             pagination(); 
        },
        error: handleAjaxError
     });
 }
 

//  var lastday=
 function displaySalesReport(data) {
     var $tbody = $('#sales-table').find('tbody');
     $tbody.empty();
 
     if(data.length===0)
     {
         $('#sales-table').hide()
     }
     else
     {
         $('#sales-table').show()
     }
     for(var i in data){
         let srNo = Number.parseInt(i) + 1
         var b = data[i];
         var row = '<tr>'
         + '<td>'+srNo+'</td>'
         + '<td>' + b.brand + '</td>'
         + '<td>' + b.category + '</td>'
         + '<td>' + b.quantity + '</td>'
         + '<td>' + b.revenue.toFixed(2) + '</td>'
         + '</tr>';
         $tbody.append(row);
     }
 }
 
 function startDateGreaterThanEndDate(startDate,endDate){
     if(!startDate||!endDate)
         return false
     let partsStartDate =startDate.split('-');
     let startDateConverted = new Date(partsStartDate[0], partsStartDate[1] - 1, partsStartDate[2])
     let partsEndDate =endDate.split('-');
     let endDateConverted  = new Date(partsEndDate[0], partsEndDate[1] - 1, partsEndDate[2])
     return startDateConverted >endDateConverted
 }
 
 function resetDateFields(){
     $("input[type=date]").val("")
     $("input[type=date]").attr('min','')
     $("input[type=date]").attr('max','')
 
 }

 function downloadcsvfile(){
	var url = getSalesReportUrl();
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
 
 let primary = ""; //defines which of the Brand Category option was selected first
 
 function brandChanged(event){
     if(primary===""||primary==="brand"){
     primary="brand"
     let brand = event.target.value
     if(brand==="")
     {
         populateBrandCategoryDropDown(brandSet,categorySet)
         primary=""
         return
     }
     $('#inputCategory').empty()
     $('#inputCategory').append('<option selected="" value="">Select Category</option>')
     for(let i in brandCategoryData){
         if(brandCategoryData[i].brand===brand){
             let categoryOptionHTML = '<option value="'+ brandCategoryData[i].category +'">'+ brandCategoryData[i].category+'</option>'
                                  $('#inputCategory').append(categoryOptionHTML)
         }
     }
     }
 }
 
 
 function categoryChanged(event){
     if(primary===""||primary==="category"){
     primary = "category"
     let category = event.target.value
     if(category===""){
         populateBrandCategoryDropDown(brandSet,categorySet)
         primary=""
         return
     }
     $('#inputBrand').empty()
     $('#inputBrand').append('<option selected="" value="">Select Brand</option>')
     for(let i in brandCategoryData){
         if(brandCategoryData[i].category===category){
             let brandOptionHTML = '<option value="'+ brandCategoryData[i].brand +'">'+ brandCategoryData[i].brand+'</option>'
                                  $('#inputBrand').append(brandOptionHTML)
         }
     }
   }
 }
 
 function startDateChanged(event){
     if( $('#inputEndDate').val()==="")
         $('#inputEndDate').attr('min',event.target.value)
 }
 
 function endDateChanged(event){
     if( $('#inputStartDate').val()==="")
         $('#inputStartDate').attr('max',event.target.value)
 }
 
 //INITIALIZATION CODE
 function init(){
    $('#filter-sales-report').click(filterSalesReport);
    $('#reports-link').addClass('active').css("border-bottom","2px solid black")
    $('#inputBrand').change(brandChanged)
    $('#inputCategory').change(categoryChanged)
    $('#inputStartDate').change(startDateChanged)
    $('#inputEndDate').change(endDateChanged)
    $('#csv').click(downloadcsvfile)
    $('#generate-daily-sales-report').click(generateReportForRestDay);
    displaySalesReport([])
 }

 
 
 $(document).ready(init);
 $(document).ready(getBrandCategory);