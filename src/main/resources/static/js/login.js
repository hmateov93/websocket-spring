var stompClient = null;
var error = null;

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

//Here we process the answer from the server
function loadUser(message){
	if(message.code === "OK"){
		user=message.user;
		setUser(user);
		window.location.href = "lobby.html";			
	}
	if(message.code === "INVALID_PASSWORD"){
		document.getElementById('error').innerHTML = "";
		document.getElementById('error').appendChild(document.createTextNode("Invalid Password"));
	}
}

function checkUser() {
    stompClient.send("/app/login", {}, JSON.stringify(user));
}

//We perform the initial user login
function logIn(){ 
	user.name = document.getElementById('name').value;
	user.password = document.getElementById('password').value;
	checkUser();
}