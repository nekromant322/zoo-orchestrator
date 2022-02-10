$(document).ready(function () {
    getRequests();
});

function insertTd(value, parent) {
    let element = document.createElement("td");
    element.scope = "row";
    element.innerText = value;
    parent.insertAdjacentElement("beforeend", element)
}

function deleteRequest(id) {

    $.ajax({
        method: 'POST',
        url: "/api/callRequest/delete/" + id,
        success: function (response) {
            console.log(response);
            getRequests();
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function getRequests() {
    $.ajax({
        url: '/api/callRequest/all',
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            console.log(response);
            drawRequests(response);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function drawRequests(data) {
    while (document.getElementById("requests-table").getElementsByTagName("tbody")[0].rows[0])
        document.getElementById("requests-table").getElementsByTagName("tbody")[0].deleteRow(0);
    for (let i = 0; i < data.length; i++) {
        addRequest(data[i]);
    }
}

function addRequest(data) {
    let table = document.getElementById("requests-table").getElementsByTagName("tbody")[0];
    let tr = table.insertRow(table.rows.length);
    let td;

    insertTd(data.id, tr);
    insertTd(data.name, tr);
    insertTd(data.phone, tr);

    let acceptBtn = document.createElement("button");
    acceptBtn.className = "btn btn-success";
    acceptBtn.innerHTML = "Done";
    acceptBtn.type = "submit";
    acceptBtn.addEventListener("click", () => {
        deleteRequest(data.id);
    });
    td = tr.insertCell(3);
    td.insertAdjacentElement("beforeend", acceptBtn);
}