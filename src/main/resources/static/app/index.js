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
        videos: ''
    },
    methods: {
        move: function (fileName) {
            playVideo(fileName);
        }
    }
});

function loadVideoData(category) {
    $.ajax({
        url: "/video?category=" + category,
        type: "GET",
        contentType: "application/json",
        success: function (data) {
            console.log(data.status);
            contentList.videos = data.data;
        }
    });
}
function playVideo(fileName){
    window.location.href="/video/"+categories.currentCategory+"/"+fileName;
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
});

