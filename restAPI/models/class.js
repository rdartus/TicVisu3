//Dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;
var returned= mongoose.model;

//Schema 
var class_schema =new mongoose.Schema(
{
	class_id: Number,
	class_type : Number,
	class_date : String,
	class_startdate : String,
	class_dayofweek : String,
	class_starttime : String,
	class_duration : Number,
	class_capacity : Number,
	class_bookable_capacity : Number,
	class_level: Number,
	class_level_text : String,
	class_level_icon_URL : String,
	class_isclosed : Boolean ,
	class_hasbooking : Boolean,
	class_ispacked : Boolean,
	location_id : Number,
	classroom_id : Number,
	teacher_list : [String],
	host_list : [String],
	class_pass_green : Boolean,
	class_pass_blue : Boolean,
	class_pass_yellow : Boolean,
	class_pass_white : Boolean,
	class_is_free : Boolean,
	class_unit_value : String,
	currency : String,
	class_has_issue : Boolean,
	class_issue_code : Number,
	classroom_country_code : String
}
);
var class_schema2 =new mongoose.Schema(
{
	class_id: Number,
	class_type : Number,
	class_date : String,
	class_startdate : String,
	class_dayofweek : String,
	class_starttime : String,
	class_duration : Number,
	class_capacity : Number,
	class_bookable_capacity : Number,
	class_level: Number,
	class_level_text : String,
	class_level_icon_URL : String,
	class_isclosed : Boolean ,
	class_hasbooking : Boolean,
	class_ispacked : Boolean,
	location_id : Number,
	classroom_id : Number,
	teacher_list : [Number],
	host_list : [Number],
	class_pass_green : Boolean,
	class_pass_blue : Boolean,
	class_pass_yellow : Boolean,
	class_pass_white : Boolean,
	class_is_free : Boolean,
	class_unit_value : String,
	currency : String,
	class_has_issue : Boolean,
	class_issue_code : Number,
	classroom_country_code : String
}
);

returned = restful.model('class_schema',class_schema,'classes');

returned.real=mongoose.model('class_schema2',class_schema2,'classes');
module.exports = returned;
//Return model



