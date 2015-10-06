$(document).ready(function(){

    getStaff();

    function getStaff() {

        $.ajax({
            url: '../../api/staff',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){

            data.each(function(i){
                alert(data[i].username);
            });

        });
    }
});

