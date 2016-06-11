//Dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;

//Schema 
var classroom_schema =new mongoose.Schema(
{
				classroom_id : Number,
				classroom_name : String,
				classroom_address : String,
				classroom_zip : String,
				classroom_city : String,
				classroom_country_code : String,
				classroom_gps_lat : Number,
				classroom_gps_lon : Number,
				classroom_access : Boolean,
				classroom_type : Number,
				classroom_capacity : Number,
				classroom_description : String,
				classroom_display : Boolean,
				classroom_feature_parking : Boolean,
				classroom_feature_shower : Boolean,
				classroom_feature_locker : Boolean,
				classroom_feature_credit : Boolean,
				classroom_photo_url : String,
				classroom_photo_large_url :String,
				location_id : Number
				
				
}
);



//Return model
module.exports = restful.model('classroom',classroom_schema);



