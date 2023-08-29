create table message (
    id bigint not null auto_increment,
    user_id bigint,
    file_name varchar(255),
    tag varchar(255),
    text varchar(2048) not null,
    primary key (id));

create table user (
    id bigint not null auto_increment,
    activation_code varchar(255),
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    active bit not null,
    primary key (id));

create table user_role (
    user_id bigint not null,
    roles enum ('ADMIN','USER'));

alter table message
    add constraint message_user_fk
    foreign key (user_id) references user (id);

alter table user_role
     add constraint user_role_fk
     foreign key (user_id) references user (id);
