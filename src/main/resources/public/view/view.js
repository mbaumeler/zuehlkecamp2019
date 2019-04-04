function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    let id = Date.now().toString();
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {
    };
    stompClient.connect({}, () => {
        let sliderTop = document.getElementById('sliderTop');
        let sliderSide = document.getElementById('sliderSide');
        let sliderBottom = document.getElementById('sliderBottom');
        let ballElm = document.getElementById('ball');


        const stompObserver = rxjs.Observable.create(observer => {
            stompClient.subscribe('/topic/game/' + id,
                result => observer.next(result));
            stompClient.send("/app/requestGameState", {}, id);
        });

        stompObserver
            .pipe(rxjs.operators.debounceTime(15))
            .subscribe(result => {
                let {ball, left} = JSON.parse(result.body);
                ballElm.style.left = `${ball.position.x}%`;
                ballElm.style.top = `${ball.position.y}%`;
                sliderTop.style.left = `${left.x}%`;
                sliderBottom.style.left = `${left.x}%`;
                sliderSide.style.top = `${left.y}%`;
                stompClient.send("/app/requestGameState", {}, id);
            });
    });
}

function getAndShowQrCode(side) {
    document.getElementById('qrcode').src = `/qrcode/${side}`;
}

function registerLeft() {
    getAndShowQrCode("LEFT");

}

function registerRight() {
    getAndShowQrCode("RIGHT");
}