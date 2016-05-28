var stompClient = null;
var error = null;

function init(){
	resetCookies();
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
	else if(message.code === "INVALID_PASSWORD"){
		document.getElementById('error').innerHTML = "";
		document.getElementById('error').appendChild(document.createTextNode("Invalid Password"));
	}
	else if(message.code === "BANNED"){
		document.getElementById('error').innerHTML = "";
		document.getElementById('error').appendChild(document.createTextNode("This user has been banned"));		
	}
	else if(message.code === "EMPTY_USERNAME"){
		document.getElementById('error').innerHTML = "";
		document.getElementById('error').appendChild(document.createTextNode("The username can't be empty"));		
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