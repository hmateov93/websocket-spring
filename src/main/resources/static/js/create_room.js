stompClient = null;

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

function createRoom(){
	var name = document.getElementById('name');
	var newroom= { 'id': 500, 'name': name};
    stompClient.send("/app/createRoom", {}, JSON.stringify(newroom));	
    disconnect();
    window.location.href = "/lobby.html";
}
