
$(function() {
    $('.inactive-sub').hide();
    $('#status').change(function() {
        var statusOptions = document.getElementById('status');
        var currentStatus = statusOptions.options[statusOptions.selectedIndex].text;
        if (currentStatus == 'INACTIVE') {
            $('.inactive-sub').show();
        } else {
            $('.inactive-sub').hide();
        }
    });
});



$(document).ready(function () {
    $('.product-listing').on('click', 'button[data-filter-listing-url]', function (event){
        event.preventDefault();
        var url = $("#filter_form").attr("action") + $(this).data('filter-listing-url');
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
