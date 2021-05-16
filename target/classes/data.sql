
INSERT INTO Users (username, password) VALUES
  ('Stephen Gary Wozniak', '11081950'),
  ('James Gosling', '19051955'),
  ('Vlad Shmunis', 'RingZeroSystems'),
  ('Elon Reeve Musk', '28061971'),
  ('Timothy Donald Cook', '01111960'),
  ('William Henry Gates III', '28101955'),
  ('Mark Elliot Zuckerberg', '14051984');

INSERT INTO Phone_Number (phonenumber, username, user_id) VALUES
  ('+1(932)197-53-55','Stephen Gary Wozniak', 1),
  ('+1(159)496-55-77','James Gosling', 2),
  ('+1(996)989-30-09','Vlad Shmunis', 3),
  ('+1(635)137-96-84','Elon Reeve Musk', 4),
  ('+1(221)229-74-81','Timothy Donald Cook', 5),
  ('+1(895)004-43-97','William Henry Gates III', 6),
  ('+1(069)449-52-98','Mark Elliot Zuckerberg', 7);

INSERT INTO Phone_Book (user_id, phone_number_id) VALUES
  (1, 2),
  (1, 5),
  (2, 6),
  (3, 4),
  (3, 7),
  (3, 5),
  (4, 3),
  (4, 5),
  (5, 2),
  (5, 6),
  (5, 1),
  (6, 2),
  (6, 4),
  (7, 3);

