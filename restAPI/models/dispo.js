//Dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;

//Schema 
var dispo_schema =new mongoose.Schema(
{
	id : Number,
	id_user :Number,
	date_start : String,
	date_end :String,
	classrooms_id : [Number]
}
);


//Return model
module.exports = restful.model('dispo',dispo_schema);



