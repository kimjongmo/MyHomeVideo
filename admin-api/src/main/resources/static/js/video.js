//TODO: 2020-02-22 썸네일 url 마우스를 올렸을 때 이미지 화면에 팝업 형식으로 보여주는 함수 작성하기
//TODO: 2020-02-22 삭제 및 수정 작성하기


function remove(id) {
    alert(id + '삭제합니다');
}

function modify(id) {
    alert(id + "수정합니다");
}

/**
 * 해당하는 카테고리와 페이지의 컨텐츠를 가져와, 페이지에 추가시킨다.
 * @param category 카테고리
 * @param page 가져올 페이지
 * */
// 비디오 컨텐츠를 가져온다. /video?category=&page=
function getVideoContent(category, page) {
    let data = {
        category: category,
        page: page
    };

    $.ajax({
        url: '/video',
        method: 'GET',
        dataType: 'json',
        data: data,
        success: function (data) {
            if (data.status === 'OK') {
                alert("성공");
                $('#contentBody').html("");
                $('#contentBody').append(dataToContents(data.data));
            }
        }
    });
}

/**
 * 서버로부터 받은 json 배열객체를 html의 element로 만들어 리스트에 추가한다.
 * @param data 비디오 배열
 * [{id: 6,
 * title: "런닝맨 431화",
 * thumbnail_url: "/img/thumbnail/일요일이 좋다 2부 런닝맨.E431.160515.720p-NEXT.jpg",
 * view: 2,
 * description: "일요일이 좋다 2부 런닝맨.E431.160515.720p-NEXT.mp4"}]
 * */
function dataToContents(data) {
    let contents = "";
    for (let i = 0; i < data.length; i++) {
        contents += "<tr>" +
            "<th scope=\"row\">" + data[i].id + "</th>" +
            "<td>" + data[i].title + "</td>" +
            "<td class='col-xs-2 text-truncate' style='max-width: 200px;'>" + data[i].thumbnail_url + "</td>" +
            "<td>" + data[i].view + "</td>" +
            "<td class='col-xs-2 text-truncate' style='max-width: 200px;' title='" + data[i].description + "'>" + data[i].description + "</td>" +
            "<td>" + "2020-02-18 20:00:00" + "</td>" +
            "<td>" +
            "<a href=\"javascript:void(0);\" onclick='modify(" + data[i].id + ")' title=\"수정\"><i class='far fa-edit' style='font-size:24px'></i></a>" +
            "<a href=\"javascript:void(0);\" onclick='remove(" + data[i].id + ")' title=\"삭제\"><i class='far fa-trash-alt' style='font-size:24px'></i></a>" +
            "</td>" +
            "</tr>";
    }
    return contents;
}

$(document).ready(function () {
    //HTML 로딩 후 카테고리 정보를 가져와서 dropdown 메뉴에 추가한다.
    $.ajax({
        url: "/category",
        dataType: "json",
        method: "GET",
        success: function (data) {
            if (data.status === "OK") {

                let dropdownMenu = $('.dropdown-menu'); // dropdown menu

                //드랍다운 메뉴에 각 카테고리를 추가한다.
                for (let i = 0; i < data.data.length; i++) {
                    dropdownMenu.append("<a class='dropdown-item'>" + data.data[i].name + "</a>");
                }

                //카테고리 클릭 시 이벤트를 추가한다.
                $('.dropdown-item').click(function () {
                    //드랍다운메뉴에 선택한 카테고리 노출시키기
                    $('.dropdown-toggle').text($(this).text());
                    //클릭 시 카테고리 데이터 가져오기
                    getVideoContent($(this).text(), 0);
                });
            } else
                alert(data.description);
        },
        error: function () {
            alert("카테고리 요청 실패");
        }
    });
});