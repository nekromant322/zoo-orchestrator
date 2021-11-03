$(document).ready(function () {
    new UsersNavBar({
        block: document.querySelector("#user-bar-block")
    });
    new MainMenu({
        block: document.querySelector("#menu-block")
    });
});

class UsersNavBar {
    constructor(props) {
        this.block = props.block;
        this.initEvents();
    }

    initEvents() {
        let that = this;
        this.block.innerHTML =
            "<nav class=\"navbar navbar-dark bg-dark navbar-expand-lg\">\n" +
            "    <div class=\"container-fluid\">\n" +
            "        <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\n" +
            "            <ul class=\"navbar-nav ms-auto mb-2 mb-lg-0\">\n" +
            "                <li class=\"nav-item dropdown\">\n" +
            "                    <a class=\"nav-link dropdown-toggle\" href=\"\" id=\"navbarDropdown\" role=\"button\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">\n" +
            "                        Добро пожаловать!\n" +
            "                    </a>\n" +
            "                    <ul class=\"dropdown-menu\" aria-labelledby=\"navbarDropdown\">\n" +
            "                        <li><a id='login-href' class=\"dropdown-item\" data-bs-toggle=\"modal\" href=\"#loginModalLogin\">Войти</a></li>\n" +
            "                        <li><a id='register-href' class=\"dropdown-item\" data-bs-toggle=\"modal\" href=\"#loginModalReg\">Регистрация</a></li>\n" +
            "                        <li><a class=\"dropdown-item\" href=\"userProfilePage\">Сменить пароль</a></li>\n" +
            "                        <li><hr class=\"dropdown-divider\"></li>\n" +
            "                        <li><a class=\"dropdown-item\" href=\"\">Выйти</a></li>\n" +
            "                    </ul>\n" +
            "                </li>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</nav>";

        that.renderLoginModalWindow(document.querySelector("#modal-login-block"));
        that.renderRegisterModalWindow(document.querySelector("#modal-register-block"));
    }

    renderLoginModalWindow(modalBlock) {

        let submitListener = function () {
            let data = {
                email: modalBlock.querySelector("#loginAuth").value,
                password: modalBlock.querySelector("#passwordAuth").value
            };

            $.ajax({
                url: "/login",
                type: 'POST',
                contentType: "application/x-www-form-urlencoded",
                data: data,
                success: function (response) {
                    console.log(response);
                    setCookie("email", modalBlock.querySelector("#loginAuth").value);
                    setCookie("token", response);
                    document.getElementById("navbarDropdown").innerText = modalBlock.querySelector("#loginAuth").value;
                },
                error: function (error) {
                    console.log(error);
                }
            })
        };

        modalBlock.innerHTML = "<div class=\"modal fade\" tabindex=\"-1\" id=\"loginModalLogin\" aria-labelledby=\"exampleModalCenterTitle\" style=\"display: none;\"\n" +
            "     aria-hidden=\"false\">\n" +
            "    <div class=\"modal-dialog modal-dialog-centered\">\n" +
            "        <div class=\"modal-content\">\n" +
            "            <div class=\"modal-header\">\n" +
            "                <h5 id='modal-title' class=\"modal-title\"> Вход в аккунт </h5>\n" +
            "                <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
            "            </div>\n" +
            "            <div class=\"modal-body\">\n" +
            "                <form id=\"loginForm\">\n" +
            "                    <div class=\"row\">\n" +
            "                        <label for=\"loginAuth\">Email: </label>\n" +
            "                        <input type=\"text\" name=\"loginAuth\" id=\"loginAuth\"/>\n" +
            "                        <label for=\"passwordAuth\">Пароль: </label>\n" +
            "                        <input type=\"password\" name=\"passwordAuth\" id=\"passwordAuth\"/>\n" +
            "                    </div>\n" +
            "                </form>\n" +
            "            </div>\n" +
            "            <div class=\"modal-footer\">\n" +
            "                <button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\">Закрыть</button>\n" +
            "                <button id=\"modalSubmitLogin\" type=\"button\" class=\"btn btn-primary\" data-bs-dismiss=\"modal\">Войти\n" +
            "                </button>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>";

        modalBlock.querySelector("#modalSubmitLogin").addEventListener('click', submitListener);
    }

    renderRegisterModalWindow(modalBlock) {

        let submitListener = function () {
            let data = {
                email: modalBlock.querySelector("#loginReg").value,
                password: modalBlock.querySelector("#passwordReg").value
            };

            console.log("Sent data = " + JSON.stringify(data));

            $.ajax({
                url: "/register",
                type: 'POST',
                contentType: "application/x-www-form-urlencoded",
                data: data,
                success: function (response) {
                    console.log(response);
                    setCookie("email", modalBlock.querySelector("#loginReg").value);
                    setCookie("token", response);
                    document.getElementById("navbarDropdown").innerText = modalBlock.querySelector("#loginReg").value;
                },
                error: function (error) {
                    console.log(error);
                }
            })
        };

        modalBlock.innerHTML = "<div class=\"modal fade\" tabindex=\"-1\" id=\"loginModalReg\" aria-labelledby=\"exampleModalCenterTitle\" style=\"display: none;\"\n" +
            "     aria-hidden=\"false\">\n" +
            "    <div class=\"modal-dialog modal-dialog-centered\">\n" +
            "        <div class=\"modal-content\">\n" +
            "            <div class=\"modal-header\">\n" +
            "                <h5 id='modal-title' class=\"modal-title\"> Регистрация </h5>\n" +
            "                <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
            "            </div>\n" +
            "            <div class=\"modal-body\">\n" +
            "                <form id=\"regForm\">\n" +
            "                    <div class=\"row\">\n" +
            "                        <label for=\"loginReg\">Email: </label>\n" +
            "                        <input type=\"text\" name=\"loginReg\" id=\"loginReg\"/>\n" +
            "                        <label for=\"passwordReg\">Пароль: </label>\n" +
            "                        <input type=\"password\" name=\"passwordReg\" id=\"passwordReg\"/>\n" +
            "                    </div>\n" +
            "                </form>\n" +
            "            </div>\n" +
            "            <div class=\"modal-footer\">\n" +
            "                <button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\">Закрыть</button>\n" +
            "                <button id=\"modalSubmitReg\" type=\"button\" class=\"btn btn-primary\" data-bs-dismiss=\"modal\">Зарегистрироваться\n" +
            "                </button>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>";

        modalBlock.querySelector("#modalSubmitReg").addEventListener('click', submitListener);
    }
}

class MainMenu {
    constructor(props) {
        this.block = props.block;
        this.initEvents();
    }

    initEvents() {
        let that = this;
        this.block.innerHTML = "<div class=\"container-fluid\" style=\"background: #8d06cc; /* Old browsers */\n" +
            "    background: -moz-linear-gradient(top,  #8d06cc 0%, #220051 87%, #1c0049 100%); /* FF3.6-15 */\n" +
            "    background: -webkit-linear-gradient(top,  #8d06cc 0%,#220051 87%,#1c0049 100%); /* Chrome10-25,Safari5.1-6 */\n" +
            "    background: linear-gradient(to bottom,  #8d06cc 0%,#220051 87%,#1c0049 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */\n" +
            "    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#8d06cc', endColorstr='#1c0049',GradientType=0 );\">\n" +
            "        <div class=\"row justify-content-md-center\">\n" +
            "            <div class=\"col-xs-1 col-sm-1 col-lg-1\">\n" +
            "            </div>\n" +
            "            <div class=\"col-xs-4 col-sm-4 col-lg-2 align-self-center\">\n" +
            "                <div class=\"text-center\">\n" +
            "                    <img class=\"img-fluid\" src=\"/img/animals/unicorn.png\" alt=\"\">\n" +
            "                </div>\n" +
            "                <h2 class=\"text-center text-light\">Happy Unicorn</h2>\n" +
            "                <p class=\"text-center text-light\">Зоогостиница</p>\n" +
            "            </div>\n" +
            "            <div class=\"col-xs-7 col-sm-7 col-lg-9 d-flex align-items-center\">\n" +
            "                <nav>\n" +
            "                    <a href=\"/mainPage\" class=\"btn btn-secondary\" role=\"button\">Главная</a>\n" +
            "                    <a href=\"#\"  class=\"btn btn-secondary\" role=\"button\">Номера</a>\n" +
            "                    <a href=\"#\"  class=\"btn btn-secondary\" role=\"button\">Правила</a>\n" +
            "                    <a href=\"/contactsPage\"  class=\"btn btn-secondary\" role=\"button\">Контакты</a>\n" +
            "                    <a href=\"/photoGalleryPage\"  class=\"btn btn-secondary\" role=\"button\">Фото</a>\n" +
            "                    <a href=\"/videoPage\"  class=\"btn btn-secondary\" role=\"button\">Видео</a>\n" +
            "                    <a href=\"/animalRequestPage\"  class=\"btn btn-secondary\" role=\"button\">Подать заявку</a>\n" +
            "                    <a href=\"/pricePage\"  class=\"btn btn-secondary\" role=\"button\">Изменить цены</a>\n" +
            "                    <a href=\"/AnimalRequestPage/onlyNew\" class=\"btn btn-secondary\" role=\"button\">Новые заявки</a>\n" +
            "                    <a href=\"/controlPage\"  class=\"btn btn-secondary\" role=\"button\">Панелька</a>\n" +
            "                    <a href=\"/mailingPage\"  class=\"btn btn-secondary\" role=\"button\">Рассылка</a>\n" +
            "                    <a href=\"/bookingPage\"  class=\"btn btn-secondary\" role=\"button\">Букинг комнат</a>\n" +
            "\n" +
            "                </nav>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>";
    }
}