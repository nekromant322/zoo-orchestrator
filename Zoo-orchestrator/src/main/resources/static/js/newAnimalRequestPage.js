var globalId;

$(document).ready(function () {
    console.log("ready");
});

function show_popap(id_popap, animalRequestId) {
    var id = "#" + id_popap;
    $(id).addClass('active');
    globalId = animalRequestId;
}

$(".close_popap").click(function () {
    $(".overlay").removeClass("active");
});

function decl() {
    var x = new XMLHttpRequest();
    x.open("POST", "/AnimalRequestPage/onlyNew/decline/" + globalId, false);
    x.send(null);
    window.location.reload();
}

function bl() {
    var x = new XMLHttpRequest();
    x.open("POST", "/api/blackListPage/" + globalId, false);
    x.send(null);
    window.location.reload();
}

function getData(item) {
    let data = {};

    switch (item.id) {
        case "clear-requests":
            data = {Type:"CLEAR"};
            break;
        case "spam-requests":
            data = {Type:"SPAM"};
            break;
    }

    $.ajax({
        url: '/AnimalRequestPage/onlyNew',
        dataType: 'json',
        type: 'GET',
        contentType: 'application/json',
        data: data
    })
}
