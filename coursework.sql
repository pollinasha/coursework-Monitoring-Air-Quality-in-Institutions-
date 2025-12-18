--create database monitoring_quality_air;

create table buildings(
	id_building int primary key generated always as identity,
	address_building varchar not null unique,
	name_institution varchar not null unique,
	abbreviation_institution varchar(50)
);

create table rooms(
	id_rooom smallint primary key generated always as identity,
	number_room varchar(5) not null unique,
	type_room varchar(20) not null check(type_room in('кабинет','актовый','столовая')),
	id_building smallint not null references buildings(id_building)
);

create table sensors(
	id_sensor int primary key generated always as identity,
	id_rooom smallint not null references rooms(id_rooom),
	type_sensor varchar(20) not null check(type_sensor in('co2','temp','humidity')),
	location_note varchar

);

create table measurements(
	id_measurement bigint primary key generated always as identity,
	id_sensor smallint references sensors(id_sensor),
	datetime timestamp not null,
	value numeric(5,2) not null
); 

create table params(
	id_param int primary key generated always as identity,
	param varchar(20) not null
);

create table standards(
	id_standard smallint primary key generated always as identity,
	id_rooom smallint not null references rooms(id_rooom),
	id_param smallint not null references params(id_param),
	max_value numeric(5,2) not null,
	min_value numeric(5,2) not null

);

create table alerts(
	id_alert int primary key generated always as identity,
	id_rooom smallint not null references rooms(id_rooom),
	id_param smallint not null references params(id_param),
	value numeric(5,2) not null,
	datetime timestamp not null,
	resolved bool not null,
	resolved_by text not null,
	resolution_time timestamp not null
	
);



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


INSERT INTO sensors (id_rooom, type_sensor, location_note) VALUES
(1, 'co2', 'На столе учителя'),
(1, 'temp', 'На внутренней стене, высота 1.5м'),
(2, 'humidity', 'У центральной колонны'),
(3, 'temp', 'В зоне раздачи пищи');


INSERT INTO params (param) VALUES
('Температура'),
('CO2'),
('Влажность'),
('Освещённость');


INSERT INTO standards (id_rooom, id_param, max_value, min_value) VALUES
(1, 1, 24.00, 18.00),
(1, 2, 999.99, 400.00),  
(3, 1, 22.00, 19.00),
(2, 3, 60.00, 30.00);


INSERT INTO measurements (id_sensor, datetime, value) VALUES
(1, '2024-04-10 08:30:00', 850.50),
(2, '2024-04-10 08:30:05', 22.10),
(4, '2024-04-10 12:15:30', 21.80),
(1, '2024-04-10 14:45:00', 999.99);



INSERT INTO alerts (id_rooom, id_param, value, datetime, resolved, resolved_by, resolution_time) VALUES
(1, 2, 999.99, '2024-04-10 14:45:05', true, 'Иванов И.И.', '2024-04-10 15:30:00'), 
(3, 1, 25.50, '2024-04-09 13:20:00', false, 'Чернова М.А.', '2024-04-11 11:30:00'),
(1, 1, 17.50, '2024-04-10 07:45:00', true, 'Петрова С.А.', '2024-04-10 08:15:00'),
(2, 3, 75.20, '2024-04-08 11:00:00', true, 'Сидоров В.В.', '2024-04-08 12:05:00');





