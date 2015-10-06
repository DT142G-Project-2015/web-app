$(document).ready(function(){

    getStaff();

    function getStaff() {

        $.ajax({
            url: '../../api/staff',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){

            data.forEach(function(i, staff){
                alert(staff[i].username);
            });

        });
    }
});

