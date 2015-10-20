$(document).ready(function(){

    // Initializing

    var shiftTemplate = $('#shift-template').html();
    Mustache.parse(shiftTemplate);

    var scheduleTemplate = $('#schedule-template').html();
    Mustache.parse(scheduleTemplate);


    // Functions

    function getShifts(){

        $('#loader').show();

        $.ajax({
            url: '../../api/shift',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){

            $('#standard-shift-holder table').empty();


            var now = new Date();

            $.each(data, function(i, shift){

                var renderedShifts = Mustache.render(shiftTemplate, {shifts: shift});
                $('#standard-shift-holder table').append(renderedShifts);

                var shiftDate = new Date(shift.start);

                if(shiftDate.format('Y-W') == now.format('Y-W')){
                    alert('shift in current week: ' + now.format('Y-W'));
                }

                var renderedShift = Mustache.render(scheduleTemplate, {shift: data});
                $('#mon').html(renderedShift);

                alert(JSON.stringify(shift));
            });

            events();
        });
    }

    function addShift(){

        $('#loader').show();

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
            alert('success');
        }).fail(function(){
            $('#loader').fadeOut(200);
            alert('fail');
        });
    }


    // Events

    function events(){

        $('#add-shift-btn').off().click(function(){
            addShift();
            //$("#add-shift-holder input").val("");
        });

        $('#loader').fadeOut(200);

    }

    getShifts();
    events();

});