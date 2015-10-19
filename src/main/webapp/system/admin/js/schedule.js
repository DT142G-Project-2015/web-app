$(document).ready(function(){

    // Initializing

    var template = $('#shift-template').html();
    Mustache.parse(template);


    // Functions

    function getShifts(){

        $("#loader").show();

        $.ajax({
            url: '../../api/shifts',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){
            $('#standard-shift-holder table').empty();
            var rendered = Mustache.render(template, {articles: data})
            $('#standard-shift-holder table').append(rendered);
            events();
        });
    }

    function addShift(){

        $("#loader").show();

        var shift = {}
        $('#add-shift-holder form input').each(function(index, element){
            shift[element.id] = element.value;
        });

        alert(JSON.stringify(shift));

        $.ajax({
            url: '../../api/shift',
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(shift)
        }).done(function(addedShift){
            getShifts();
        }).fail(function(){
            $("#add-shift-error").slideDown(200);
            $("#loader").fadeOut(200);
        });
    }


    // Events

    function events(){

        $("#add-shift-btn").click(function(){
            addShift();
            $("#add-shift-holder input").val("");
        });

        $("#loader").fadeOut(200);

    }

    getShifts();
    events();

});