-- Initialisation des transporteurs
INSERT INTO carriers (id, code, name) VALUES
    (gen_random_uuid(), 'CARRIER-DPD', 'DPD'),
    (gen_random_uuid(), 'CARRIER-CHRONOPOST', 'Chronopost');

-- Initialisation des traductions des transporteurs
INSERT INTO carrier_translation (id, carrier_id, code, label, locale) 
SELECT 
    gen_random_uuid(),
    c.id,
    c.code,
    CASE 
        WHEN c.code = 'CARRIER-DPD' THEN 'DPD Express'
        WHEN c.code = 'CARRIER-CHRONOPOST' THEN 'Chronopost Express'
    END,
    'fr'
FROM carriers c
UNION ALL
SELECT 
    gen_random_uuid(),
    c.id,
    c.code,
    CASE 
        WHEN c.code = 'CARRIER-DPD' THEN 'DPD Express'
        WHEN c.code = 'CARRIER-CHRONOPOST' THEN 'Chronopost Express'
    END,
    'en'
FROM carriers c;

-- Initialisation des entrepôts
INSERT INTO warehouse (id, code, name, address, marketplace) VALUES
    (gen_random_uuid(), 'WH-PARIS', 'Entrepôt Paris', '123 rue de Paris, 75001 Paris', 'FR'),
    (gen_random_uuid(), 'WH-LYON', 'Entrepôt Lyon', '456 rue de Lyon, 69001 Lyon', 'FR');

-- Initialisation des traductions des entrepôts
INSERT INTO warehouse_translation (id, warehouse_id, code, label, locale)
SELECT 
    gen_random_uuid(),
    w.id,
    w.code,
    CASE 
        WHEN w.code = 'WH-PARIS' THEN 'Paris Warehouse'
        WHEN w.code = 'WH-LYON' THEN 'Lyon Warehouse'
    END,
    'en'
FROM warehouse w
UNION ALL
SELECT 
    gen_random_uuid(),
    w.id,
    w.code,
    CASE 
        WHEN w.code = 'WH-PARIS' THEN 'Entrepôt de Paris'
        WHEN w.code = 'WH-LYON' THEN 'Entrepôt de Lyon'
    END,
    'fr'
FROM warehouse w;

-- Association des transporteurs aux entrepôts
INSERT INTO carrier_warehouse (carrier_id, warehouse_id)
SELECT c.id, w.id
FROM carriers c, warehouse w
WHERE c.code = 'CARRIER-DPD' AND w.code = 'WH-PARIS';

-- Initialisation des commandes
INSERT INTO fulfillment_orders (id, customer_id, status, created_at, updated_at) VALUES
    (gen_random_uuid(), gen_random_uuid(), 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), gen_random_uuid(), 'IN_PREPARATION', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), gen_random_uuid(), 'IN_DELIVERY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Initialisation des articles de commande
INSERT INTO order_item (id, fulfillment_order_id, product_id, quantity, price)
SELECT 
    gen_random_uuid(),
    fo.id,
    'PROD-' || generate_series(1, 3),
    2,
    29.99
FROM fulfillment_orders fo
CROSS JOIN generate_series(1, 3);

-- Initialisation des expéditions
INSERT INTO shipments (
    id, 
    fulfillment_order_id, 
    order_item_id,
    carrier_id,
    tracking_number,
    created_at,
    updated_at,
    carrier_code,
    tracking_url,
    shipping_price,
    currency
)
SELECT 
    gen_random_uuid(),
    fo.id,
    oi.id,
    c.id,
    'TRACK-' || oi.product_id,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    c.code,
    'https://tracking.example.com/' || oi.product_id,
    9.99,
    'EUR'
FROM fulfillment_orders fo
JOIN order_item oi ON oi.fulfillment_order_id = fo.id
JOIN carriers c ON c.code = 'CARRIER-DPD'
WHERE fo.status = 'IN_DELIVERY';

-- Initialisation des indicateurs d'expédition
INSERT INTO shipment_indicators (
    id,
    shipment_id,
    event_type,
    event_description,
    created_at,
    updated_at
)
SELECT 
    gen_random_uuid(),
    s.id,
    'IN_TRANSIT',
    'Colis en cours de livraison',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM shipments s; 