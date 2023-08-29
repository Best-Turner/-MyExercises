insert into user (id, username, password, active)
    value(1, 'admin', '9bc70c09-ca4d-4cf0-b3ea-ff55732e0b1f', true);

insert into user_role (user_id, roles)
    value (1, "ADMIN"), (1, "USER");