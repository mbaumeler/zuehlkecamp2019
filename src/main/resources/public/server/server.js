function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {
    };
    stompClient.connect({}, () => {
        let slider = document.getElementById('slider');
        let ball = document.getElementById('ball');
        stompClient.subscribe('/topic/greetings', greeting =>
            slider.style.left = JSON.parse(greeting.body).content + '%');

        stompClient.subscribe('/topic/game', greeting =>
            ball.style.left = JSON.parse(greeting.body).ball.x + '%')
    });
}