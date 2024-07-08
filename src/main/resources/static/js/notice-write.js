// Ensure the CKEditor instance is properly initialized
document.addEventListener('DOMContentLoaded', function () {
    // Initialize CKEditor if not already initialized
    if (CKEDITOR.instances.editor) {
        CKEDITOR.instances.editor.destroy();
    }
    CKEDITOR.replace('editor', {
        enterMode: CKEDITOR.ENTER_BR, // 줄 바꿈 시 <br> 태그 사용
        shiftEnterMode: CKEDITOR.ENTER_P, // Shift + Enter 시 <p> 태그 사용
        height: 400 // CKEditor 높이 조정
    });
});

// Initialize CKEditor
CKEDITOR.replace('editor', {
    enterMode: CKEDITOR.ENTER_BR, // 줄 바꿈 시 <br> 태그 사용
    shiftEnterMode: CKEDITOR.ENTER_P, // Shift + Enter 시 <p> 태그 사용
    height: 400 // CKEditor 높이 조정
});

// Ensure CKEditor data is included in form submission
document.getElementById('notice-form').addEventListener('submit', function (e) {
    // Validate CKEditor content
    const editorValue = CKEDITOR.instances.editor.getData();
    if (editorValue.trim() === '') {
        alert('내용을 입력하세요.');
        e.preventDefault(); // Prevent form submission
    }
});