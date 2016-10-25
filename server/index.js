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
            return;
        }

        res.send(JSON.stringify(rows));
    });
});

app.listen(PORT, function() {
    console.log('Listening on port', PORT);
});
