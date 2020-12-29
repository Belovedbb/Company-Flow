MultiSelectEnum = {
    ADD : 0,
    REMOVE : 1
};

NumberTypeEnum = {
    DISCRETE : 0,
    CONTINOUS : 1
};

function addTotalTextNumbers(allQuantities, number_type){
    let total = 0.0;
    allQuantities.forEach(function (item, index) {
        if(item.trim() || !(0 === item.length)) {
            total += parseFloat(item.trim());
        }
    });
    return number_type === NumberTypeEnum.CONTINOUS ? Number(total).toFixed(2) : Number(total).toFixed(0);
}

jQuery.fn.get_column_values = function(cellIndexMap) {
    let data = [];
    $(".entries-table tr").each(function(rowIndex) {
        $(this).find("td").each(function(cellIndex) {
            if (cellIndexMap[cellIndex])
                data.push($(this).text());
        });
    });
    return data;
};

jQuery.fn.get_all_totals = function() {
    let dataQuantity = [];
    $('.entries-table tr').each(function() {
        let val = $(this).find(".order_quantity input").val();
        dataQuantity.push(val);
    });
    let allQuantities = dataQuantity.toString().split(",");
    let totalQuantity = addTotalTextNumbers(allQuantities, NumberTypeEnum.DISCRETE);
    $('#total_quantity').val(totalQuantity);

    let dataAmount = $(this).get_column_values({ 3: true });
    let allAmount = dataAmount.toString().split(",");
    let totalAmount = addTotalTextNumbers(allAmount, NumberTypeEnum.CONTINOUS);
    $('#total_amount').val(totalAmount);
};

let addOrRemoveOrderEntry = function(values, operation){
    let persistent_list = [];
    for (let i = 0; i < values.length; i++) {
        persistent_list.push(values[i]);
    }
    let url = $("#purchase-order-form").attr("action") + "/addRemoveOrderEntry";
    let formData = $('#purchase-order-form').serializeArray();
    let param = {}, paramOperation={};
    param["name"] = 'productIds';
    param["value"] = persistent_list;
    paramOperation["name"] = 'operation';
    paramOperation["value"] = operation;
    formData.push(param);
    formData.push(paramOperation);
    $('#purchase-order-current-products').load(url, formData, function () {
        $(this).get_all_totals();
    });
};

function order_entity_quantity_change(object){
    let cell = $(object).closest('td');
    let row = cell.closest('tr');
    let rowIndex = row[0].rowIndex;
    let url = $("#purchase-order-form").attr("action") + "/displayEntryAmount";
    let formData = $('#purchase-order-form').serializeArray();
    let paramQuantity = {}, paramIndex = {};
    paramQuantity["name"] = 'quantityValue';
    paramQuantity["value"] = object.value;
    paramIndex["name"] = 'indexValue';
    paramIndex["value"] = rowIndex;
    formData.push(paramQuantity);
    formData.push(paramIndex);
    $('#purchase-order-current-products').load(url, formData,function() {
        $(this).get_all_totals();
    });
}

$('#purchase-order-product-list').multiSelect({
    selectableHeader: "<div id='rem-products'  class='custom-header'><span style='color:#999'>Remaining Products</span></div>",
    selectionHeader: "<div id='selected-products'  class='custom-header'><span style='color:#999'>Selected Products</span></div>",
    afterSelect: function(values){
        addOrRemoveOrderEntry(values, MultiSelectEnum.ADD);
    },
    afterDeselect: function(values){
        addOrRemoveOrderEntry(values, MultiSelectEnum.REMOVE);
    }
});

$(document).ready(function () {
    $('.order-listing').on('click', 'button[data-filter-order-listing-url]', function (event){
        event.preventDefault();
        var url = $("#filter_form").attr("action") + $(this).data('filter-order-listing-url');
        var formData = $('#filter_form').serializeArray();
        var param = {};
        param["name"] = $(this).attr('name');
        param["value"] = $(this).val();
        formData.push(param);
        $('#p_list_body').load(url, formData);
    });

    $("#clear_button").click(function(event){
        event.preventDefault();
        document.forms["filter_form"].reset();
    });



    window.setTimeout(function() {
        $(".alert").fadeTo(500, 0).slideUp(500, function(){
            $(this).remove();
        });
    }, 4000);
});
