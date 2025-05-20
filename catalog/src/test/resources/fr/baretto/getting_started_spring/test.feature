Feature: a polite spring boot service

  Scenario: our service can greet us
    When we call "/product"
    Then we receive "Hello world!"