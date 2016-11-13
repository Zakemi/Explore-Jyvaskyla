var express = require('express');
var mysql = require('mysql');
var app = express();

var sql = mysql.createPool({
    host: 'localhost',
    user: 'jyvaskyla',
    password: 'jyvaskyla',
    database: 'jyvaskyla'
    // jyvaskyla: 'jyvaskyla'
});

var PORT = 3000;

app.get('/locations', function(req, res) {
    sql.query('SELECT * FROM locations', function(err, rows, fields) {
        if(err) {
            console.log('Locations query fail!', err);
            res.statusCode = 500;
            res.send('Query fail!');
            return;
        }

        res.send(JSON.stringify(rows));
    });
});

app.post('/locations', function(req, res) {
    var body = [];

    req.on('error', function(err) {
        console.log(err);
    }).on('data', function(chunk) {
        body.push(chunk);
    }).on('end', function() {
        body = Buffer.concat(body).toString();

        // Handle request
        json = JSON.parse(body);
        res.send(JSON.stringify(json));
        console.log(json);
    });
});

app.listen(PORT, function() {
    console.log('Listening on port', PORT);
});
