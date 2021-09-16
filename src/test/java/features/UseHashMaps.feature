Feature: Using Hashmaps for Validating Place APIs

  Scenario Outline:Verify if Place is being succcessfully added using addPlaceAPI
    Given  I set parameters
      |key |qaclick123|
    And  we have a payload for the addPlaceAPI with "<name>", "<language>" and "<address>"
    When user calls "addPlaceAPI" with "POST" http request
    Then the API call is successful with status code 200
    Examples:
      |name      |language  |address             |
      |AAhouse   |English   |World Cross Centre  |

    #Works as stand alone scenario, but not in feature. Investigate why?
   # Seems that response is null in getNodeValueFromResponse()
  Scenario: Get the location of added placed
    Given  I set parameters
      | key      | qaclick123                       |
      | place_id | 15590dc255ef204ea7e6a54b512d42fb |
    And I create a GET request
    When user calls "getPlaceAPI" with "GET" http request
    Then the API call is successful with status code 200
    And "accuracy" in the response bode is "50"
    And the following values are checked using hashmaps:
      | node     | latitude   | longitude |
      | location | -38.383494 | 33.427362 |



