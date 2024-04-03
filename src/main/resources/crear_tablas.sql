CREATE TABLE IF NOT EXISTS EMPLOYEE (
                                        id integer primary key,
                                        name varchar(100),
    last_name varchar(100),
    email varchar(100),
    direction varchar(50),
    phone varchar(10),
    birthdate date,
    image BYTEA
    );
