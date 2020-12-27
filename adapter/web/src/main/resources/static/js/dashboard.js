//charts

$(function () {

    var violet = '#DF99CA',
        red    = '#F0404C',
        green  = '#7CF29C';

    // ------------------------------------------------------- //
    // Charts Gradients
    // ------------------------------------------------------ //

    var ctx1 = $("canvas").get(0).getContext("2d");
    var gradient1 = ctx1.createLinearGradient(150, 0, 150, 300);
    gradient1.addColorStop(0, 'rgba(210, 114, 181, 0.91)');
    gradient1.addColorStop(1, 'rgba(177, 62, 162, 0)');

    var gradient2 = ctx1.createLinearGradient(10, 0, 150, 300);
    gradient2.addColorStop(0, 'rgba(252, 117, 176, 0.84)');
    gradient2.addColorStop(1, 'rgba(250, 199, 106, 0.92)');

    // ------------------------------------------------------- //
   // Frontal Begin
   // ------------------------------------------------------ //
    var DOUGHNUTCHARTACTIVITY  = $('#doughnutChartActivity');
    var doughnutChartActivity = new Chart(DOUGHNUTCHARTACTIVITY, {
        type: 'doughnut',
        options: {
            cutoutPercentage: 80,
        },
        data: {
            labels: Object.keys(summaryDataMonth),
            datasets: [
                {
                    data: Object.keys(summaryDataMonth).map(function(key) {return summaryDataMonth[key];}),
                    borderWidth: 0,
                    backgroundColor: [
                        '#df99ca',
                        '#c374ab',
                        "#a44e8a",
                        "#81376a"
                    ],
                    hoverBackgroundColor: [
                        '#df99ca',
                        '#c374ab',
                        "#a44e8a",
                        "#81376a"
                    ]
                }]
            }
    });

    var doughnutChartActivity = {
        responsive: true
    };


    var PIECHARTPROCESS    = $('#pieChartProcess');
    var pieChartProcess = new Chart(PIECHARTPROCESS, {
        type: 'pie',
        data: {
            labels: Object.keys(summaryDataYear),
            datasets: [
                {
                    data: Object.keys(summaryDataYear).map(function(key) {return summaryDataYear[key];}),
                    borderWidth: 0,
                    backgroundColor: [
                        green,
                        "#6adf8a",
                        "#50c670",
                        "#3fac5c",
                        "#2a9346"
                    ],
                    hoverBackgroundColor: [
                        green,
                        "#6adf8a",
                        "#50c670",
                        "#3fac5c",
                        "#2a9346"
                    ]
                }]
            }
    });

    var pieChartProcess = {
        responsive: true
    };
    // ------------------------------------------------------- //
       // Frontal End
       // ------------------------------------------------------ //



    // ------------------------------------------------------- //
   // Sub Processes Start
   // ------------------------------------------------------ //

   // ------------------------------------------------------- //
      // PRODUCT Start
      // ------------------------------------------------------ //
   var PIECHARTPRODUCT1 = $('#pieChartProduct1');
   var myPieChartProduct1 = new Chart(PIECHARTPRODUCT1, {
       type: 'doughnut',
       options: {
           cutoutPercentage: 90,
           legend: {
               display: false
           }
       },
       data: {
           labels: Object.keys(productDataStatus),
           datasets: [
               {
                   data: Object.keys(productDataStatus).map(function(key) {return productDataStatus[key];}),
                   borderWidth: [0, 0],
                   backgroundColor: [
                       green,
                       "#eee",
                   ],
                   hoverBackgroundColor: [
                       green,
                       "#eee",
                   ]
               }]
       }
   });

    var BARCHARTPRODUCT    = $('#barChartProduct');
    var barChartProduct = new Chart(BARCHARTPRODUCT, {
        type: 'bar',
        options: {
            scales: {
                xAxes: [{
                    display: true,
                    gridLines: {
                        color: '#fff'
                    }
                }],
                yAxes: [{
                    display: true,
                    gridLines: {
                        color: '#fff'
                    }
                }]
            },
            legend: false
        },
        data: {
            labels: Object.keys(productDataYear),
            datasets: [
                {
                    label: "Data Set 1",
                    backgroundColor: [
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2
                    ],
                    hoverBackgroundColor: [
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2
                    ],
                    borderColor: [
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2,
                        gradient2
                    ],
                    borderWidth: 1,
                    data: Object.keys(productDataYear).map(function(key) {return productDataYear[key];}),
                }
            ]
        }
    });

// ------------------------------------------------------- //
   // PRODUCT End
   // ------------------------------------------------------ //



   // ------------------------------------------------------- //
     // ORDER Start
     // ------------------------------------------------------ //
      var PIECHARTORDER1 = $('#pieChartOrder1');
      var myPieChartOrder1 = new Chart(PIECHARTORDER1, {
          type: 'doughnut',
          options: {
              cutoutPercentage: 90,
              legend: {
                  display: false
              }
          },
          data: {
              labels: Object.keys(orderDataStatus),
              datasets: [
                  {
                      data: Object.keys(orderDataStatus).map(function(key) {return orderDataStatus[key];}),
                      borderWidth: [0, 0],
                      backgroundColor: [
                          green,
                          "#eee",
                      ],
                      hoverBackgroundColor: [
                          green,
                          "#eee",
                      ]
                  }]
          }
      });


       var BARCHARTORDER    = $('#barChartOrder');
       var barChartOrder = new Chart(BARCHARTORDER, {
           type: 'bar',
           options: {
               scales: {
                   xAxes: [{
                       display: true,
                       gridLines: {
                           color: '#fff'
                       }
                   }],
                   yAxes: [{
                       display: true,
                       gridLines: {
                           color: '#fff'
                       }
                   }]
               },
               legend: false
           },
           data: {
               labels: Object.keys(orderDataYear),
               datasets: [
                   {
                       label: "Data Set 1",
                       backgroundColor: [
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2
                       ],
                       hoverBackgroundColor: [
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2
                       ],
                       borderColor: [
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2,
                           gradient2
                       ],
                       borderWidth: 1,
                       data: Object.keys(orderDataYear).map(function(key) {return orderDataYear[key];}),
                   }
               ]
           }
       });

   // ------------------------------------------------------- //
      // ORDER End
      // ------------------------------------------------------ //

    // ------------------------------------------------------- //
   // Sub Processes End
   // ------------------------------------------------------ //


});
