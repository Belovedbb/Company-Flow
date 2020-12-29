$(document).ready(function () {
    $('.performance-listing').on('click', 'button[data-filter-performance-listing-url]', function (event){
        event.preventDefault();
        var url = $("#filter_form").attr("action") + $(this).data('filter-performance-listing-url');
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