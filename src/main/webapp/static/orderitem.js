function getOrderUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/order";
}
function getProductUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/product";
}

function getOrderListPageUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/ui/order";
}

let cartItems = [];

function addToCart(event) {
  event.preventDefault();
  const $form = $("#cart-item-form");
  const data = JSON.parse(toJson($form));

  const barcode = data.barcode;
  // Update existing item
  const existingItem = cartItems.find((item) => {
    return item.barcode === barcode;
  });
  if (existingItem && existingItem.sellingPrice==data.sellingPrice) {
    existingItem.quantity =
      Number(existingItem.quantity) + Number(data.quantity);
    existingItem.sellingPrice =
      Number(existingItem.sellingPrice);
  }
  else if(existingItem && existingItem.sellingPrice!=data.sellingPrice) {
    console.log("selling price different");
  }
  else {
    // Add new item
    cartItems.push({
      barcode: barcode,
      quantity: Number(data.quantity),
      sellingPrice: Number(data.sellingPrice),
    });
  }
  //   Clear the form
  $form.trigger("reset");
  displayCartItemsToTable();
}

function displayCartItemsToTable() {
  if (cartItems.length > 0) {
    $("#place-order").show();
  }
  const $table = $("#cart-items-table");
  $table.empty();
  $table.append(`<thead>
    <tr>
        <th>Barcode</th>
        <th>Quantity</th>
        <th>Selling Price</th>
        <th>Total</th>
        <th>Action</th>
    </tr>
</thead>`);
  $table.append(`<tbody>`);
  cartItems.forEach((item) => {
    $table.append(`<tr id=${item.barcode}>
        <td>${item.barcode}</td>
        <td>${item.quantity}</td>
        <td>${item.sellingPrice}</td>
        <td>${item.quantity * item.sellingPrice}</td>
        <td><button class="delete-row btn btn-outline-danger" onclick='deleteRow("${
          item.barcode
        }")'>Delete</button></td>
    </tr>`);
  });
  $table.append(`</tbody>`);
}

function placeOrder() {
  $.ajax({
    url: getOrderUrl(),
    type: "POST",
    data: JSON.stringify(cartItems),
    contentType: "application/json",
    success: function (data) {
      $("#success-modal").modal("show");
      SuccessMessage("Order Placed");
      getOrderList();
    },
    error: function (data) {
      handleAjaxError(data);
    },
  });
}

function deleteRow(barcode) {
  console.log(barcode);
  cartItems = cartItems.filter((item) => {
    return item.barcode !== barcode;
  });

  displayCartItemsToTable();
}


function resetProductDetails(rowId) {
  var row = $("#row-" + rowId);
  row.find("#inputSellingPrice" + rowId).val("");
  row.find("#inputName" + rowId).val("");
}
function init() {
  $("#cart-item-form").submit(addToCart);
  $("#place-order").hide();
  $("#place-order").click(placeOrder);
  $(".delete-row").on("click", deleteRow);
}

$(document).ready(init);
