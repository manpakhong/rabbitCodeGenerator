

function scrollDiv(width){
			//console.log("scrollDiv1");
			//console.log("width : " + width);
			//console.log("scrollDivWidth" + scrollDivWidth);
			//console.log("box-bodywidth : " + $(".box-body").width());
			
			if(width!=null){
			if(width < scrollDivWidth){
				$("#scrollDiv").css("overflow-x","auto");
			} else{
				$("#scrollDiv").css("overflow-x","hidden");
			}	
		}else{
			if($(".box-body").width()<scrollDivWidth){
				$("#scrollDiv").css("overflow-x","auto");
			}
		}
	}
		
		//determine when to make #scrollDiv overflow-x auto or hidden 
		function scrollDiv(width, scrollDivWidth){
			//console.log("scrollDiv2");
			//console.log("width : " + width);
			//console.log("scrollDivWidth" + scrollDivWidth);
			//console.log("box-bodywidth(adjusted) : " + $(".box-body").width());
			
			if(width!=null){
				if(width <= scrollDivWidth){
					$("#scrollDiv").css("overflow-x","auto");
				} else{
					$("#scrollDiv").css("overflow-x","hidden");
				}	
			}else{
				if($(".box-body").width() - 30 <= scrollDivWidth){
					$("#scrollDiv").css("overflow-x","auto");
				}
			}
		}
		
		
		// example1 is the id of all the datatable
		function tableWraper(scrollDivWidth){
			$("#example1").wrap("<div id='scrollDiv'></div>" );
			$("#example1").css("min-width",scrollDivWidth+"px");
		}
		
		function headerWarper(){
			$(".main-header").wrap("<div id='scrollHeader' style='overflow-x:auto'></div>" );
			
			var tun = $("#titileUserName").width()
			if(tun != null){
				$(".main-header").css("min-width","420px");
			} else{
				$(".main-header").css("min-width","360px");
			}
			
		}
		