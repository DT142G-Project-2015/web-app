$(document).ready(function(){

   $("#change-item-box").hide();

   $(".add-storage").hide();


    var template = $('#storage_template').html();
    Mustache.parse(template);

    var category_place = $('#category_template').html();
    Mustache.parse(category_place);

    getStorage();


    $('#show-add-storage').click(function() {
       $(".add-storage").show();
       $("#show-add-storage").hide();
    });

    function getStorage(){

        $.ajax({
            url: '../../api/storage/categories',
            type: 'GET',
            dataType: 'json'
        }).done(function(data){
            var cat = {}
            var render_data = Mustache.render(template, {categories: data})


           $('#content .section').empty();
           $('#content .section').append(render_data);


            //Is used to delete an article and fetching the new menu.
            $('.delete-btn').click(function() {
                //Fetches the id from the closest list
                var storage_id = $(this).closest('.inventory-list-item').data('id');

                $.ajax({
                    url: '../../api/storage/' + storage_id,
                    type: 'DELETE',
                    dataType: 'json'
                }).done(function() {
                    alert('DELETED');
                    getStorage(); //Used to fetch the new menu.
                });
            });

            //Is used to update an article and fetching the new menu.
            $('.change-btn').click(function() {
                //Fetches the id from the closest list
                var storage_id = $(this).closest('.inventory-list-item').data('id');
                //Creates an array containing the storage parameters.
                var storage = {}
                // Sets the storage parameters in the storage array from the input
                $('#inventory-list'+storage_id+' form :input').each(function(index, element){
                    storage[element.name] = element.value;//Inserts each elemet at the right position
                });
                //Sets the id.
                storage.article_id = storage_id;
       //         alert(JSON.stringify(storage));
                //Sends data to StorageResource PUT method.
                $.ajax({
                    url: '../../api/storage/'+storage_id,
                    type: 'PUT',
                    dataType: 'text',
                    data: JSON.stringify(storage)
                }).done(function() {
                    alert('UPDATED..');
                    getStorage(); //Used to fetch the new menu.
                });
            });
        });

         /* Is used to add a new item to the storage. */
         $("#add-storage-btn").click(function(){
                var storage = {}
                $('#add-storage form :input').each(function(index, element){
                    storage[element.id] = element.value;
                });

                // Gets the value of the radio button checked from the function getRadioVal.
                var val = getRadioVal( document.getElementById('form'), 'category_id' );

                //Sets the category_id to the value of the radio button which is from 1-5.
                storage.category_id = val;
                checkDate(storage.exp_date);
               // alert(JSON.stringify(storage));
                $.ajax({
                    url: '../../api/storage',
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(storage)
                }).done(function(addedStorage){
                        alert('ADDED');
                        $(".add-storage").hide();
                        $("#show-add-storage").show();
                        getStorage(); //Used to fetch the new menu.
                });
         });
    }


    /* getRadioVal return the value of the checked radiobutton.*/
    function getRadioVal(form, name) {
        var val;
        // get list of radio buttons with specified name
        var radios = form.elements[name];
        // loop through list of radio buttons
        for (var i=0, len=radios.length; i<len; i++) {
            if ( radios[i].checked ) { // radio checked?
                val = radios[i].value; // if so, hold its value in val
                break; // and break out of for loop
            }
        }
        return val; // return value of checked radio or undefined if none checked
    }

    /* checkDate() notifies the user if the wrong expire date is made. */
    function checkDate(exp_date){
        var todays_date = new Date();
        d = new Date(exp_date);
        if(Date.parse(todays_date) >= Date.parse(d)){
            alert("Expire date is earlier than today's date!");
        }
    }
});