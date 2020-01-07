function removeDiv(fileNumber) {
    $('#file' + fileNumber).remove();
}

function categoryLoad() {
    $.ajax({
        url: '/category',
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            let content = '';
            let categories = data.data;
            for (let i = 0; i < categories.length; i++)
                content += '<option value="' + categories[i].id + '">' + categories[i].name + '</option>';
            $('#category_name').html(content);
        }
    });
}

$(document).ready(function () {
    var fileCnt = 1;
    $('#addFile').click(function () {
        var add = '<div class="row" id="file' + fileCnt + '"> ' +
            '   <div class="col-md-3"></div> ' +
            '   <div class="col-md-6">' +
            '       <div class="form-group">' +
            '           <div class="input-group mb-2 mr-sm-2 mb-sm-0">' +
            '               <input type="file" name="file" accept=".mp4" class="form-control" placeholder="동영상_제목.mp4">' +
            '           </div> ' +
            '       </div> ' +
            '   </div> ' +
            '   <div class="col-md-3">' +
            '       <input type="button" class="btn btn-primary" onclick="removeDiv(' + fileCnt + ')" value="지우기">' +
            '   </div>' +
            '</div>';
        $('#file-list').append(add);

        $('#newCategory').click(function () {
            let name = prompt("추가할 카테고리 명을 입력하세요.", '');
            if (name !== '') {
                $.ajax({
                    url: '/category',
                    type: "POST",
                    data: JSON.stringify(name),
                    contentType: 'application/json',

                    success: function (data) {
                        alert(data.status);
                        categoryLoad();
                    }
                })
            }
        });

        categoryLoad();
    });
});
