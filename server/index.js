var express = require('express');
var app = express();

var PORT = 3000;

app.get('/', function(req, res) {
    res.send([
        '<html>',
        '   <head></head>',
        '   <body>Hello world!</body>',
        '</html>'
    ].join('\n'));
});

app.listen(PORT, function() {
    console.log('Listening on port ', PORT);
});
