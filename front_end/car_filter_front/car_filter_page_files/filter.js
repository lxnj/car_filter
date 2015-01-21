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
    var weekday= ["", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    var actualDate = new Date(); // actual date
    var i;
    for (i = 2; i <= $("#daytabs").find("li").length; ++i) {
      var nth = new Date(actualDate.getFullYear(),
                    actualDate.getMonth(),
                    actualDate.getDate() + i)
              .getDay();
      $($("#daytabs").find("a")[i])
        .text(weekday[nth]);
    }
  })();

  function fetchFormParameters() {
    var list = $('form#formSearch').serialize().split("&");
    var i, pair, serialized = "", parameters = [];
    var defaults = {"make": "Make", "model": "Model", "year": "Year"};
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

  function getDateStr(daysFromToday) {
    daysFromToday = parseInt(daysFromToday);
    if (isNaN(daysFromToday)) {
      daysFromToday = 1;
    }
    var actualDate = new Date();
    var date = new Date(actualDate.getFullYear(),
                        actualDate.getMonth(),
                        actualDate.getDate()+daysFromToday);
    return $.datepicker.formatDate('yy-mm-dd', date); //2015-01-19
  }


  function numberWithCommas(x) {
      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }

  // fetch car list from server and fill in the correct table
  function fetchCars(tab, panel) {
    var daysFromTodayStr = tab.find('a').attr('href').split('-')[1] || '1';
    var formparams = fetchFormParameters();
    var serialized = 'sale_date=' + getDateStr(daysFromTodayStr) + (formparams ? "&" + formparams : "");
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
                .append($("<td></td>").text(item['run']))
                .append($("<td></td>").text(item['make']))
                .append($("<td></td>").text(item['model']))
                .append($("<td></td>").text(item['year']))
                .append($("<td></td>").text(item['engine']))
                .append($("<td></td>").text(item['type']))
                .append($('<td class="odometer"></td>').text(numberWithCommas(item['odometer'])))
                .append($("<td></td>").text(item['condition']))
                .append($("<td></td>").text(item['color']))
                .append($("<td></td>").text('bid'))
                .append($("<td></td>").text(item['vin']))
                .append($('<td class="price"></td>').text(numberWithCommas(item['price']))))
            ;
          });
          panel.find(".car_table_class").trigger("update");
          setTimeout(function () {
            panel.find(".car_table_class")
              .tablesorterPager({
                container: $(".pager", panel),
                positionFixed: false
              });
          }, 0);
        } else {
          panel.find(".car_table_class tbody")
            .append($("<tr></tr>")
              .append($('<td colspan="12"></td>').text("No data!")));
        }
        
      },
      'json' // I expect a JSON response
    );
  }

  $('#u10').click( function() {
    var index = $( "#daytabs" ).tabs('option', 'active');
    var li = $($( "#daytabs" ).find('li')[index]);
    var panel = $($( "#daytabs .tab_panel" )[index]);
    fetchCars(li, panel);
  });

  $( "#daytabs" ).tabs({
    beforeActivate: function( event, ui ) {
      fetchCars(ui.newTab, ui.newPanel);
    }
  });

  $(".car_table_class").tablesorter({widthFixed: true, widgets: ['zebra']});
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

});

