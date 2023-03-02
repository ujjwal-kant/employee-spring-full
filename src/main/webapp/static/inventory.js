
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getRole(){
    var role = $("meta[name=role]").attr("content")
    return role;
}


function populateBarcodeDropdown(formId) {
    var url = getInventoryUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function (data) {
            addDataToBarcodeDropdown(data, formId)
        },
        error: handleAjaxError
    });
}

function addDataToBarcodeDropdown(data, formId) {
	var $barcode = $(`${formId} select[name=barcode]`);
	$barcode.empty();

	var barcodeDefaultOption = '<option value="">Select a barcode</option>';
    $barcode.append(barcodeDefaultOption);

	for (var i in data) {
	  var e = data[i];
	  var option =
		'<option value="' +
		e.barcode +
		'">' +
		e.barcode +
		"</option>";
	  $barcode.append(option);
	}
}

//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
	var url = getInventoryUrl();
	console.log(url)

	var parsed=JSON.parse(json);
	console.log(parsed.quantity);
	if(parsed.barcode==="" || parsed.quantity==="")
    return frontendErrors("Fields are empty");

    if(isInteger(parsed.quantity)==false)
    return frontendErrors("Quantity is not an integer");

    if(parsed.quantity<0)
    return frontendErrors("Quantity can not be negative");

	if(parsed.quantity==0)
    return frontendErrors("Quantity can not be equal to zero");

	if(parsed.quantity>100000000)
    return frontendErrors("Quantity can not be greater than 100000000")

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		    SuccessMessage("Successfully Updated");
			$('#edit-inventory-modal').modal('toggle');
	   		getInventoryList();  
	   },
	   error: handleAjaxError
	});

	return false;
}

function resetForm() {
    var element = document.getElementById("inventory-add-form");
    element.reset()
}

function isInteger(str) {
	// Regular expression to match an integer
	var integerPattern = /^-?\d+$/;
	
	// Test if the string matches the integer pattern
	return integerPattern.test(str);
  }
  

function addon(event){
	//Set the values to update
	var $form = $("#inventory-add-form");
	var json = toJson($form);
	var url = getInventoryUrl()+"/add";
	console.log(url)

	var parsed=JSON.parse(json);
	console.log(parsed.quantity);
	if(parsed.barcode==="" || parsed.quantity==="")
    return frontendErrors("Quantity is Empty");

    if(isInteger(parsed.quantity)==false)
    return frontendErrors("Quantity is not an integer");

    if(parsed.quantity<0)
    return frontendErrors("Quantity can not be negative")

	if(parsed.quantity>100000000)
    return frontendErrors("Quantity can not be greater than 100000000")

	if(parsed.quantity==0)
    return frontendErrors("Quantity can not be equal to zero");

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(data) {
		   SuccessMessage("Successfully Added");
		   resetForm();
		   getInventoryList();  
        },
	   error: handleAjaxError
	});

	return false;
}

function updateInventory(event){
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#inventory-edit-form");
	var json = toJson($form);

	var parsed=JSON.parse(json);
  console.log(parsed);
  if(parsed.quantity=="")
    return frontendErrors("Quantity is Empty");

	if(isInteger(parsed.quantity)==false)
    return frontendErrors("Quantity is not an Integer");
  if(parsed.quantity<0)
    return frontendErrors("Quantity can not be negative");

	if(parsed.quantity>100000000)
    return frontendErrors("Quantity can not be greater than 100000000")

	if(parsed.quantity==0)
    return frontendErrors("Quantity can not be equal to zero");

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();   
			$('#edit-inventory-modal').modal('toggle');
	   },
	   error: handleAjaxError
	});

	return false;
}

function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
		$('.datatable').DataTable().destroy();
	   		displayInventoryList(data);  
			populateBarcodeDropdown("#inventory-add-form");
			populateBarcodeDropdown("#inventory-update-form");
			pagination();
	   },
	   error: handleAjaxError
	});
}

function deleteInventory(id){
	var url = getInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getInventoryList();  
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
	console.log(file);
	if(!file)
    {
        ErrorMessage("Please select a file")
        return;
    }
	if(file.name.split('.').pop()!="tsv"){
		ErrorMessage("File format is not TSV");
		return ;
	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
    var headers = results.meta;
    if(headers.fields.length!=2) {
        var row = {};
        row.error="Number of coloumns do not match!";
        errorData.push(row);
        updateUploadDialog()
        return;
    }
	if(headers.fields[0]!="barcode" || headers.fields[1]!="quantity")
    {
        var row = {};
        row.error="Incorrect headers name!";
        errorData.push(row);
        updateUploadDialog()
        return;
    }
    const MAX_ROWS = 5000
    if(results.data.length>MAX_ROWS){
        ErrorMessage("File contains more than 5000 rows!")
        return
    }
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		getInventoryList();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getInventoryUrl()+"/add";
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		if(getRole()==="supervisor"){
		    var buttonHtml = ' <button type="button" class="btn btn-dark" onclick="displayEditInventory(' + "'"+ String(e.barcode) +"'"+ ')">Edit</button>'
		    var row = '<tr>'
		    + '<td>' + e.barcode + '</td>'
		    + '<td>'  + e.quantity + '</td>'
		    + '<td>' + buttonHtml + '</td>'
		    + '</tr>';
		}
		else{
            var row = '<tr class="text-center">'
            + '<td>' + e.barcode + '</td>'
            + '<td>'  + e.quantity + '</td>'
            + '</tr>';
        }
        $tbody.append(row);
	}
}

function displayEditInventory(barcode){
	console.log(barcode);
	var url = getInventoryUrl() + "/" + barcode;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);

	if(errorData.length>0)
         $('#download-errors').show()
    else
        $('#download-errors').hide()
}

function updateFileName(){
	var $file = $('#inventoryFile');
	// var fileName = $file.val();
	var fileName = document.getElementById("inventoryFile").files[0].name;
	$('#inventoryFileName').html(fileName);
	
	fileData = [];
    errorData = [];
    processCount = 0;
    updateUploadDialog()
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$('#edit-inventory-modal').modal('toggle');
}

function displayInventoryAddForm(){
	resetUploadDialog(); 	
	resetForm();
   $('#addOn-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-on-inventory').click(displayInventoryAddForm);
	$('#add-inventory').click(addInventory);
	$('#addon-inventory').click(addon);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getInventoryList);