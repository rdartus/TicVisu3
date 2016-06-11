//Dependencies
var restful = require('node-restful');
var mongoose = restful.mongoose;

//Schema 
var staff_schema =new mongoose.Schema(
{
	
	user_id : String,
	user_nom : String,
	user_prenom : String,
	user_adresse : String,
	user_adresse_mail : String,
	staff_photo_url : String,
	staff_photo_large_url : String,
	staff_is_global : Boolean,
	staff_is_country : Boolean,
	staff_is_zone : Boolean,
	staff_is_teacher : Boolean,
	staff_is_host : Boolean,
	staff_issue_type : Number,
	user_dipsonibilities : [{
			id : String,
			id_user : String,
			date_start : String,
			date_end : String,
			classrooms_id : []

}]
}
);


//Return model
module.exports = restful.model('staff',staff_schema);



