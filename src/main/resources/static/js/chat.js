var client = null;
var username = null;

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

function connect(username) {
    this.username = username
    var user = document.getElementById('user').innerText;
    client = Stomp.client('ws://localhost:8080/chat');
    // client.connect({}, function (frame) {
    //     client.subscribe("/topic/messages", function(message){
    //         showMessage(JSON.parse(message.body).id, JSON.parse(message.body).senderId)
    //     });
    // })
    client.connect({}, function (frame) {
        client.subscribe("/user/" + user + "/queue/messages", function(message){
            showMessage(JSON.parse(message.body).content, JSON.parse(message.body).senderId)
        });
    })
}

function sendMessage() {
    var messageToSend = document.getElementById('messageToSend').value;
    var user = document.getElementById('user').innerText;
    const message = {
        conversationId: "",
        senderId: user,
        recipientId: this.username,
        content: messageToSend,
        timestamp: new Date().toISOString(),
    };

    client.send("/app/chat", {}, JSON.stringify(message));
    showMessage(message.content, message.senderId);
    document.getElementById('messageToSend').value = "";
}