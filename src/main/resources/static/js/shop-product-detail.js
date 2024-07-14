$(document).ready(function() {

    getAllReviews();
    $('#toggle-button').click(toggleDetails);

    $('.review-rating > .star').click(function() {
        $(this).parent().children('span').removeClass('on');
        $(this).addClass('on').prevAll('span').addClass('on');
    });

    $('#review-submit').on('click', saveReview);

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

function callAjax(url, method, data, successCallback, errorCallback, options = {}) {
    $.ajax({
        url: url,
        type: method,
        data: data,
        processData: options.processData !== undefined ? options.processData : true,
        contentType: options.contentType !== undefined ? options.contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: successCallback,
        error: errorCallback
    });
}

function getAllReviews() {
    const productId = $('#review-product-id').val();
    const url = '/review/list/' + productId;
    const reviewContainer = $('#reviews-container');

    callAjax(url, 'GET', null, function(data) {
        reviewContainer.empty();
        data.forEach(function(review) {
            const reviewDiv = $('<div class="review-box"></div>');
            const reviewImg = $('<div class="review-img"><img src="" alt=""></div>');
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

function saveReview() {
    const reviewContent = $('#review-content').val();
    const reviewRating = $('.review-rating > .star.on').length;
    const productId = $('#review-product-id').val();
    const reviewImgFile = $('#review-image')[0].files[0];
    const userId = $('#review-writer').val();

    if (reviewRating === 0) {
        alert('별점을 선택해주세요.');
        return;
    }

    const formData = new FormData();
    formData.append('reviewContent', reviewContent);
    formData.append('reviewRating', reviewRating);
    formData.append('productId', productId);
    formData.append('userId', userId);

    if (reviewImgFile) {
        const reader = new FileReader();
        reader.onloadend = function() {
            const base64Image = reader.result.split(',')[1];
            formData.append('reviewImg', base64Image);
            sendReviewData(formData);
        };
        reader.readAsDataURL(reviewImgFile);
    } else {
        sendReviewData(formData);
    }
}

function sendReviewData(formData) {
    const url = '/review/insert';
    callAjax(url, 'POST', JSON.stringify(Object.fromEntries(formData.entries())), function(data) {
        getAllReviews();
        $('#review-content').val('');
        $('.review-rating > .star').removeClass('on');
        $('#review-image').val('');
    }, function(error) {
        console.log(error);
    }, {
        processData: false,
        contentType: 'application/json'
    });
}
