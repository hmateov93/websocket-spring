function logIn(){ // LOGIN - We perform the initial user login here
	user.name = document.getElementById('name').value;
	user.password = document.getElementById('password').value;
	setUser(user);
	window.location.href = "lobby.html";
}