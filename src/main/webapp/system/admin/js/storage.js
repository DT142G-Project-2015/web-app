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
            var cat = {}
            var rendered = Mustache.render(template, {articles: data})
           $('#content .section').empty();
           $('#content .section').append(rendered);

            //Is used to delete an article and fetching the new menu.
            $('.delete-btn').click(function() {
                var storage_id = $(this).closest('.inventory-list-item').data('id');
                //alert(JSON.stringify(storage_id));
                $.ajax({
                    url: '../../api/storage/' + storage_id,
                    type: 'DELETE',
                    dataType: 'text'
                }).done(function() {
                    getStorage(); //Used to fetch the new menu.
                });
            });

            //Is used to update an article and fetching the new menu.
            $('.change-btn').click(function() {
                var storage_id = $(this).closest('.inventory-list-item').data('id');
                var exp_date = $(this).closest('.inventory-list-item').data('date');
//                var category = $(this).closest('.inventory-list-item').data('cat');

                //alert(JSON.stringify(storage_id) + JSON.stringify(exp_date));

                var storage = {}
                storage.id = storage_id;
                //storage.category = category;
                $('#inventory-list'+storage.id+' form :input').each(function(index, element){
                    storage[element.name] = element.value;
                });
                storage.exp_date = exp_date;

                alert(JSON.stringify(storage));
                $.ajax({
                    url: '../../api/storage/'+storage_id,
                    type: 'PUT',
                    dataType: 'json',
                    data: JSON.stringify(storage)
                }).done(function() {
                    alert('Uptaded menu');
                    getStorage(); //Used to fetch the new menu.
                });
            });


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
                getStorage(); //Used to fetch the new menu.
        });
    }

    $("#add-storage-btn").click(function(){
        addStorage();
        getStorage();
    });


});