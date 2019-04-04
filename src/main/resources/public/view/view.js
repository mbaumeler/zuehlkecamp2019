function connect() {
    const socket = new SockJS('/pong-websocket');
    const id = Date.now().toString();
    const isLeftSide = location.search === '?side=LEFT';
    getAndShowQrCode(isLeftSide ? "LEFT" : "RIGHT")
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {
    };
    stompClient.connect({}, () => {
        const sliderTop = document.getElementById('sliderTop');
        const sliderSide = document.getElementById('sliderSide');
        const sliderBottom = document.getElementById('sliderBottom');
        const ballElm = document.getElementById('ball');

        if (!isLeftSide) {
            sliderSide.classList.add('right')
        }

        const stompObserver = rxjs.Observable.create(observer => {
            stompClient.subscribe('/topic/game/' + id, result => observer.next(result));
            stompClient.send("/app/join", {}, id);
        });

        stompObserver
            .pipe(rxjs.operators.first())
            .subscribe(() => {
                document.getElementById('qrcode').classList.add('hide');
                document.getElementById('area').classList.remove('hide');
            });

        stompObserver
            .pipe(rxjs.operators.debounceTime(15))
            .subscribe(result => {
                const {ball, left, right} = JSON.parse(result.body);
                const slider = isLeftSide ? left : right;
                ballElm.style.left = `${isLeftSide ? ball.position.x / 2 : (ball.position.x - 200) / 2}%`;
                ballElm.style.top = `${ball.position.y}%`;
                sliderTop.style.left = `${isLeftSide ? slider.x : slider.x - 200}%`;
                sliderBottom.style.left = `${isLeftSide ? slider.x : slider.x - 200}%`;
                sliderSide.style.top = `${slider.y}%`;
                stompClient.send("/app/requestGameState", {}, id);
            });
    });
}

function getAndShowQrCode(side) {
    document.getElementById('qrcode').src = `/qrcode/${side}`;
}
