var stompClient = null;
var activeView = "rooms";

function init(){ // We read the user from cookies and load the rooms
	getUser();
	checkLoggedIn();
	disconnect();
	connect();
	if(user.type=="ADMIN" && activeView == "rooms")goToManageRooms();
}

function connect() {
    var socket = new SockJS('/lobby');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/rooms', function(message){
        	var processedmessage = JSON.parse(message.body);
        	if(activeView == "rooms")loadRooms(processedmessage);
        	statusCheck();
        });
        stompClient.subscribe('/topic/status', function(message){
        	var processedmessage = JSON.parse(message.body);
        	updateUser(processedmessage);
        });        
        if(user.type=="ADMIN"){
            stompClient.subscribe('/topic/users', function(message){
            	var processedmessage = JSON.parse(message.body);
            	if(activeView == "users")loadUsers(processedmessage);
            	statusCheck();
            });         	
        }
         
        requestRooms();
        statusCheck();
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

function requestUsers() {
    stompClient.send("/app/users", {}, JSON.stringify(user));	
}

function loadRooms(message){
	var roomscontainer = document.getElementById('rooms');
	roomscontainer.innerHTML = "";
	
	document.getElementById('users').innerHTML = "";
	
	if(user.type!="ADMIN"){
		document.getElementById('usermenu').style.display = "block";
		document.getElementById('menu').style.display = "none";
	}
	else{
		document.getElementById('usermenu').style.display = "none";
		document.getElementById('menu').style.display = "block";		
	}

	for(var i=0;i<message.length;i++){
		room=message[i];
	    var p = document.createElement('p');
	    var joinbutton = document.createElement('button');
	    joinbutton.id = room.id;
	    joinbutton.className = "joinbutton"; 
	    joinbutton.addEventListener("click", joinRoom); 
	    joinbutton.appendChild(document.createTextNode("Join"));
	    
	    p.appendChild(document.createTextNode(room.name));
	    p.appendChild(document.createElement('br'));
	    p.appendChild(joinbutton);

	    if(user.type=="ADMIN"){
	    	var deletebutton = document.createElement('button');
		    deletebutton.id = room.id;
	    	deletebutton.className = "deletebutton";
	    	deletebutton.addEventListener("click", deleteRoom);
	    	deletebutton.appendChild(document.createTextNode("Delete"));
	    	p.appendChild(deletebutton);
	    }

	    roomscontainer.appendChild(p);
	}

}

function loadUsers(message){
	document.getElementById('rooms').innerHTML = "";
	
	var userscontainer= document.getElementById('users');
	userscontainer.innerHTML = "";
	
	for(var i=0;i<message.length;i++){
		localuser=message[i];
	    var p = document.createElement('p');
	    
    	var deletebutton = document.createElement('button');
	    deletebutton.id = localuser.name;
    	deletebutton.className = "userdeletebutton";
    	deletebutton.addEventListener("click", deleteUser);
    	deletebutton.appendChild(document.createTextNode("Delete"));
    	
    	var banbutton = document.createElement('button');
    	banbutton.id = localuser.name;
    	banbutton.className = "banbutton";
    	if(localuser.status == "OK"){
        	banbutton.addEventListener("click", banUser);
        	banbutton.appendChild(document.createTextNode("Ban"));    		
    	}
    	else if(localuser.status == "BANNED"){
        	banbutton.addEventListener("click", unbanUser);
        	banbutton.appendChild(document.createTextNode("Unban"));     		
    	}
    	
	    p.appendChild(document.createTextNode("Name: "+localuser.name));
	    p.appendChild(document.createElement('br'));
	    p.appendChild(document.createTextNode("Password: "+localuser.password));
	    p.appendChild(document.createElement('br'));	    
	    p.appendChild(document.createTextNode("Role: "+localuser.type));
	    p.appendChild(document.createElement('br'));
	    p.appendChild(document.createTextNode("Status: "+localuser.status));
	    p.appendChild(document.createElement('br'));	
	    p.appendChild(banbutton);
	    p.appendChild(deletebutton);



	    userscontainer.appendChild(p);
	}	
}

function goToManageUsers(){
	activeView = "users";
	document.getElementById('rooms').innerHTML = "";
	document.getElementById('usermenu').style.display = "none";
	
	document.getElementById('manage_rooms_button').style.display = 'inline';
	document.getElementById('create_room_button').style.display = 'none';

	document.getElementById('manage_users_button').style.display = 'none';
	document.getElementById('create_user_button').style.display = 'inline';
	
	
	requestUsers();
}

function goToManageRooms(){
	activeView = "rooms";
	document.getElementById('users').innerHTML = "";
	document.getElementById('usermenu').style.display = "none";
	
	document.getElementById('manage_rooms_button').style.display = 'none';
	document.getElementById('create_room_button').style.display = 'inline';
	
	document.getElementById('manage_users_button').style.display = 'inline';
	document.getElementById('create_user_button').style.display = 'none';
	
	requestRooms();
	
}

function joinRoom(){
	window.location.href = "/room.html?id="+this.id;
}

function goToCreateUser(){
	window.location.href = "/create_user.html";
}

function goToCreateRoom(){
	window.location.href = "/create_room.html";
}

function deleteRoom(){
    stompClient.send("/app/deleteRoom", {}, ""+this.id);	
}

function banUser(){
    stompClient.send("/app/banUser", {}, ""+this.id);	
}

function unbanUser(){
    stompClient.send("/app/unbanUser", {}, ""+this.id);	
}

function deleteUser(){
    stompClient.send("/app/deleteUser", {}, ""+this.id);
    if(user.name == ""+this.id)window.location.href = "/index.html";
}

function statusCheck(){
	stompClient.send("/app/status_refresh", {}, user.name);	
}

function goBack(){
	window.location.href = "/index.html";
}