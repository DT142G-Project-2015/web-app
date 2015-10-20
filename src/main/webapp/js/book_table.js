$(document).ready(function(){
    var book_holder = {};
    $(".button_persons").click(function(){
        book_holder["persons"] = $(this).text();
        console.log(book_holder);
        $(".button_persons").removeClass("button_persons_selected");
        $(this).addClass("button_persons_selected");
        $("#section_2").slideDown(200);$('html, body').animate({
            scrollTop: $("#section_2").offset().top
        }, 2000);
    });

    $("#book_date").datepicker({
        onSelect: function(date) {
            book_holder["book_date"] = $(this).val();
            console.log(book_holder);
            $("#section_3").slideDown(200);
            $('html, body').animate({
                scrollTop: $("#section_3").offset().top
            }, 2000);
        },
        minDate: 0,
        beforeShowDay: noSundays
    });

    $(".button_time").click(function(){
        book_holder["book_time"] = $(this).text();
        console.log(book_holder);
        $(".button_time").removeClass("button_time_selected");
        $(this).addClass("button_time_selected");
        $("#section_4").slideDown(200);
        $('html, body').animate({
            scrollTop: $("#section_4").offset().top
        }, 2000);
    });

    $("#book_done").click(function(){
        book_holder["name"] = $("#name").val();
        book_holder["phone"] = $("#tel").val();
        book_holder["email"] = $('#email').val();
        console.log(book_holder);
    });

    function noSundays(date) {
          return [date.getDay() != 0, ''];
    }

});