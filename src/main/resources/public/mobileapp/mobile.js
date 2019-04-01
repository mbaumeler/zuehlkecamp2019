function send() {
    fetch('http://localhost:8080/hello').then(result => result.text()).then(console.log)
}