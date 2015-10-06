$(document).ready(function(){

    getStaff();

    function getStaff() {

        $.ajax({
            url: '../../api/staff',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){

            $.each(data, function(i, staff){
                alert(data[i].username);
            });

        });
    }
});

