

function logout() {
    $.post('/login', function (data) {
        window.location.href = "/";
    });
}

function setData(name,version,height,width,xposition,yposition){
    console.log(name+" "+version+" : "+width+" "+height+" "+xposition+" "+yposition)
    //TODO set the position and size of each widget to the databse - main panel is called main
    // send it to the server
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

        collection=$(".resizable")
        for(i=0;i<collection.length;i++){
            var h=$(collection[i]).height()
            var w=$(collection[i]).width()
            var nam=collection[i].getAttribute("name")
            var ver=collection[i].getAttribute("version")
            var p=$(collection[i]).position()
            setData(nam,ver,h,w, p.left, p.top)
        }
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

