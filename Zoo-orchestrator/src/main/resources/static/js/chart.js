var allMonths = ['JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE', 'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER', 'DECEMBER'];

$(document).ready(function () {
    let currentYear = new Date().getFullYear();
    let dataByMonths = getRequestNumbersFromAPiForYear(currentYear, "/api/statistics/requestNumber?year=");

    redrawChart(dataByMonths, "myChart", "Кол-во выполненых заказов");

    let dataByMonthsMoney = getRequestNumbersFromAPiForYear(currentYear, "/api/statistics/moneyEarned?year=");

    redrawChart(dataByMonthsMoney, "myChart2", "Выручка с заказов");
});


function getRequestNumbersFromAPiForYear(year, URL) {
    let dataByMonths;
    $.ajax({
        method: 'GET',
        url: URL + year,
        async: false,
        success: function (response) {
            console.log(response)
            dataByMonths = response;
        },
        error: function (error) {
            console.log(error);
        }
    });
    return dataByMonths;
}

function redrawChart(dataByMonths, parentItem, label) {


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

    var ctx = document.getElementById(parentItem).getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: allMonths,
            datasets: [{
                label: label,
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
