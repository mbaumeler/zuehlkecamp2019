function connect() {
    let socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        let slider = document.getElementById('slider');
        stompClient.subscribe('/topic/greetings', function (greeting) {
            slider.style.left = JSON.parse(greeting.body).content + '%';
        });
    });
}