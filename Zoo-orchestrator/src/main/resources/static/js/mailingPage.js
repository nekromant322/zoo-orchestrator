function sendMailing() {
    let data = {};
    if (checkAlert("mailing-topic", "mailing-topic-alert"))
        return;
    if (checkAlert("mailing-text", "mailing-text-alert"))
        return;
    if (checkAlert("mailing-date", "mailing-date-alert"))
        return;
    if (checkAlert("mailing-type", "mailing-type-alert"))
        return;

    data.topic = $("#mailing-topic").val();
    data.text = $("#mailing-text").val();
    data.dateFrom = $("#mailing-date").val();
    data.type = $("#mailing-type").val();
    $.ajax({
        url: '/api/mailingPage',
        dataType: 'json',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data)
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

