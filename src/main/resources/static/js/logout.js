var message = null;

function init(){
	loadGoodbye();
	setTimeout(goToIndex, 2500);
	
}

function goToIndex(){
	window.location.href = "/index.html"
}

function loadGoodbye(){
	this.message = getQueryVariable("goodbye");
	if(this.message == "standard")document.getElementById('message').innerHTML = "<p>Logging out...</p>";
	else if(this.message == "banned")document.getElementById('message').innerHTML = "Your account has been banned";
	else if(this.message == "no_login")document.getElementById('message').innerHTML = "Log in, please.";
	else document.getElementById('message').innerHTML = "Why would you edit this message?";
}
