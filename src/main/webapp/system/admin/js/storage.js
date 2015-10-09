$(document).ready(function(){

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
            alert("works");
        });

    }

    $("#add-storage-btn").click(function(){
        addStorage();
    });

});