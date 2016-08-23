create user 'admin'@'localhost' identified by 'test';
create user 'admin'@'%' identified by 'test';

grant all privileges on board.* to 'admin'@'localhost';
grant all privileges on board.* to 'admin'@'%';