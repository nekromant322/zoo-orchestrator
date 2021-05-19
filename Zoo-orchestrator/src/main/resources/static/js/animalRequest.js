var animalRequest = {};
var steps = ["#animal-choose", "#room-choose", "#dates-choose", "#personal-data-choose", "#result-data-choose"];
var currentStep = 0;

$(document).ready(function () {
    createBtnPrevState(document.getElementById("back-choose"));
    $("#prev-state").hide();
});

function chooseAnimalType(animalType) {
    animalRequest.animalType = animalType;
    if (animalRequest.animalType === "CAT" || animalRequest.animalType === "DOG") {
        $("#animal-choose").animate({
            opacity: 0.10
        }, 500, goToNextStep());
    } else {
        $("#animal-choose").animate({
            opacity: 0.10
        }, 500, goToNextStep())
        goToNextStep();
    }
}

function chooseRoomType(roomType) {
    animalRequest.roomType = roomType;
    $("#room-choose").animate({
        opacity: 0.10
    }, 500, goToNextStep());
}

function chooseDates() {
    if (checkAlert("begin-date-input", "begin-date-input-alert"))
        return;
    if (checkAlert("end-date-input", "end-date-input-alert"))
        return;

    animalRequest.beginDate = $("#begin-date-input").val();
    animalRequest.endDate = $("#end-date-input").val();
    getPrice()
    $("#date-choose").animate({
        opacity: 0.10
    }, 500, goToNextStep());
}

function goToNextStep() {
    $(steps[currentStep]).css("display", "none");
    if (currentStep <= steps.length) {
        currentStep++;
        $(steps[currentStep]).css("display", "grid");
    }
    controlPrevBtn();
}

function back() {
    $(steps[currentStep]).css("display", "none");
    currentStep--;
    $(steps[currentStep]).css("display", "grid");
    controlPrevBtn();
}

function controlPrevBtn() {
    if (currentStep === 0 || currentStep === steps.length - 1)
        $("#prev-state").hide();
    else
        $("#prev-state").show();
}

function createBtnPrevState(item) {
    try {
        document.getElementById("prev-state").remove();
    } catch (e) {
    }
    let btn = document.createElement("button");
    btn.innerText = "Назад";
    btn.className = "fadeIn fourth sendRequestBtn";
    btn.id = "prev-state"
    btn.addEventListener("click", back);
    item.insertAdjacentElement('afterend', btn);
}

function checkAlert(itemId, alertId) {
    let item = document.getElementById(itemId);
    try {
        document.getElementById(alertId).remove();
    } catch (e) {
    }
    item.classList.remove('invalid');
    if (item.value === undefined || item.value === "") {
        let alert = document.createElement("p");
        alert.innerText = "Пожалуйста заполните поле!";
        alert.className = "inputDateAlert";
        alert.role = "alert";
        alert.id = alertId
        item.insertAdjacentElement('beforebegin', alert);
        return true;
    }
    return false;
}

function sendRequest() {

    let data = {"phoneNumber": $("#phone-input").val()};

    console.log(data);

    $.ajax({
        url: '/api/animalRequest',
        dataType: 'json',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            $('.timerBlock').show();
            let timerBlock = $('.seconds');
            let num = response; //количество секунд

            let index = num;
            let timerId = setInterval(function () {
                timerBlock.html(--index);
            }, 1000);

            setTimeout(function () {
                clearInterval(timerId);
                $('.confirm-cod').show();
                $('.create-request').show();
                $('.confirm-request').hide();
            }, num * 1000);
        },
    })
}

function createRequest() {

    animalRequest.animalName = $("#animal-name-input").val();
    animalRequest.phoneNumber = $("#phone-input").val();
    animalRequest.email = $("#email-input").val();
    animalRequest.name = $("#name-input").val();
    animalRequest.surname = $("#surname-input").val();
    animalRequest.requestStatus = "NEW";
    animalRequest.location = $("#location-input").val();
    animalRequest.code = $("#confirm-cod").val();

    let timerBlock = $('.seconds');
    let num = 10; //количество секунд

    let index = num;
    let timerId = setInterval(function () {
        timerBlock.html(--index);
    }, 1000);

    setTimeout(function () {
        clearInterval(timerId);
        $('#timerBlock').html('<button>hello!</button>')
    }, num * 1000);

    $.ajax({
        url: '/api/animalRequest',
        dataType: 'json',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(animalRequest),
        success: function (data) {
            let text = "Заявка №" + data + " успешно создана!";
            document.getElementById("result-data-choose").insertAdjacentHTML('beforeend',
                "<p style=\"background-color: #6baf6b; margin-top: 10px; padding: 10px;\"\n" +
                "               id=\"result-message\">" + text + "</p>");
            document.getElementById("result-message")
            $("#personal-data-choose").animate({
                opacity: 0.10
            }, 500, goToNextStep());

        },
    })
}

function minBeginDate() {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();
    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }

    today = yyyy + '-' + mm + '-' + dd;
    $('#begin-date-input').attr('min', today)
}

$(document).ready(function () {
    minBeginDate()
})

function minEndDate() {
    $('#end-date-input').attr('min', $('#begin-date-input').val())
}


function getPrice() {

    if ($('#video-input').is(":checked")) {
        animalRequest.videoNeeded = true
    } else {
        animalRequest.videoNeeded = false
    }

    $.ajax({
        url: '/api/pricePage/calculate',
        dataType: 'json',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(animalRequest),
        success: function (result) {
            $("#calculatedPrice").val(result)
        },
        error: function (result) {
            $("#calculatedPrice").val(result)
        }
    })
}

