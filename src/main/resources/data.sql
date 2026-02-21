-- Brands
INSERT INTO brands (name, description, image_url, created_at, updated_at) VALUES
('Apple', 'Premium Consumer Electronics', 'https://placehold.co/100x100/1e293b/ffffff?text=Apple', NOW(), NOW()),
('Samsung', 'Global Electronics Giant', 'https://placehold.co/100x100/1e3a8a/ffffff?text=Samsung', NOW(), NOW()),
('Nike', 'Performance Footwear & Apparel', 'https://placehold.co/100x100/0f172a/ffffff?text=Nike', NOW(), NOW()),
('Lays', 'Potato Chips & Snacks', 'https://placehold.co/100x100/fca5a5/7f1d1d?text=Lays', NOW(), NOW()),
('Haldirams', 'Traditional Indian Snacks', 'https://placehold.co/100x100/fed7aa/7c2d12?text=Haldirams', NOW(), NOW()),
('Kelloggs', 'Breakfast Cereals', 'https://placehold.co/100x100/fde047/854d0e?text=Kelloggs', NOW(), NOW()),
('Nescafe', 'Instant Coffee', 'https://placehold.co/100x100/78350f/ffffff?text=Nescafe', NOW(), NOW()),
('FreshFarm', 'Organic Fruits & Vegetables', 'https://placehold.co/100x100/22c55e/ffffff?text=FreshFarm', NOW(), NOW()),
('Dove', 'Personal Care & Beauty', 'https://placehold.co/100x100/6366f1/ffffff?text=Dove', NOW(), NOW()),
('Nivea', 'Skin Care & Grooming', 'https://placehold.co/100x100/1d4ed8/ffffff?text=Nivea', NOW(), NOW()),
('LOreal', 'Hair & Makeup Expert', 'https://placehold.co/100x100/be185d/ffffff?text=LOreal', NOW(), NOW()),
('Maybelline', 'Cosmetics & Makeup', 'https://placehold.co/100x100/db2777/ffffff?text=Maybelline', NOW(), NOW()),
('Colgate', 'Oral Hygiene', 'https://placehold.co/100x100/dc2626/ffffff?text=Colgate', NOW(), NOW()),
('Gillette', 'Mens Grooming Expert', 'https://placehold.co/100x100/2563eb/ffffff?text=Gillette', NOW(), NOW()),
('Axe', 'Fragrance & Deodorants', 'https://placehold.co/100x100/171717/ffffff?text=Axe', NOW(), NOW()),
('Whisper', 'Feminine Hygiene', 'https://placehold.co/100x100/ec4899/ffffff?text=Whisper', NOW(), NOW()),
('The Body Shop', 'Nature-Inspired Beauty', 'https://placehold.co/100x100/065f46/ffffff?text=BodyShop', NOW(), NOW()),
-- New Brands
('Amul', 'The Taste of India', 'https://placehold.co/100x100/ef4444/ffffff?text=Amul', NOW(), NOW()),
('Britannia', 'Healthy & Tasty', 'https://placehold.co/100x100/b91c1c/ffffff?text=Britannia', NOW(), NOW()),
('Surf Excel', 'Detergent Brand', 'https://placehold.co/100x100/1d4ed8/ffffff?text=SurfExcel', NOW(), NOW()),
('Pampers', 'Baby Care', 'https://placehold.co/100x100/0ea5e9/ffffff?text=Pampers', NOW(), NOW()),
('Coca-Cola', 'Soft Drinks', 'https://placehold.co/100x100/dc2626/ffffff?text=CocaCola', NOW(), NOW()),
('Pedigree', 'Pet Food', 'https://placehold.co/100x100/f59e0b/ffffff?text=Pedigree', NOW(), NOW());

-- Categories (Root)
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES
('Electronics', 'Gadgets and Devices', 'https://placehold.co/100x100/3b82f6/ffffff?text=Electronics', NULL, NOW(), NOW()),
('Fashion', 'Clothing and Accessories', 'https://placehold.co/100x100/ec4899/ffffff?text=Fashion', NULL, NOW(), NOW()),
('Snacks & Munchies', 'Chips, Nuts, and Savory Snacks', 'https://placehold.co/100x100/f97316/ffffff?text=Snacks', NULL, NOW(), NOW()),
('Breakfast Essentials', 'Cereals, Coffee, and Oats', 'https://placehold.co/100x100/eab308/ffffff?text=Breakfast', NULL, NOW(), NOW()),
('Personal Care & Beauty', 'Hygiene, Makeup, and Wellness', 'https://placehold.co/100x100/a855f7/ffffff?text=Personal+Care', NULL, NOW(), NOW()),
('Fresh Produce', 'Fruits and Vegetables', 'https://placehold.co/100x100/22c55e/ffffff?text=Fresh+Produce', NULL, NOW(), NOW()),
-- New Root Categories
('Dairy, Bread & Eggs', 'Daily Essentials', 'https://placehold.co/100x100/facc15/ffffff?text=Dairy', NULL, NOW(), NOW()),
('Cold Drinks & Juices', 'Soft Drinks and Juices', 'https://placehold.co/100x100/ef4444/ffffff?text=Cold+Drinks', NULL, NOW(), NOW()),
('Biscuits & Bakery', 'Cookies, Cakes and Bread', 'https://placehold.co/100x100/78350f/ffffff?text=Bakery', NULL, NOW(), NOW()),
('Home & Cleaning', 'Detergents and Cleaners', 'https://placehold.co/100x100/06b6d4/ffffff?text=Home+Cleaning', NULL, NOW(), NOW()),
('Baby Care', 'Diapers and Baby Food', 'https://placehold.co/100x100/f472b6/ffffff?text=Baby+Care', NULL, NOW(), NOW()),
('Pet Care', 'Food and Accessories', 'https://placehold.co/100x100/84cc16/ffffff?text=Pet+Care', NULL, NOW(), NOW());


-- Categories (Sub)
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Smartphones', 'Mobile Phones', 'https://placehold.co/100x100/60a5fa/ffffff?text=Smartphones', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Electronics') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Laptops', 'Portable Computers', 'https://placehold.co/100x100/93c5fd/1e3a8a?text=Laptops', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Electronics') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Men''s Wear', 'Clothing for Men', 'https://placehold.co/100x100/f472b6/831843?text=Mens+Wear', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Fashion') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Chips & Crisps', 'Potato Chips and Nachos', 'https://placehold.co/100x100/fdba74/9a3412?text=Chips', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Snacks & Munchies') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Beverages', 'Coffee and Tea', 'https://placehold.co/100x100/78350f/ffffff?text=Beverages', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Breakfast Essentials') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Cereals', 'Flakes and Oats', 'https://placehold.co/100x100/fde047/854d0e?text=Cereals', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Breakfast Essentials') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Fruits', 'Fresh Seasonal Fruits', 'https://placehold.co/100x100/86efac/14532d?text=Fruits', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Fresh Produce') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Skin Care', 'Moisturizers, Serums, Face Wash', 'https://placehold.co/100x100/fbcfe8/be185d?text=Skin+Care', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Personal Care & Beauty') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Hair Care', 'Shampoos, Conditioners, Oils', 'https://placehold.co/100x100/e9d5ff/6b21a8?text=Hair+Care', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Personal Care & Beauty') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Bath & Body', 'Body Wash, Soaps, Scrubs', 'https://placehold.co/100x100/c7d2fe/4338ca?text=Bath+Body', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Personal Care & Beauty') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Makeup & Cosmetics', 'Lipsticks, Foundations, Mascaras', 'https://placehold.co/100x100/fecdd3/9f1239?text=Makeup', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Personal Care & Beauty') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Fragrances & Deos', 'Perfumes, Deodorants, Body Mists', 'https://placehold.co/100x100/ddd6fe/5b21b6?text=Fragrances', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Personal Care & Beauty') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Oral Care', 'Toothpaste, Brushes, Mouthwash', 'https://placehold.co/100x100/bae6fd/0369a1?text=Oral+Care', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Personal Care & Beauty') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Men''s Grooming', 'Shaving, Beard Care, Face Wash', 'https://placehold.co/100x100/94a3b8/1e293b?text=Mens+Grooming', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Personal Care & Beauty') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Feminine Hygiene', 'Pads, Tampons, Intimate Wash', 'https://placehold.co/100x100/fce7f3/db2777?text=Feminine', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Personal Care & Beauty') AS temp), NOW(), NOW());
-- New Subcategories
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Milk', 'Fresh Milk', 'https://placehold.co/100x100/fde047/ffffff?text=Milk', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Dairy, Bread & Eggs') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Cheese & Butter', 'Processing Cheese and Butter', 'https://placehold.co/100x100/facc15/854d0e?text=Cheese', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Dairy, Bread & Eggs') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Bread', 'Whole Wheat and White Bread', 'https://placehold.co/100x100/d97706/ffffff?text=Bread', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Dairy, Bread & Eggs') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Soft Drinks', 'Cola and Sodas', 'https://placehold.co/100x100/b91c1c/ffffff?text=Soft+Drinks', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Cold Drinks & Juices') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Juices', 'Fruit Juices', 'https://placehold.co/100x100/fbbf24/ffffff?text=Juices', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Cold Drinks & Juices') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Cookies', 'Choco Chip and Oats', 'https://placehold.co/100x100/78350f/ffffff?text=Cookies', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Biscuits & Bakery') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Detergents', 'Washing Powder', 'https://placehold.co/100x100/3b82f6/ffffff?text=Detergents', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Home & Cleaning') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Dishwashing', 'Bar and Liquid', 'https://placehold.co/100x100/10b981/ffffff?text=Dishwash', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Home & Cleaning') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Diapers', 'Baby Diapers', 'https://placehold.co/100x100/f472b6/ffffff?text=Diapers', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Baby Care') AS temp), NOW(), NOW());
INSERT INTO categories (name, description, image_url, parent_id, created_at, updated_at) VALUES ('Dog Food', 'Tastiest Food for Dogs', 'https://placehold.co/100x100/a16207/ffffff?text=Dog+Food', (SELECT category_id FROM (SELECT category_id FROM categories WHERE name = 'Pet Care') AS temp), NOW(), NOW());


-- Products
INSERT INTO products (name, description, price, stock, discount_price, selling_quantity_value, selling_quantity_unit, rating, brand_id, category_id, created_at, updated_at) VALUES
-- Electronics (Prices in high thousands/lakhs INR)
('iPhone 15 Pro', 'Titanium design, A17 Pro chip', 134900.00, 50, 129900.00, 1.0, 'PIECE', 4.8, (SELECT brand_id FROM brands WHERE name = 'Apple'), (SELECT category_id FROM categories WHERE name = 'Smartphones'), NOW(), NOW()),
('Samsung Galaxy S24', 'AI-powered smartphone', 79999.00, 75, 74999.00, 1.0, 'PIECE', 4.7, (SELECT brand_id FROM brands WHERE name = 'Samsung'), (SELECT category_id FROM categories WHERE name = 'Smartphones'), NOW(), NOW()),
('MacBook Air M3', 'Supercharged by M3', 114900.00, 30, 104900.00, 1.0, 'PIECE', 4.9, (SELECT brand_id FROM brands WHERE name = 'Apple'), (SELECT category_id FROM categories WHERE name = 'Laptops'), NOW(), NOW()),
-- Fashion (Prices in thousands INR)
('Nike Air Max', 'Comfortable running shoes', 8995.00, 100, 7595.00, 1.0, 'PIECE', 4.5, (SELECT brand_id FROM brands WHERE name = 'Nike'), (SELECT category_id FROM categories WHERE name = 'Men''s Wear'), NOW(), NOW()),
-- Snacks (Prices in INR)
('Lays Classic Salted', 'Crispy Potato Chips', 20.00, 200, 18.00, 50.0, 'GRAM', 4.6, (SELECT brand_id FROM brands WHERE name = 'Lays'), (SELECT category_id FROM categories WHERE name = 'Chips & Crisps'), NOW(), NOW()),
('Haldirams Aloo Bhujia', 'Spicy Potato Noodles', 50.00, 150, 45.00, 200.0, 'GRAM', 4.8, (SELECT brand_id FROM brands WHERE name = 'Haldirams'), (SELECT category_id FROM categories WHERE name = 'Chips & Crisps'), NOW(), NOW()),
-- Breakfast
('Kelloggs Corn Flakes', 'Toasted Corn Cereal', 320.00, 80, 290.00, 475.0, 'GRAM', 4.4, (SELECT brand_id FROM brands WHERE name = 'Kelloggs'), (SELECT category_id FROM categories WHERE name = 'Cereals'), NOW(), NOW()),
('Nescafe Classic Coffee', '100% Pure Instant Coffee', 650.00, 60, 580.00, 100.0, 'GRAM', 4.6, (SELECT brand_id FROM brands WHERE name = 'Nescafe'), (SELECT category_id FROM categories WHERE name = 'Beverages'), NOW(), NOW()),
-- Fresh
('Fresh Red Apples', 'Sweet and Crunchy Apples', 180.00, 50, 160.00, 1.0, 'KILOGRAM', 4.8, (SELECT brand_id FROM brands WHERE name = 'FreshFarm'), (SELECT category_id FROM categories WHERE name = 'Fruits'), NOW(), NOW()),
-- Skin Care
('Nivea Soft Cream', 'Light Moisturizer for Face, Hand & Body', 399.00, 120, 299.00, 200.0, 'MILLILITRE', 4.5, (SELECT brand_id FROM brands WHERE name = 'Nivea'), (SELECT category_id FROM categories WHERE name = 'Skin Care'), NOW(), NOW()),
('Garnier Vitamin C Serum', 'Brightening Serum', 549.00, 80, 440.00, 30.0, 'MILLILITRE', 4.3, (SELECT brand_id FROM brands WHERE name = 'LOreal'), (SELECT category_id FROM categories WHERE name = 'Skin Care'), NOW(), NOW()),
-- Hair Care
('LOreal Total Repair 5', 'Shampoo for Damaged Hair', 750.00, 100, 599.00, 650.0, 'MILLILITRE', 4.6, (SELECT brand_id FROM brands WHERE name = 'LOreal'), (SELECT category_id FROM categories WHERE name = 'Hair Care'), NOW(), NOW()),
('Dove Intense Repair', 'Conditioner for Smooth Hair', 250.00, 100, 210.00, 180.0, 'MILLILITRE', 4.5, (SELECT brand_id FROM brands WHERE name = 'Dove'), (SELECT category_id FROM categories WHERE name = 'Hair Care'), NOW(), NOW()),
-- Bath & Body
('Dove Body Wash', 'Deeply Nourishing Body Wash', 699.00, 90, 549.00, 800.0, 'MILLILITRE', 4.7, (SELECT brand_id FROM brands WHERE name = 'Dove'), (SELECT category_id FROM categories WHERE name = 'Bath & Body'), NOW(), NOW()),
('Body Shop Strawberry Shower Gel', 'Soap-Free Shower Gel', 895.00, 50, 895.00, 250.0, 'MILLILITRE', 4.8, (SELECT brand_id FROM brands WHERE name = 'The Body Shop'), (SELECT category_id FROM categories WHERE name = 'Bath & Body'), NOW(), NOW()),
-- Makeup
('Maybelline Colossal Kajal', '12H Smudge Proof Black', 199.00, 200, 159.00, 1.0, 'PIECE', 4.4, (SELECT brand_id FROM brands WHERE name = 'Maybelline'), (SELECT category_id FROM categories WHERE name = 'Makeup & Cosmetics'), NOW(), NOW()),
('Maybelline Matte Eraser', 'Liquid Foundation', 799.00, 60, 639.00, 30.0, 'MILLILITRE', 4.3, (SELECT brand_id FROM brands WHERE name = 'Maybelline'), (SELECT category_id FROM categories WHERE name = 'Makeup & Cosmetics'), NOW(), NOW()),
-- Fragrances
('Axe Dark Temptation', 'Chocolate Scent Deodorant', 240.00, 150, 195.00, 150.0, 'MILLILITRE', 4.2, (SELECT brand_id FROM brands WHERE name = 'Axe'), (SELECT category_id FROM categories WHERE name = 'Fragrances & Deos'), NOW(), NOW()),
('Nivea Men Fresh Active', '48h Protection Deo', 225.00, 150, 180.00, 150.0, 'MILLILITRE', 4.4, (SELECT brand_id FROM brands WHERE name = 'Nivea'), (SELECT category_id FROM categories WHERE name = 'Fragrances & Deos'), NOW(), NOW()),
-- Oral Care
('Colgate Strong Teeth', 'Calcium Boost Toothpaste', 110.00, 250, 95.00, 200.0, 'GRAM', 4.6, (SELECT brand_id FROM brands WHERE name = 'Colgate'), (SELECT category_id FROM categories WHERE name = 'Oral Care'), NOW(), NOW()),
('Colgate ZigZag Brush', 'Medium Bristles Pack of 3', 85.00, 200, 75.00, 1.0, 'PACK', 4.5, (SELECT brand_id FROM brands WHERE name = 'Colgate'), (SELECT category_id FROM categories WHERE name = 'Oral Care'), NOW(), NOW()),
-- Mens Grooming
('Gillette Mach3 Razor', '3 Blade System Razor', 375.00, 100, 325.00, 1.0, 'PIECE', 4.7, (SELECT brand_id FROM brands WHERE name = 'Gillette'), (SELECT category_id FROM categories WHERE name = 'Men''s Grooming'), NOW(), NOW()),
('Nivea Men Shaving Foam', 'Sensitive Skin Shave Foam', 249.00, 80, 219.00, 200.0, 'MILLILITRE', 4.5, (SELECT brand_id FROM brands WHERE name = 'Nivea'), (SELECT category_id FROM categories WHERE name = 'Men''s Grooming'), NOW(), NOW()),
-- Feminine Hygiene
('Whisper Ultra Clean', 'XL Wings Sanitary Pads', 450.00, 100, 399.00, 30.0, 'PIECE', 4.8, (SELECT brand_id FROM brands WHERE name = 'Whisper'), (SELECT category_id FROM categories WHERE name = 'Feminine Hygiene'), NOW(), NOW()),
-- New Products: Dairy
('Amul Gold Milk', 'Full Cream Milk', 34.00, 100, 34.00, 500.0, 'MILLILITRE', 4.9, (SELECT brand_id FROM brands WHERE name = 'Amul'), (SELECT category_id FROM categories WHERE name = 'Milk'), NOW(), NOW()),
('Amul Butter', 'Pasteurized Salted Butter', 56.00, 100, 54.00, 100.0, 'GRAM', 4.8, (SELECT brand_id FROM brands WHERE name = 'Amul'), (SELECT category_id FROM categories WHERE name = 'Cheese & Butter'), NOW(), NOW()),
('Amul Cheese Slices', 'Processed Cheese', 145.00, 50, 135.00, 200.0, 'GRAM', 4.7, (SELECT brand_id FROM brands WHERE name = 'Amul'), (SELECT category_id FROM categories WHERE name = 'Cheese & Butter'), NOW(), NOW()),
('Britannia Whole Wheat Bread', '100% Atta Bread', 55.00, 80, 50.00, 450.0, 'GRAM', 4.6, (SELECT brand_id FROM brands WHERE name = 'Britannia'), (SELECT category_id FROM categories WHERE name = 'Bread'), NOW(), NOW()),
-- New Products: Drinks
('Coca-Cola Original', 'Carbonated Water', 40.00, 200, 38.00, 750.0, 'MILLILITRE', 4.5, (SELECT brand_id FROM brands WHERE name = 'Coca-Cola'), (SELECT category_id FROM categories WHERE name = 'Soft Drinks'), NOW(), NOW()),
('Tropicana Mixed Fruit', '100% Juice', 120.00, 60, 110.00, 1.0, 'LITRE', 4.4, (SELECT brand_id FROM brands WHERE name = 'Apple'), (SELECT category_id FROM categories WHERE name = 'Juices'), NOW(), NOW()),
-- New Products: Bakery
('Britannia Good Day', 'Cashew Cookies', 30.00, 150, 25.00, 100.0, 'GRAM', 4.7, (SELECT brand_id FROM brands WHERE name = 'Britannia'), (SELECT category_id FROM categories WHERE name = 'Cookies'), NOW(), NOW()),
-- New Products: Cleaning
('Surf Excel Easy Wash', 'Detergent Powder', 155.00, 80, 145.00, 1.0, 'KILOGRAM', 4.6, (SELECT brand_id FROM brands WHERE name = 'Surf Excel'), (SELECT category_id FROM categories WHERE name = 'Detergents'), NOW(), NOW()),
('Vim Bar', 'Dishwash Bar', 15.00, 200, 12.00, 150.0, 'GRAM', 4.5, (SELECT brand_id FROM brands WHERE name = 'Surf Excel'), (SELECT category_id FROM categories WHERE name = 'Dishwashing'), NOW(), NOW()),
-- New Products: Baby
('Pampers Pants L', 'Large Diaper Pants', 1499.00, 40, 1199.00, 30.0, 'PIECE', 4.8, (SELECT brand_id FROM brands WHERE name = 'Pampers'), (SELECT category_id FROM categories WHERE name = 'Diapers'), NOW(), NOW()),
-- New Products: Pet
('Pedigree Adult Chicken', 'Dog Food with Chicken', 350.00, 30, 310.00, 1.2, 'KILOGRAM', 4.7, (SELECT brand_id FROM brands WHERE name = 'Pedigree'), (SELECT category_id FROM categories WHERE name = 'Dog Food'), NOW(), NOW());


-- Product Images
INSERT INTO product_images (product_id, image_url) VALUES
((SELECT product_id FROM products WHERE name = 'iPhone 15 Pro'), 'https://placehold.co/100x100/60a5fa/ffffff?text=iPhone+15'),
((SELECT product_id FROM products WHERE name = 'Samsung Galaxy S24'), 'https://placehold.co/100x100/1e3a8a/ffffff?text=Galaxy+S24'),
((SELECT product_id FROM products WHERE name = 'MacBook Air M3'), 'https://placehold.co/100x100/94a3b8/ffffff?text=MacBook'),
((SELECT product_id FROM products WHERE name = 'Nike Air Max'), 'https://placehold.co/100x100/ec4899/ffffff?text=Nike+Shoes'),
((SELECT product_id FROM products WHERE name = 'Lays Classic Salted'), 'https://placehold.co/100x100/fca5a5/7f1d1d?text=Lays+Salted'),
((SELECT product_id FROM products WHERE name = 'Haldirams Aloo Bhujia'), 'https://placehold.co/100x100/fed7aa/7c2d12?text=Bhujia'),
((SELECT product_id FROM products WHERE name = 'Kelloggs Corn Flakes'), 'https://placehold.co/100x100/fde047/854d0e?text=Corn+Flakes'),
((SELECT product_id FROM products WHERE name = 'Nescafe Classic Coffee'), 'https://placehold.co/100x100/78350f/ffffff?text=Coffee'),
((SELECT product_id FROM products WHERE name = 'Fresh Red Apples'), 'https://placehold.co/100x100/ef4444/ffffff?text=Apples'),
((SELECT product_id FROM products WHERE name = 'Nivea Soft Cream'), 'https://placehold.co/100x100/1d4ed8/ffffff?text=Nivea+Soft'),
((SELECT product_id FROM products WHERE name = 'Garnier Vitamin C Serum'), 'https://placehold.co/100x100/fef08a/854d0e?text=Garnier+Serum'),
((SELECT product_id FROM products WHERE name = 'LOreal Total Repair 5'), 'https://placehold.co/100x100/be185d/ffffff?text=Loreal+Shampoo'),
((SELECT product_id FROM products WHERE name = 'Dove Intense Repair'), 'https://placehold.co/100x100/6366f1/ffffff?text=Dove+Conditioner'),
((SELECT product_id FROM products WHERE name = 'Dove Body Wash'), 'https://placehold.co/100x100/818cf8/ffffff?text=Dove+BodyWash'),
((SELECT product_id FROM products WHERE name = 'Body Shop Strawberry Shower Gel'), 'https://placehold.co/100x100/be123c/ffffff?text=Shower+Gel'),
((SELECT product_id FROM products WHERE name = 'Maybelline Colossal Kajal'), 'https://placehold.co/100x100/fb7185/881337?text=Kajal'),
((SELECT product_id FROM products WHERE name = 'Maybelline Matte Eraser'), 'https://placehold.co/100x100/fecdd3/9f1239?text=Foundation'),
((SELECT product_id FROM products WHERE name = 'Axe Dark Temptation'), 'https://placehold.co/100x100/262626/ffffff?text=Axe+Deo'),
((SELECT product_id FROM products WHERE name = 'Nivea Men Fresh Active'), 'https://placehold.co/100x100/3b82f6/ffffff?text=Nivea+Deo'),
((SELECT product_id FROM products WHERE name = 'Colgate Strong Teeth'), 'https://placehold.co/100x100/dc2626/ffffff?text=Colgate+Paste'),
((SELECT product_id FROM products WHERE name = 'Colgate ZigZag Brush'), 'https://placehold.co/100x100/ef4444/ffffff?text=Toothbrush'),
((SELECT product_id FROM products WHERE name = 'Gillette Mach3 Razor'), 'https://placehold.co/100x100/1d4ed8/ffffff?text=Razor'),
((SELECT product_id FROM products WHERE name = 'Nivea Men Shaving Foam'), 'https://placehold.co/100x100/60a5fa/ffffff?text=Shaving+Foam'),
((SELECT product_id FROM products WHERE name = 'Whisper Ultra Clean'), 'https://placehold.co/100x100/f472b6/831843?text=Sanitary+Pads'),
-- New Images
((SELECT product_id FROM products WHERE name = 'Amul Gold Milk'), 'https://placehold.co/100x100/facc15/ffffff?text=Amul+Milk'),
((SELECT product_id FROM products WHERE name = 'Amul Butter'), 'https://placehold.co/100x100/facc15/ffffff?text=Butter'),
((SELECT product_id FROM products WHERE name = 'Amul Cheese Slices'), 'https://placehold.co/100x100/facc15/ffffff?text=Cheese'),
((SELECT product_id FROM products WHERE name = 'Britannia Whole Wheat Bread'), 'https://placehold.co/100x100/b91c1c/ffffff?text=Bread'),
((SELECT product_id FROM products WHERE name = 'Coca-Cola Original'), 'https://placehold.co/100x100/000000/ffffff?text=Coke'),
((SELECT product_id FROM products WHERE name = 'Tropicana Mixed Fruit'), 'https://placehold.co/100x100/f59e0b/ffffff?text=Juice'),
((SELECT product_id FROM products WHERE name = 'Britannia Good Day'), 'https://placehold.co/100x100/b91c1c/ffffff?text=Cookies'),
((SELECT product_id FROM products WHERE name = 'Surf Excel Easy Wash'), 'https://placehold.co/100x100/2563eb/ffffff?text=SurfExcel'),
((SELECT product_id FROM products WHERE name = 'Vim Bar'), 'https://placehold.co/100x100/22c55e/ffffff?text=VimBar'),
((SELECT product_id FROM products WHERE name = 'Pampers Pants L'), 'https://placehold.co/100x100/0ea5e9/ffffff?text=Diapers'),
((SELECT product_id FROM products WHERE name = 'Pedigree Adult Chicken'), 'https://placehold.co/100x100/facc15/ffffff?text=Pedigree');
