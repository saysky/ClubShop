$(function () {
    $('#keyword').keypress(function (e) {
        var key = e.which; //e.which是按键的值
        if (key == 13) {
            var q = $(this).val();
            if (q && q != '') {
                $('#submitSearch').click();
            }
        }
    });
});

function search() {
   $('#submitSearch').click();
