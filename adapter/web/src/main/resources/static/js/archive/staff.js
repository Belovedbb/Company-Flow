function get_checked(){
    let checkboxes = document.getElementsByClassName("dt-checkboxes");
    let enable = false;
    for(let checkbox of checkboxes){
        if(checkbox.checked){
            enable = true;
            break;
        }
    }
    return enable;
}

$(document).ready(function () {
    let dataTable = $('.archive_table').DataTable({
        "ordering": false,
        'columnDefs': [
             {
                'targets': 0,
                'checkboxes': {
                   'selectRow': true
                }
             }
          ],
          'select': {
             'style': 'multi'
          },
          'order': [[1, 'asc']],
            "pagingType": "full_numbers",
            responsive: {
                details: false
            },
        });


$('.dt-checkboxes').change(function() {
    let enable = get_checked();
   if($(this).is(":checked")) {
      enable = true;
   }
   if(enable){
     $('#create-staff').removeClass('disabled');
     $('#create-staff').attr("data-toggle", "modal");
   }else{
        $('#create-staff').addClass('disabled');
         $('#create-staff').removeAttr('data-toggle');
   }

});

    $('.dt-checkboxes-cell input:checkbox').change(function() {
        setTimeout(function() {
          let enable = get_checked();
          if(enable){
               $('#create-staff').removeClass('disabled');
               $('#create-staff').attr("data-toggle", "modal");
             }else{
                  $('#create-staff').addClass('disabled');
                   $('#create-staff').removeAttr('data-toggle');
             }
        }, 100);
    });

    $('#create-staff').on('click', function(e){
      let rows_selected = dataTable.column(0).checkboxes.selected();
      let linkValue = "";
      $.each(rows_selected, function(index, rowId){
        linkValue += rowId + ',';
        });
        if(linkValue.trim() !== ''){
            linkValue = linkValue.substr(0, linkValue.length - 1);
        }
        this.href += '/' + linkValue;
        window.location = this.href;
        return false;
    });
});
