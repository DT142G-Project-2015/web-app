$(document).ready(function(){

    var i = 1;
    setInterval(function(){
        if(i){
            $("#strobe").css({"background": "#000"});
            i--;
        }
        else{
            $("#strobe").css({"background": "#fff"});
            i++;
        }
    }, 50);

    var template = $('#staff-template').html();
    Mustache.parse(template);

    getStaff();

    function getStaff(){

        $.ajax({
            url: '../../api/staff',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){
            $('#staff-list table').html("<tr><td>Id</td><td>Användarnamn</td><td>Namn</td><td>Roll</td><td>Åtgärder</td></tr>");
            var rendered = Mustache.render(template, {articles: data})
            $('#staff-list table').append(rendered);
            initVisuals();
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

    $("#add-staff-btn").click(function(){
        addStaff();
    });

    $("#overlay").hide();
    $("#alter-staff-popup").hide();

    function initVisuals() {

        $("#alter-staff-btn").click(function(){
            $("#overlay").fadeIn(2000);
            $("#alter-staff-popup").fadeIn(200);
        });

    }

});