var animalRequest = {};

function chooseAnimalType(animalType) {
    animalRequest.animalType = animalType;

    $("#animal-choose").animate({
        opacity: 0.10
    }, 500, function () {
        $("#animal-choose").css("display", "none");
        $("#room-choose").css("display", "grid");
    });
}

function chooseRoomType(roomType) {
    animalRequest.roomType = roomType;
    $("#room-choose").animate({
        opacity: 0.10
    }, 500, function () {
        $("#room-choose").css("display", "none");
        // $("#room-choose").css("display", "grid");
        //показать следующее меню
    });
}

