// AJAX autocomplete
$(function () {
    $('#search').autocomplete({
        source:
            function (request, response) {
            $.ajax({
                url: "../search.api?term=" + request.term,
                success: function (data) {
                    response(data);
                }
            });
        },
        minLength: 2,
         select: function (event, ui) {
             $('#search').val(ui.item.value);
             $("#form_search").submit();
         }});
})

// Hiding of left sidebar in tablet and less
$(document).ready(function () {
    $('[data-toggle="offcanvas"]').click(function () {
        $('.row-offcanvas').toggleClass('active')
    });
});