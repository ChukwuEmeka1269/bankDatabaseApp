create table if not exists users (
                       id identity not null primary key,
                       first_name varchar(255),
                       last_name varchar(255),
                       registration_date datetime
)


ALTER TABLE users ADD COLUMN IF NOT EXISTS balance int default 100;

CREATE TABLE IF NOT EXISTS transfers (
                                         id identity not null primary key,
                                         sender int not null,
                                         receiver int not null,
                                         amount int not null,
                                         foreign key (sender) references users(id),
                                        foreign key (receiver) references users(id)
)