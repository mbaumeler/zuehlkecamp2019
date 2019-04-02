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