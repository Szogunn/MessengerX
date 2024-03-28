var client = null;
var selectedUser = null;
var nickname = null;
var eventSource = null;
var currentPage = 0;
var totalPage = 0;

var chatArea = document.getElementById('chat-area');

function showMessage(value, user, prepend) {
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
    if (prepend) {
        chatArea.prepend(messageContainer);
        chatArea.scrollBy(0, 1);
    } else {
        chatArea.appendChild(messageContainer);
        chatArea.scrollTop = chatArea.scrollHeight;
    }
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
    client.connect({}, function () {
        client.subscribe("/user/" + nickname + "/queue/messages", function(message){
            var parsedMessage = JSON.parse(message.body);
            if (selectedUser && parsedMessage.senderId === selectedUser){
                showMessage(parsedMessage.content, parsedMessage.senderId, false)
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
    showMessage(message.content, message.senderId, false);
    document.getElementById('messageToSend').value = "";
}

function showNotification(senderId) {
    alert("Nowa wiadomość od użytkownika: " + senderId);
}

function handleClick(element) {
    currentPage = 0;
    selectedUser = element.innerText;
    chatArea.innerHTML = '';
    fetchAndDisplayUserChat(20).then();
    console.log('Username:', selectedUser);
}

function getFriends() {

    eventSource = new EventSource('/notification/subscribe');
    eventSource.addEventListener(nickname, function (event) {
        const eventData = JSON.parse(event.data);
        if (eventData.type === 'USER_STATUS') {
            updateUserStatus(eventData.body.username, eventData.body.online);
        }
    });

    eventSource.addEventListener('error', function (event) {
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

async function fetchAndDisplayUserChat(pageSize) {
    const userChatResponse = await fetch(`/messages/${selectedUser}?pageNo=${currentPage}&pageSize=${pageSize}`);
    const userChat = await userChatResponse.json();
    userChat.content.forEach(chat => {
        showMessage(chat.content, chat.senderId, true);
    });

    totalPage = userChat.totalPages;
    if (currentPage <= totalPage){
        currentPage++
        console.log(currentPage)
    }
}

chatArea.addEventListener('scroll', function () {
    if (chatArea.scrollTop === 0) {
        fetchAndDisplayUserChat(10).then()
    }
});


