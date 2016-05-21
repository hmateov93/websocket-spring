var stompClient = null;

function init(){ // We read the user from cookies
	getUser();
	checkLoggedIn();
	disconnect();
	connect();
}

function connect() {
    var socket = new SockJS('/createRoom');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);      
    }); 
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function checkLoggedIn(){
	if(user.type=="")window.location.href = "/index.html";
}

function createUser(){
	var name = document.getElementById('name').value;
	var password = document.getElementById('password').value;
	var type = document.getElementById('type').value;
	var newuser= { 'name': name, 'password': password, 'type': type, 'status': 'OK'};
    stompClient.send("/app/createUser", {}, JSON.stringify(newuser));	
    disconnect();
    window.location.href = "/lobby.html";
}


function goBack(){
	window.location.href = "/lobby.html";
}
