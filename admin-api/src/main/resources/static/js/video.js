//TODO: 2020-02-22 썸네일 url 마우스를 올렸을 때 이미지 화면에 팝업 형식으로 보여주는 함수 작성하기
//TODO: 2020-02-22 삭제 및 수정 작성하기


function remove(id) {
    alert(id + '삭제합니다');
}

function modify(id) {
    alert(id + "수정합니다");
}

/**
 * 썸네일 URL 에 마우스를 올리면 이미지를 보여준다.
 * */
function thumbImgShow(e) {
    //이벤트 발생한 일레먼트의 x, y, 썸네일 URL
    let x = e.pageX;
    let y = e.pageY;
    let imgSource = "http://localhost:8080" + $(e.target).text();

    //이미지 팝업의 display, x, y 설정
    $('.img-popup').css("display", "block");
    $('.img-popup').css("left", x + "px");
    $('.img-popup').css("top", y + "px");

    //이미지 src 설정
    $('.img-popup img').attr("src", imgSource);

    //OK 버튼 이벤트 설정
    $('.img-popup span').click(function () {
        $('.img-popup').css("display", "none");
    });
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
                if(page === 0)
                    $('#contentBody').html("");
                //데이터를 엘리먼트로 변경 후 contentBody 이어 붙인다.
                $('#contentBody').append(dataToContents(data.data));
                //데이터 추가 버튼 추가
                loadBtn(category,page+1);
                return;
            }
            alert(data.description);
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
    //data 배열에 있는 수만큼 엘리먼트를 만들어낸다.
    for (let i = 0; i < data.length; i++) {
        contents += "<tr>" +
            "<th scope=\"row\">" + data[i].id + "</th>" +
            "<td>" + data[i].title + "</td>" +
            "<td><p class='col-xs-2 text-truncate' style='max-width: 200px;' onclick='thumbImgShow(event)'>" + data[i].thumbnail_url + "</p></td>" +
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

/** 버튼 generate
 * @param page 다음 페이지
 * */
function loadBtn(category, page) {
    let btn = "<button id='loadBtn'>More Data</button>";

    //버튼 유무 확인
    if($('#loadBtn').length === 0){
        //버튼이 없을 시 컨텐츠 테이블 아래에 추가
        $('#contentTable').after(btn);
    }

    //기존에 이벤트 해제
    $('#loadBtn').unbind("click");
    // 버튼이 있을 때에는 click 이벤트를 재설정.
    $('#loadBtn').click(function () {
        getVideoContent(category,page);
    });
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
