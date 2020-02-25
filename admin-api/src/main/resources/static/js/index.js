function login(){
    let loginData = {
        email: $('#email').val(),
        password: $('#pwd').val()
    };

    alert(JSON.stringify(loginData));

    //ajax 요청
}


$(document).ready(function () {
    $('#loginBtn').click(function () {
        login();
    });
});