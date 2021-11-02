$(document).ready(function () {
    getData();
});

function getData() {
    $.ajax({
        url: '/api/booksPage',
        dataType: 'json',
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            console.log(response);
            let tableBody = document.getElementById("request-table").getElementsByTagName("tbody")[0];
            tableBody.remove();
            let tbody = document.createElement('tbody');
            tbody.className = "table-light";
            document.getElementById("request-table").appendChild(tbody);
            drawTable(response);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function drawTable(resp) {
    for (let i = 0; i < resp.length; i++) {
        addRow(resp[i]);
    }
}

function addRow(data) {
    let table = document.getElementById("request-table").getElementsByTagName("tbody")[0];
    let tr = table.insertRow(table.rows.length);

    insertTd(data.id, tr);
    insertTd(data.animalRequestId, tr);
    insertTd(data.beginDate, tr);
    insertTd(data.endDate, tr);
    insertTd(data.roomId, tr);
}

function insertTd(value, parent) {
    let element = document.createElement("td");
    element.scope = "row";
    element.innerText = value;
    parent.insertAdjacentElement("beforeend", element)
}

function getRooms() {
    if (checkAlert("room-animal-type", "room-animal-type-alert"))
        return;
    if (checkAlert("room-size-type", "room-size-type-alert"))
        return;
    if (checkAlert("room-date-from", "room-date-from-alert"))
        return;
    if (checkAlert("room-date-to", "room-date-to-alert"))
        return;
    if (checkAlert("room-location", "room-location-alert"))
        return;

    let data = {};

    data.animalType = $("#room-animal-type").val();
    data.roomType = $("#room-size-type").val();
    data.begin = $("#room-date-from").val();
    data.end = $("#room-date-to").val();
    data.videoSupported = $("#room-video").val() === "on";
    data.location = $("#room-location").val();

    $.ajax({
        url: '/api/roomPage/spareRooms',
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

