//Dependencies

var express = require('express')
var mongoose = require('mongoose');
var bodyParser = require('body-parser');

//MongoDB

mongoose.connect('mongodb://localhost:27017/VisuDb');

//Express

var app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(function(req, res, next) 
{ res.header('Access-Control-Allow-Origin', "*"); 
res.header('Access-Control-Allow-Methods','GET');
 res.header('Access-Control-Allow-Headers', 'Content-Type');
 next();
});

//Routes
app.use('/api', require('./routes/api'))




//Start Server
app.listen(3000);
console.log("API Is listen on port 3000")

//nbuib