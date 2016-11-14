const http = require('http');
const https = require('https');
const EventEmitter = require('events');

const API_KEY = "AIzaSyDd3X275fD1ZBotmfU52iDTPSmfAjaZO9M";

function assembleUrl(base, options) {
    var postfix = [];
    for(var attrib in options)
        postfix.push(attrib + '=' + urlOptions[attrib]);

    return base + '?' + postfix.join('&');
}

//=============================================================================================
// Assemble request URL
var Sydney = [-33.8670, 151.1957];
var Jyvaskyla = [62.1367014, 25.0954831];
var Helsinki = [60.1637088, 24.7600917];

var urlBase = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json';
var urlOptions = {
    location: Jyvaskyla.join(','),
    key: API_KEY,

    rankby: 'distance'
}

var url = assembleUrl(urlBase, urlOptions);
console.log('Request URL is', url);

//=============================================================================================
// GET from Google
console.log('Sending request... ');

class PageGetter extends EventEmitter {
    get(url) {
        var places = [];
        var pageGetter = this;

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
                process.stdout.write('\n');

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
            for(var i = 0; i < results.length; i+=1) {
                let place = results[i];

                places.push({
                    Name: place.name,
                    Latitude: place.geometry.location.lat,
                    Longitude: place.geometry.location.lng,
                    Type: place.types
                });
            }

            if(data.next_page_token) {
                // Issue next
                pageGetter.emit('page', places, data.next_page_token);
            }
            else {
                // Done
                pageGetter.emit('done', places);
            }
        });

        return this;
    }
};

// Request page by page
var places = [];
var getter = new PageGetter();
getter.get(url).on('page', (new_places, next_page_token) => {
    console.log('Page with', new_places.length, 'results');
    places = places.concat(new_places);

    urlOptions = {pagetoken: next_page_token};
    url = assembleUrl(urlBase, urlOptions);
    getter.get(url);
}).on('done', (new_places) => {
    places = places.concat(new_places);

    // Output places for debug
    console.log('Got', places.length, 'results');
    for(var i = 0; i < places.length; i+=1) {
        let place = places[i];
        console.log('\t', place.Name, '@', place.Latitude, place.Longitude, '-', place.Type[0]);
    }
})
