$(document).ready(function() {

    var menu_id = $('body').data('id');

    var template = $('#item_template').html();
    Mustache.parse(template);

    refreshMenuItems();


    // Opens up list of items to choose from when the user clicks the "Add Item" button
    $('#add_menu_item').click(function() {
        $('#add_item_section').fadeIn(200);

        refreshItems();
    });


    // Creates a new item and adds it as a menu item to the menu.
    $('#create_item').click(function() {

        var item = {}
        $('#add_item_section form :input').each(function(index, element) {
            item[element.name] = element.value;
        })

        $.ajax({
            url: '../api/item',
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(item)
        }).done(function(addedItem) {
            addMenuItem(addedItem.id).done(function() {
                $('#add_item_section').fadeOut(200);
                refreshMenuItems();
            });
        });
    });


    function addMenuItem(item_id) {
        return $.ajax({
                url: '../api/menu/' + menu_id + '/item',
                type: 'POST',
                dataType: 'text',
                data: JSON.stringify({item_id: item_id})
        });
    }

    function refreshMenuItems() {

        $.ajax({
            url: '../api/menu/' + menu_id + '/item',
            type: 'GET',
            dataType: 'json'
        }).done(function(data) {

            $('#menu_item_list').empty();

            data.forEach(function(item) {
                var rendered = Mustache.render(template, item);
                $('#menu_item_list').append(rendered);
            });

            $('#menu_item_list .menu_item img').click(function() {
                var item_id = $(this).parent().data('id');

                $.ajax({
                    url: '../api/menu/' + menu_id + '/item/' + item_id,
                    type: 'DELETE',
                    dataType: 'text'
                }).done(function() {
                    refreshMenuItems();
                    refreshItems();
                });
            });
        });
    }

    function refreshItems() {

        $('#item_list').empty();

        var fetchItems = $.ajax({
            url: '../api/item?excludeMenuId=' + menu_id,
            type: 'GET',
            dataType: 'json'
        });

        fetchItems.done(function(data) {

            data.forEach(function(item) {
                var rendered = Mustache.render(template, item);
                $('#item_list').append(rendered);
            });

            $('#item_list .menu_item b').click(function() {
                var item_id = $(this).parent().data('id');

                addMenuItem(item_id).done(function() {
                    $('#add_item_section').fadeOut(200);
                    refreshMenuItems();
                });
            });

            $('#item_list .menu_item img').click(function() {
                var item_id = $(this).parent().data('id');

                $.ajax({
                    url: '../api/item/' + item_id,
                    type: 'DELETE',
                    dataType: 'text'
                }).done(function() {
                    refreshItems();
                });
            });
        });
    }
});


