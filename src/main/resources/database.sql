INSERT INTO comic (id, title, author, publisher, category, price, quantity, version)
VALUES
    (2, 'Watchmen', 'Alan Moore', 'DC Comics', 'Graphic Novel', 24.99, 30, 1),
    (3, 'Saga', 'Brian K. Vaughan', 'Image Comics', 'Fantasy', 14.99, 40, 1),
    (4, 'Maus', 'Art Spiegelman', 'Pantheon Books', 'Historical Fiction', 15.99, 25, 1),
    (5, 'Sandman', 'Neil Gaiman', 'DC Comics', 'Fantasy', 29.99, 20, 1);

INSERT INTO carts (id, user_id)
VALUES
    (4, 1),
    (3, 1),
    (5, 2);

INSERT INTO cart_details (id, comic, price, quantity, cart, subtotal, version)
VALUES
    (1, 2, 24.99, 1, 1, 24.99, 1),
    (2, 3, 14.99, 2, 1, 29.98, 1),
    (4, 5, 29.99, 1, 2, 29.99, 1);

INSERT INTO orders (id, create_time, user_id, total)
VALUES
    (1, '2024-04-11 10:00:00', 1, 24.99),
    (2, '2024-04-11 11:30:00', 2, 39.97);

INSERT INTO order_details (id, comic, price, quantity, purchase, subtotal)
VALUES
    (1, 2, 24.99, 1, 1, 24.99),
    (2, 3, 14.99, 2, 1, 29.98),
    (4, 5, 29.99, 1, 3, 29.99);

INSERT INTO users (id, first_name, last_name, email, address, phone)
VALUES
    (1, 'John', 'Doe', 'john.doe@example.com', '123 Main St', '123-456-7890'),
    (2, 'Jane', 'Smith', 'jane.smith@example.com', '456 Elm St', '987-654-3210');
