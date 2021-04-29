class MainMenu{
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
            "\n" +
            "                    <a style=\"margin-left: 20px\" href=\"/loginPage\"  class=\"btn btn-secondary\" role=\"button\">Войти</a>\n" +
            "                </nav>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>";
    }
}