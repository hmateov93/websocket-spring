stompClient = null;
var chatid;

function init(){ // We read the user from cookies
	getUser();
	checkLoggedIn();
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
            if(processedmessage.userlist!=null)updateUserList(processedmessage.userlist);
            //if(processedmessage.roomActive==false) disconnect();
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
    var response = document.getElementById('conversationDiv');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.appendChild(p);
}

function updateUserList(message){
	var div = document.getElementById('userListDiv');
	div.innerHTML = "";
	var users = JSON.parse(message);
	for(var i=0;i<users.length;i++){
		var p = document.createElement('p');
		p.appendChild(document.createTextNode(users[i].name));
		div.appendChild(p);
	}
}

function checkLoggedIn(){
	if(user.type=="")window.location.href = "/index.html";
}