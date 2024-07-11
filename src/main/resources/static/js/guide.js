$(document).ready(function() {

    // 기존 css에서 플로팅 배너 위치(top)값을 가져와 저장한다.
    var floatPosition = parseInt($("#walk_add_btn").css('top'));

    $(window).scroll(function () {
        // 현재 스크롤 위치를 가져온다.
        var scrollTop = $(window).scrollTop();
        var newPosition = scrollTop + floatPosition + "px";


        $("#walk_add_btn").stop().animate({
            "top": newPosition
        }, 50);
    }).scroll();

});