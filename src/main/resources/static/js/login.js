var loggedSuccess = false;
var stompClient = null;

function init(){
	disconnect();
	connect();
}

function connect() {
    var socket = new SockJS('/login');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/login', function(message){
        	var processedmessage = JSON.parse(message.body);
        	loadUser(processedmessage);
        });
    }); 
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function loadUser(message){
	user=message;
	console.log(user);
}

function checkUser() {
    stompClient.send("/app/login", {}, JSON.stringify(user));
}

function logIn(){ // We perform the initial user login here
	user.name = document.getElementById('name').value;
	user.password = document.getElementById('password').value;
	checkUser();
	if(loggedSuccess){
		setUser(user);
		window.location.href = "lobby.html";
	}
}