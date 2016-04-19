stompClient = null;
var chatid;

function init(){ // We read the user from cookies
	getUser();
	chatid=getQueryVariable("id");
	disconnect();
	connect();
	
}

function connect() {
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages/'+chatid, function(message){
        	var processedmessage = JSON.parse(message.body);
            showMessage(processedmessage.content);
        });     
        sendMessage(1);
    });
}

function disconnect() {
    if (stompClient != null) {
    	sendMessage(2);
        stompClient.disconnect();
        window.location.href = "/lobby.html";
    }
    console.log("Disconnected");
}

function sendMessage(type) {
    var message = document.getElementById('message').value;
    document.getElementById('message').value="";
    stompClient.send("/app/chat/"+chatid, {}, JSON.stringify({ 'type': type, 'user': user.name, 'content': message  }));	
}

function showMessage(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.appendChild(p);
}