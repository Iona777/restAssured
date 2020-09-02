 Feature: Validating Place APOIs

   Scenario Outline:Verify if Place is being succcessfully added using addPlaceAPI
     Given  we have a payload for the addPlaceAPI with "<name>", "<language>" and "<address>"
     When user calls "addPlaceAPI" with "POST" http request
     Then the API call is successful with status code 200
     And "status" in the response bode is "OK"
     And "scope" in the response bode is "APP"
     And place_Id created maps correctly to "<name>" using "getPlaceAPI"
     Examples:
       |name      |language  |address             |
       |AAhouse   |English   |World Cross Centre  |


   @DeletePlace
   Scenario: Verify if Delete Place functionality is working
     Given DeletePlace Payload
     When user calls "deletePlaceAPI" with "POST" http request
     Then the API call is successful with status code 200
     And "status" in the response bode is "OK"