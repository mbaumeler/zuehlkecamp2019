function sendMessage(percent) {
    stompClient.send("/app/move", {}, JSON.stringify({'x': percent}));
}

function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
        rxjs
            .fromEvent(document, 'touchmove')
            .subscribe((event) => sendMessage(100 / event.target.clientWidth * event.touches[0].clientX));
    });
}