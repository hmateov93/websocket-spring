var stompClient = null;

function init(){ // We read the user from cookies
	getUser();
	checkLoggedIn();
	checkAdmin();
	disconnect();
	connect();
}

function connect() {
    var socket = new SockJS('/editUser');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        if(user.type=="ADMIN")console.log('Connected: ' + frame);      
    }); 
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    if(user.type=="ADMIN")console.log("Disconnected");
}

function checkLoggedIn(){
	if(user.type=="")window.location.href = "/index.html";
}

function editUser(){
	var name = getQueryVariable("user");
	var password;
	var type;
	if(document.getElementById("password").disabled == false){
		password = document.getElementById('password').value;
	}
	else{
		password = null;
	}
	if(document.getElementById("type").disabled == false){
		type = document.getElementById('type').value;
	}
	else{
		type = null;
	}
	var newuser= { 'name': name, 'password': password, 'type': type, 'status': 'UNKNOWN'};
    stompClient.send("/app/editUser", {}, JSON.stringify(newuser));	
    disconnect();
    window.location.href = "/lobby.html";
}

function lockPassword(){
	 if(document.getElementById("password").disabled == true){
		 document.getElementById("password").disabled = false;
	 }
	 else{
		 document.getElementById("password").disabled = true; 
	 }
}

function lockType(){
	 if(document.getElementById("type").disabled == true){
		 document.getElementById("type").disabled = false;
	 }
	 else{
		 document.getElementById("type").disabled = true; 
	 }
}


function goBack(){
	window.location.href = "/lobby.html";
}
