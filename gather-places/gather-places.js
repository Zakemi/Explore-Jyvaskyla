var http = require('http');
var https = require('https');

const API_KEY = "AIzaSyDd3X275fD1ZBotmfU52iDTPSmfAjaZO9M";

// Middle of Jyvaskyla ( according to Google Maps )
var Latitude = 62.1367014;
var Longitude = 25.0954831;
var Range = 2500;

//=============================================================================================
// Assemble request URL
var urlBase = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json';
var urlOptions = {
    location: [Latitude, Longitude].join(','),
    radius: Range,
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
    console.log(JSON.stringify(data, undefined, 4));
});
