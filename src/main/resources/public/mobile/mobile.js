function sendMessage(x, y, side) {
    stompClient.send("/app/move/" + side, {}, JSON.stringify({x, y}));
}

function connect() {
    let side = location.search === '?side=LEFT' ? 'LEFT' : 'RIGHT';
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
        rxjs
            .fromEvent(document, 'touchmove')
            .subscribe((event) =>
                sendMessage(
                    100 / event.target.clientWidth * event.touches[0].clientX,
                    100 / event.target.clientHeight * event.touches[0].clientY,
                    side));
    });
}