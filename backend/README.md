# Create Order Request

## Successful Order Creation

The happy path order creation is as follows:

1. `Order Service` creates a new Order in the PENDING_APPROVAL state and publishes an `order-created` event.

2. `Order Line Item Service` consumes the `order-created` event, extracts the list of products and quantities from the order payload, and persists them into the `Order Line Item Table`.

3. `Product Service` consumes the `order-created` event, verifies that there is sufficient stock for all products referenced in the order, reduces the stock, and publishes a `stock-reserved` event.

4. `Payment Service` consumes the `order-created` event and creates a Payment record in a PENDING state.

5. `Payment Service` consumes the `stock-reserved` event, authorizes or charges the customer’s payment method, and publishes a `payment-authorized` event.

6. `Order Service` consumes the `payment-authorized` event, updates the Order state to APPROVED, and publishes an `order-approved` event.

## Insufficient Stock

The failed order creation flow due to insufficient stock is as follows:

1. `Order Service` creates a new Order in the PENDING_APPROVAL state and publishes an `order-created` event.

2. `Order Line Item Service` consumes the `order-created` event, extracts the list of products and quantities from the order payload, and persists them into the `Order Line Item Table`.

3. `Product Service` consumes the `order-created` event, checks the availability of all products in the order, and detects that one or more products have insufficient stock. It does not reserve any stock and publishes a `stock-reservation-failed` event.

4. `Payment Service` consumes the `order-created` event and creates a Payment record in a PENDING state.

5. `Payment Service` consumes the `stock-reservation-failed` event and cancels the pending payment request. It then publishes a `payment-cancelled` event (reason: INSUFFICIENT_STOCK).

6. `Order Service` consumes the `stock-reservation-failed` event and changes the Order state to REJECTED. It then publishes an `order-rejected` event.

## Payment Failed

The failed order creation flow due to payment authorization failure is as follows:

1. `Order Service` creates a new Order in the PENDING_APPROVAL state and publishes an `order-created` event.

2. `Order Line Item Service` consumes the `order-created` event, extracts the list of products and quantities from the order payload, and persists them into the `Order Line Item Table`.

3. `Product Service` consumes the `order-created` event, verifies that there is sufficient stock for all products referenced in the order, reduces the stock, and publishes a `stock-reserved` event.

4. `Payment Service` consumes the `order-created` event and creates a Payment record in a PENDING state.

5. `Payment Service` consumes the `stock-reserved` event, attempts to authorize the payment, but the payment authorization fails (e.g., due to insufficient funds or card error). It then publishes a `payment-failed` event.

6. `Product Service` consumes the `payment-failed` event and releases any previously reserved stock.

7. `Order Service` consumes the `payment-failed` event, changes the Order state to REJECTED, and publishes an `order-rejected` event.