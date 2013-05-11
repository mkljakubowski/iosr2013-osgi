function removeNote(span)
{
    var notes = document.getElementById('noteDiv');
    notes.removeChild(span);

    return false;
}