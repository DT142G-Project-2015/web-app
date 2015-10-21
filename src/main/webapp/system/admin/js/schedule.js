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

            $('#standard-shift-holder table').html('<tr><td>Från</td><td>Till</td><td>Bokade</td><td>Beskrivning</td><td>Åtgärder</td></tr>');
            $('.day').empty();

            var now = new Date();

            var zIndex = 0;
            $.each(data, function(i, shift){

                var renderedShifts = Mustache.render(shiftTemplate, {shifts: shift});
                $('#standard-shift-holder table').append(renderedShifts);

                var shiftStart = new Date(shift.start);
                var shiftStop = new Date(shift.stop);

                shift.zIndex = zIndex;

                if(shiftStart.format('Y-W') == now.format('Y-W')){
                    shift.start = shiftStart.format('H:i');
                    shift.stop = shiftStop.format('H:i');
                    shift.marginTop = ((parseFloat(shiftStart.format('H')) + parseFloat(shiftStart.format('i'))/60) - 8)*25;
                    if(shiftStop.format('H-i') == '00-00'){
                        shift.height = 400-shift.marginTop;
                    }
                    else{
                        shift.height = (((parseFloat(shiftStop.format('H')) + parseFloat(shiftStop.format('i'))/60)-8)*25)-shift.marginTop;
                    }
                    if(shift.max_staff == shift.count_staff){
                        shift.color = '#ff4d4d';
                        shift.title = 'Fullbokat';
                    }
                    else{
                        shift.color = '#8c8';
                        shift.title = 'Boka passet';
                    }
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

        if(!$('#repeated').is(':checked')){
            shift['repeated'] = 'false';
        }

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
        });
    }

    function deleteStaff(id){

        $("#loader").show();

        $.ajax({
            url: '../../api/shift/'+id,
            type: 'DELETE'
        }).done(function(deleted){
            getShifts();
        }).fail(function(){
          $('#loader').fadeOut(200);
          $('#delete-shift-btn' + id).html('Redan bokat');
      });
    }

    function bookShift(id){

        $("#loader").show();

        $.ajax({
            url: '../../api/shift/'+id+'/schedule',
            type: 'POST'
        }).done(function(data){
            getShifts();
        }).fail(function(){
          $('#loader').fadeOut(200);
      });
    }


    // Events

    function events(){

        $('#add-shift-btn').off().click(function(){
            addShift();
            //$("#add-shift-holder input").val("");
        });

        $("span[id^='delete-shift-btn']").off().click(function(){

            id = $(this).attr("id").match(/\d+/);
            if($(this).html() == "Är du säker?"){
                deleteStaff(id);
            }
            else{
                $(this).html("Är du säker?");
            }
        });

        $('.time-interval').off().click(function(){
            var id = $(this).attr('id');
            bookShift(id);
        });

        $('#loader').fadeOut(200);

    }

    getShifts();
    events();

});