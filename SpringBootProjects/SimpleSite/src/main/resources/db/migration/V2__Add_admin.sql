insert into user (id, username, password, active)
    value(1, 'admin', '123', true);

insert into user_role (user_id, roles)
    value (1, "ADMIN"), (1, "USER");