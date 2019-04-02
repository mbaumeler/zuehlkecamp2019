function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {
    };
    stompClient.connect({}, () => {
        let slider = document.getElementById('slider');
        let ball = document.getElementById('ball');
        stompClient.subscribe('/topic/moves', greeting =>
            slider.style.left = JSON.parse(greeting.body).x + '%');

        stompClient.subscribe('/topic/game', greeting => {
            let data = JSON.parse(greeting.body);
            ball.style.left = data.ball.x + '%';
            ball.style.top = data.ball.y + '%';
        })
    });
}

function getAndShowQrCode(side) {
    fetch('http://localhost:8080/qrcode/' + side)
        .then(result => result.arrayBuffer())
        .then(buffer => btoa(String.fromCharCode(...new Uint8Array(buffer))))
        .then(base64 => document.getElementById('qrcode').src = `data:image/png;base64,${base64}`);
}

function registerLeft() {
    getAndShowQrCode("left");

}

function registerRight() {
    getAndShowQrCode("right");
}