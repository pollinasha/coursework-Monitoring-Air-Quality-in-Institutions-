--create database monitoring_quality;

create table buildings(
	id_building int primary key generated always as identity,
	address_building varchar not null unique,
	name_institution varchar not null unique,
	abbreviation_institution varchar(10)
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










