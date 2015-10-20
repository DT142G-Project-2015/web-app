$(document).ready(function(){


    // Initializing

    var template = $('#booth-template').html();
    Mustache.parse(template);


    // Functions

    function getBooth(){

        $("#loader").show();

        $.ajax({
            url: '../../api/booth',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){
            data.forEach(function(b) {
                b.status = b.status == 0 ? 'Väntar på svar' : (b.status == 1 ? 'Godkänd' : 'Nekad');
            });
            $('#booth-list table').empty();
            var rendered = Mustache.render(template, {articles: data})
            $('#booth-list table').append(rendered);
            events();
        });
    }

    function approveBooth(id){
        $("#loader").show();
        var booth = {}
        booth["status"] = "1";
        console.log(booth);
        $.ajax({
            url: '../../api/booth/'+id,
            type: 'PUT',
            dataType: 'json',
            data: JSON.stringify(booth)
        }).done(function(){
            getBooth();
        });
    }

    function rejectBooth(id){
        $("#loader").show();
        var booth = {}
        booth["status"] = "2";
        console.log(booth);
        $.ajax({
            url: '../../api/booth/'+id,
            type: 'PUT',
            dataType: 'json',
            data: JSON.stringify(booth)
        }).done(function(){
            getBooth();
        });
    }



    // Events

    function events(){

        $("span[id^='approve-booth-btn']").off().click(function(){
            id = $(this).attr("id").match(/\d+/);
            approveBooth(id);
        });

        $("span[id^='reject-booth-btn']").off().click(function(){
            id = $(this).attr("id").match(/\d+/);
            rejectBooth(id);
        });

        $('#css-black').off().click(function (){
           $('link[href="../style-white.css"]').attr('href','../style-test.css');
        });
        $('#css-white').off().click(function (){
           $('link[href="../style-test.css"]').attr('href','../style-white.css');
        });

        $("#loader").delay(200).fadeOut(200);

    }

    getBooth();
    events();

});