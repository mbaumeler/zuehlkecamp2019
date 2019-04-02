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
        let ball = document.getElementById('ball');

        stompClient.subscribe('/topic/game/' + id, result => {
            let data = JSON.parse(result.body);
            ball.style.left = data.ball.x + '%';
            ball.style.top = data.ball.y + '%';
            sliderTop.style.left = data.left.x + '%';
            sliderBottom.style.left = data.left.x + '%';
            sliderSide.style.top = data.left.y + '%';
            stompClient.send("/app/requestGameState", {}, id);
        });
        stompClient.send("/app/requestGameState", {}, id);
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