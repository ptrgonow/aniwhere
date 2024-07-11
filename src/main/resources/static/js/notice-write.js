document.addEventListener('DOMContentLoaded', function () {
    CKEDITOR.replace('editor', {
        toolbar: [
            { name: 'editing', items: [ 'Undo', 'Redo' ] },
            { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline' ] },
            { name: 'links', items: [ 'Link' ] },
            { name: 'insert', items: [ 'Image', 'Table' ] },
            { name: 'paragraph', items: [ 'NumberedList', 'BulletedList' ] },
            { name: 'styles', items: [ 'Format' ] }
        ],
        language: 'ko',
        height : 500
    });

    document.getElementById('notice-form').addEventListener('submit', function (e) {
        const titleInput = document.querySelector('input[name="title"]');
        const title = titleInput ? titleInput.value.trim() : '';
        const editorValue = CKEDITOR.instances.editor.getData().trim();

        let errorMessages = [];

        if (title === '') {
            errorMessages.push('제목을 입력하세요.');
        }

        if (editorValue === '') {
            errorMessages.push('내용을 입력하세요.');
        }

        if (errorMessages.length > 0) {
            alert(errorMessages.join('\n'));
            e.preventDefault();
        } else {
            // Update the hidden textarea with the editor's data
            document.querySelector('textarea[name="content"]').value = editorValue;
        }
    });
});