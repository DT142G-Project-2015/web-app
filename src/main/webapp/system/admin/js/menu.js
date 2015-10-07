$(document).ready(function() {

    var menu_id = $('body').data('id');

    var template = $('#item_template').html();

    var menusTemplate = $('#menus_template').html();

    Mustache.parse(template);
    Mustache.parse(menusTemplate);

    refreshMenus();

    function refreshMenus() {
        $.ajax({
            url: '../../api/menu',
            type: 'GET',
            dataType: 'json'
        }).done(function(data) {

            menuFetches = data.map(function(menu) {
                return $.ajax({
                    url: '../../api/menu/' + menu.id + '?expand=true',
                    type: 'GET',
                    dataType: 'json'
                });
            });

            $.when.apply($, menuFetches).done(function() {
                var menus = Array.prototype.slice.call(arguments).slice(0, -2);

                var rendered = Mustache.render(menusTemplate, {menus: menus});
                $('#menus').empty();
                $('#menus').append(rendered);

                $('#menus .menu-row .button').click(function() {
                    var menu_id = $(this).closest('.menu').data('menu-id');
                    var group_id = $(this).closest('.menu-group').data('group-id');
                    var item_id = $(this).closest('.menu-row').data('item-id');

                    $.ajax({
                        url: '../../api/menu/' + menu_id + '/group/' + group_id + '/item/' + item_id,
                        type: 'DELETE',
                        dataType: 'text'
                    }).done(function() {
                        refreshMenus();
                    });
                });

                $('#menus .add_item').click(function() {
                    var menu_id = $(this).closest('.menu').data('menu-id');
                    var group_id = $(this).closest('.menu-group').data('group-id');
                    openAddItemDialog(menu_id, group_id);
                });
            });
        });
    }

    function openAddItemDialog(menu_id, group_id) {
        $('#add_item_section').fadeIn(200);
        refreshItems(menu_id, group_id);

        $('#create_item').click(function() {

            var item = {}
            $('#add_item_section form :input').each(function(index, element) {
                item[element.name] = element.value;
            })

            item.type = 0;

            $.ajax({
                url: '../../api/item',
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(item)
            }).done(function(addedItem) {
                addGroupItem(menu_id, group_id, addedItem.id).done(function() {
                    $('#add_item_section').fadeOut(200);
                    refreshMenus();
                });
            });
        });
    }

    function addGroupItem(menu_id, group_id, item_id) {
        return $.ajax({
            url: '../../api/menu/' + menu_id + '/group/' + group_id + '/item',
            type: 'POST',
            dataType: 'text',
            data: JSON.stringify({id: item_id})
        }).done(function() {
            $('#add_item_section').fadeOut(200);
            refreshMenus();
        });
    }

    function refreshItems(menu_id, group_id) {

        $('#item_list').empty();

        var fetchItems = $.ajax({
            url: '../../api/item?excludeGroupId=' + group_id,
            type: 'GET',
            dataType: 'json'
        });

        fetchItems.done(function(data) {

            data.forEach(function(item) {
                var rendered = Mustache.render(template, item);
                $('#item_list').append(rendered);
            });

            $('#item_list .add_btn').click(function() {
                var item_id = $(this).closest('.menu_item').data('id');

                addGroupItem(menu_id, group_id, item_id).done(function() {
                    $('#add_item_section').fadeOut(200);
                    refreshMenus();
                });
            });

            $('#item_list .delete_btn').click(function() {
                var item_id = $(this).closest('.menu_item').data('id');

                $.ajax({
                    url: '../../api/item/' + item_id,
                    type: 'DELETE',
                    dataType: 'text'
                }).done(function() {
                    refreshItems(menu_id, group_id);
                });
            });
        });
    }
});


