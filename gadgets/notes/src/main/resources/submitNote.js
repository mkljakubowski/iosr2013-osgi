function submitNote()
{
    var note = document.getElementById('noteText').value;

    if(note == "")
    {
        alert("Cannot add blank note.");
        return false;
    }

    var div = document.getElementById('noteDiv');
    var span = document.createElement('span');
    var button = document.createElement('input');

    button.type = "button";
    button.className = "cross";
    button.onclick = function() {
        return removeNote(this.parentNode);
    }

    span.innerHTML = note;
    span.appendChild(button);
    span.appendChild(document.createElement('br'));

    div.appendChild(span);

    document.getElementById('noteText').value="";

    return false;
}