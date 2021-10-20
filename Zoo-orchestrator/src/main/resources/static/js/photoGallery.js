$(document).ready(function () {
    getURLs();
});

function getURLs() {
    $.ajax({
        url: '/photoGallery/photos',
        dataType: 'json',
        type: 'GET',
        contentType: 'application/json',
        success: function (response) {
            console.log(response);
            drawPhotos(response);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function drawPhotos(data) {
    for (let i = 0; i < data.length; i++) {
        addIndicator(i);
        addPhotoItem(i, data[i]);
    }
}

function addIndicator(index) {
    let indBtn = "<button type=\"button\" id='indicator-button' data-bs-target=\"#carouselBasicExample\" data-bs-slide-to=\"" + index + "\"\n" +
        "                    aria-label=\"Slide " + index + "\"></button>";

    document.getElementById("carousel-indicators").insertAdjacentHTML("beforeend", indBtn);

    if (index === 0) {
        document.getElementById("indicator-button").className = "active";
    }
}

function addPhotoItem(index, url) {
    let photoItem = "<div id='photo-item' class=\"carousel-item img-fluid\">\n" +
        "                <img\n" +
        "                        src=\"" + url + "\"\n" +
        "                        class=\"d-block w-100\"\n" +
        "                        style='width: 100%; max-height: 720px; object-fit: contain'" +
        "                        alt=\"...\"\n" +
        "                />\n" +
        "                <div class=\"carousel-caption d-none d-md-block\">\n" +
        "                    <h5>Slide label</h5>\n" +
        "                    <p>\n" +
        "                        Any text\n" +
        "                    </p>\n" +
        "                </div>\n" +
        "            </div>";

    document.getElementById("carousel-inner").insertAdjacentHTML("beforeend", photoItem);

    if (index === 0) {
        document.getElementById("photo-item").className = "carousel-item img-fluid active";
    }
}