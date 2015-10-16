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
            events();
        });
    }

    function getOneStaff(id){

        alert("getOneStaff()");

        return $.ajax({
            url: '../../api/staff/'+id,
            type: 'GET',
            dataType: 'json'
        }).done(function(data){
            $("#alter-id").val(data.id);
            $("#alter-username").val(data.username);
            $("#alter-role").val(data.role);
            $("#alter-first_name").val(data.first_name);
            $("#alter-last_name").val(data.last_name);
        });

    }

    function addStaff(){

        var staff = {}
        $('#add-staff form input').each(function(index, element){
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

    function alterStaff(){

        alert("alterStaff()");

        var staff = {}
        $('#alter-staff-popup form input').each(function(index, element){
            staff[element.id.replace("alter-", "")] = element.value;
        });

        //alert(JSON.stringify(staff));

        $.ajax({
            url: '../../api/staff',
            type: 'PUT',
            dataType: 'json',
            data: JSON.stringify(staff)
        }).done(function(altedStaff){
            getStaff();
            alert(JSON.stringify(altedStaff));
        });
    }

    function deleteStaff(id){
        $.ajax({
            url: '../../api/staff/'+id,
            type: 'DELETE'
        }).done(function(deletedId){
            getStaff();
        });
    }


    // Events

    function events(){

        $("span[id^='alter-staff-btn']").off().click(function(){

            id = $(this).attr("id").match(/\d+/);

            $("#overlay").fadeIn(400);
            $("#alter-staff-popup").fadeIn(400);

            getOneStaff(id);

            $("#delete-staff-btn").off().click(function(){
                deleteStaff(id);
                $(".popup").hide();
                $("#overlay").hide();
            });

            $(".apply-btn").off().click(function(){
                $(".popup").hide();
                $("#overlay").hide();

                alterStaff(id);
            });
        });

        $("#add-staff-btn").off().click(function(){
            addStaff();
            $("#add-staff input").val("");
        });

        $("#overlay").hide();
        $(".popup").hide();
        $("#overlay").off().click(function(){
            $(".popup").fadeOut(400);
            $(this).fadeOut(400);
        });
        $(".cancel-btn").off().click(function(){
            $(".popup").fadeOut(400);
            $("#overlay").fadeOut(400);
        });

        $('#css-black').off().click(function (){
           $('link[href="../style-white.css"]').attr('href','../style-test.css');
        });
        $('#css-white').off().click(function (){
           $('link[href="../style-test.css"]').attr('href','../style-white.css');
        });

    }

    getStaff();
    events();

});