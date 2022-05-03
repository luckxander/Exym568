Feature: EXYM-568 As a Clinician, 
         I want to be able to sort the last service and scheduled date, 
         so that I can see which client I might need to reach out to if it's been over 30 days.
         

		 Background
			Given I am a Clinician: 
			When I view the vNext homepage, 
			I am able to sort the last service and scheduled date, 
			so that I can see wich client I might need to reach out to if it is been over 30 days..
	
	
	Scenario: Sort column by Service date
		Given I am a clinician user
		 When I go to the main page Exym vNext portal
		  And I click in the sort arrow located to the right of the ‘LAST SERVICE’ text in the ‘Client’s table
		 Then I should see the services sorted by the most recent service date
		  And I click in the sort arrow located to the right of the ‘SCHEDULED’ text in the ‘My Notes’ table
          And I should see the services sorted by the most recent service date
	      And I should see the notes sorted by scheduled date

		

		
		
		
		
		