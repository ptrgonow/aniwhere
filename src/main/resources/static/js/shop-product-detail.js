$(document).ready(function() {

    $('#toggle-button').click(toggleDetails);

    $('.star_rating > .star').click(function() {
        $(this).parent().children('span').removeClass('on');
        $(this).addClass('on').prevAll('span').addClass('on');
    })

});

function toggleDetails() {
    const detailDiv = $('#product-detail');
    detailDiv.toggleClass('expanded');
    const button = $('#toggle-button');
    button.toggleClass('expanded');
    if (detailDiv.hasClass('expanded')) {
        button.find('i').removeClass('fa-chevron-down').addClass('fa-chevron-up');
    } else {
        button.find('i').removeClass('fa-chevron-up').addClass('fa-chevron-down');
    }
}
