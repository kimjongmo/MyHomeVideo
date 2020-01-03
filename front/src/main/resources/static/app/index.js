var $ = jQuery.noConflict();

var categories = new Vue({
    el: '#category-list',
    data: {
        currentCategory: '',
        categories: ''
    },
    methods: {
        selectCategory: function (category) {
            this.currentCategory = category;
            loadVideoData(category);
        }
    }
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
        move: function (fileName) {
            playVideo(fileName);
        },
    }
});

function loadVideoData(category) {
    $.ajax({
        url: "/contents?category=" + category + "&page=0",
        type: "GET",
        contentType: "application/json",
        success: function (data) {
            contentList.videos = data.data;
            contentList.page = 0;
            contentList.isContentEnd = false;
        }
    });
}

function playVideo(fileName) {
    window.location.href = "/player/" + categories.currentCategory + "/" + fileName;
}

$(document).ready(function () {
    $.ajax({
        url: '/category',
        type: 'GET',
        contentType: 'application/json',
        success: function (data) {
            console.log(data.status);
            categories.categories = data.data;
        }
    });

    $(window).scroll(function () {
        if ($(document).height() - 30 <= $(window).height() + $(window).scrollTop()) {
            contentList.addData();
        }
    });
});

