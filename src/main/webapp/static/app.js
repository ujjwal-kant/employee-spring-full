
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function setActive(){
    var navLinks = document.querySelectorAll('.nav-link');
    
    // Get the current URL
    var currentURL = window.location.href;
    
    // Loop through all the navbar links
    console.log(currentURL);
    for (var i = 0; i < navLinks.length; i++) {
      var link = navLinks[i];
      // If the link URL matches the current URL
      if (link.href === currentURL) {
        // Add the active class to the link
        link.classList.add('active');
        link.style.borderBottom = "2px solid #f16366";
      }
    }
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	console.log(response);
    toastr.error(response.message, "Error: ", {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "preventDuplicates": true,
        "onclick": null,
        "showDuration": "100",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "show",
        "hideMethod": "hide"
    })
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}




function SuccessMessage(res)
{
    toastr.success(res,"Success: ",{
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "preventDuplicates": true,
        "onclick": null,
        "showDuration": "100",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "show",
        "hideMethod": "hide"
    })
}

function ErrorMessage(res)
{
    toastr.error(res,"Error ",{
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "preventDuplicates": true,
        "onclick": null,
        "showDuration": "100",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "show",
        "hideMethod": "hide"
    })
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

  function calculateTotalPrice(orderItems) {
    let totalPrice = 0;
    for (let i = 0; i < orderItems.length; i++) {
        const orderItem = orderItems[i];
        const quantity = orderItem.quantity;
        const sellingPrice = orderItem.sellingPrice;
        totalPrice = totalPrice + quantity * sellingPrice;
    }
    return totalPrice.toFixed(2);
}

function extractNameAndCategory(brandCategory) {
    var index = brandCategory.indexOf('~');
    var name = brandCategory.substr(0, index);
    var category = brandCategory.substr(index + 1);
    return {
        "brandName": name,
        "brandCategory": category
    };
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
}

var arrayInputNumber = document.querySelectorAll("input[type=number]");
arrayInputNumber.forEach( (input) => input.addEventListener("keypress", function (evt) {
    if (evt.which != 8 && evt.which != 0 && evt.which != 46 && evt.which < 48 || evt.which > 57) {
        evt.preventDefault();
    }
    if (window.location.pathname === "/pos/ui/inventory" && evt.which === 46) {
        evt.preventDefault();
    }
}));

