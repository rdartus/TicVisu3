//Dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;

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


//Return model
module.exports = mongoose.model('realcours',class_schema,'classes');



