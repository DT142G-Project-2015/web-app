$(document).ready(function(){

    getStaff();
    alert();
    function getStaff() {

        $.ajax({
            url: '../api/staff',
            type: 'GET',
            dataType: 'json'
        }).done(function(data) {

            alert(data);

        });
    }
});

