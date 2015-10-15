$(document).ready(function(){



    // Initializing

    var template = $('#staff-template').html();
    Mustache.parse(template);


    // Functions

    function getStaff(){

        $.ajax({
            url: '../../api/staff',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){
            $('#staff-list table').empty();
            var rendered = Mustache.render(template, {articles: data})
            $('#staff-list table').append(rendered);
            initStaffEvents();
        });
    }

    function addStaff(){

        var staff = {}
        $('#add-staff form :input').each(function(index, element){
            staff[element.id] = element.value;
        });

        $.ajax({
            url: '../../api/staff',
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(staff)
        }).done(function(addedStaff){
            getStaff();
        });
    }

    function initStaffEvents(){

        function deleteStaff(id){
            $.ajax({
                url: '../../api/staff/'+id,
                type: 'DELETE'
            }).done(function(deletedId){
                getStaff();
            });
        }

        $("span[id^='alter-staff-btn']").click(function(){

            id = $(this).attr("id").match(/\d+/);

            $("#overlay").fadeIn(400);
            $("#alter-staff-popup").fadeIn(400);

            $("#delete-staff-btn").click(function(){
                deleteStaff(id);
                $(".popup").hide();
                $("#overlay").hide();
            });

            $(".apply-btn").click(function(){
                $(".popup").hide();
                $("#overlay").hide();
            });
        });
    }



    // Events

    getStaff();

    $("#add-staff-btn").click(function(){
        addStaff();
    });

    $("#overlay").hide();
    $(".popup").hide();
    $("#overlay").click(function(){
        $(".popup").fadeOut(400);
        $(this).fadeOut(400);
    });
    $(".cancel-btn").click(function(){
        $(".popup").fadeOut(400);
        $("#overlay").fadeOut(400);
    });

    $('#css-black').click(function (){
       $('link[href="../style-white.css"]').attr('href','../style-test.css');
    });
    $('#css-white').click(function (){
       $('link[href="../style-test.css"]').attr('href','../style-white.css');
    });

});