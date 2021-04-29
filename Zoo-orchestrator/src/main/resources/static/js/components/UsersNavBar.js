class UsersNavBar {

    constructor(props) {
        this.block = props.block;
        this.initEvents();
    }

    initEvents() {
        let that = this;
        this.block.innerHTML = "<nav class=\"navbar navbar-dark bg-dark navbar-expand-lg\">\n" +
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
            "                        <li><a class=\"dropdown-item\" href=\"\">Сменить пароль</a></li>\n" +
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
                'username': modalBlock.querySelector("#loginAuth").value,
                'password': modalBlock.querySelector("#passwordAuth").value
            };
            modalBlock.querySelector('#modalSubmitLogin').disabled = true;

            $.ajax({
                url: "/login",
                dataType: 'json',
                type: 'POST',
                contentType: "application/x-www-form-urlencoded",
                data: data,
                success: function (response) {
                    console.log(response);
                    modalBlock.querySelector('#modalSubmitLogin').disabled = false;
                    document.cookie = "token=" + response;
                    document.getElementById("navbarDropdown").innerText = modalBlock.querySelector("#loginReg").value;
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
            modalBlock.querySelector('#modalSubmitReg').disabled = true;

            console.log("Sent data = " + JSON.stringify(data));

            $.ajax({
                url: "/register",
                type: 'POST',
                contentType: "application/x-www-form-urlencoded",
                data: data,
                success: function (response) {
                    console.log(response);
                    modalBlock.querySelector('#modalSubmitReg').disabled = false;
                    document.cookie = "token=" + response;
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