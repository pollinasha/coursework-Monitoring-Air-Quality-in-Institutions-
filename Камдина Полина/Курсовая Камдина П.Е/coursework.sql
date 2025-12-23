--create database monitoring_quality;

create table buildings(
	id_building int primary key generated always as identity,
	address_building varchar not null unique,
	name_institution varchar not null unique,
	abbreviation_institution varchar(50)
);

create table rooms(
	id_room smallint primary key generated always as identity,
	number_room varchar not null unique,
	type_room varchar(20) not null check(type_room in('кабинет','актовый','столовая')),
	id_building smallint not null references buildings(id_building)
);

create table sensors(
	id_sensor int primary key generated always as identity,
	id_room smallint not null references rooms(id_room),
	type_sensor varchar(20) not null check(type_sensor in('co2','temp','humidity')),
	location_note varchar
);

create table measurements(
	id_measurement bigint primary key generated always as identity,
	id_sensor smallint references sensors(id_sensor),
	datetime timestamp not null,
	value numeric(10,3) not NULL 
);

create table params(
	id_param int primary key generated always as identity,
	param varchar(20) not null
);

create table standards(
	id_standard smallint primary key generated always as identity,
	id_room smallint not null references rooms(id_room),
	id_param smallint not null references params(id_param),
	max_value numeric(10,3) not null,
	min_value numeric(10,3) not null
);

create table alerts(
	id_alert int primary key generated always as identity,
	id_room smallint not null references rooms(id_room),
	id_param smallint not null references params(id_param),
	value numeric(10,3) not null,
	datetime timestamp not null,
	resolved bool not null,
	resolved_by text not null,
	resolution_time timestamp not null
);

--При добавление в таблицу measurements проверяем standards, если не норма, создаём запись в alerts
--Модуль 2
CREATE OR replace FUNCTION check_state()
RETURNS TRIGGER AS $$
DECLARE
	type_sensor_v varchar(20);
	room_number_v varchar(5);
	id_room_v SMALLINT;
	id_param_v SMALLINT;
	max_value_v numeric(10,3);
	min_value_v numeric(10,3);
	param_name VARCHAR(20);
BEGIN
	SELECT type_sensor INTO type_sensor_v FROM sensors WHERE id_sensor = NEW.id_sensor;
	SELECT id_room INTO id_room_v FROM sensors WHERE id_sensor = NEW.id_sensor;
	SELECT number_room INTO room_number_v FROM rooms WHERE id_room = id_room_v;
	IF type_sensor_v = 'co2' THEN
        param_name := 'CO2';
    ELSIF type_sensor_v = 'temp' THEN
        param_name := 'Температура';
    ELSIF type_sensor_v = 'humidity' THEN
        param_name := 'Влажность';
    ELSE
        param_name := NULL;
    END IF;
	SELECT id_param INTO id_param_v FROM params WHERE param = param_name;
	SELECT max_value INTO max_value_v FROM standards WHERE id_room = id_room_v AND id_param = id_param_v;
	SELECT min_value INTO min_value_v FROM standards WHERE id_room = id_room_v AND id_param = id_param_v;

	IF NEW.value > max_value_v or NEW.value < min_value_v THEN
		INSERT INTO alerts values(DEFAULT, id_room_v, id_param_v, NEW.value, NEW.datetime, FALSE, 'Отвественный за ' || room_number_v, now());
	END IF;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR replace TRIGGER state_check
BEFORE INSERT ON measurements
FOR EACH ROW
EXECUTE FUNCTION check_state();

-- Заполнение таблицы buildings (здания)
INSERT INTO buildings (address_building, name_institution, abbreviation_institution) VALUES
('ул. Ленина, д. 25, г. Москва', 'Средняя общеобразовательная школа № 1', 'СОШ №1'),
('пр. Мира, д. 10, г. Санкт-Петербург', 'Гимназия № 45 им. А.С. Пушкина', 'Гимн. №45'),
('ул. Школьная, д. 7, г. Новосибирск', 'Лицей информационных технологий', 'ЛИТ'),
('ул. Строителей, д. 18, г. Екатеринбург', 'Центр образования "Созвездие"', 'ЦО "Созвездие"'),
('ул. Победы, д. 15, г. Казань', 'Татарско-русская гимназия № 12', 'ТРГ №12'),
('ул. Советская, д. 30, г. Нижний Новгород', 'Школа с углублённым изучением математики', 'ФМШ №30'),
('ул. Гагарина, д. 8, г. Самара', 'Гимназия "Источник знаний"', 'Гимн. "ИЗ"'),
('ул. Мира, д. 42, г. Челябинск', 'Лицей № 31 физико-математический', 'Лицей №31'),
('ул. Ленинградская, д. 55, г. Омск', 'Образовательный центр "Перспектива"', 'ОЦ "Перспектива"'),
('ул. Центральная, д. 3, г. Ростов-на-Дону', 'Гимназия "Донская"', 'Гимн. "Донская"'),
('ул. Московская, д. 17, г. Уфа', 'Башкирская гимназия № 20', 'БГ №20'),
('ул. Кирова, д. 22, г. Красноярск', 'Школа "Эврика-развитие"', 'Школа "Эврика"'),
('ул. Горького, д. 11, г. Пермь', 'Лицей "Приоритет"', 'Лицей "Приоритет"'),
('ул. Комсомольская, д. 9, г. Воронеж', 'Гимназия им. А.Платонова', 'Гимн. им. Платонова'),
('ул. Новая, д. 14, г. Волгоград', 'Центр образования "Олимпия"', 'ЦО "Олимпия"');

-- Заполнение таблицы rooms (помещения) для первого здания (id=1)
INSERT INTO rooms (number_room, type_room, id_building) VALUES
-- Кабинеты
('101', 'кабинет', 1),
('102', 'кабинет', 1),
('103', 'кабинет', 1),
('104', 'кабинет', 1),
('105', 'кабинет', 1),
('106', 'кабинет', 1),
('107', 'кабинет', 1),
('108', 'кабинет', 1),
('109', 'кабинет', 1),
('110', 'кабинет', 1),
-- Специальные помещения
('201', 'актовый', 1),
('202', 'столовая', 1),
('301', 'кабинет', 1),
('302', 'кабинет', 1),
('303', 'кабинет', 1);

-- Заполнение таблицы sensors (датчики) для первого здания
-- Для каждого кабинета добавляем по 3 датчика: temp, humidity, co2
INSERT INTO sensors (id_room, type_sensor, location_note) VALUES
-- Кабинет 101
(1, 'temp', 'У доски'),
(1, 'humidity', 'Центр кабинета'),
(1, 'co2', 'Возле окна'),
-- Кабинет 102
(2, 'temp', 'Учительский стол'),
(2, 'humidity', 'Задняя стена'),
(2, 'co2', 'У двери'),
-- Кабинет 103
(3, 'temp', 'Лабораторная зона'),
(3, 'humidity', 'У аквариума'),
(3, 'co2', 'Компьютерный класс'),
-- Кабинет 104
(4, 'temp', 'Спортивный инвентарь'),
(4, 'humidity', 'Раздевалка'),
(4, 'co2', 'Основной зал'),
-- Кабинет 105
(5, 'temp', 'Музыкальные инструменты'),
(5, 'humidity', 'Хоровой класс'),
(5, 'co2', 'Репетиционная'),
-- Кабинет 106
(6, 'temp', 'Химическая лаборатория'),
(6, 'humidity', 'Вытяжной шкаф'),
(6, 'co2', 'Реактивы'),
-- Кабинет 107
(7, 'temp', 'Биологический кабинет'),
(7, 'humidity', 'Террариум'),
(7, 'co2', 'Микроскопы'),
-- Кабинет 108
(8, 'temp', 'Физическая лаборатория'),
(8, 'humidity', 'Приборы'),
(8, 'co2', 'Экспериментальная зона'),
-- Кабинет 109
(9, 'temp', 'Математический кабинет'),
(9, 'humidity', 'Проектор'),
(9, 'co2', 'Стеллажи'),
-- Кабинет 110
(10, 'temp', 'Литературный класс'),
(10, 'humidity', 'Библиотека'),
(10, 'co2', 'Читальный зал'),
-- Актовый зал 201
(11, 'temp', 'Сцена'),
(11, 'humidity', 'Зрительный зал'),
(11, 'co2', 'Гардероб'),
-- Столовая 202
(12, 'temp', 'Кухня'),
(12, 'humidity', 'Обеденный зал'),
(12, 'co2', 'Раздаточная'),
-- Кабинеты 301-303
(13, 'temp', 'Кабинет информатики'),
(13, 'humidity', 'Серверная'),
(13, 'co2', 'Рабочие места'),
(14, 'temp', 'Кабинет географии'),
(14, 'humidity', 'Глобусы и карты'),
(14, 'co2', 'Демонстрационная'),
(15, 'temp', 'Кабинет истории'),
(15, 'humidity', 'Экспонаты'),
(15, 'co2', 'Архив');

-- Заполнение таблицы params (параметры)
INSERT INTO params (param) VALUES
('Температура'),
('CO2'),
('Влажность'),
('Освещённость'),
('Шум');

-- Заполнение таблицы standards (стандарты) для всех комнат первого здания
INSERT INTO standards (id_room, id_param, max_value, min_value) VALUES
-- Для всех кабинетов - температура
(1, 1, 24.00, 18.00),
(2, 1, 24.00, 18.00),
(3, 1, 24.00, 18.00),
(4, 1, 24.00, 18.00),
(5, 1, 24.00, 18.00),
(6, 1, 24.00, 18.00),
(7, 1, 24.00, 18.00),
(8, 1, 24.00, 18.00),
(9, 1, 24.00, 18.00),
(10, 1, 24.00, 18.00),
(13, 1, 24.00, 18.00),
(14, 1, 24.00, 18.00),
(15, 1, 24.00, 18.00),
-- Для актового зала - температура
(11, 1, 22.00, 18.00),
-- Для столовой - температура
(12, 1, 23.00, 19.00),
-- Для всех кабинетов - CO2
(1, 2, 1000.00, 400.00),
(2, 2, 1000.00, 400.00),
(3, 2, 1000.00, 400.00),
(4, 2, 1000.00, 400.00),
(5, 2, 1000.00, 400.00),
(6, 2, 1000.00, 400.00),
(7, 2, 1000.00, 400.00),
(8, 2, 1000.00, 400.00),
(9, 2, 1000.00, 400.00),
(10, 2, 1000.00, 400.00),
(13, 2, 1000.00, 400.00),
(14, 2, 1000.00, 400.00),
(15, 2, 1000.00, 400.00),
-- Для актового зала - CO2
(11, 2, 1200.00, 350.00),
-- Для столовой - CO2
(12, 2, 1100.00, 300.00),
-- Для всех кабинетов - влажность
(1, 3, 60.00, 30.00),
(2, 3, 60.00, 30.00),
(3, 3, 60.00, 30.00),
(4, 3, 60.00, 30.00),
(5, 3, 60.00, 30.00),
(6, 3, 60.00, 30.00),
(7, 3, 60.00, 30.00),
(8, 3, 60.00, 30.00),
(9, 3, 60.00, 30.00),
(10, 3, 60.00, 30.00),
(13, 3, 55.00, 35.00),
(14, 3, 60.00, 30.00),
(15, 3, 60.00, 30.00),
-- Для актового зала - влажность
(11, 3, 65.00, 35.00),
-- Для столовой - влажность
(12, 3, 70.00, 40.00);

-- Дополнительные измерения с аномалиями для тестирования триггера
INSERT INTO measurements (id_sensor, datetime, value) VALUES
-- Температура ниже нормы
(1, '2025-12-22 07:30:00', 16.500),  -- Датчик температуры в кабинете 101
(4, '2025-12-22 07:45:00', 17.200),  -- Датчик температуры в кабинете 102
-- Температура выше нормы
(7, '2025-12-22 14:30:00', 26.800),  -- Датчик температуры в кабинете 103
(10, '2025-12-22 15:00:00', 25.500), -- Датчик температуры в кабинете 104
-- CO2 выше нормы
(3, '2025-12-22 12:00:00', 1100.250), -- Датчик CO2 в кабинете 101
(6, '2025-12-22 13:15:00', 1050.800), -- Датчик CO2 в кабинете 102
-- Влажность ниже нормы
(2, '2025-12-22 08:30:00', 28.300),   -- Датчик влажности в кабинете 101
(5, '2025-12-22 09:00:00', 25.700),   -- Датчик влажности в кабинете 102
-- Влажность выше нормы
(8, '2025-12-22 16:30:00', 68.900),   -- Датчик влажности в кабинете 103
(11, '2025-12-22 17:00:00', 72.500);  -- Датчик влажности в кабинете 104

-- Дополнительные тестовые алерты (ручное добавление)
INSERT INTO alerts (id_room, id_param, value, datetime, resolved, resolved_by, resolution_time) VALUES
(1, 1, 16.500, '2025-12-22 07:30:00', false, 'Иванова М.И.', '2025-12-22 08:00:00'),
(1, 2, 1100.250, '2025-12-22 12:00:00', true, 'Петров А.С.', '2025-12-22 12:30:00'),
(2, 1, 17.200, '2025-12-22 07:45:00', true, 'Сидорова Е.В.', '2025-12-22 08:15:00'),
(3, 1, 26.800, '2025-12-22 14:30:00', false, 'Козлов Д.Н.', '2025-12-22 15:00:00'),
(4, 1, 25.500, '2025-12-22 15:00:00', true, 'Николаева О.П.', '2025-12-22 15:30:00'),
(1, 3, 28.300, '2025-12-22 08:30:00', true, 'Васильев И.К.', '2025-12-22 09:00:00'),
(2, 3, 25.700, '2025-12-22 09:00:00', false, 'Макарова Т.Л.', '2025-12-22 09:30:00'),
(3, 3, 68.900, '2025-12-22 16:30:00', true, 'Орлов В.М.', '2025-12-22 17:00:00'),
(4, 3, 72.500, '2025-12-22 17:00:00', false, 'Зайцева А.Б.', '2025-12-22 17:30:00'),
(2, 2, 1050.800, '2025-12-22 13:15:00', true, 'Волков С.Д.', '2025-12-22 13:45:00');

-- Генерация временных меток каждые 30 минут за период 16–22 декабря 2025
WITH RECURSIVE time_series AS (
    SELECT '2025-12-16 00:00:00'::timestamp AS ts
    UNION ALL
    SELECT ts + INTERVAL '30 minutes'
    FROM time_series
    WHERE ts < '2025-12-22 23:30:00'
),
-- Температура (датчик id=1)
temp_data AS (
    SELECT
        1 AS id_sensor,
        ts AS datetime,
        ROUND(
            (21.0 + 2.0 * SIN(EXTRACT(HOUR FROM ts) * PI() / 12) + (RANDOM() - 0.5) * 0.8)::numeric,
            3
        ) AS value
    FROM time_series
),
-- Влажность (датчик id=2)
humidity_data AS (
    SELECT
        2 AS id_sensor,
        ts AS datetime,
        ROUND(
            (45.0 + 5.0 * COS(EXTRACT(HOUR FROM ts) * PI() / 12) + (RANDOM() - 0.5) * 1.0)::numeric,
            3
        ) AS value
    FROM time_series
),
-- CO2 (датчик id=3)
co2_data AS (
    SELECT
        3 AS id_sensor,
        ts AS datetime,
        ROUND(
            (
                500.0 +
                CASE
                    WHEN EXTRACT(HOUR FROM ts) BETWEEN 8 AND 17 THEN
                        300.0 + RANDOM() * 100.0
                    ELSE
                        50.0 + RANDOM() * 50.0
                END +
                (RANDOM() - 0.5) * 20.0
            )::numeric,
            3
        ) AS value
    FROM time_series
),
-- Объединение
all_measurements AS (
    SELECT * FROM temp_data
    UNION ALL
    SELECT * FROM humidity_data
    UNION ALL
    SELECT * FROM co2_data
)
-- Вставка
INSERT INTO measurements (id_sensor, datetime, value)
SELECT id_sensor, datetime, value
FROM all_measurements;

-- Аномалии
INSERT INTO measurements (id_sensor, datetime, value) VALUES
(3, '2025-12-20 10:00:00', 1350.000::numeric),
(1, '2025-12-19 03:00:00', 15.200::numeric),
(2, '2025-12-21 16:30:00', 75.800::numeric);