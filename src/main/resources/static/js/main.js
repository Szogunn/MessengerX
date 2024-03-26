var client = null;
var selectedUser = null;
var nickname = null;
var eventSource = null;

var chatArea = document.getElementById('chat-area');
function showMessage(value, user) {
    var today    = new Date();
    var date     = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
    var time     = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    var dateTime = date+' '+time;

    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (user === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    var newResponse = document.createElement('p');
    newResponse.textContent = value;
    messageContainer.appendChild(newResponse)
    chatArea.appendChild(messageContainer);
    chatArea.scrollTop = chatArea.scrollHeight;
}

function connect() {
    if (client){
        console.log("Połączono już z serverem")
        return
    }
    nickname = document.getElementById('authenticated-username').innerText;

    getFriends();

    var socket = new SockJS('/chat');
    client = Stomp.over(socket);
    client.connect({}, function (frame) {
        client.subscribe("/user/" + nickname + "/queue/messages", function(message){
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
    const message = {
        conversationId: "",
        senderId: nickname,
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
    fetchAndDisplayUserChat().then();
    console.log('Username:', selectedUser);
}

function getFriends() {

    eventSource = new EventSource('/notification/subscribe');
    eventSource.addEventListener(nickname, function(event) {
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

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${selectedUser}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        showMessage(chat.content, chat.senderId);
    });
}


