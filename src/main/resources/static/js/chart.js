$(document).ready(function () {
    populateYearOptions();
    getChartData('/admin/dash/chart', handleYearChartData);
    getChartData('/admin/dash/chart/month', handleMonthChartData);
    getChartData('/admin/dash/chart/yoy', handleYoYChartData);
});

function populateYearOptions() {
    const currentYear = new Date().getFullYear();
    const yearSelect = document.getElementById('year-select');
    for (let year = currentYear; year >= 2021; year--) {
        const option = document.createElement('option');
        option.value = year;
        option.text = year;
        yearSelect.add(option);
    }
    yearSelect.value = currentYear;
}

function getChartData(url, callback) {
    $.ajax({
        url: url,
        type: "GET",
        success: function (data) {
            console.log(data);
            callback(data);
        },
        error: function (xhr, status, error) {
            console.error("차트 데이터를 가져오는 중 오류가 발생했습니다: " + error);
        }
    });
}

function handleYearChartData(data) {
    window.chartData = data.chartData;
    filterYearData(); // 기본 선택된 연도로 차트 초기화
}

function filterYearData() {
    const selectedYear = document.getElementById('year-select').value;
    const filteredData = window.chartData.filter(item => item.date.startsWith(selectedYear));
    const amountData = filteredData.map(item => item.amount);
    const categories = filteredData.map(item => item.date);
    createBarChart('#year-chart', '금액', amountData, categories);

}
function handleMonthChartData(data) {

    const amountData = data.chartData.map(item => item.amount);
    const categories = data.chartData.map(item => item.date);
    const monthlyAmount = amountData.reduce((acc, cur) => acc + cur, 0);
    $("#mom-sales").text(monthlyAmount.toLocaleString() + " 원");

    createAreaChart('#current-month-chart', '금액', amountData, categories);
}

function handleYoYChartData(data) {
    const amountData = data.chartData.map(item => item.amount);
    const categories = data.chartData.map(item => item.date);
    const totalSalesThisYear = amountData.reduce((acc, cur) => acc + cur, 0);
    $("#tot-sales").text(totalSalesThisYear.toLocaleString() + " 원");

    createDonutChart('#total-year-chart', amountData, categories);
}

function createBarChart(element, seriesName, data, categories) {
    let options = {
        series: [{ name: seriesName, data: data }],
        chart: {
            type: "bar",
            height: 345,
            offsetX: -15,
            toolbar: { show: true },
            foreColor: "#adb0bb",
            fontFamily: 'inherit',
            sparkline: { enabled: false },
        },
        colors: ["#f5964f"],
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: "35%",
                borderRadius: [6],
                borderRadiusApplication: 'end',
                borderRadiusWhenStacked: 'all'
            },
        },
        markers: { size: 0 },
        dataLabels: { enabled: false },
        legend: { show: false },
        grid: {
            borderColor: "rgba(0,0,0,0.1)",
            strokeDashArray: 3,
            xaxis: { lines: { show: false } },
        },
        xaxis: {
            type: "category",
            categories: categories,
            labels: { style: { cssClass: "grey--text lighten-2--text fill-color" } },
        },
        yaxis: {
            show: true,
            min: 0,
            max: 2000000,
            tickAmount: 5,
            labels: {
                style: { cssClass: "grey--text lighten-2--text fill-color" },
                formatter: function (value) {
                    return (value / 10000).toLocaleString() + " 만원";
                }
            },
        },
        stroke: { show: true, width: 3, lineCap: "butt", colors: ["transparent"] },
        tooltip: {
            theme: "light",
            y: { formatter: function (value) { return value.toLocaleString() + " 원"; } }
        },
        responsive: [
            {
                breakpoint: 600,
                options: {
                    plotOptions: {
                        bar: {
                            borderRadius: 3,
                        }
                    },
                }
            }
        ]
    };

    const chartElement = document.querySelector(element);
    chartElement.innerHTML = ''; // 이전 차트 삭제

    const chartInstance = new ApexCharts(chartElement, options);
    chartInstance.render();
}

function createAreaChart(element, seriesName, data, categories) {
    let options = {
        chart: {
            id: "sparkline3",
            type: "area",
            height: 60,
            sparkline: { enabled: true },
            group: "sparklines",
            fontFamily: "Plus Jakarta Sans', sans-serif",
            foreColor: "#adb0bb",
        },
        series: [{ name: seriesName, color: "#ea6767", data: data }],
        stroke: { curve: "smooth", width: 2 },
        fill: { colors: ["#f3feff"], type: "solid", opacity: 0.05 },
        markers: { size: 0 },
        tooltip: {
            theme: "dark",
            fixed: { enabled: true, position: "right" },
            x: {
                formatter: function(val, opts) {
                    const date = new Date(categories[opts.dataPointIndex]);
                    const month = date.getMonth() + 1;
                    const day = date.getDate();
                    return month + " 월 " + day + " 일";
                }
            },
            y: { formatter: function (value) { return value.toLocaleString() + " 원"; } }
        },
        xaxis: {
            type: "category",
            categories: categories,
            labels: { style: { cssClass: "grey--text lighten-2--text fill-color" } },
        }
    };

    new ApexCharts(document.querySelector(element), options).render();
}

function createDonutChart(element, seriesData, labels) {
    let options = {
        color: "#adb5bd",
        series: seriesData,
        labels: labels,
        chart: {
            width: 180,
            type: "donut",
            fontFamily: "Plus Jakarta Sans', sans-serif",
            foreColor: "#adb0bb",
        },
        plotOptions: {
            pie: {
                startAngle: 0,
                endAngle: 360,
                donut: { size: '75%' },
            },
        },
        stroke: { show: false },
        dataLabels: { enabled: false },
        legend: { show: false },
        colors: ["#ced98c", "#e3e1b4", "#eebf93"],
        responsive: [
            {
                breakpoint: 991,
                options: {
                    chart: { width: 150 },
                },
            },
        ],
        tooltip: {
            theme: "dark",
            fillSeriesColor: false,
            y: { formatter: function (value) { return value.toLocaleString() + " 원"; } },
        },
    };

    new ApexCharts(document.querySelector(element), options).render();
}
