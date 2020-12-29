$(function () {
    $('.datetimepicker').datetimepicker({
        defaultDate: new Date(),
        format: 'yyyy-mm-ddTHH:mm'
    });
    let isDisabled = $('.form-control').prop('disabled');
    if(isDisabled){
        $('.datetimepicker').data("DateTimePicker").disable();
        $('.disable_div').fadeTo('slow',.6);
        $('.disable_div').append('<div style="position: absolute;top:0;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)"></div>');
    }
});