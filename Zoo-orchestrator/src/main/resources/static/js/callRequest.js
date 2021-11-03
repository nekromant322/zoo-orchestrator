function sendCall() {
    if (checkAlert("call-phone", "call-phone-alert"))
        return;
    if (checkAlert("call-name", "call-name-alert"))
        return;

    let data = {};

    data.phone = $("#call-phone").val();
    data.name = $("#call-name").val();

    $.ajax({
        url: '/api/callRequest/create',
        dataType: 'json',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            console.log(response);
        },
        error: function (error) {
            console.log(error);
        }
    })
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
        alert.className = "input_logo__alert";
        alert.role = "alert";
        alert.id = alertId
        item.insertAdjacentElement('beforebegin', alert);
        return true;
    }
    return false;
}



