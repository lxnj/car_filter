$(document).ready(function(){
  $("#next-card").click(function(){
    $("#card-1").hide();
    $("#card-2").show();
  });
  $("#previous-card").click(function(){
    
    $("#card-2").hide();
    $("#card-1").show();
  });
});