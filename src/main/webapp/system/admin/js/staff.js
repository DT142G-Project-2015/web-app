$(document).ready(function(){

    getStaff();

    function getStaff() {

        $.ajax({
            url: '../../api/staff',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){

            data.forEach(function(staff){
                alert(staff[0]);
            });

        });
    }
});

