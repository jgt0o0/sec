var layout = {}

layout.selectMenu = function () {
    var li = $('#' + selected_menu_id).addClass('active');
}

layout.hideSideMenu = function () {
    $('#hide_menu').click(function () {
    });
}

$(function () {
    layout.selectMenu();

    layout.hideSideMenu();
})