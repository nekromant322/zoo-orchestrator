var globalId;

function show_popap(id_popap,animalRequestId) {
    var id = "#" + id_popap;
    $(id).addClass('active');
    globalId = animalRequestId;
}

$(".close_popap").click( function(){
    $(".overlay").removeClass("active");
});

function decl(){
    var x = new XMLHttpRequest();
    x.open("POST", "/AnimalRequest/onlyNew/decline/" + globalId, false);
    x.send(null);
    window.location.reload();
}

function bl(){
    var x = new XMLHttpRequest();
    x.open("POST", "/api/blackList/" + globalId, false);
    x.send(null);
    window.location.reload();
}