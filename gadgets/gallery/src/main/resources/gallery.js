function loadImages() {
		var imageContainer = document.getElementById("list");
		var highlightContainer = document.getElementById("highlight");
		images=new Array();
		images[0] = "/gallery/1/images/pic1.jpg";
		images[1] = "/gallery/1/images/pic2.jpg";
		images[2] = "/gallery/1/images/pic3.jpg";
		images[3] = "/gallery/1/images/pic4.jpg";
		images[4] = "/gallery/1/images/pic5.jpg";
		
		for(var i = 0; i < images.length; i++) {
			var newImage = document.createElement("img");
			newImage.setAttribute("src", images[i]);
			newImage.setAttribute("width", "200px");
			newImage.setAttribute("height", "200px");
			
			
			newImage.onclick = function() {
				var tmpImage = document.createElement("img");
				var src=this.getAttribute("src");
				tmpImage.setAttribute("src", src);
				tmpImage.setAttribute("width", "400px");
				tmpImage.setAttribute("height", "400px"); 
				$('#highlight').empty();
				highlightContainer.appendChild(tmpImage);
			};
			
			
			imageContainer.appendChild(newImage);
		}
		
	}
