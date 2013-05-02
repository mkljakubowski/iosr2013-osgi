function GetTime() {
	var dt = new Date(); 
	document.getElementById('clock').innerHTML = IfZero(dt.getHours()) + ":" + IfZero(dt.getMinutes()) + ":" +  IfZero(dt.getSeconds());
	timeoutID = setTimeout("GetTime()", 1000);
}

function IfZero(num) {
	return ((num <= 9) ? ("0" + num) : num);
}
