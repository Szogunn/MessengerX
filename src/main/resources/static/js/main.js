var client = null;
var selectedUser = null;
var eventSource = null;

function showMessage(value, user) {
    var today    = new Date();
    var date     = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
    var time     = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    var dateTime = date+' '+time;

    var newResponse = document.createElement('p');
    newResponse.appendChild(document.createTextNode("[" + dateTime + "] "));
    newResponse.appendChild(document.createTextNode(user));
    newResponse.appendChild(document.createTextNode(" : "));
    newResponse.appendChild(document.createTextNode(value));
    var respone = document.getElementById('reponse');
    respone.appendChild(newResponse);
}

function connect() {
    if (client){
        console.log("Połączono już z serverem")
        return
    }

    getFriends();

    console.log("wywołano")
    var user = document.getElementById('authenticated-username').innerText;
    var socket = new SockJS('/chat');
    client = Stomp.over(socket);
    client.connect({}, function (frame) {
        client.subscribe("/user/" + user + "/queue/messages", function(message){
            var parsedMessage = JSON.parse(message.body);
            if (selectedUser && parsedMessage.senderId === selectedUser){
                showMessage(parsedMessage.content, parsedMessage.senderId)
            } else {
                showNotification(parsedMessage.senderId);
            }
        });
    })
}

function sendMessage() {
    var messageToSend = document.getElementById('messageToSend').value;
    var user = document.getElementById('authenticated-username').innerText;
    const message = {
        conversationId: "",
        senderId: user,
        recipientId: selectedUser,
        content: messageToSend,
        timestamp: new Date().toISOString(),
    };

    client.send("/app/chat", {}, JSON.stringify(message));
    showMessage(message.content, message.senderId);
    document.getElementById('messageToSend').value = "";
}

function showNotification(senderId) {
    alert("Nowa wiadomość od użytkownika: " + senderId);
}

function handleClick(element) {
    selectedUser = element.innerText;
    console.log('Username:', selectedUser);
}

function getFriends() {
    const username = document.getElementById('authenticated-username').innerText;
    console.log(eventSource)
    if (eventSource){
        console.log("zasubskrybowano już kanał")
        return
    }

    eventSource = new EventSource('/notification/subscribe');
    eventSource.addEventListener(username, function(event) {
        const eventData = JSON.parse(event.data);
        if (eventData.type === 'USER_STATUS') {
            updateUserStatus(eventData.body.username, eventData.body.online);
        }
    });

    eventSource.addEventListener('error', function(event) {
        if (event.readyState === EventSource.CLOSED) {
            console.log('connection is closed');
        } else {
            console.log("Error occured", event);
        }
        event.target.close();
    });
}

function updateUserStatus(user, online) {
    const statusSpan = document.getElementById('status-' + user);
    if (statusSpan) {
        statusSpan.classList.remove('bg-success', 'bg-secondary');
        if (online) {
            statusSpan.classList.add('bg-success');
            statusSpan.textContent = 'Online';
        } else {
            statusSpan.classList.add('bg-secondary');
            statusSpan.textContent = 'Offline';
        }
    }
}


