function sendMessage(percent, side) {
    stompClient.send("/app/move", {}, JSON.stringify({'x': percent, side}));
}

function connect() {
    let side = location.search === '?side=LEFT' ? 'LEFT' : 'RIGHT';
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
        rxjs
            .fromEvent(document, 'touchmove')
            .subscribe((event) => sendMessage(100 / event.target.clientWidth * event.touches[0].clientX, side));
    });
}