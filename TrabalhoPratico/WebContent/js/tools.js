function progress(timeleft, timetotal, $element) {
			var progressBarWidth = timeleft * $element.width() / timetotal;
			$element.find('div').animate({
				width : progressBarWidth
			}, 500).html(timeleft);
			if (timeleft > 0) {
				setTimeout(function() {
					progress(timeleft - 1, timetotal, $element);
				}, 1000);
			}
			
			if(timeleft == 0){
				
				$("#send1").prop("disabled",false);
				$("#sendRand").prop("disabled",false);
				$("#sendAll").prop("disabled",false);
			}
		};
		
		
