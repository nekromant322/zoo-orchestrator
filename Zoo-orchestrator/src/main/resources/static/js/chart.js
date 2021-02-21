var allMonths = ['JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE', 'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER', 'DECEMBER'];

$(document).ready(function () {
    let currentYear = new Date().getFullYear();
    let dataByMonths = getRequestNumbersFromAPiForYear(currentYear);

    redrawChart(dataByMonths);
});


function getRequestNumbersFromAPiForYear(year) {
    let dataByMonths;
    $.ajax({
        method: 'GET',
        url: '/api/chart/requestNumber?year=' + year,
        async: false,
        success: function (response) {
            console.log('data from /api/char/requestNumber received successfully');
            console.log(response)
            dataByMonths = response;
        },
        error: function (error) {
            console.log(error);
        }
    });
    return dataByMonths;
}

function redrawChart(dataByMonths) {


    let intValuesForMonths = [];
    allMonths.forEach(element => intValuesForMonths.push(dataByMonths[element]));
    intValuesForMonths = intValuesForMonths.map(el => el === undefined ? 0.01 : el);

    const usualColor = 'rgba(54, 162, 235, 0.2)';
    const maxColor = 'rgba(75, 192, 192, 0.2)';
    const minColor = 'rgba(255, 99, 132, 0.2)';

    const usualBorderColor = 'rgba(54, 162, 235, 1)';
    const maxBorderColor = 'rgba(75, 192, 192, 1)';
    const minBorderColor = 'rgba(255, 99, 132, 1)';

    let borderColorsArray = Array(12).fill(usualBorderColor);
    let colorsArray = Array(12).fill(usualColor);

    let maxPos = indexOfMax(intValuesForMonths);
    let minPos = indexOfMin(intValuesForMonths);

    colorsArray[maxPos] = maxColor;
    colorsArray[minPos] = minColor;

    borderColorsArray[maxPos] = maxBorderColor;
    borderColorsArray[minPos] = minBorderColor;

    var ctx = document.getElementById('myChart').getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: allMonths,
            datasets: [{
                label: 'Кол-во выполненых заказов',
                data: intValuesForMonths,
                backgroundColor: colorsArray,
                borderColor: borderColorsArray,
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            },
            legend: {
                display: true,
                labels: {
                    fontColor: 'rgb(54, 162, 235)'
                }
            }
        }
    });
}

function indexOfMax(arr) {
    if (arr.length === 0) {
        return -1;
    }

    var max = arr[0];
    var maxIndex = 0;

    for (var i = 1; i < arr.length; i++) {
        if (arr[i] > max) {
            maxIndex = i;
            max = arr[i];
        }
    }

    return maxIndex;
}

function indexOfMin(arr) {
    if (arr.length === 0) {
        return -1;
    }

    var max = arr[0];
    var maxIndex = 0;

    for (var i = 1; i < arr.length; i++) {
        if (arr[i] < max) {
            maxIndex = i;
            max = arr[i];
        }
    }

    return maxIndex;
}
