var $ = jQuery.noConflict();

var carousels = new Vue({
    el: '#carousels',
    data: {
        recentVideos: '',
        selected: '',
    },
    methods: {
        select: function (index) {
            carousels.selected = index;
            console.log("selected =>"+carousels.selected);
        },
        isActive: function (index) {
            return carousels.selected === index;
        },
        prev: function () {
            if(carousels.selected == 0)
                carousels.selected = 4;
            else
                carousels.selected = ((carousels.selected-1))
        },
        next: function () {
           carousels.selected = ((carousels.selected+1)%5);
        },
        move: function (id){
            playVideo(id);
        }


    },
    updated: function () {
        carousels.selected = 0;
    }
});

var categories = new Vue({
    el: '#category-list',
    data: {
        currentCategory: '',
        categories: ''
    },
    methods: {
        selectCategory: function (category) {
            location.href = '/' + category;
        },
        loadData: function (category) {
            categories.currentCategory = category;
            loadVideoData(category);
        }
    },
});

var contentList = new Vue({
    el: '#contentList',
    data: {
        page: '',
        videos: '',
        isContentEnd: ''
    },
    methods: {
        addData: function () {
            //더이상 컨텐츠가 없을 때
            if (contentList.isContentEnd)
                return;

            this.page += 1;
            $.ajax({
                url: "/contents?category=" + categories.currentCategory + "&page=" + this.page,
                type: "GET",
                contentType: "application/json",
                success: function (data) {
                    if (data.data.length === 0) {
                        contentList.isContentEnd = true;
                        return;
                    }
                    contentList.videos = contentList.videos.concat(data.data);
                }
            });
        },
        move: function (id) {
            playVideo(id);
        },
    }
});

function loadVideoData(category) {
    $.ajax({
        url: "/contents?category=" + category + "&page=0",
        type: "GET",
        contentType: "application/json",
        success: function (data) {
            if (data.status === 'NOT_FOUND') {
                alert('알수없는 카테고리입니다.');
                return;
            }
            contentList.videos = data.data;
            contentList.page = 0;
            contentList.isContentEnd = false;
        }
    });

    $.ajax({
        url: "/recentVideo?" + category,
        type: "GET",
        contentType: "application/json",
        success: function (data) {
            if (data.status === 'ERROR') {
                alert('최근 등록된 영상을 가져오지 못하였습니다.');
                return;
            }
            carousels.recentVideos = data.data;
            carousels.selected = 0;
        }
    });

}

function playVideo(id) {
    window.location.href = "/player/" + id;
}

//TODO 화면이 너무 크다보면 스크롤이 안생기는 경우 생김
function infiniteScroll() {
    if ($(document).height() - 30 <= $(window).height() + $(window).scrollTop()) {
        $(window).off('scroll');
        contentList.addData();
        setTimeout(function () {
            $(window).scroll(infiniteScroll);
        }, 500);
    }
}

$(document).ready(function () {
    $.ajax({
        url: '/category',
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            if (data.status === 'ERROR') {
                alert(data.description);
                return;
            }
            categories.categories = data.data;
            var selected = decodeURI(location.pathname.split('/')[1]);
            if (selected !== '')
                categories.loadData(selected);
        }
    });

    $.ajax({
        url: "/recentVideo",
        type: "GET",
        contentType: "application/json",
        success: function (data) {
            if (data.status === 'ERROR') {
                alert('최근 등록된 영상을 가져오지 못하였습니다.');
                return;
            }
            carousels.recentVideos = data.data;
            carousels.selected = 0;
        }
    });
    $(window).scroll(infiniteScroll);
});

