https://github.com/eperesite/java-filmorate/blob/main/Flowcharts.png

users: содержит информацию о пользователях, такие как ID, имя, email, логин, дата рождения и список друзей.
film: хранит данные о фильмах, включая ID, название, описание, жанры, рейтинг, дату выхода, продолжительность и количество лайков.
friends: отражает отношения дружбы между пользователями. У каждого пользователя может быть несколько друзей с указанием статуса дружбы.
likes: хранит информацию о том, какие пользователи "лайкнули" определённые фильмы.
geners (genres): содержит список всех жанров с их ID и названиями.
geners_film: это промежуточная таблица, которая связывает фильмы с жанрами.
rating: хранит информацию о рейтингах фильмов (например, MPAA) по названиям.

1. Получить всех пользователей
SELECT * FROM users;
2. Получить все фильмы с их жанрами
SELECT f.name, g.geners_name 
FROM film f
JOIN geners_film gf ON f.id = gf.filmId
JOIN geners g ON gf.genersId = g.genersId;
3. Найти всех друзей пользователя
SELECT u.name, f.status 
FROM friends f
JOIN users u ON f.friendId = u.id
WHERE f.userId = ?;
(где ? заменяется на ID пользователя)
4. Получить информацию о фильме и его рейтинге
SELECT f.name, f.description, r.rating_name 
FROM film f
JOIN rating r ON f.id = r.filmId
WHERE f.id = ?;
(где ? заменяется на ID фильма)
5. Найти самые популярные фильмы (по количеству лайков)
SELECT f.name, COUNT(l.userId) as likes_count
FROM film f
LEFT JOIN likes l ON f.id = l.filmId
GROUP BY f.id
ORDER BY likes_count DESC
LIMIT 10;
