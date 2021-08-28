create table if not exists topology
(
    id             bigserial primary key,
    args           varchar,
    name           varchar,
    description    varchar,
    injection_rate double precision,
    columns        integer,
    nodes          integer,
    rows           integer,
    created_at     timestamp default current_timestamp
);

create table if not exists topology_table
(
    id          bigserial primary key,
    id_topology bigint    references topology(id),
    name        varchar,
    content     varchar
);

create table if not exists topology_report
(
    id          bigserial primary key,
    id_topology bigint    references topology(id),
    name        varchar,
    content     varchar,
    path        varchar
);

create table if not exists topology_xml
(
    id          bigserial primary key,
    id_topology bigint    references topology(id),
    name        varchar,
    content     varchar
);
