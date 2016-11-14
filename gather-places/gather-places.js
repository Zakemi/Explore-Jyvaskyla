var http = require('http');

var API_KEY = "AIzaSyDd3X275fD1ZBotmfU52iDTPSmfAjaZO9M";
var Latitude = 62.1367014;
var Longitude = 25.0954831;
var Range = 2500;

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
