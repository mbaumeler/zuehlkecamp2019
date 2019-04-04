function connect() {
    const side = location.search === '?side=LEFT' ? 'LEFT' : 'RIGHT';
    const socket = new SockJS('/pong-websocket');
    const stompClient = Stomp.over(socket);
    stompClient.debug = () => {
    };
    stompClient.connect({}, () => {
        rxjs
            .fromEvent(document, 'touchmove')
            .pipe(rxjs.operators.throttleTime(10))
            .subscribe(({target, touches}) => {
                const x = 100 / target.clientWidth * touches[0].clientX;
                const y = 100 / target.clientHeight * touches[0].clientY;
                const payload = JSON.stringify({x, y});
                return stompClient.send(`/app/move/${side}`, {}, payload);
            });
    });
}