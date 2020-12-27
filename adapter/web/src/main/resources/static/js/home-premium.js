
$(function () {

    Messenger.options = {
        extraClasses: 'messenger-fixed messenger-on-bottom  messenger-on-right',
        theme: 'flat',
        messageDefaults: {
            showCloseButton: true
        }
    }
    Messenger().post({
        message: 'Hey, how are you?<br>Welcome to Company Flow.',
        type: 'success'
    });
});

$(function () {
    var dataTable = $('.catalogue').DataTable({
            "pagingType": "full_numbers",
            responsive: {
                details: false
            },
        });

    $(document).on('sidebarChanged', function () {
        dataTable.columns.adjust();
        dataTable.responsive.recalc();
        dataTable.responsive.rebuild();
    });

});