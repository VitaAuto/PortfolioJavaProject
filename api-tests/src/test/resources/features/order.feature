Feature: Order management
  As an user
  I want to manage orders
  So that I can view, create, update, patch, and delete orders

  @smoke
  Scenario: Get all orders when no orders exist
    When database table "orders" is cleared
    And user tries to get all orders
    Then the response should not contain at least one order

  @smoke
  Scenario: No orders with negative amounts exist
    Given user creates a new order with username "Andrew", description "Initial order comment", and amount "29.01"
    Then the response should have status code 201
    When user tries to get all orders
    Then the response should have status code 200
    And no order should have negative amount

  #The scenario can be used as a mock by activating @mock tag below (test stub has been prepared)
  #@mock
  Scenario: Get all orders when at least 1 has been created
    Given user creates a new order with username "Kevin", description "Second order", and amount "0.99"
    Then the response should have status code 201
    When user tries to get all orders
    Then the response should have status code 200
    And the response should contain at least one order
    And no order should have negative amount

  #The scenario can be used as a mock by activating @mock tag below (test stub has been prepared)
  #@mock
  Scenario Outline: Create a new order with valid data
    Given user creates a new order with username "<username>", description "<description>", and amount "<amount>"
    Then the response should have status code 201
    And the response should contain the order with username "<username>", status CREATED, description "<description>", and amount <amount>

    Examples:
      | username   | description      | amount  |
      | Alice      | First order      | 100.50  |
      | Kevin      | Second order     | 0.99    |

  Scenario Outline: Create a new order with invalid data
    Given user creates a new order with username "<username>", description "<description>", and amount "<amount>"
    Then the response should have status code 400

    Examples:
      | username   | description      | amount  |
      |            | No username      | 100.00  |
      | alice      | Negative amount  | -10.00  |
      | bob        | No amount        |         |

  Scenario Outline: Get order by id
    Given user creates a new order with username "<username>", description "<description>", and amount "<amount>"
    Then the response should have status code 201
    And user tries to get order by id "<id>"
    And the response should contain the order with id "<id>"

    Examples:
      | username   | description      | amount |   id  |
      | Michael    | Small amount sum | 1.01   | saved |

  Scenario: Get order by non-existent id
    Given user tries to get order by id "99099"
    Then the response should have status code 404

  Scenario Outline: Update order with valid data
    Given user creates a new order with username "<username>", description "<description>", and amount "<amount>"
    Then the response should have status code 201
    When user updates the order with id "<id>" and username "<username>", description "<description>", and amount <amount>
    Then the response should have status code 200
    And the response should contain the order with username "<username>", status UPDATED, description "<description>", and amount <amount>

    Examples:
      | id    | username      | description     | amount  |
      | saved | John_udated   | Updated order   | 149.99  |

  Scenario Outline: Partial update order with valid data
    Given user creates a new order with username "<initial_username>", description "<initial_description>", and amount "<initial_amount>"
    Then the response should have status code 201
    When user partially updates the fields of the order with id "saved"
      | description | Patched order           |
      | amount      | 375.50                  |
    Then the response should have status code 200
    And the response should contain the order with username "<initial_username>", status PARTIALLY_UPDATED, description "<updated_description>", and amount <updated_amount>

    Examples:
      | initial_username     | initial_description     | initial_amount  | updated_description | updated_amount|
      | Larry_initial        | Updated order           | 225.00          | Patched order       | 375.50        |

  Scenario: Delete order (soft)
    Given user creates a new order with username "User_To_Soft_Delete", description "To soft delete", and amount "50.00"
    Then the response should have status code 201
    When user deletes the order with id "saved"
    Then the response should have status code 200
    And the response should contain the order with username "User_To_Soft_Delete", status DELETED, description "To soft delete", and amount 50.00

  Scenario: Delete order (hard)
    Given user creates a new order with username "User_To_Hard_Delete", description "To hard delete", and amount "3001.00"
    Then the response should have status code 201
    When user hard deletes the order with id "saved"
    Then the response should have status code 204

  Scenario: Patch order with invalid field
    Given user creates a new order with username "User for PATCH", description "InitialInvalid", and amount "1.00"
    Then the response should have status code 201
    When user partially updates the fields of the order with id "saved"
      | field      | value           |
      | unknown    | test            |
    Then the response should have status code 500
