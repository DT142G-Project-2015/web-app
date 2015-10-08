$(document).ready(function(){

    getStaff();

    function getStaff(){

        $.ajax({
            url: '../../api/staff',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){
            var htmlString;
            $.each(data, function(i, staff){
                htmlString = '<tr><td>Id</td><td>';
                htmlString += staff.username;
                htmlString += '</td><td>';
                htmlString += staff.first_name + " " + staff.last_name;
                htmlString += '</td><td>';
                htmlString += staff.role;
                htmlString += '</td><td><span id="delete-staff-btn" class="button">Radera</span><span id="adjust-staff-btn" class="button">Ändra</span></td></tr>';
                $("#staff-list table").append(htmlString);
            });
        });
    }

    function addStaff(){

        var staff = {}
        $('#add-staff form :input').each(function(index, element){
            staff[element.id] = element.value;
        });

        alert(JSON.stringify(staff));

        $.ajax({
            url: '../../api/staff',
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(staff)
        }).done(function(addedStaff){
            alert("works");
        });

    }

    $("#add-staff-btn").click(function(){
        addStaff();
    });

});