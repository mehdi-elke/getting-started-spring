Feature: product integraiton test

  Scenario: we call "/product" and receive an empty list
    When we call "/product"
    Then we receive "[]"
    
  Scenario: we send create product and receive a new product
    Given a productRequestDto with name "kohaku", description "le chat de thémis", and price 10
    When we send POST "/product" with this productRequestDto
    Then we receive a product with name "kohaku"
    Then we call "/product"
    Then we receive a status OK and:
        """
        [{
          "name":"kohaku",
          "description":"le chat de thémis",
          "price":10.0
        }]
        """
