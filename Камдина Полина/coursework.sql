--create database monitoring_quality;

create table buildings(
	id_building int primary key generated always as identity,
	address_building varchar not null unique,
	name_institution varchar not null unique,
	abbreviation_institution varchar(50)
);

create table rooms(
	id_room smallint primary key generated always as identity,
	number_room varchar(5) not null unique,
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


INSERT INTO buildings (address_building, name_institution, abbreviation_institution) VALUES
('ул. Ленина, д. 25', 'Средняя общеобразовательная школа № 1', 'СОШ №1'),
('пр. Мира, д. 10', 'Детский сад «Солнышко»', '«Солнышко»'),
('ул. Школьная, д. 7', 'Гимназия имени А.С. Пушкина', 'СОШ №9'),
('ул. Строителей, д. 18, корп. 2', 'Центр детского творчества «Радуга»', 'ЦДТ «Радуга»');


INSERT INTO rooms (number_room, type_room, id_building) VALUES
('101', 'кабинет', 1),
('102', 'актовый', 1),
('201', 'столовая', 2),
('205', 'кабинет', 3);


INSERT INTO sensors (id_room, type_sensor, location_note) VALUES
(1, 'co2', 'На столе учителя'),
(1, 'temp', 'На внутренней стене, высота 1.5м'),
(2, 'humidity', 'У центральной колонны'),
(3, 'temp', 'В зоне раздачи пищи');


INSERT INTO params (param) VALUES
('Температура'),
('CO2'),
('Влажность'),
('Освещённость');


INSERT INTO standards (id_room, id_param, max_value, min_value) VALUES
(1, 1, 24.00, 18.00),
(1, 2, 999.99, 400.00),  
(3, 1, 22.00, 19.00),
(2, 3, 60.00, 30.00);


INSERT INTO measurements (id_sensor, datetime, value) VALUES
(1, '2024-04-10 08:30:00', 850.50),
(2, '2024-04-10 08:30:05', 22.10),
(4, '2024-04-10 12:15:30', 21.80),
(1, '2024-04-10 14:45:00', 999.99);



INSERT INTO alerts (id_room, id_param, value, datetime, resolved, resolved_by, resolution_time) VALUES
(1, 2, 999.99, '2024-04-10 14:45:05', true, 'Иванов И.И.', '2024-04-10 15:30:00'), 
(3, 1, 25.50, '2024-04-09 13:20:00', false, 'Чернова М.А.', '2024-04-11 11:30:00'),
(1, 1, 17.50, '2024-04-10 07:45:00', true, 'Петрова С.А.', '2024-04-10 08:15:00'),
(2, 3, 75.20, '2024-04-08 11:00:00', true, 'Сидоров В.В.', '2024-04-08 12:05:00');




