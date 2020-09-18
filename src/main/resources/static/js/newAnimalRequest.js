var animalRequest = {};
var steps = ["#animal-choose", "#room-choose", "#personal-data-choose"];
var currentStep = 0;

function chooseAnimalType(animalType) {
    animalRequest.animalType = animalType;

    $("#animal-choose").animate({
        opacity: 0.10
    }, 500, goToNextStep());
}

function chooseRoomType(roomType) {
    animalRequest.roomType = roomType;
    $("#room-choose").animate({
        opacity: 0.10
    }, 500, goToNextStep());
}

function goToNextStep() {
    $(steps[currentStep]).css("display", "none");
    if(currentStep <= steps.length) {
        currentStep++;
        $(steps[currentStep]).css("display", "grid");
    }

}

