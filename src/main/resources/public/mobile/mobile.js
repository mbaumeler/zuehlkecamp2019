function sendMessage() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': "Matthias"}));
}

function log(str) {
    console.log(str);
}


function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            log("Received content", JSON.parse(greeting.body).content);
        });
    });
}