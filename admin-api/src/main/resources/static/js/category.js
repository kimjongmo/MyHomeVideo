/**
 * 카테고리와 그 내용을 삭제한다.
 * */
function deleteCategory(id) {
    if(!confirm(id+"번 카테고리를 삭제하시겠습니까?"))
        return;
    $.ajax({
        url: "/category/" + id,
        method: "DELETE",
        dataType: "json",
        success: function (data) {
            alert(data.description);
        },
        error: function () {
            alert("요청 오류");
        }
    });
}

function getCategoryInfo(id) {
    alert(id+'정보를 가져옵니다.');
}


/**
 * 카테고리 정보를 수정한다.
 * */


/**
 * 카테고리 데이터를 엘리먼트 형식으로 반환
 *  @param data Header<List<CategoryListResponse>>형태의 데이터
 *  @return string 형태의 엘리먼트 내용
 * */
function dataToContents(data) {
    let contents = "";
    //data 배열에 있는 수만큼 엘리먼트를 만들어낸다.
    for (let i = 0; i < data.length; i++) {
        contents += "<tr>" +
            "<th scope=\"row\">" + data[i].id + "</th>" +
            "<td>" + data[i].name + "</td>" +
            "<td>" +
            "<a href=\"javascript:void(0);\" onclick='getCategoryInfo("+data[i].id+")' title=\"수정\"><i class='far fa-edit' style='font-size:24px'></i></a>" +
            "<a href=\"javascript:void(0);\" onclick='deleteCategory("+data[i].id+")' title=\"삭제\"><i class='far fa-trash-alt' style='font-size:24px'></i></a>" +
            "</td>" +
            "</tr>"
    }
    return contents;
}


$(document).ready(function () {
    //HTML 로딩 후 카테고리 정보를 가져오기
    $.ajax({
        url: "/category",
        dataType: "json",
        method: "GET",
        success: function (data) {
            if (data.status === "OK") {
                $('#contentBody').append(dataToContents(data.data));
                return;
            }
            alert(data.description);
        },
        error: function () {
            alert("카테고리 요청 실패");
        }
    });
});