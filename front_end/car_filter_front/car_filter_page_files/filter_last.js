$(function() {
  //sync Model with Make
  $("#u0_input").change(function(){
    var make = $("#u0_input").val();
    $.getJSON("getModels?make=" + make, '', function(data, status, xhr){
      $('#u1_input')
        .find('option')
        .remove()
      ;
      $('#u1_input')
        .append($("<option></option>")
          .attr("value","Model")
          .text("Model")
          .attr('selected', true))
      ;
      $.each(data, function(key,value){
        $('#u1_input')
          .append($("<option></option>")
          .attr("value",value)
          .text(value));
      });
    });
  });

  $( "#slider" ).slider({
    value:2,
    min: 0,
    max: 5,
    step: 0.5,
    slide: function( event, ui ) {
      $( "#slider_description span" ).text( "> " + ui.value.toFixed(1) );
      $("#slider input").val(ui.value);
    }
  });

  // set day of week for tabs' names
  (function() {
    var weekday= ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    var actualDate = new Date(); // actual date
    var i, daysFromToday;
    for (i = $("#daytabs").find("li").length - 1, daysFromToday = -1; i >= 0; --daysFromToday) {
      var date = new Date(actualDate.getFullYear(),
                          actualDate.getMonth(),
                          actualDate.getDate() + daysFromToday);
      var nth = date.getDay();
      if (nth == 0 || nth == 1 || nth == 5 || nth == 6) {
        continue;
      }
      $($("#daytabs").find("a")[i])
        .text($.datepicker.formatDate('M d', date))
        .attr('title', weekday[nth]);
      $($("#daytabs .tab_panel")[i])
        .attr("date-data", $.datepicker.formatDate('yy-mm-dd', date)); //2015-01-19
      i -= 1;
    }
  })();

  function fetchFormParameters() {
    var list = $('form#formSearch').serialize().split("&");
    var i, pair, serialized = "", parameters = [];
    var defaults = {"make": "Make", "model": "Model", "year": "Year", "color": "all",
                    "price_max": "INF", "odometer_max": "INF"};
    //remove defaults and empty strings
    for (i = 0; i < list.length; ++i) {
      pair = list[i].split("=");
      if (defaults[pair[0]] == pair[1]) {
        continue;
      }
      if (pair[1]) {
        parameters.push(pair[0] + '=' + pair[1])
      }
    }
    serialized = parameters.join("&");
    return serialized;
  }

  function numberWithCommas(x) {
      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }

  // fetch car list from server and fill in the correct table
  function fetchCars(tab, panel) {
    var formparams = fetchFormParameters();
    var serialized = 'sale_date=' + panel.attr('date-data') + (formparams ? "&" + formparams : "");
    panel.find(".car_table_class tbody").find('tr').remove();
    panel.find(".car_table_class tbody")
      .append($("<tr></tr>")
        .append($('<td colspan="12"></td>').text("Please wait...")));
    $.post('lookup',serialized,
      function(data) {
        panel.find(".car_table_class tbody").find('tr').remove();
        if (data && data.length) {
          $.each(data, function(key,item){
            panel.find(".car_table_class tbody")
              .append($("<tr></tr>")
                .append($("<td></td>").text(item['ln']))
                .append($("<td></td>").text(item['run']))
                .append($("<td></td>").text(item['make']))
                .append($("<td></td>").text(item['model']))
                .append($("<td></td>").text(item['abstractStr'].substr((item['year'] + ' ' + item['make'] + ' ').length)))
                .append($("<td></td>").text(item['year']))
                .append($('<td class="odometer"></td>').text(numberWithCommas(item['odometer'])))
                .append($('<td></td>').text(item['condition']))
                .append($("<td></td>").text(item['color']))
                .append($("<td></td>").text(item['vin']))
                .append($('<td class="price"></td>').text("$" + numberWithCommas(item['price']))))
            ;
          });
        }
        panel.find(".car_table_class").trigger("update");
        setTimeout(function () {
          panel.find(".car_table_class")
            .tablesorterPager({
              size: 50,
              container: $(".pager", panel),
              positionFixed: false
            });
        }, 0);
        if (!(data && data.length)) {
          setTimeout(function () {
            panel.find(".car_table_class tbody")
            .append($("<tr></tr>")
              .append($('<td colspan="12"></td>').text("There is no car on this day satisfied now. Coming soon.")));
          }, 200);
        }
        
      },
      'json' // I expect a JSON response
    );
  }

  
  $( "#daytabs" ).tabs({
    beforeActivate: function( event, ui ) {
      fetchCars(ui.newTab, ui.newPanel);
    }
  });

  $(".car_table_class").tablesorter({
    widgets: ['zebra']
  });
  // $("#cars_table_0").tablesorter({widthFixed: true})
  //   .tablesorterPager({container: $("#pager-0")});
  // $("#cars_table_1").tablesorter({widthFixed: true})
  //   .tablesorterPager({container: $("#pager-1")});
  // $("#cars_table_2").tablesorter({widthFixed: true})
  //   .tablesorterPager({container: $("#pager-2")});
  // $("#cars_table_3").tablesorter({widthFixed: true})
  //   .tablesorterPager({container: $("#pager-3")});
  // $("#cars_table_4").tablesorter({widthFixed: true})
  //   .tablesorterPager({container: $("#pager-4")});

  function searchClicked() {
    var index = $( "#daytabs" ).tabs('option', 'active');
    var li = $($( "#daytabs" ).find('li')[index]);
    var panel = $($( "#daytabs .tab_panel" )[index]);
    fetchCars(li, panel);
  }

  $('#u10').click(searchClicked);
  searchClicked();

  // Calculator Box
  function refreshCalculation() {
    var bidprice = parseFloat($('#calculator_bid').val().replace('$', '').replace(',', ''));
    var X = parseFloat($('#calculator_addition').val().replace('$', '').replace(',', ''));
    if (!isNaN(bidprice)) {
      if (!isNaN(X)) {
        var tuangouFee = 500;
        if (bidprice + X >= 15000) {
          tuangouFee = 500 + parseInt((bidprice + X - 5000) / 10000) * 50;
        }
        $('#calculator_tuangou').val(tuangouFee);
        var minimalFee = (bidprice + X) * 1.09 + tuangouFee + 95;
        $('#calculator_result').val("" + parseInt(minimalFee + 100) + "~" + parseInt(minimalFee + 350));
      } else {
        $('#calculator_result').val("Please input a number in X");
      }
    }
  }

  selectedRows = [];
  
  $("#calculatorBox input").on( "focusout", function() {
    refreshCalculation();
  } );
  $(".car_table_class").on('click', 'tr', function(event) {
    if ($('.table_make', this).text().toLowerCase() != "make") {
      while (selectedRows.length > 0) {
        var tr = selectedRows.pop();
        $(tr).css('outline', '');
      }
      selectedRows.push(this);
      $(this).css('outline', 'thin solid blue');
    }
    var bidprice = parseFloat($('.price', this).text().replace('$', '').replace(',', ''));
    if (!isNaN(bidprice)) {
      $('#calculator_bid').val(bidprice);
    }
    refreshCalculation();
  });
});
