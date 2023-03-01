function getOrderUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/orders";
}

function getInventoryUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/inventory";
}

function getProductUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/products";
}

function getInvoiceUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/pdf";
}




function getRole() {
  var role = $("meta[name=role]").attr("content");
  return role;
}

//Forntend Check(add Item)
var mapbarcodequantity = {};
var mapbarcodesellingPrice = {};

function MapBarcodeToQuantity() {
  var url = getInventoryUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      for (let i in data) {
        // console.log(data[i].barcode);
        if (!mapbarcodequantity[data[i].barcode])
          mapbarcodequantity[data[i].barcode] = [data[i].quantity];
      }
    },
    error: handleAjaxError,
  });
}

function MapBarcodeToSellingPrice() {
  var url = getProductUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      console.log(data);
      for (let i in data) {
        // console.log(data[i]);
        if (!mapbarcodesellingPrice[data[i].barcode])
          mapbarcodesellingPrice[data[i].barcode] = [data[i].mrp];
      }
    },
    error: handleAjaxError,
  });
}

//BUTTON ACTIONS

function updateOrder(event) {
  var ok = true;
  const data = orderItems.map((it) => {
    if (isNaN(it.quantity)) {
      $.notify.defaults({ clickToHide: true });
      $(".notifyjs-wrapper").trigger("notify-hide");
      $.notify("Quantity cannot be empty", "error");
      ok = false;
    }
    if (isNaN(it.sellingPrice)) {
      $.notify.defaults({ clickToHide: true });
      $(".notifyjs-wrapper").trigger("notify-hide");
      $.notify("Selling Price cannot be empty", "error");
      ok = false;
    }
    return {
      barcode: it.barcode,
      quantity: it.quantity,
      sellingPrice: it.sellingPrice,
    };
  });

  if (!ok) return;
  //	var json = toJson($form);
  const json = JSON.stringify(data);
  var id = $("#order-edit-modal input[name=id]").val();
  var url = getOrderUrl() + "/" + id;
  $.ajax({
    url: url,
    type: "PUT",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      $("#order-edit-modal").modal("toggle");
      // $('.notifyjs-wrapper').trigger('notify-hide');
      // $.notify("Order successfully edited!", 'success');
      SuccessMessage("Successfully Updated");
      getOrderList();
    },
    error: handleAjaxError,
  });
}

function getOrderList() {
  var url = getOrderUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      $('.datatable').DataTable().destroy();
      displayOrderList(data);
      pagination();
    },
    error: handleAjaxError,
  });
}

function populateBarcodeDropdown(formId) {
  var url = getInventoryUrl();
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      addDataToBarcodeDropdown(data, formId);
    },
    error: handleAjaxError,
  });
}

function addDataToBarcodeDropdown(data, formId) {
  var $barcode = $(`${formId} select[name=barcode]`);
  $barcode.empty();

  var barcodeDefaultOption = '<option value="">Select a barcode</option>';
  $barcode.append(barcodeDefaultOption);

  for (var i in data) {
    var e = data[i];
    var option = '<option value="' + e.barcode + '">' + e.barcode + "</option>";
    $barcode.append(option);
  }
}

function displayCreateOrderItems(orderItems, total = 0) {
  var $tbody = $("#order-item-table").find("tbody");
  $tbody.empty();

  var cnt = 0;
  // <td class="text-center">
  //                 <input
  //                   id="order-item-${e.barcode}"
  //                   type="number"
  //                   class="form-control quantityData"
  //                   value="${e.quantity}"
  //                   onchange="onQuantityChanged('${e.barcode}')"
  //                   style="width:70%" min="1">
  //            </td>
  //            <td class="text-center">
  //                 <input
  //                   id="order-item-sp-${e.barcode}"
  //                   type="number"
  //                   class="form-control sellingPriceData"
  //                   value="${e.sellingPrice}"
  //                   onchange="onSellingPriceChanged('${e.barcode}')"
  //                   style="width:70%" min="0" step="0.01">
  //            </td>
  var Delete_item = "Delete item";
  for (var i in orderItems) {
    var e = orderItems[i];
    cnt++;
    const row = `
         <tr>
               <td class="text-center">${cnt}</td>
               <td class="text-center">${e.barcode}</td>
               <td class="text-center">${e.quantity}</td>
               <td class="text-center">${e.sellingPrice.toFixed(2)}</td>
               <td class="text-center">
                     <button class="btn btn-dark" 
                       title="Delete item" onclick="deleteOrderItem('${e.barcode}','${e.quantity}')">
                       ${Delete_item}
                     </button>
               </td>
             </tr>
           `;

    $tbody.append(row);
  }
  $("#add-grand-total").text(total.toFixed(2));
}

//UI DISPLAY METHODS

let orderItems = [];

function getCurrentOrderItem() {

  if(isInteger($("#inputQuantity").val())==false)
    return frontendErrors("Quantity is not an integer");

  return {
    barcode: $("#inputBarcode").val(),
    sellingPrice: Number.parseFloat($("#inputSellingPrice").val()),
    quantity: Number.parseInt($("#inputQuantity").val()),
  };
}

function addItem(item) {
  const index = orderItems.findIndex((it) => it.barcode === item.barcode.toString());
  console.log(item);

  // if(isInteger(item.quantity)==false)
  //   return frontendErrors("Quantity is not an integer");

  console.log(mapbarcodequantity[item.barcode]);
  if (index == -1) {
    if (mapbarcodequantity[item.barcode] < item.quantity) {
      ErrorMessage("Inventory Not Present");
      return;
    }
    // console.log(item.sellingPrice);
    console.log(mapbarcodesellingPrice[item.barcode]);
    if(mapbarcodesellingPrice[item.barcode]<item.sellingPrice){
      ErrorMessage("selling Price cannot be greater than MRP");
      return ;
    }
    orderItems.push(item);
    mapbarcodequantity[item.barcode]-=item.quantity;
  } 
  else if (orderItems[index].sellingPrice == item.sellingPrice) {
    if (mapbarcodequantity[item.barcode] < item.quantity) {
      ErrorMessage("Inventory Not Present");
      return;
    }
    orderItems[index].quantity += item.quantity;
    mapbarcodequantity[item.barcode]-=item.quantity;
  } 
  else {
    if(mapbarcodesellingPrice[item.barcode]<item.sellingPrice){
      ErrorMessage("selling Price cannot be greater than MRP");
      return; 
    }
    ErrorMessage("Selling Price Differnet");
      return;
  }
  console.log(mapbarcodequantity[item.barcode]);
  SuccessMessage(item.barcode+" added in orderlist");
  
}

function calculateTotalPrice() {
  let total = 0;
  orderItems.forEach((item) => (total += item.sellingPrice * item.quantity));
  return total;
}

function deleteOrderItem(barcode,quantity) {

  const index = orderItems.findIndex((it) => it.barcode === barcode.toString());
  
  if (index == -1) return;

  console.log(mapbarcodequantity[barcode]);
  mapbarcodequantity[barcode]+=quantity;
  console.log(mapbarcodequantity[barcode]);

  orderItems.splice(index, 1);
  displayCreateOrderItems(orderItems, calculateTotalPrice());
}

function addOrderItem(event) {
  // console.log("hello");
  event.preventDefault();
  const item = getCurrentOrderItem();
  addItem(item);
  // console.log("hello1");
  displayCreateOrderItems(orderItems, calculateTotalPrice());
  $("#order-item-form").trigger("reset");
}

function addEditOrderItem() {
  const item = getCurrentEditOrderItem();
  addItem(item);
  console.log(item);
  console.log("H");
  displayEditOrder(orderItems, calculateTotalPrice());
  $("#edit-order-item-form").trigger("reset");
}

function deleteEditOrderItem(barcode,quantity) {
  console.log(barcode);
  const index = orderItems.findIndex((it) => it.barcode === barcode.toString());
  console.log(index);

  if (index == -1) return;

  console.log(mapbarcodequantity[barcode]);
  mapbarcodequantity[barcode]+=quantity;
  console.log(mapbarcodequantity[barcode]);

  orderItems.splice(index, 1);
  displayEditOrder(orderItems, calculateTotalPrice());
}

function displayOrderList(data) {
  var $tbody = $("#order-table").find("tbody");
  $tbody.empty();
  var cnt=0;
  console.log(data);
  for (var i=data.length-1; i>=0; i--) {
    cnt++;
    var b = data[i];
    // console.log(b);
    // var buttonHtml = '<button onclick="deleteOrder(' + b.id + ')">delete</button>'
    var orderDateStr = convertTimeStampToDateTime(b.createdAt);
    // var totalBillAmount = calculateTotalPrice(b.orderItems);
    var buttonHtml = "";
    if (getRole() === "supervisor") {
      if (b.isInvoiceCreated == true) {
          // buttonHtml =
          // '<button type="button" class="btn btn-dark" disabled="disabled"  title="Edit"></button>';
      } else {
        buttonHtml =
          '<button type="button" class="btn btn-dark" title="Edit" onclick="displayEditOrderModal(' +
          b.id +
          ')">Edit</button>';
      }
    }

    buttonHtml +=
      ' <button type="button" class="btn btn-dark"  title="Details" onclick="displayOrderDetails(' +
      b.id +
      ')">Details</button>';

      if (b.isInvoiceCreated == false) {
    buttonHtml +=
      ' <button type="button" class="btn btn-dark" id="generateInvoice" title="Generate Invoice" onclick="GenerateInvoice(' +
      b.id +
      ')">Generate Invoice</button>'; 
      }
      else{
        buttonHtml +=
      ' <button type="button" class="btn btn-dark" id="downloadInvoice" title="Download Invoice" onclick="download(' +
      b.id +
      ')">Download Invoice</button>'; 
      }

    
    var row =
      "<tr>" +
      '<td class="text-center">' +
      cnt +
      "</td>" +
      '<td class="text-center">' +
      orderDateStr +
      "</td>" +
      '<td class="text-center">' +
      numberWithCommas(b.billAmount.toFixed(2)) +
      "</td>" +
      "<td>" +
      buttonHtml +
      "</td>" +
      "</tr>";
    $tbody.append(row);
  }
}

function displayEditOrder(data, total = 0) {
  // const orderId = data.id;
  // const orderItems = data.orderItems;

  //Display list of order items
  const $orderItemsTable = $("#edit-order-items-table");
  $orderItemsTable.find("tbody").empty();

  var cnt = 0;
  var Delete_item = "Delete item";
  for (var i in data) {
    var e = data[i];
    cnt++;
    //  <td class="text-center">
    //    <input
    //      id="order-item-${e.barcode}"
    //      type="number"
    //      class="form-control quantityData"
    //      value="${e.quantity}"
    //      onchange="onQuantityChanged('${e.barcode}')"
    //      style="width:70%" min="1">
    //  </td>
    //  <td class="text-center">
    //       <input
    //         id="order-item-sp-${e.barcode}"
    //         type="number"
    //         class="form-control sellingPriceData"
    //         value="${e.sellingPrice}"
    //         onchange="onSellingPriceChanged('${e.barcode}')"
    //         style="width:70%" min="0" step="0.01">
    //  </td>
    const row = `
                 <tr>
                   <td class="text-center">${cnt}</td>
                   <td class="barcode text-center">${e.barcode}</td>
                   <td class="barcode text-center">${e.quantity}</td>
                   <td class="barcode text-center">${e.sellingPrice.toFixed(2)}</td>
                   <td class="text-center">
                     <button type="button" onclick="deleteEditOrderItem('${e.barcode}','${e.quantity}')" 
                          data-placement="bottom" title="Delete" class="btn btn-dark">
                          ${Delete_item}
                      </button>
                   </td>
                 </tr>
               `;
    $orderItemsTable.find("tbody").append(row);
  }
  $("#edit-grand-total").text("₹" + total.toFixed(2));
}

function displayEditOrderModal(id) {
  var url = getOrderUrl() + "/" + id;
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      orderItems = data.orderItems;
      $("#order-edit-modal").modal("toggle");
      $("#order-edit-modal input[name=id]").val(id);
      populateBarcodeDropdown("#edit-order-item-form");
      displayEditOrder(orderItems, calculateTotalPrice());
    },
    error: handleAjaxError,
  });
}

function displayOrderDetails(id) {
  var url = getOrderUrl() + "/" + id;
  $.ajax({
    url: url,
    type: "GET",
    success: function (data) {
      displayOrderDetailsInModal(data);
    },
    error: handleAjaxError,
  });
}

function displayOrderDetailsInModal(data) {
  const orderId = data.id;
  const orderItems = data.orderItems;
  const orderDateStr = convertTimeStampToDateTime(data.createdAt);

  $("#order-id").text(orderId);
  $("#order-date").text(orderDateStr);

  //Display list of order items
  var grandTotal = 0;
  const $orderItemsTable = $("#order-items-table");
  $orderItemsTable.find("tbody").empty();

  for (let i = 0; i < orderItems.length; i++) {
    const orderItem = orderItems[i];
    const productName = orderItem.productName;
    const quantity = orderItem.quantity;
    const sellingPrice = orderItem.sellingPrice;
    const totalPrice = quantity * sellingPrice;
    grandTotal += totalPrice;
    const totalPriceStr = totalPrice.toFixed(2);

    const row =
      "<tr>" +
      '<td class="text-center">' +
      (i + 1) +
      "</td>" +
      '<td class="text-center">' +
      productName +
      "</td>" +
      '<td class="text-center">' +
      (quantity) +
      "</td>" +
      '<td class="text-center">' +
      (sellingPrice.toFixed(2)) +
      "</td>" +
      '<td class="text-center">' +
      (totalPriceStr) +
      "</td>" +
      "</tr>";

    $orderItemsTable.find("tbody").append(row);
  }

  const grandTotalStr = grandTotal.toFixed(2);
  $("#grand-total").text(grandTotalStr);
  $("#order-details-modal").modal("toggle");
}

function displayAddModal() {
  orderItems = [];
  $("#order-item-table tbody tr").remove();
  $("#add-order-modal").modal("toggle");
  populateBarcodeDropdown("#order-item-form");
}

function onQuantityChanged(barcode) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  const newQuantity = $(`#order-item-${barcode}`).val();
  orderItems[index].quantity = Number.parseInt(newQuantity);
  $("#add-grand-total").text(calculateTotalPrice().toFixed(2));
  $("#edit-grand-total").text(calculateTotalPrice().toFixed(2));
}

function onSellingPriceChanged(barcode) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  const newSellingPrice = $(`#order-item-sp-${barcode}`).val();
  orderItems[index].sellingPrice = Number.parseFloat(newSellingPrice);
  $("#add-grand-total").text(calculateTotalPrice().toFixed(2));
  $("#edit-grand-total").text(calculateTotalPrice().toFixed(2));
}

function getCurrentEditOrderItem() {
  // console.log()
  if(isInteger($("#inputEditQuantity").val())==false)
    return frontendErrors("Quantity is not an integer");
  return {
    barcode: $("#inputEditBarcode").val(),

    quantity: Number.parseInt($("#inputEditQuantity").val()),
    sellingPrice: Number.parseFloat($("#inputEditSellingPrice").val()),
  };
}

//INITIALIZATION CODE
function init() {
  // $('#add-order-item').submit()
  $("#nav-order").addClass("active");
  $("#add-order").click(placeNewOrder);
  $("#order-item-form").submit(addOrderItem);
  $("#add-edit-order-item").click(addEditOrderItem);
  $("#add-order-button").click(displayAddModal);
  $("#update-order").click(updateOrder);
  $("#refresh-data").click(getOrderList);
  $('#generateInvoice').click(getOrderList);
  $('#downloadInvoice').click(getOrderList);
  
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(MapBarcodeToQuantity);
$(document).ready(MapBarcodeToSellingPrice);

// Place Order
function placeNewOrder() {
  var ok = true;
  const data = orderItems.map((it) => {
    if (isNaN(it.quantity)) {
      $.notify.defaults({ clickToHide: true, autoHide: false });
      $(".notifyjs-wrapper").trigger("notify-hide");
      $.notify("Quantity cannot be empty", "error");
      ok = false;
    }
    if (isNaN(it.sellingPrice)) {
      $.notify.defaults({ clickToHide: true, autoHide: false });
      $(".notifyjs-wrapper").trigger("notify-hide");
      $.notify("Selling Price cannot be empty", "error");
      ok = false;
    }
    return {
      barcode: it.barcode,
      quantity: it.quantity,
      sellingPrice: it.sellingPrice,
    };
  });
  if (!ok) return;
  const json = JSON.stringify(data);
  placeOrder(json);
}

//BUTTON ACTIONS
function placeOrder(json) {
  //Set the values to update
  const url = getOrderUrl();

  $.ajax({
    url: url,
    type: "POST",
    data: json,
    headers: {
      "Content-Type": "application/json",
    },
    success: function (response) {
      $("#add-order-modal").modal("toggle");
      SuccessMessage("Successfully Added");
      getOrderList();
    },
    error: handleAjaxError,
  });

  return false;
}

function disableOrderEdit(id) {
  //Set the values to update
  const url = getOrderUrl() + "/invoice/" + id;
  //    url = `${url}/invoice/${id}`;

  $.ajax({
    url: url,
    type: "GET",
    success: function (response) {
      getOrderList();
    },
    error: handleAjaxError,
  });
}

function GenerateInvoice(id) {
  var url = getInvoiceUrl() + "/" + id;
  $.ajax({
      url: url,
      type: 'GET',
      success: function () {
          // var url2 = getInvoiceUrl()+"/download/"+id;
          // window.location.href = url;
          getOrderList();

          // console.log("Hello")
          // download(id);
          // console.log("hello");
      },

      error: handleAjaxError
  });
}

function download(id) {
  var url = getInvoiceUrl() + "/download/" + id;

  window.location.href = url;

}
