
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

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

function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
		 fillselectbarndcategory(data); 
	   },
	   error: handleAjaxError
	});
}

brandCategoryData = []
 brandSet = new Set()
 categorySet = new Set()
 function getBrandCategory(){
      $.ajax({
            url: getBrandUrl(),
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

function fillselectbarndcategory(data)
{
	listofbrands=[];
	listofcategorys=[];
	mapofbrands={};
	mapofcategory={};

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
	}

	var select = document.getElementById("form-brand-select");

    for(var i = 0; i < listofbrands.length; i++) {
		// console.log(listofbrands[i]);
        var opt = listofbrands[i];
        var el = document.createElement("option");
        el.textContent = opt;
        el.value = opt;
        select.appendChild(el);
    }

	var select = document.getElementById("form-category-select");

    for(var i = 0; i < listofcategorys.length; i++) {
        var opt = listofcategorys[i];
        var el = document.createElement("option");
        el.textContent = opt;
        el.value = opt;
        select.appendChild(el);
    }
}

function dropdownCategory(){
	var a=document.getElementById("form-brand-select").value;
	var b=document.getElementById("form-category-select").value;
	console.log(a);
	console.log(b);
	if(a=="None")
	{
		listofbrands=mapofcategory[b];
		var $select_brand=$("#form-brand-select");
		$select_brand.empty()
		// console.log(listofcategorys);
		var select = document.getElementById("form-brand-select");
		for(var i = 0; i < listofbrands.length; i++) {
			var opt = listofbrands[i];
			var el = document.createElement("option");
			el.textContent = opt;
			el.value = opt;
			select.appendChild(el);
		}
		
	}
	else if(b!="None" )
	{
		// listofbrands=mapofcategory[b];
		// var $select_brand=$("#form-brand-select");
		// $select_brand.empty()
		// // console.log(listofcategorys);
		// var select = document.getElementById("form-brand-select");
		// for(var i = 0; i < listofbrands.length; i++) {
		// 	var opt = listofbrands[i];
		// 	var el = document.createElement("option");
		// 	el.textContent = opt;
		// 	el.value = opt;
		// 	select.appendChild(el);
		// }
	}
}


function dropdownBrand(){
	var a=document.getElementById("form-brand-select").value;
	var b=document.getElementById("form-category-select").value;
	console.log(a);
	console.log(b);
	if(b=="None")
	{
		listofcategorys=mapofbrands[a];
		var $select_brand=$("#form-category-select");
		$select_brand.empty()
		// console.log(listofcategorys);
		var select = document.getElementById("form-category-select");
		for(var i = 0; i < listofcategorys.length; i++) {
			var opt = listofcategorys[i];
			var el = document.createElement("option");
			el.textContent = opt;
			el.value = opt;
			select.appendChild(el);
		}
	}
	else if(b!="None" )
	{
		// listofbrands=mapofcategory[b];
		// var $select_brand=$("#form-brand-select");
		// $select_brand.empty()
		// // console.log(listofcategorys);
		// var select = document.getElementById("form-brand-select");
		// for(var i = 0; i < listofbrands.length; i++) {
		// 	var opt = listofbrands[i];
		// 	var el = document.createElement("option");
		// 	el.textContent = opt;
		// 	el.value = opt;
		// 	select.appendChild(el);
		// }
	}
}

function search(event)
{
	var $form =$("#search-form");
	var json=toJson($form);
	var url= getProductUrl()+"/search";
	console.log(url);

	$.ajax({
		url: url,
		type: 'POST',
		data: json,
		headers: {
		 'Content-Type': 'application/json'
		},
		success: function(response) {
			 displayProductList(response);
		},
		error: handleAjaxError
	 });
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update

	$('#add-product-modal').modal('toggle');
	var $form = $("#add-product-form");
	var json = toJson($form);
	console.log(json);
	var url = getProductUrl();
	//console.log(url);

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		    SuccessMessage("Successfully Added");
			resetForm();
	   		getProductList();  
	   },
	   error: handleAjaxError
	});

	return false;
}

function resetForm() {
    var element = document.getElementById("add-product-form");
    element.reset()
}

function updateProduct(event){
	
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();	
	var url = getProductUrl() + "/" + id;
	console.log(id);

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
		   SuccessMessage("Successfully updated");
		   $('#edit-product-modal').modal('toggle');
	   		getProductList();   
	   },
	   error: handleAjaxError
	});

	return false;
}


function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
		$('.datatable').DataTable().destroy();
	   		displayProductList(data);  
			pagination();
	   },
	   error: handleAjaxError
	});
}

function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();  
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
	if(!file)
    {
        ErrorMessage("Please select a file");
        return;
    }
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	if(headers.fields.length!=5) {
        var row = {};
        row.error="Number of coloumns do not match!";
        errorData.push(row);
        updateUploadDialog()
        return;
    }
    if(headers.fields[0]!="name" || headers.fields[1]!="brand" || headers.fields[2]!="category" || headers.fields[3]!="barcode" || headers.fields[4]!="mrp")
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
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getProductUrl();

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

function displayProductList(data){
	// console.log(data);
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	var cnt=0;
	for(var i in data){
		var e = data[i];
		cnt++;
		if(getRole()=="supervisor"){
		    var buttonHtml =' <button type="button" class="btn btn-dark" onclick="displayEditProduct(' + e.id + ')">edit </button>'
		    var row = '<tr>'
		    + '<td>' + cnt + '</td>'
		    + '<td>' + e.barcode + '</td>'
		    + '<td>' + e.brand + '</td>'
		    + '<td>' + e.category + '</td>'
		    + '<td>'  + e.name + '</td>'
		    + '<td>'  + (e.mrp).toFixed(2) + '</td>'
		    + '<td>' + buttonHtml + '</td>'
		    + '</tr>';
		}
		else{
			var row = '<tr>'
		    + '<td>' + cnt + '</td>'
		    + '<td>' + e.barcode + '</td>'
		    + '<td>' + e.brand + '</td>'
		    + '<td>' + e.category + '</td>'
		    + '<td>'  + e.name + '</td>'
		    + '<td>'  + (e.mrp).toFixed(2) + '</td>'
		    + '</tr>';
		}
        $tbody.append(row);
	}
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);   
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
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
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
	fileData = [];
    errorData = [];
    processCount = 0;
    updateUploadDialog()
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
	// console.log(data);
	$("#product-edit-form input[name=brand]").val(data.brand);	
	$("#product-edit-form input[name=category]").val(data.category);	
	$("#product-edit-form input[name=mrp]").val((data.mrp).toFixed(2));
	$("#product-edit-form input[name=name]").val(data.name);
	$("#product-edit-form input[name=barcode]").val(data.barcode);	
	$("#product-edit-form input[name=id]").val(data.id);	
	$('#edit-product-modal').modal('toggle');
}

function displayProductData(){	
	resetForm();
   $('#add-product-modal').modal('toggle');
   
}


//INITIALIZATION CODE
function init(){
	$('#add-product-1').click(addProduct);
	// $('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#search-by-name-barcode').click(search);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
	$('#add-product').click(displayProductData);
	$('#inputBrand').change(brandChanged);
    $('#inputCategory').change(categoryChanged);
    $('#ProductFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getProductList);
// $(document).ready(getBrandList);
$(document).ready(getBrandCategory);

