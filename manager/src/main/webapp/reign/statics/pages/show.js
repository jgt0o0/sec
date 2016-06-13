$.userName;

$.login = function () {
    $.userName = $('#id_user_name').val();
    $('#login_div').hide();
    $('#id_info_div').show();
    $('#id_name_div').html($.userName)
}

$(document).ready(function () {
    $('#login_btn').click(function () {
        $.login()
    })
})