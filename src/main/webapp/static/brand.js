
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

function getRole(){
	var role = $("meta[name=role]").attr("content")
	return role;
}


var listofbrands=[];
var listofcategorys=[];
var mapofbrands={};
var mapofcategory={};

function search(event)
{
	var $form =$("#search-form");
	var json=toJson($form);
	var url= getBrandUrl()+"/search";
	console.log(url);

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
		 'Content-Type': 'application/json'
		},
		success: function(response) {
			 
			 $('.datatable').DataTable().destroy();
			 displayBrandList(response);
			pagination();
		},
		error: handleAjaxError
	 });
}

function addBrand(event){
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {	   		
			$('#add-brand-modal').modal('toggle');  
			resetForm();
			SuccessMessage("Successfully Added");
			getBrandList();
			// $.notify(JSON.parse(json).brand + " in category: "+JSON.parse(json).category + " added successfully!","success");
	   },
	   error: handleAjaxError
	});

	return false;
}

function resetForm() {
    var element = document.getElementById("brand-form");
    element.reset()
}

function updateBrand(event){
	var id = $("#brand-edit-form input[name=id]").val();	
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		$('#edit-brand-modal').modal('toggle');	
		    SuccessMessage("Successfully Updated");
			getBrandList();
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList(){
	// console.log(getRole());
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
		$('.datatable').DataTable().destroy();
	   		displayBrandList(data);  
			   pagination();
	   },
	   error: handleAjaxError
	});
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getBrandList();  
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandCategoryFile')[0].files[0];
	console.log(file);
	if(!file)
    {
        ErrorMessage("Please select a file")
        return;
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
    if(headers.fields[0]!="brand" || headers.fields[1]!="category")
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
		getBrandList();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;

	
	var json = JSON.stringify(row);
	var url = getBrandUrl();

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

function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	
	listofbrands=[];
	listofcategorys=[];
	mapofbrands={};
	mapofcategory={};

	$tbody.empty();
	for(var i in data){
		var e = data[i];

		var a=e.brand;
		var b=e.category;

		listofbrands.push(e.brand);
		listofcategorys.push(e.category);

		if(!mapofbrands[a])mapofbrands[a]=[b];
		else mapofbrands[a].push(b);

		if(!mapofcategory[b])mapofcategory[b]=[a];
		else mapofcategory[b].push(a);
		// mapofcategory[e.category].push(e.brand);

		// console.log(e);
		// '<button type="button" class="btn btn-outline-primary" onclick="deleteBrand(' + e.id + ')">delete</button>'
		if(getRole()==="supervisor"){
		    var buttonHtml = ' <button type="button" class="btn btn-dark" onclick="displayEditBrand(' + e.id + ')">edit</button>'
		    var row = '<tr>'
		    // + '<td>' + e.id + '</td>'
		    + '<td>' + e.brand + '</td>'
		    + '<td>'  + e.category + '</td>'
		    + '<td>' + buttonHtml + '</td>'
		    + '</tr>';
		}
		else{
			var row = '<tr>'
		    // + '<td>' + e.id + '</td>'
		    + '<td>' + e.brand + '</td>'
		    + '<td>'  + e.category + '</td>'
		    + '</tr>';
		}
            $tbody.append(row);
		
	}
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	var $file = $('#brandCategoryFile');
	$file.val('');
	$('#brandCategoryFileName').html("Choose File");
	processCount = 0;
	fileData = [];
	errorData = [];
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
	var $file = $('#brandCategoryFile');
	// var fileName = $file.val();
	// console.log($file.val());
	var fileName = document.getElementById("brandCategoryFile").files[0].name;
	console.log("hello");
	$('#brandCategoryFileName').html(fileName);
	fileData = [];
    errorData = [];
    processCount = 0;
    updateUploadDialog()
}

function displayUploadData(){
	resetUploadDialog(); 	
   $('#upload-brand-category-modal').modal('toggle');
}


function displayAddData(){
	resetUploadDialog(); 	
	resetForm();
   $('#add-brand-modal').modal('toggle');
}

function showPassword() {
	var x = document.getElementById("password");
	if (x.type === "password") {
	  x.type = "text";
	} else {
	  x.type = "password";
	}
  }

function displayBrand(data){
	console.log(data);
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=id]").val(data.id);	
	$('#edit-brand-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-brand-1').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#search-by-brand-category').click(search);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#add-brand').click(displayAddData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName)
	
}


$(document).ready(init);
$(document).ready(getBrandList);

