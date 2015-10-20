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

            $('#standard-shift-holder table').html('<tr><td>Från</td><td>Till</td><td>Bokade</td><td>Beskrivning</td><td>Upprepar</td><td>Åtgärder</td></tr>');
            $('.day').empty();

            var now = new Date();

            var zIndex = 0;
            $.each(data, function(i, shift){

                var renderedShifts = Mustache.render(shiftTemplate, {shifts: shift});
                $('#standard-shift-holder table').append(renderedShifts);

                var shiftStart = new Date(shift.start);
                var shiftStop = new Date(shift.stop);

                shift.zIndex = zIndex;

                if(shift.repeated == true){

                    shift.start = shiftStart.format('H:i');
                    shift.stop = shiftStop.format('H:i');
                    shift.marginTop = ((parseFloat(shiftStart.format('H')) + parseFloat(shiftStart.format('i'))/60) - 8)*25;
                    shift.height = (((parseFloat(shiftStop.format('H')) + parseFloat(shiftStop.format('i'))/60)-8)*25)-shift.marginTop;
                    var renderedShift = Mustache.render(scheduleTemplate, {shift: shift});
                    $('#' + shiftStart.format('D')).append(renderedShift);
                }
                else if(shiftStart.format('Y-W') == now.format('Y-W')){

                    shift.start = shiftStart.format('H:i');
                    shift.stop = shiftStop.format('H:i');
                    shift.marginTop = ((parseFloat(shiftStart.format('H')) + parseFloat(shiftStart.format('i'))/60) - 8)*25;
                    shift.height = (((parseFloat(shiftStop.format('H')) + parseFloat(shiftStop.format('i'))/60)-8)*25)-shift.marginTop;
                    var renderedShift = Mustache.render(scheduleTemplate, {shift: shift});
                    $('#' + shiftStart.format('D')).append(renderedShift);
                }
                zIndex++;
            });

            events();
        });
    }

    function addShift(){

        $('#loader').show();

        var shift = {}

        $('#add-shift-holder form :input').each(function(index, element){
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