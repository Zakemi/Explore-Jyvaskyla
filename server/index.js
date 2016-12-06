var express = require('express');
var mysql = require('mysql');
var verifier = require('google-id-token-verifier');
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
    sql.query('SELECT l.*, Coalesce( Avg( r.Rating ), 0 ) As Rate FROM locations l LEFT JOIN rating r ON l.ID=r.PlaceID group by l.ID', function(err, rows, fields) {
        if(err) {
            console.log('Locations query fail!', err);
            res.statusCode = 500;
            res.send('Query fail!');
            return;
        }
        res.send(JSON.stringify(rows));
    });
});

app.get('/login/:idToken', function(req, res){
	var idToken = req.params.idToken;
	var clientId = "155033622944-qvat1jcvr6o5u709g8n50ecr889osrl7.apps.googleusercontent.com";
	verifier.verify(idToken, clientId, function (err, tokenInfo) {
		if (!err) {
			// make query to find the user in the db
			checkUserInDatabase(tokenInfo, function(err, rows){
				if (err){
					console.log('Find user query fail', err);
				} else {
					if (rows.length == 0){
						// no user in the db, save it...
						sql.query('INSERT INTO users (Id, Name, Picture) VALUES (?, ?, ?)', [tokenInfo.sub, tokenInfo.name, tokenInfo.picture], 
						function(err, results) {
							if(err) {
								console.log('Insert user query fail', err);
								res.send(JSON.stringify({"id": null}))
								return;
							} else {
								// ... and send to the client
								result = {};
								result["Id"] = tokenInfo.sub;
								result["Name"] = tokenInfo.name;
								result["Picture"] = tokenInfo.picture;
								res.send(JSON.stringify(result));
								return;
							}
						});
					} else {
						// send the first row from the db
						res.send(JSON.stringify(rows[0]));
						return;
					}
				}
			});
		}
	});
});

function checkUserInDatabase(tokenInfo, callback){
	sql.query('SELECT * FROM users WHERE Id LIKE ' + tokenInfo.sub, function(err, rows, fields){
		if (err){
			console.log('Find user query fail', err);
			callback(err, null);
		}
		callback(null, rows);
	});
}

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

app.post('/rate', function(req, res) {
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
            UserId: 'string',
            PlaceId: 'integer',
            Rating: 'integer'
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

        // Save rating
        sql.query('INSERT INTO rating (UserId, PlaceId, Rating) ' +
                 'VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE Rating=?', [body.UserId, body.PlaceId, body.Rating, body.Rating],
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
