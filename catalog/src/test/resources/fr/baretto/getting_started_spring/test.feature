Feature: product integraiton test

  Scenario: we call "/product" and receive an empty list
    When we call "/product"
    Then we receive "[]"

  Scenario: we call create product and receive a new product
    When we call "/product" with a new product
    Then we receive "[]"