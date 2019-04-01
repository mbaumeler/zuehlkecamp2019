function log(str) {
    document.getElementById("output").value += "\n" + str;
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