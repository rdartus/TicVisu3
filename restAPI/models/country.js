//Dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;

//Schema
var countrySchema = new mongoose.Schema({
country_code: String,
country_name : String,
country_name_ISO : String,
country_default_language : String
});




//Return model
module.exports = restful.model('country',countrySchema);

