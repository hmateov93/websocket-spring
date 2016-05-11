stompClient = null;

function init(){ // We read the user from cookies and load the rooms
	getUser();
	checkLoggedIn();
	disconnect();
	connect();
}

function connect() {
    var socket = new SockJS('/lobby');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/rooms', function(message){
        	var processedmessage = JSON.parse(message.body);
        	loadRooms(processedmessage);
        });  
        requestRooms();
    }); 
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function requestRooms() {
	message="Message";
    stompClient.send("/app/lobby", {}, message);	
}

function loadRooms(message){
	var roomscontainer = document.getElementById('rooms');
	roomscontainer.innerHTML = "";
	for(var i=0;i<message.length;i++){
		room=message[i];
	    var p = document.createElement('p');
	    var joinbutton = document.createElement('button');
	    var deletebutton = document.createElement('button');
	    joinbutton.id = room.id;
	    deletebutton.id = room.id;
	    joinbutton.className = "joinbutton";
	    deletebutton.className = "deletebutton";
	    joinbutton.addEventListener("click", joinRoom);
	    deletebutton.addEventListener("click", deleteRoom);
	    joinbutton.appendChild(document.createTextNode("Join"));
	    deletebutton.appendChild(document.createTextNode("Delete"));
	    p.appendChild(document.createTextNode(room.name));
	    p.appendChild(document.createElement('br'));
	    p.appendChild(joinbutton);
	    p.appendChild(deletebutton);
	    roomscontainer.appendChild(p);
	}

}

function joinRoom(){
	window.location.href = "/room.html?id="+this.id;
}

function checkLoggedIn(){
	if(user.type=="")window.location.href = "/index.html";
}

function goToCreateRoom(){
	window.location.href = "/create_room.html";
}

function deleteRoom(){
    stompClient.send("/app/deleteRoom", {}, ""+this.id);	
}