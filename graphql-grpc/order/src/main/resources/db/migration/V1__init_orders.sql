CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        user_id VARCHAR(255) NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
                             id UUID PRIMARY KEY,
                             order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                             medication_id VARCHAR(255) NOT NULL,
                             quantity INT NOT NULL
);
