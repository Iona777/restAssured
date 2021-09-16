Feature: I connect to a MySQL DB

  Scenario Outline: I connect to DB
    Given I connect to DB with "<URL>", "<Username>" and "<Password>"
    #When I execute the SQL query "<QueryString>"
    When I execute the SQL query in file "src/test/java/resources/sqlFiles/sql1.txt"
    Then I print out the names
    Examples:
    |URL                                            |Username   |Password   |QueryString|
    |jdbc:mysql://localhost/test?serverTimezone=UTC |myUsername |myPassword |select * from users where first_name =  'John'  |
