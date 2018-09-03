Feature: User can add a new ticket and see it

	Scenario: Add ticket with randomly generated title and description
        Given I do not see ticket text and description
         When I click on add ticket button
          And I enter a ticket title
          And I enter a ticket description
          And I click on submit button
         Then I see ticket text and description