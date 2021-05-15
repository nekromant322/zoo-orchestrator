let urlParams;

$(document).ready(function () {
    let queryString = window.location.search;
    urlParams = new URLSearchParams(queryString);

    // let userBar = new UsersNavBar({
    //     block: document.querySelector("#user-bar-block")
    // });
    new MainMenu({
        block: document.querySelector("#menu-block")
    });
});

function savePassword() {

    if (document.querySelector("#new-password").value !== document.querySelector("#new-password2").value
        || document.querySelector("#new-password").value === 0)
        return;

    let data = {
        token: urlParams.get("key"),
        password: document.querySelector("#new-password").value
    };

    $.ajax({
        url: "/confirmReg",
        dataType: 'json',
        type: 'POST',
        data: data,
        success: function (response) {
            console.log("password has been changed");
            setCookie("token", response);
            window.location = "/mainPage";
        },
        error: function (error) {
            console.log(error);
        }
    })
}