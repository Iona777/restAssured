 Feature: Validating Place APOIs
   Background:
     Given  I set parameters
       |key |qaclick123|
     #And I set resource URL to "/maps/api/place/add/json"
     #And I set a payload using file "src/test/java/resources/payloadFiles/addPlace1.json"

   Scenario Outline:Verify if Place is being succcessfully added using addPlaceAPI
     Given we have a payload for the addPlaceAPI with "<name>", "<language>" and "<address>"
     When user calls "addPlaceAPI" with "POST" http request
     Then the API call is successful with status code 200
     And "status" in the response bode is "OK"
     And "scope" in the response bode is "APP"
     And place_Id created maps correctly to "<name>" using "getPlaceAPI"
     Examples:
       |name      |language  |address             |
       |AAhouse   |English   |World Cross Centre  |

   Scenario Outline: Call addPlaceAPI
     Given  I set parameters
       |key |qaclick123|
     And I set resource URL to "/maps/api/place/add/json"
     And I set a payload using file "src/test/java/resources/payloadFiles/addPlace1.json"
     #Aim to include this step into some other(s) all it does is create the spec with given parameters and payload
     When we have a payload loaded from a file
     And user calls given URL with "POST" http request
     Then the API call is successful with status code 200
     And "status" in the response bode is "OK"
     And "scope" in the response bode is "APP"
     And place_Id created maps correctly to "<name>" using "getPlaceAPI"
     Examples:
       | name            |
       | Frontline house |




   #@DeletePlace
   #Scenario: Verify if Delete Place functionality is working
    # Given Delete Place Payload
     #When user calls "deletePlaceAPI" with "POST" http request
     #Then the API call is successful with status code 200
     #And "status" in the response bode is "OK"