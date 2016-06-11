//Dependencies

var express = require('express');
var router=express.Router();

var mongoose=require('mongoose');

//Models

var country =require('../models/country');
var region =require('../models/region');
var classroom =require('../models/classroom');
var staff =require('../models/staff');
var dispo =require('../models/dispo');
var cours =require('../models/class');

//Routes

country.methods(['get']);
country.register(router,'/country');

region.methods(['get']);
region.register(router,'/region');

classroom.methods(['get']);
classroom.register(router,'/classroom');

cours.methods(['get']);
cours.register(router,'/class');

staff.methods(['get']);
staff.register(router,'/staff');

dispo.methods(['get']);
dispo.register(router,'/dispo');


//Custom Get

router.get('/suggestion/:lon/:lat/:rayon',function(req, res, next) {  
    

    var maxDistance = req.params.rayon;
	
	var r = maxDistance / 6371;
	
	var lat=req.params.lat
	var lon=req.params.lon
	var tested=[];
	
	var returned="0";
	var latmin=parseFloat(lat)-parseFloat(r);
    var latmax=parseFloat(lat)+parseFloat(r);
	
	var deltalon = Math.asin((Math.sin(r)/Math.cos(lat)));
	
    var lonmin=lon-deltalon;
    var lonmax=parseFloat(lon)+parseFloat(deltalon);
	
    // find a location
    classroom.find({
	classroom_gps_lon : {$gt : latmin,$lt : latmax},
	classroom_gps_lat : {$gt : lonmin,$lt : lonmax}
}).exec(function(err, locations) {	
      if (err) {return res.json(500, err);}
	  
		for(i=0;i<locations.length;i++)
		{tested.push(locations[i].classroom_id);
		}
			
	
	}).then(function(err,data){
		for(i=0;i<tested.length;i++)
			{returned=returned+","+tested[i];}
		
		var tojson ='{"userids" : '+'"'+returned+'"'+'}';
		var obj= JSON.parse(tojson);
		res.setHeader('Content-Type', 'application/json');
		res.send(obj);
      
	});
	
			
			
    
});

//"date_start" : "2016-06-06 07:30:00",
//	"date_end" : "2016-06-06 10:30:00",


router.get('/dispo/crid2/:classroomid/:hourstart/:hourend',function(req,res){
	
	var tested = req.params.classroomid.toString();
	var hourstart = req.params.hourstart.toString();
	var hourend = req.params.hourend.toString();
	
	hourstart = hourstart.split(":");
	hourstart=hourstart[0];
	console.log(hourstart);
	
	//hourend=hourend.split(" ");
	//hourend=hourend[1].split(":");
	//hourend=hourend[0];
	 hourend=parseInt(hourstart)+ hourend / 60;
	 hourend=parseInt(hourend);
	 console.log(hourend);
	

	var returned="0";
	
	dispo.find({classrooms_id :tested}, function(err,data){
       
	   for(i=0;i<data.length;i++)
		{	
			
			datestart=data[i].date_start.toString().split(" ");
			datestart=datestart[1].split(":");
			datestart=datestart[0];
			
			dateend=data[i].date_end.toString().split(" ");
			dateend=dateend[1].split(":");
			dateend=dateend[0];
			
			datestart=datestart-2;
			dateend=parseInt(dateend)+2;
			
			
			
			if(datestart>=hourstart && dateend>=hourend)			
			{returned=returned+","+data[i].id_user;}
			
			
			}
		var tojson ='{"userids" : '+'"'+returned+'"'+'}';
		var obj= JSON.parse(tojson);
		res.setHeader('Content-Type', 'application/json');
		res.send(obj);
    });
	
		
	
});



router.get('/dispo/crid/:classroomid',function(req,res){
	
	var tested = req.params.classroomid;
	
	var returned="0";
	
	dispo.find({classrooms_id :tested}, function(err,data){
       
	   for(i=0;i<data.length;i++)
		{returned=returned+","+data[i].id_user;}
		var tojson ='{"userids" : '+'"'+returned+'"'+'}';
		var obj= JSON.parse(tojson);
		res.setHeader('Content-Type', 'application/json');
		res.send(obj);
    });
	
		
	
});

router.get('/dispo/prof/:profid',function(req,res){
	
	var  tested = req.params.profid;
	var returned="0"
	//
	cours.real.find({teacher_list :tested}, function(err,data){
      
	   for(i=0;i<data.length;i++)
		{returned=returned+","+data[i].class_id;}
		var tojson ='{"userids" : '+'"'+returned+'"'+'}';
		var obj= JSON.parse(tojson);
		res.setHeader('Content-Type', 'application/json');
		res.send(obj);
    });
	
		
	
});

router.get('/dispo/host/:profid',function(req,res){
	
	var tested = req.params.profid;
	
	var returned="0";
	
	cours.real.find({host_list :tested}, function(err,data){
       
	   for(i=0;i<data.length;i++)
		{returned=returned+","+data[i].class_id;} 
		var tojson ='{"userids" : '+'"'+returned+'"'+'}';
		var obj= JSON.parse(tojson);
		res.setHeader('Content-Type', 'application/json');
		res.send(obj);
    });
	
		
	
});

router.get('/RetrieveErrorList/:regionid',function(req,res){
	
	var tested = req.params.regionid;
	var returned= 0;
	var ClassroomList="";
	
	region.find({region_id:tested}).exec(function(err,data){
		
		
	for(i=0;i<data.length;i++){
		
		ClassroomList =data[i].classroom_list;
	
	}
	}).then(function(){
		
		cours.find({classroom_id:{$in: ClassroomList},class_has_issue:true}).exec(function(err,data){
		
		if(!err){
			for(j=0;j<data.length;j++){
				returned=returned+","+data[j].class_id;
			}
			
		res.setHeader('Content-Type', 'application/json');
		res.send(data);
		}
		
	
	});
		
		
	});
		
		
});
//Rturn route
module.exports =router;
