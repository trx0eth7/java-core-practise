### start server
 Just run Main

### send message
You can use netcat

```
nc -v localhost 8083
```

### example commands

```
{"type":"SEND_URL","args":"https://jsonplaceholder.typicode.com/comments"}
{"type":"RECEIVE_URL","args":"0"}
```