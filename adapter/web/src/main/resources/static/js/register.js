function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#imageResult')
                .attr('src', e.target.result);
        };
        reader.readAsDataURL(input.files[0]);
    }
}

$(function () {
    $('#upload').on('change', function () {
        readURL(input);
    });
});

var input = document.getElementById( 'upload' );
var infoArea = document.getElementById( 'upload-label' );

input.addEventListener( 'change', showFileName );
function showFileName( event ) {
    var input = event.srcElement;
    var fileName = input.files[0].name;
    infoArea.textContent = 'File name: ' + fileName;
}

var current_fs, next_fs, previous_fs;
var left, opacity, scale;
var animating;

jQuery( ".next" ).click(
    function () {
        if (animating) {
            return false;
        }
        animating = true;

        current_fs = jQuery( this ).parent();
        next_fs    = jQuery( this ).parent().next();

        jQuery( "#progressbar li" ).eq( jQuery( "fieldset" ).index( next_fs ) ).addClass( "active" );

        next_fs.show();
        current_fs.animate(
            {opacity: 0},
            {
                step: function (now, mx) {
                    scale = 1 - (1 - now) * 0.2;
                    left = (now * 50) + "%";
                    opacity = 1 - now;
                    current_fs.css(
                        {
                            'transform': 'scale(' + scale + ')',
                            'position': 'absolute'
                        }
                    );
                    next_fs.css( {'left': left, 'opacity': opacity} );
                },
                duration: 800,
                complete: function () {
                    current_fs.hide();
                    current_fs.css( {'position': 'relative'} );
                    next_fs.css( {'position': 'relative'} );
                    animating = false;
                },
                easing: 'easeInOutBack'
            }
        );
    }
);

jQuery( ".previous" ).click(
    function () {
        if (animating) {
            return false;
        }
        animating = true;

        current_fs  = jQuery( this ).parent();
        previous_fs = jQuery( this ).parent().prev();

        jQuery( "#progressbar li" ).eq( jQuery( "fieldset" ).index( current_fs ) ).removeClass( "active" );

        previous_fs.show();
        previous_fs.css( {'position': 'absolute'} );
        current_fs.animate(
            {opacity: 0},
            {
                step: function (now, mx) {
                    scale = 0.8 + (1 - now) * 0.2;
                    left = ((1 - now) * 50) + "%";
                    opacity = 1 - now;
                    current_fs.css( {'left': left} );
                    previous_fs.css( {'transform': 'scale(' + scale + ')', 'opacity': opacity} );
                },
                duration: 800,
                complete: function () {
                    current_fs.hide();
                    previous_fs.css( {'position': 'relative'} );
                    animating = false;
                },
                easing: 'easeInOutBack'
            }
        );
    }
);

jQuery( ".submit" ).click(
    function () {
        return false;
    }
);
