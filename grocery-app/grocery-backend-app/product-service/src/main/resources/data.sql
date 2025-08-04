CREATE TABLE IF NOT EXISTS products (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    product_category VARCHAR(255) NOT NULL,
    price FLOAT(53) NOT NULL,
    product_image_url VARCHAR(255) NOT NULL,
    brand_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id)
    );

INSERT INTO products
    (product_name, product_category, price, product_image_url, brand_name)
VALUES
  ('Organic Bananas', 'Fruits', 1.29, 'https://example.com/images/organic-bananas.jpg', 'Fresh Farms'),
 ('Almond Milk', 'Dairy Alternatives', 3.49, 'https://example.com/images/almond-milk.jpg', 'Nutty Co.'),
  ('Whole Wheat Bread', 'Bakery', 2.99, 'https://example.com/images/whole-wheat-bread.jpg', 'BakeHouse'),
('Free Range Eggs', 'Dairy', 4.50, 'https://example.com/images/free-range-eggs.jpg', 'Happy Hens'),
('Red Apples', 'Fruits', 2.20, 'https://example.com/images/red-apples.jpg', 'Orchard Best'),
('Greek Yogurt', 'Dairy', 5.00, 'https://example.com/images/greek-yogurt.jpg', 'Creamy Delight'),
 ('Brown Rice', 'Grains', 1.80, 'https://example.com/images/brown-rice.jpg', 'Healthy Grains'),
('Peanut Butter', 'Spreads', 3.99, 'https://example.com/images/peanut-butter.jpg', 'Nutty Co.'),
('Orange Juice', 'Beverages', 4.29, 'https://example.com/images/orange-juice.jpg', 'Citrus Fresh'),
('Carrots', 'Vegetables', 0.99, 'https://example.com/images/carrots.jpg', 'Veggie Farm');
