
//$(function() {
//    $("#resizable").resizable();
//
//    $("#resizable").resizable({
//        resize: function(event, ui) {
//            $("#myfr").css({ "height": ui.size.height,"width":ui.size.width});
//        }
//    });
//});

function logout(){
    $.post('/login', function(data) {
        window.location.href = "/";
    });
}