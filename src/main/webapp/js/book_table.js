$(document).ready(function(){

    $.ajaxSetup({
        xhrFields: {
            withCredentials: true
        }
    });



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
            checkDay()
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
        $.ajax({
            url: 'api/booth',
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(book_holder)
        }).done(function() {
            alert("Din bokning behandlas nu av personalen");
        }).fail(function(){
            alert("Det gick inte att lägga bordsbeställning");
        });
    });

    function noSundays(date) {
          return [date.getDay() != 0, ''];
    }

    function checkDay() {
        var date1 = $('#book_date').datepicker('getDate');
        var day = date1.getDay();
        if (day == 6)
            $('.sat_day').slideDown();
        else
            $('.sat_day').slideUp();
    }

});