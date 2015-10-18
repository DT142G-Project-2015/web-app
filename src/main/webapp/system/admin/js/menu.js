$(document).ready(function() {

    $.ajaxSetup({
        xhrFields: {
            withCredentials: true
        }
    });
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
        }).done(function(menus) {

            menus.forEach(function(m) {
                m.name = m.type == 1 ? 'Middag' : 'Lunch';
            });

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

            $('#add_menu').click(function(){
                openAddMenu();
            });

            $('.add_group').click(function(){
                var menu_id = $(this).closest('.menu').data('menu-id');
                openAddGroup(menu_id);
                console.log("asd")
            });

            $('.delete-menu').click(function(){
                var menu_id = $(this).closest('.menu').data('menu-id');
                openDeleteMenu(menu_id);
            });


            //CHANGE DATE
            menus.forEach(function(m) {
                var menuToSend = {};
                //START
                $( "#start" + m.id ).datepicker({
                   onSelect: function(date) {
                      menuToSend["type"] = m.type;
                      menuToSend["start_date"] = date;
                      menuToSend["stop_date"] = m.stop_date;
                      $.ajax({
                          url: '../../api/menu/' + m.id,
                          type: 'PUT',
                          dataType: 'json',
                          data: JSON.stringify(menuToSend)
                      }).done(function() {
                          refreshMenus();
                      });
                  }
                });
                $( ".start_date" ).datepicker("option", "dateFormat", "yy-mm-dd");
                $( "#start" + m.id ).datepicker("setDate", m.start_date);
                //END

                //STOP
                $( "#stop" + m.id ).datepicker({
                   onSelect: function(date) {
                      menuToSend["type"] = m.type;
                      menuToSend["start_date"] = m.start_date;
                      menuToSend["stop_date"] = date;
                      console.log(menuToSend);
                      $.ajax({
                          url: '../../api/menu/' + m.id,
                          type: 'PUT',
                          dataType: 'json',
                          data: JSON.stringify(menuToSend)
                      }).done(function() {
                          refreshMenus();
                      });
                  }
                });
                $( ".stop_date" ).datepicker("option", "dateFormat", "yy-mm-dd");
                $( "#stop" + m.id ).datepicker("setDate", m.stop_date);
                //END

            });

            //END

        });
    }

    function openAddMenu(){
        $('#add_menu_section').fadeIn(200);
        $( "#start_picker" ).datepicker();
        $( "#stop_picker" ).datepicker();
        $( "#start_picker" ).datepicker("option", "dateFormat", "yy-mm-dd");
        $( "#stop_picker" ).datepicker("option", "dateFormat", "yy-mm-dd");

        var menu = {}
        $('#create_menu').off('click').click(function() {

            menu["type"] = $('input[name="type"]:checked', '#add_menu_section form').val();
            menu["start_date"] = $('input[name="start_date"]', '#add_menu_section form').val();
            menu["stop_date"] = $('input[name="stop_date"]', '#add_menu_section form').val();

            console.log(menu);
            $.ajax({
                url: '../../api/menu',
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(menu)
            }).done(function(addedItem) {
                refreshMenus();
                $('#add_menu_section').fadeOut(200);
            });
        });
    }

    function openDeleteMenu(menu_id){
        $('#delete-menu-section').fadeIn(200);

        $('#no-delete').click(function(){
            $('#delete-menu-section').fadeOut(200);
        });

        $('#yes-delete').click(function(){
            $.ajax({
                url: '../../api/menu/' + menu_id,
                type: 'DELETE',
                dataType: 'text'
            }).done(function() {
                refreshMenus();
                $('#delete-menu-section').fadeOut(200);
            });
        });
    }

    function openAddGroup(menu_id){
        $('#add_group_section').fadeIn(200);
        var group = {};
        console.log(group);
        $('#create_group').off('click').click(function(){
            group["name"] = $('input[name="name"]', '#add_group_section form').val();
            $.ajax({
                url: '../../api/menu/' + menu_id + '/group',
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(group)
            }).done(function(){
                refreshMenus();
                $('#add_group_section').fadeOut(200);
            });
        });
    }

    function openAddItemDialog(menu_id, group_id) {
        $('#add_item_section').fadeIn(200);
        refreshItems(menu_id, group_id);

        $('#create_item').off('click').click(function() {

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
                addGroupItem(menu_id, group_id, addedItem.id);
                $('#add_item_section').fadeOut(200);

            });
        });
    }

    function addGroupItem(menu_id, group_id, item_id) {
        $.ajax({
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

                addGroupItem(menu_id, group_id, item_id);
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


