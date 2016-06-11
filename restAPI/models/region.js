//Dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;

//Schema 
var region_schema =new mongoose.Schema(
{
region_gps_lon: Number,
region_gps_lat: Number,
region_country_code: String,
region_name: String,
region_id: Number,
location_list: [Number],
classroom_list : [Number]
}
);


//Return model
module.exports = restful.model('region',region_schema);



