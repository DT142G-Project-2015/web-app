$(document).ready(function(){

   $("#change-item-box").hide();



    var template = $('#storage_template').html();
    Mustache.parse(template);

    getStorage();


    function getStorage(){

        $.ajax({
            url: '../../api/storage',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){
            var rendered = Mustache.render(template, {articles: data})
           $('#content .section').append(rendered);
        });
    }

    function addStorage(){

        var storage = {}
        $('#add-storage form :input').each(function(index, element){
            storage[element.id] = element.value;
        });

        alert(JSON.stringify(storage));

        $.ajax({
            url: '../../api/storage',
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(storage)
        }).done(function(addedStorage){
        });

    }
/*
    function deleteStorage(){

        var storage = {}
        $('#add-storage form :input').each(function(index, element){
            storage[element.id] = element.value;
        });

        alert(JSON.stringify(storage));
        $('#item_list .delete_btn').click(function() {
                var item_id = $(this).closest('.menu_item').data('id');
                $.ajax({
                    url: '../../api/storage',
                    type: 'DELETE',
                    dataType: 'json',
                    data: JSON.stringify(storage)
                }).done(function(addedStorage){
                    alert('Borttagen..');
                });


    }*/

    $('.inventory-list .delete_btn').click(function() {
       var storage_id = $(this).closest('.inventory-list-item').data('id');

        $.ajax({
            url: '../../api/storage/' + storage_id,
            type: 'DELETE',
            dataType: 'text'
        }).done(function() {
            alert('removed');
        });
    });

    $("#add-storage-btn").click(function(){
        addStorage();
    });


});