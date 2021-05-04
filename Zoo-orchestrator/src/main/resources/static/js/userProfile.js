$(document).ready(function () {
    let userBar = new UsersNavBar({
        block: document.querySelector("#user-bar-block")
    });
    let menu = new MainMenu({
        block: document.querySelector("#menu-block")
    });

    document.getElementById("email").value = getCookie("email");
});

function changePassword() {
    let container = document.getElementById("change-pass");

    if (container.querySelector("#old-password").value === container.querySelector("#new-password").value
        || container.querySelector("#new-password").value !== container.querySelector("#new-password2").value
        || container.querySelector("#old-password").value.length === 0
        || container.querySelector("#new-password").value === 0)
        return;

    let data = {
        'email': getCookie("email"),
        'oldPassword': container.querySelector("#old-password").value,
        'newPassword': container.querySelector("#new-password").value
    };

    $.ajax({
        url: "/changePassword",
        dataType: 'json',
        type: 'POST',
        data: data,
        success: function (response) {
            console.log("password has been changed");
        },
        error: function (error) {
            console.log(error);
        }
    })
}