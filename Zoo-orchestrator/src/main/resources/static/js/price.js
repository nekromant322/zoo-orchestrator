$(document).ready(function () {
    getPrice();
});


function getPrice() {
    $.ajax({
        url: '/api/pricePage/actual',
        dataType: 'json',
        type: 'GET',
        contentType: 'application/json',
        success: function (price) {
            $("#commonRoomPrice").val(price.commonRoomPrice)
            $("#largeRoomPrice").val(price.largeRoomPrice)
            $("#vipRoomPrice").val(price.vipRoomPrice)
            $("#dogPrice").val(price.dogPrice)
            $("#catPrice").val(price.catPrice)
            $("#reptilePrice").val(price.reptilePrice)
            $("#ratPrice").val(price.ratPrice)
            $("#birdPrice").val(price.birdPrice)
            $("#otherPrice").val(price.otherPrice)
            $("#discountAdvanced").val(price.discountAdvanced)
            $("#discountVip").val(price.discountVip)
            $("#videoPrice").val(price.videoPrice)
        }
    })
}

function sendPrice() {
    let price = {
        commonRoomPrice: $("#commonRoomPrice").val(),
        largeRoomPrice: $("#largeRoomPrice").val(),
        vipRoomPrice: $("#vipRoomPrice").val(),
        dogPrice: $("#dogPrice").val(),
        catPrice: $("#catPrice").val(),
        reptilePrice: $("#reptilePrice").val(),
        ratPrice: $("#ratPrice").val(),
        birdPrice: $("#birdPrice").val(),
        otherPrice: $("#otherPrice").val(),
        discountAdvanced: $("#discountAdvanced").val(),
        discountVip: $("#discountVip").val(),
        videoPrice: $("#videoPrice").val()
    };
    $.ajax({
        url: '/api/pricePage/new',
        dataType: 'json',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(price),
        complete: function (){
            getPrice()
        }
    })
}