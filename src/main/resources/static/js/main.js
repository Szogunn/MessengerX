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

    subscribeToNotifications();

    var socket = new SockJS('/chat');
    client = Stomp.over(socket);
    client.connect({}, function () {
        client.subscribe("/user/" + nickname + "/queue/messages", function(message){
            var parsedMessage = JSON.parse(message.body);
            if (selectedUser && parsedMessage.senderId === selectedUser){
                showMessage(parsedMessage.content, parsedMessage.senderId, false)
                markMessagesAsRead(parsedMessage.messageId)
            } else {
                // showNotification(parsedMessage.senderId);
                updateMessageNotificationBadge(+1, parsedMessage.senderId)
            }
        });
    })
}

function markMessagesAsRead(messageIds) {
    client.send("/app/readMessages", {}, messageIds);
}

function sendMessage() {

    var messageToSend = document.getElementById('messageToSend').value;
    const message = {
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

function subscribeToNotifications() {

    eventSource = new EventSource('/notification/subscribe');
    eventSource.addEventListener(nickname, function (event) {
        const eventData = JSON.parse(event.data);
        if (eventData.type === 'USER_STATUS') {
            updateUserStatus(eventData.username, eventData.online);
        }

        if (eventData.type === 'FRIEND_REQUEST') {
            addFriendInvitationNotificationToList(eventData.fromUser);
            updateNotificationBadge(+1);
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

function addFriendInvitationNotificationToList(notification) {
    const newNotification = document.createElement('div');
    newNotification.classList.add('dropdown-item');

    // Dodaj treść powiadomienia
    const statement = document.createElement('span');
    statement.textContent = "Otrzymano zaproszenie do znajomych od " + notification;

    // Przycisk akceptacji
    const acceptButton = document.createElement('button');
    acceptButton.textContent = 'Akceptuj';
    acceptButton.classList.add('btn', 'btn-success', 'mx-2');
    acceptButton.addEventListener('click', function() {
        sendResponseToServer(true, notification); // Wysyłanie akceptacji na serwer
    });

    // Przycisk odrzucenia
    const rejectButton = document.createElement('button');
    rejectButton.textContent = 'Odrzuć';
    rejectButton.classList.add('btn', 'btn-danger');
    rejectButton.addEventListener('click', function() {
        sendResponseToServer(false, notification); // Wysyłanie odrzucenia na serwer
    });

    // Dodaj treść i przyciski do powiadomienia
    newNotification.appendChild(statement);
    newNotification.appendChild(acceptButton);
    newNotification.appendChild(rejectButton);

    const notificationList = document.getElementById('notificationList');
    notificationList.appendChild(newNotification);
}

// Funkcja do wysyłania odpowiedzi na serwer
function sendResponseToServer(isAccepted, username) {
    console.log(isAccepted)
    console.log(username)
    fetch(`/friends/response/${isAccepted}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Obsługa odpowiedzi z serwera
            console.log(data);
        })
        .catch(error => {
            console.error('There was a problem with your fetch operation:', error);
        });
}


function updateNotificationBadge(change) {
    const notificationBadge = document.getElementById('notificationBadge');
    let currentCount = parseInt(notificationBadge.textContent);
    currentCount += change;
    notificationBadge.textContent = currentCount;
}

function updateMessageNotificationBadge(change, user) {
    const notificationBadge = document.getElementById('messageBadge-' + user);
    let currentCount = parseInt(notificationBadge.textContent);
    console.log(notificationBadge.textContent)
    if (isNaN(currentCount)) {
        currentCount = 0; // Przypisanie wartości 0, jeśli currentCount jest NaN
    }
    currentCount += change;
    notificationBadge.textContent = currentCount;
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


