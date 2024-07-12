$(document).ready(function() {

    getAllReviews();
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

function callAjax(url, method, data, successCallback, errorCallback) {
    $.ajax({
        url: url,
        type: method,
        data: data,
        success: successCallback,
        error: errorCallback
    });
}

function getAllReviews() {
    const productId = $('#product-id').val();
    console.log(productId)
    const url = '/reviews/list/' + productId;
    const reviewContainer = $('#reviews-container');

    callAjax(url, 'GET', null, function(data) {
        reviewContainer.empty();
        data.forEach(function(review) {
            const reviewDiv = $('<div class="review-box"></div>');
            const reviewImg = $('<div class="review-img"><img alt=""></div>');
            reviewImg.find('img').attr('src', review.reviewImg); // 이미지 URL 설정
            const reviewContent = $('<div class="review-content"></div>');
            const userName = $('<h5 class="userName"></h5>').text(review.userName);
            const reviewRating = $('<div class="review-rating"></div>');


            for (let i = 1; i <= 5; i++) {
                const star = $('<span class="star"></span>').attr('data-value', i);
                if (i <= review.reviewRating) {
                    star.addClass('on');
                }
                reviewRating.append(star);
            }

            const reviewText = $('<p class="review-content"></p>').text(review.reviewContent);
            const reviewCreatedAt = $('<p class="createAt"></p>').text(review.createdAt);

            reviewContent.append(userName, reviewRating, reviewText, reviewCreatedAt);
            reviewDiv.append(reviewImg, reviewContent);
            reviewContainer.append(reviewDiv);
        });
    }, function(error) {
        console.log(error);
    });
}
