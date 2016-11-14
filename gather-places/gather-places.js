var http = require('http');
var https = require('https');

const API_KEY = "AIzaSyDd3X275fD1ZBotmfU52iDTPSmfAjaZO9M";

//=============================================================================================
// Assemble request URL
var urlBase = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json';
var urlOptions = {
    location: [62.1367014, 25.0954831].join(','),
    radius: 2500,
    key: API_KEY
}

var urlPostfix = [];
for(var attrib in urlOptions)
    urlPostfix.push(attrib + '=' + urlOptions[attrib]);

urlPostfix = urlPostfix.join('&');

var url = urlBase + '?' + urlPostfix;
console.log('Request URL is', url);

//=============================================================================================
// GET from Google
console.log('Sending request... ');

https.get(url, function(res) {
    if(res.statusCode != 200) {
        console.log('Request not OK with code', res.statusCode);
        return false;
    }

    res.setEncoding('utf8');
    var data = '';
    var req = this;

    var i = 0;
    res.on('data', (chunk) => {
        data += chunk;

        i += 1;
        process.stdout.write('Receiving' + '.'.repeat(i) + '\r');
    });
    res.on('end', () => {
        try {
            data = JSON.parse(data);
        }
        catch(e) {
            console.log('Couldn\'t parse data as JSON');
            console.log(e.message);
            return false;
        }
        req.emit('done', data);
    });
}).on('done', (data) => {
    console.log('Request complete');

    var results = data.results;
    var places = [];
    for(var i = 0; i < results.length; i+=1) {
        let place = results[i];

        places.push({
            Name: place.name,
            Latitude: place.geometry.location.lat,
            Longitude: place.geometry.location.lng,
            Type: place.types[0]
        });
    }

    // Output places for debug
    for(var i = 0; i < places.length; i+=1) {
        let place = places[i];
        console.log('\t', place.Name, '@', place.Latitude, place.Longitude, '-', place.Type);
    }
});
