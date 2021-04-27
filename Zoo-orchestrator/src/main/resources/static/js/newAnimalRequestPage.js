let globalId, globType;

$(document).ready(function () {
    console.log("ready");
    getData();
});

function decl() {
    let x = new XMLHttpRequest();
    x.open("POST", "/api/animalRequest/onlyNew/decline/" + globalId, false);
    x.send(null);
    getData();
}

function bl() {
    let x = new XMLHttpRequest();
    x.open("POST", "/api/blackListPage/" + globalId, false);
    x.send(null);
    getData();
}

function getData(item) {
    let data;

    try {
        switch (item.id) {
            case "clear-requests":
                data = {Spam: "false"};
                globType = "CLEAR";
                break;
            case "spam-requests":
                data = {Spam: "true"};
                globType = "SPAM";
                break;
            default:
                data = {Spam: "false"};
                globType = "CLEAR";
                break;
        }
    } catch (e) {
        data = {Spam: "false"};
        globType = "CLEAR";
    }


    $.ajax({
        url: '/api/animalRequest/onlyNew',
        dataType: 'json',
        type: 'GET',
        contentType: 'application/json',
        data: data,
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
    let td;

    insertTd(data.id, tr);
    insertTd(data.animalType, tr);
    insertTd(data.beginDate, tr);
    insertTd(data.endDate, tr);
    insertTd(data.roomType, tr);
    insertTd(data.videoNeeded, tr);
    insertTd(data.phoneNumber, tr);
    insertTd(data.email, tr);
    insertTd(data.name, tr);
    insertTd(data.surname, tr);
    insertTd(data.animalName, tr);
    insertTd(data.location, tr);

    if (globType !== "SPAM") {
        let acceptBtn = document.createElement("button");
        acceptBtn.className = "btn btn-success";
        acceptBtn.innerHTML = "Accept";
        acceptBtn.type = "submit";
        acceptBtn.addEventListener("click", () => {
            acceptRequest(data.id);
        });
        td = tr.insertCell(12);
        td.insertAdjacentElement("beforeend", acceptBtn);

        let declineBtn = document.createElement("button");
        declineBtn.className = "btn btn-danger";
        declineBtn.innerHTML = "Decline";
        declineBtn.setAttribute("href", "#control-modal");
        declineBtn.setAttribute("data-bs-toggle", "modal");
        declineBtn.addEventListener("click", () => {
            globalId = data.id;
        });

        td.insertAdjacentElement("beforeend", declineBtn);
    }
}

function insertTd(value, parent) {
    let element = document.createElement("td");
    element.scope = "row";
    element.innerText = value;
    parent.insertAdjacentElement("beforeend", element)
}

function acceptRequest(value) {
    let data = {id: value};

    $.ajax({
        method: 'POST',
        url: "/api/animalRequest/onlyNew/accept/" + value,
        async: false,
        data: data,
        success: function (response) {
            console.log(response);
            getData();
        },
        error: function (error) {
            console.log(error);
        }
    });
}