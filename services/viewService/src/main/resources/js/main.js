//$(function() {
//    $("#resizable").resizable();
//
//    $("#resizable").resizable({
//        resize: function(event, ui) {
//            $("#myfr").css({ "height": ui.size.height,"width":ui.size.width});
//        }
//    });
//});

function logout() {
    $.post('/login', function (data) {
        window.location.href = "/";
    });
}


var isDisabled = true
var first = true
function changeResize() {

    if (!isDisabled) {                      //make disabled
        $(".resizable").resizable("disable");
        ;
        $(".resizable").draggable("disable");
        $(".edit_button").text("Edit widgets")
        $('div.ui-resizable-handle').hide();
        isDisabled = true
    }
    else {                      //make enabled
        if (first) {
            $(".resizable").resizable();
            $(".resizable").draggable();
            first=false
        }
        else {
            $(".resizable").resizable("enable");
            $(".resizable").draggable("enable");
            $('div.ui-resizable-handle').show()
        }
        $(".edit_button").text("Stop editing")
        isDisabled = false
    }
}