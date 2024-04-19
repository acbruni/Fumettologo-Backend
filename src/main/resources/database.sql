INSERT INTO comic (id, title, author, publisher, category, price, quantity, version, image)
VALUES
    (1, 'Naruto 1', 'Masashi Kishimoto', 'Planet Manga', 'Manga', 5.99, 50, 1, 'images/naruto1.jpg'),
    (2, 'Naruto 2', 'Masashi Kishimoto', 'Planet Manga', 'Manga', 5.99, 50, 1, 'images/naruto2.jpg'),
    (3, 'Naruto 3', 'Masashi Kishimoto', 'Planet Manga', 'Manga', 5.99, 50, 1, 'images/naruto3.jpg'),
    (4, 'Naruto 4', 'Masashi Kishimoto', 'Planet Manga', 'Manga', 5.99, 50, 1, 'images/naruto4.jpg'),
    (5, 'Naruto 5', 'Masashi Kishimoto', 'Planet Manga', 'Manga', 5.99, 50, 1, 'images/naruto5.jpg'),
    (6, 'Solo Leveling 1', 'Chugong', 'J-Pop', 'Manhwa', 9.90, 35, 1, 'images/solo_leveling1.jpg'),
    (7, 'Solo Leveling 2', 'Chugong', 'J-Pop', 'Manhwa', 9.90, 35, 1, 'images/solo_leveling2.jpg'),
    (8, 'Solo Leveling 5', 'Chugong', 'J-Pop', 'Manhwa', 9.90, 35, 1, 'images/solo_leveling5.jpg'),
    (9, 'Attacco dei Giganti 1', 'Hajime Isayama', 'Planet Manga', 'Manga', 4.65, 45, 1, 'images/aot1.jpg'),
    (10, 'Attacco dei Giganti 8', 'Hajime Isayama', 'Planet Manga', 'Manga', 4.65, 45, 1, 'images/aot8.jpg'),
    (11, 'Attacco dei Giganti 29', 'Hajime Isayama', 'Planet Manga', 'Manga', 4.65, 45, 1, 'images/aot29.jpg'),
    (12, 'Attacco dei Giganti 31', 'Hajime Isayama', 'Planet Manga', 'Manga', 4.65, 45, 1, 'images/aot31.jpg'),
    (13, 'Attacco dei Giganti 34', 'Hajime Isayama', 'Planet Manga', 'Manga', 4.66, 45, 1, 'images/aot34.jpg'),
    (14, 'One Piece 100', 'Eiichiro Oda', 'Planet Manga', 'Manga', 4.90, 60, 1, 'images/one_piece100.jpg'),
    (15, 'My Hero Academia 1', 'Kohei Horikoshi', 'Planet Manga', 'Manga', 4.90, 55, 1, 'images/my_hero_academia1.jpg'),
    (16, 'Bleach 3', 'Tite Kubo', 'Planet Manga', 'Manga', 5.80, 39, 1, 'images/bleach3.jpg'),
    (17, 'Bleach 72', 'Tite Kubo', 'Planet Manga', 'Manga', 5.80, 39, 1, 'images/bleach72.jpg'),
    (18, 'Chainsaw Man 1', 'Tatsuki Fujimoto', 'Planet Manga', 'Manga', 5.20, 60, 1, 'images/cm1.jpg'),
    (19, 'Chainsaw Man 2', 'Tatsuki Fujimoto', 'Planet Manga', 'Manga', 5.20, 60, 1, 'images/cm2.jpg'),
    (20, 'Chainsaw Man 3', 'Tatsuki Fujimoto', 'Planet Manga', 'Manga', 5.20, 60, 1, 'images/cm3.jpg'),
    (21, 'Chainsaw Man 4', 'Tatsuki Fujimoto', 'Planet Manga', 'Manga', 5.20, 60, 1, 'images/cm4.jpg'),
    (22, 'Chainsaw Man 5', 'Tatsuki Fujimoto', 'Planet Manga', 'Manga', 5.20, 60, 1, 'images/cm5.jpg'),
    (23, 'Dragon Ball 1', 'Akira Toriyama', 'Star Comics', 'Manga', 4.99, 60, 1, 'images/db1.jpg'),
    (24, 'Dragon Ball 2', 'Akira Toriyama', 'Star Comics', 'Manga', 4.99, 60, 1, 'images/db2.jpg'),
    (25, 'Dragon Ball 3', 'Akira Toriyama', 'Star Comics', 'Manga', 4.99, 60, 1, 'images/db3.jpg'),
    (26, 'Dragon Ball 4', 'Akira Toriyama', 'Star Comics', 'Manga', 4.99, 60, 1, 'images/db4.jpg'),
    (27, 'Dragon Ball 5', 'Akira Toriyama', 'Star Comics', 'Manga', 4.99, 60, 1, 'images/db5.jpg'),
    (28, 'Fullmetal Alchemist 1', 'Hiromo Arakawa', 'Planet Manga', 'Manga', 11.40, 10, 1, 'images/fm1.jpg'),
    (29, 'Fullmetal Alchemist 18', 'Hiromo Arakawa', 'Planet Manga', 'Manga', 11.40, 10, 1, 'images/fm18.jpg'),
    (30, 'Tokyo Ghoul 1', 'Sui Ishida', 'JPOP', 'Manga', 6.70, 20, 1, 'images/tg1.jpg'),
    (31, 'Tokyo Ghoul 2', 'Sui Ishida', 'JPOP', 'Manga', 6.70, 20, 1, 'images/tg2.jpg'),
    (32, 'Tokyo Ghoul 7', 'Sui Ishida', 'JPOP', 'Manga', 6.70, 20, 1, 'images/tg7.jpg');

INSERT INTO users (id, address, email, first_name, last_name, phone)
VALUES
    (10000, 'Catalimiti', 'anna.c.bruni.23@gmail.com', 'Anna Chiara', 'Bruni', '3486801242');

INSERT INTO carts (id, user_id)
VALUES
    (8000, 10000);

delete from comic;

delete from order_details;

delete from cart_details;