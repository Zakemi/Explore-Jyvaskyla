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

// Simple validation mechanism
// data must have all members that scheme has
// embedded objects or arrays are not supported!
function json_validate(scheme, data) {
    for(var x in scheme)
        if(data[x] == undefined) {
            console.log('Validation fail! Missing', x);
            return false;
        }

    return true;
}

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

    // Receive data chunk by chunk
    req.on('error', function(err) {
        console.log(err);
    }).on('data', function(chunk) {
        body.push(chunk);
    }).on('end', function() {
        body = Buffer.concat(body).toString();
        req.emit('buffer-complete');
    }).on('buffer-complete', function() {
        // Handle request
        var scheme = {
            Latitude: 'double',
            Longitude: 'double',
            Name: 'string',
            Type: 'string'
        };

        try {
            body = JSON.parse(body);
        }
        catch(e) {
            console.log('Failed parsing JSON: ');
            console.log(e.message);
            res.send(JSON.stringify({
                success: false,
                error: 'malformed-json'
            }));
        }
        
        var response = {};

        // Validate req body
        // ( i.e. see if it has all the required fields )
        if(!json_validate(scheme, body)) {
            res.statusCode = 400;
            response = {
                success: false,
                error: 'bad-json'
            }

            res.send(JSON.stringify(response));
            return;
        }

        // Save location
        sql.query('INSERT INTO locations (Name, Latitude, Longitude, Type, Address, Phone, Web, GoogleID) ' +
                 'VALUES (?, ?, ?, ?, ?, ?, ?, ?)', [body.Name, body.Latitude, body.Longitude, body.Type, body.Address, body.Phone, body.Web, body.GoogleID],
             function(err, results) {
                 if(err) {
                     response = {
                         success: false,
                         error: 'query-fail'
                     };
                 } else {
                     response = {
                         success: true
                     };
                 }

                 res.send(JSON.stringify(response));
             });
    });
});

app.listen(PORT, function() {
    console.log('Listening on port', PORT);
});
