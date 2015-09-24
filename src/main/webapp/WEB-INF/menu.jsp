<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>${menu_name}</title>
        <link rel="stylesheet" type="text/css" href="../menu.css">
    </head>

    <!-- store the menu_id as a HTML5 data attribute -->
    <body data-id="${menu_id}">

        <div class="section">

            <h1>Menu: ${menu_name}</h1>

            <div id="menu_item_list"></div>

            <input id="add_menu_item" type="button" value="Add Item"></input>

        </div>

        <div class="section" id="add_item_section">

            <h1>Add item...</h1>

            <p>Available items:
            <div id="item_list"></div>

            <form>
              <label>Name: <input type="text" name="name"></label>
              <label>Desc: <input type="text" name="description"></label>
              <label>Price: <input type="number" name="price"> kr</label>
              <input id="create_item" type="button" value="Create New Item"></input>
            </form>

        </div>


        <!-- mustache.js template used for rendering menu items from JavaScript-->
        <script id="item_template" type="x-tmpl-mustache">
            <div class="menu_item" data-id="{{ id }}">
                <b>{{ name }}</b> <i>{{ description }}</i> {{ price }} kr
                <img src="../delete.svg"></img>
            </div>
        </script>

        <script src="//cdnjs.cloudflare.com/ajax/libs/mustache.js/2.1.3/mustache.min.js"></script>
        <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
        <script src="../menu.js"></script>
    </body>
</html>
