function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    let id = Date.now().toString();
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {
    };
    stompClient.connect({}, () => {
        let slider = document.getElementById('slider');
        let ball = document.getElementById('ball');

        stompClient.subscribe('/topic/game/' + id, result => {
            let data = JSON.parse(result.body);
            ball.style.left = data.ball.x + '%';
            ball.style.top = data.ball.y + '%';
            slider.style.left = data.leftPosition + '%';
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