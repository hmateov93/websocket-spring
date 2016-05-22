var user = { 'type': 'UNASSIGNED', 'name': 'Guest', 'password' : '1234', 'status': 'OK'}; 
//Mocked user


function checkLoggedIn(){
	if(user.type=="")window.location.href = "/index.html";
	if(user.status=="BANNED")window.location.href = "/index.html";
}


//Method to set/get users easily

function setUser(user){ //Sets the cookies for user
	setCookie("user.type", user.type, 1);
	setCookie("user.name", user.name, 1);
	setCookie("user.password", user.password, 1);
	setCookie("user.status", user.status, 1);
}

function getUser(){ //Gets the user from cookies
	user.type = getCookie("user.type");
	user.name = getCookie("user.name");
	user.password = getCookie("user.password");
	user.status = getCookie("user.status");
}

function updateUser(user){
	if(this.user.name == user.name){
		setCookie("user.type", user.type, 1);
		setCookie("user.status", user.status, 1);
		this.user.type = user.type;
		this.user.status = user.status;		
		
	}
	checkLoggedIn();

}

//Cookie name, value and time expiration in days
function setCookie(cname, cvalue, exdays) { 
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

//Returns a cookie
function getCookie(cname) { 
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

//Method to get variables from URL
function getQueryVariable(variable) { 
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return null;
}