var map;
var icons = [];
function initialize() {

	var mapProp = {
		zoom:15,
		mapTypeId:google.maps.MapTypeId.ROADMAP
	};
	map=new google.maps.Map(document.getElementById("mapsection"), mapProp);
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(showPosition);
	} else {
		document.getElementById("mapsection").innerHTML = "Geolocation is not supported by this browser.";
	}
}

function showPosition(position) {

	var geolocate = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
	map.setCenter(geolocate); 
	var marker=new google.maps.Marker({
		position:geolocate,
	});
	icons.push(marker);
	marker.setMap(map);
}

google.maps.event.addDomListener(window, 'load', initialize);
var j=0;
function searchLocations(markers){
	
	if (markers.length !=0) {
		deleteMarkers();
	}
	var sf = new google.maps.LatLng(37.756139,-122.441033);
	var service = new google.maps.places.PlacesService(map);
	for(i=0; i<markers.length; i++){

		var request = {
		    query:  markers[i] + " san francisco",
		    radius: '500',
		    location: sf
		};
		
		service.textSearch(request, callback);
	}
	console.log(j++);
}
function deleteMarkers() {
	for (var i = 0; i < icons.length; i++) {
	    icons[i].setMap(null);
	 }
	 icons = [];
}


var bounds;
var infowindow = new google.maps.InfoWindow();

function callback(results, status) {
	
	if (status == google.maps.places.PlacesServiceStatus.OK) {
		console.log(results[0]);
		bounds = new google.maps.LatLngBounds();
		createMarker(results[0]);
		map.fitBounds(bounds);
		var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
	        this.setZoom(12);
	        google.maps.event.removeListener(boundsListener);
		});
	}else {
		console.log(status);
	}
}



function createMarker(place) {

	var placeLoc = place.geometry.location;
	bounds.extend(placeLoc);
	var marker = new google.maps.Marker({
	    map: map,
	    position: placeLoc
	});
	icons.push(marker);
	google.maps.event.addListener(marker, 'click', function() {
	    infowindow.setContent(place.name);
	    infowindow.open(map, this);
	});
}

//---------------------------------------------------------------------------------------

var titles = new Array();
var movies = new Array();

$(document).ready(function(){
	
	$.ajax({
		  url: "/SFfilmingLocations/listmovie",
		}).done(function(data) {
			
		  for (var i=0;i<data.data.length;i++) {
			  if (titles.indexOf(data.data[i].title) == -1)
				  titles.push(data.data[i].title);
			  
			  var t = data.data[i].title;
			  if (data.data[i].location.length > 0) {
				  if ( movies[t] == undefined) {
					  movies[t] = new Array();
				  }
				  movies[t].push(data.data[i].location);
			  }
				  
		  }
		  
		  $( "#searchbox" ).autocomplete({
		      source: titles
		    });
		  
		  $("#searchbut").click(function(){
			  $("#listresult").empty();
			  var title = $("#searchbox").val();
			  if (movies[title] == undefined ) {
				  $("#listresult").append($("<font>").attr("style","color:#FF0000").html("No movie or location found"));
			  }else {
				  var locations = movies[title];
				  searchLocations(locations);
				  
				  for (var i =0;i<locations.length;i++) {
					  $("#listresult").append($("<div>").html(locations[i]).attr('class',"loc"));
				  }
				  $('.loc').click(function(){
					  var location = $(this).text();
					  var loc = [];
					  loc.push(location);
					  searchLocations(loc);
				  });
				  
		  		}
		  });
		  
		  
	});
	
	    
	    
	
});