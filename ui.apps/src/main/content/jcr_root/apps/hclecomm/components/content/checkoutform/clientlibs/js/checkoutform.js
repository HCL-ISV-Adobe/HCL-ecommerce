$( document ).ready(function() {
    		const stateArray = ['Uttar-Pradesh', 'Andhra-Pradesh', 'Maharashtra'];
    		let selectHTML = "<select class ='add-addr-feilds' name ='state'>";
    		if(stateArray){
    			 stateArray.forEach((item)=> {
    			 selectHTML += "<option value='" + item + "'>" + item + "</option>";})
    				selectHTML += "</select>";
    		if(selectHTML){
    			//document.getElementById('state-collection').appendChild(selectHTML);
    			document.getElementById('state-collection').innerHTML = selectHTML;
    		}
    		}

		});
	let doValidation = false;
	let submitForm = false;
	function onValidatingGuestMail(event) {
		const regexEmail = /\S+@\S+\.\S+/;
		const getCustomerMailId = $('.checkout-guest-mail-id')[0].value;
		if(getCustomerMailId === ''){
				$('.customer-validation-message').text('Please Enter Email');
		}
		else if(!regexEmail.test(getCustomerMailId)){
				$('.customer-validation-message').text('Please Enter valid  Email');
		}
		else {		
				$('.customer-validation-message').text('');
				onToggleDescription(event);
		}
		
	}




	function onToggleDescription(event) {
		//$(event).parent().parent()[0].classList.remove('toggle-checkout-description');
	  const getAllSection = $('.check-out-section-description');
	  const getAllEditButton = $('.edit-check-out-section');
	  let trackIndex = '';
	  if(getAllSection){
	  trackIndex = getAllSection.toArray().findIndex( function(descriptionItem, index) {
  			return descriptionItem.classList.contains('toggle-checkout-description');
		})
	  if(trackIndex  <= getAllSection.toArray().length){
	  	getAllSection.toArray()[trackIndex].classList.remove('toggle-checkout-description')
		getAllSection.toArray()[trackIndex+1].classList.add('toggle-checkout-description')
		}
	  }
	  if(getAllEditButton){
	  	getAllEditButton.toArray()[trackIndex].classList.add('toggle-checkout-description')
	  }
	}

	function onEditCheckOutSection(event){
		const getAllSection = $('.check-out-section-description');
		 if(getAllSection){
		  getAllSection.toArray().forEach( function(descriptionItem, index) {
	  			descriptionItem.classList.remove('toggle-checkout-description')
			})
		  
	  }
	   //$('.check-out-section-description').remove('toggle-checkout-description');
	 	$(event).parent().siblings()[0].classList.add('toggle-checkout-description');
	 	//$(event).remove('.toggle-checkout-description');
	 	 
	  }

	function onEnterDeatils(event){
		const userDetails = event.name;
		const userDetailsValue = event.value;
		//$('.add-new-address-form').toggleClass('toggle-checkout-description')
		if(submitForm){
		letValidateField(userDetails, userDetailsValue, event)
		}

    }
		
	 
/// method for saving and delivering
	  function onSaveNDeliver(){
	  let	validationFeilds = true;
	  submitForm = true;
			const getAllAddressFields = $('.add-addr-feilds');
			if(getAllAddressFields){
				getAllAddressFields.toArray().forEach((fieldItem) => {
							const userDetails = fieldItem.name;
							const userDetailsValue = fieldItem.value;
							letValidateField(userDetails, userDetailsValue, fieldItem)
						
				})
			}
	 	if(validationFeilds && !doValidation){
	 			console.log('form submitted');
	 			onToggleDescription(event);

	 	}

	  }

	  function onAddCancel(){
	  	const getAllAddressFields = $('.add-addr-feilds');
			if(getAllAddressFields){
				getAllAddressFields.toArray().forEach((fieldItem) => {
						if(fieldItem){	
								fieldItem.value	 ='';
								fieldItem.nextElementSibling.innerText = '';
						}
				})
			}
	  }


	  function onToggleNewADD(){
	  	const getPlusIcon = $('.add-new-plus-icon');
	  	if(getPlusIcon){
	  		getPlusIcon.toArray().forEach((plusIconItem) =>{
	  			plusIconItem.classList.contains('toggle-icon') ? plusIconItem.classList.remove('toggle-icon')
	  			: plusIconItem.classList.add('toggle-icon')
	  		})
	  	}
		$('.add-new-address-form').toggleClass('toggle-checkout-description')

	 	 
	  }

	  function letValidateField(userDetails, userDetailsValue, event){
			switch(userDetails) {
		  case 'User Name':
		  if(!userDetailsValue){
		  		event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
		  		doValidation = true;
		  		return
		  }
		  break;
		  case 'Phone Number':
		  if(!userDetailsValue){
		  		event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
		  		doValidation = true;
		  		return
		  }
		  const phoneReg= /^\d{10}$/;
		  if(!userDetailsValue.match(phoneReg)){
		  		event.nextElementSibling.innerText = `Please Enter Valid ${userDetails}`;
		  		doValidation = true;
		  		return
		  }
		  
		  break;
		  case 'Pin Number':
		  const pinRegexp = /^[0-9]{5}(?:-[0-9]{4})?$/;
		  if(!userDetailsValue){
		  		event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
		  		doValidation = true;
		  		return
		  }

		  if(!userDetailsValue.match(pinRegexp)){
		  		event.nextElementSibling.innerText = `Please Enter valid ${userDetails}`;
		  		doValidation = true;
		  		return
		  }

		  case 'Address':
		  if(!userDetailsValue){
		  		event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
		  		doValidation = true;
		  		return
		  }
		  
		  break;

		  case 'Street':
		  if(!userDetailsValue){
		  		event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
		  		doValidation = true;
		  		return
		  }
		 
		    break;
		   case 'state':
		   return
		    break;

		   case 'optional-phone':
		   return
		  
		 
		    break;
		  default:
    		break;
		}
		  	event.nextElementSibling.innerText = '';
		  doValidation = false;
	 	 
	  }

	  function onSelecCVV(){
	  	const getCvvSection = $('.billing-cvv-section');
	  	if(getCvvSection){
	  			getCvvSection.toArray().forEach((cvvSectionItem) =>{
	  				cvvSectionItem.classList.contains('add-new-billing-address') 
	  				? cvvSectionItem.classList.remove('add-new-billing-address') : cvvSectionItem.classList.add('add-new-billing-address')
	  			})
	  	}
	  }

	  function onContinueOnlyCvv(){
	  	const getCvvSection = $('.cvv-text');
	  	if(getCvvSection[0].innerText === ''){
	  			$('.cvv-validation-mssg')[0].innerText = 'Please Enter CVV';
	  	}
	  	else 
	  		{
	  				$('.cvv-validation-mssg')[0].innerText = '';
	  	}
	  }
