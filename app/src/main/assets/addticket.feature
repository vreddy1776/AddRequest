Feature: Login screen to authenticate users

	Scenario: Invalid username and password
        Given I do not see ticket text and description
         When I click on add ticket button
          And I enter a ticket title
          And I enter a ticket description
          And I click on submit button
         Then I see ticket text and description