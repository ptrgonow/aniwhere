$(document).ready(function() {
    const productId = $('#review-product-id').val();
    getAllReviews(productId);
    $('#toggle-button').click(toggleDetails);

    $('.review-rating > .star').click(function() {
        $(this).parent().children('span').removeClass('on');
        $(this).addClass('on').prevAll('span').addClass('on');
    });

    $('#review-submit').on('click', async function(e) {
        e.preventDefault();
        await saveReview(productId);
    });
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

function formatDate(dateString) {
    const date = new Date(dateString);
    const yyyy = date.getFullYear();
    const MM = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    const HH = String(date.getHours()).padStart(2, '0');
    const mm = String(date.getMinutes()).padStart(2, '0');
    const ss = String(date.getSeconds()).padStart(2, '0');
    return `${yyyy}-${MM}-${dd} ${HH}:${mm}:${ss}`;
}

function getAllReviews(productId) {
    const url = '/review/list/' + productId;
    const reviewContainer = $('#reviews-container');

    callAjax(url, 'GET', null, function(data) {
        reviewContainer.empty();
        data.forEach(function(review) {
            const reviewDiv = $('<div class="review-box"></div>');
            const reviewId = $('<input type="hidden" class="reviewId">').val(review.reviewId);
            const reviewImg = $('<div class="review-img"><img src="" alt="Review Image"></div>');

            if (review.reviewImg) {
                reviewImg.find('img').attr('src', 'data:image/jpeg;base64,' + review.reviewImg);
            } else {
                reviewImg.find('img').attr('src', '');
            }

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
            const reviewCreatedAt = $('<p class="createAt"></p>').text(formatDate(review.updatedAt));
            const updateButton = $('<i class="fas fa-edit update-button"></i>');
            const deleteButton = $('<i class="fas fa-times delete-button"></i>');

            deleteButton.click(function() {
                deleteReview(review.reviewId, productId);
            });

            updateButton.click(function() {
                toggleEditState(reviewDiv, review, updateButton);
            });

            reviewContent.append(reviewId, userName, reviewRating, reviewText, reviewCreatedAt);

            reviewDiv.append(reviewImg, reviewContent);
            reviewContainer.append(reviewDiv);
            reviewDiv.append(updateButton);
            reviewDiv.append(deleteButton);
        });
    }, function(error) {
        console.log("Error fetching reviews: ", error);
    });
}

async function saveReview(productId) {
    const reviewContent = $('#review-content').val();
    const reviewRating = $('.review-rating > .star.on').length;
    const reviewImgFile = $('#review-image')[0].files[0];
    const userId = $('#userId').val();
    const userName = $('#review-writer').val();

    const reviewExists = await checkReview(userId, productId);
    if (reviewExists) {
        alert('이미 리뷰를 작성하셨습니다.');
        return;
    }

    if (reviewRating === 0) {
        alert('별점을 선택해주세요.');
        return;
    }

    const formData = new FormData();
    formData.append('reviewContent', reviewContent);
    formData.append('reviewRating', reviewRating);
    formData.append('productId', productId);
    formData.append('userId', userId);
    formData.append('userName', userName);

    if (reviewImgFile) {
        const reader = new FileReader();
        reader.onloadend = function() {
            const base64Image = reader.result.split(',')[1];
            formData.append('reviewImg', base64Image);
            sendReviewData(formData, productId);
        };
        reader.readAsDataURL(reviewImgFile);
    } else {
        sendReviewData(formData, productId);
    }
}

function sendReviewData(formData, productId) {
    const url = '/review/insert';
    callAjax(url, 'POST', JSON.stringify(Object.fromEntries(formData.entries())), function(data) {
        console.log("Review saved successfully: ", data);
        getAllReviews(productId);
        $('#review-content').val('');
        $('.review-rating > .star').removeClass('on');
        $('#review-image').val('');
    }, function(error) {
        console.log("Error saving review: ", error);
    }, {
        processData: false,
        contentType: 'application/json'
    });
}

function deleteReview(reviewId, productId) {
    const url = '/review/delete/' + reviewId;
    callAjax(url, 'DELETE', null, function(data) {
        console.log("Review deleted successfully: ", data);
        getAllReviews(productId);
    }, function(error) {
        console.log("Error deleting review: ", error);
    });
}

function updateReview(reviewId, productId, updatedReviewContent, updatedReviewRating, updateButton) {
    const formData = {
        reviewId: reviewId,
        reviewContent: updatedReviewContent,
        reviewRating: updatedReviewRating,
        productId: productId,
    };

    const url = '/review/update';
    callAjax(url, 'PUT', JSON.stringify(formData), function(data) {
        console.log("Review updated successfully: ", data);
        getAllReviews(productId);
        updateButton.removeClass('fa-save').addClass('fa-edit');
    }, function(error) {
        console.log("Error updating review: ", error);
    }, {
        processData: false,
        contentType: 'application/json'
    });
}

function toggleEditState(reviewDiv, review, updateButton) {
    const reviewContent = reviewDiv.find('.review-content');
    const reviewText = reviewContent.find('p.review-content');
    const reviewRating = reviewContent.find('.review-rating');

    if (updateButton.hasClass('fa-edit')) {
        const reviewTextEdit = $('<textarea class="edit-review-content"></textarea>').val(reviewText.text());
        reviewText.replaceWith(reviewTextEdit);

        reviewRating.empty();
        for (let i = 1; i <= 5; i++) {
            const star = $('<span class="star"></span>').attr('data-value', i);
            if (i <= review.reviewRating) {
                star.addClass('on');
            }
            star.click(function() {
                $(this).parent().children('span').removeClass('on');
                $(this).addClass('on').prevAll('span').addClass('on');
            });
            reviewRating.append(star);
        }

        updateButton.removeClass('fa-edit').addClass('fa-save');
    } else {
        const updatedReviewContent = reviewContent.find('textarea.edit-review-content').val();
        const updatedReviewRating = reviewRating.find('.star.on').length;
        updateReview(review.reviewId, review.productId, updatedReviewContent, updatedReviewRating, updateButton);
    }
}

function checkReview(userId, productId) {
    const url = '/review/check/' + userId + '/' + productId;

    return new Promise((resolve, reject) => {
        callAjax(url, 'GET', null, function(data) {
            resolve(data);
        }, function(error) {
            console.log("Error checking review: ", error);
            reject(error);
        });
    });
}
