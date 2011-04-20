-- Usergroups
insert into usergroup (usergroup_id, name, admin, create_timestamp, create_user, modify_timestamp, modify_user, owner_user, owner_group, access_user) values
 (1, 'System Administation', 1, NOW(), 1, NOW(), 1, 1, 1, 'WRITE_SYSTEM');
insert into usergroup (usergroup_id, name, admin, create_timestamp, create_user, modify_timestamp, modify_user, owner_user, owner_group, access_user) values
 (2, 'Standard', 0, NOW(), 1, NOW(), 1, 1, 1, 'WRITE_SYSTEM');

-- Roles
insert into role (role_id, name, default_usergroup_id, admin, create_timestamp, create_user, modify_timestamp, modify_user, owner_user, owner_group, access_user) values
 (1, 'System Administation', 1, 1, NOW(), 1, NOW(), 1, 1, 1, 'WRITE_SYSTEM');
insert into role (role_id, name, default_usergroup_id, admin, create_timestamp, create_user, modify_timestamp, modify_user, owner_user, owner_group, access_user) values
 (2, 'User', 2, 0, NOW(), 1, NOW(), 1, 1, 1, 'WRITE_SYSTEM');

-- Profile
insert into profile (profile_id, role_id, default_usergroup_id, create_timestamp, create_user, modify_timestamp, modify_user, owner_user, owner_group, access_user) values
 (1, 1, 1, NOW(), 1, NOW(), 1, 1, 1, 'WRITE_SYSTEM');

-- Usergroup Relations
insert into profile_usergroup (profile_id, usergroup_id) value (1, 1);

-- Administrational Database User (password: admin)
insert into account (user_id, profile_id, user_name, password, locale, create_timestamp, create_user, modify_timestamp, modify_user, owner_user, owner_group, access_user) values
 (1, 1, 'admin', 'A1A3AFA9FAD72527C309CA8ECA009F43', "de", NOW(), 1, NOW(), 1, 1, 1, 'WRITE_SYSTEM');

-- the calendar for the administrator
insert into calendar(calendar_id, user_id, create_timestamp, create_user, modify_timestamp, modify_user, owner_user, owner_group, access_user) values
 (1, 1, NOW(), 1, NOW(), 1, 1, 1, 'WRITE_SYSTEM');

-- Company States
INSERT INTO `company_state` (`company_state_id`,`name`, name_key,`sort`) VALUES 
 (1,'kein Kontakt', 'default.values.companyState.noContact',1),
 (2,'Kontaktaufnahme', 'default.values.companyState.initiation',2),
 (3,'Kontakt', 'default.values.companyState.inCustomerCare',3),
 (4,'ungeeignet', 'default.values.companyState.uninterested',5),
 (5,'Kunde', 'default.values.companyState.customer', 4);
 
-- Company Types
INSERT INTO `company_type` (`company_type_id`,`name`, name_key,`sort`) VALUES 
 (1,'Endkunde', 'default.values.companyType.endCustomer', 1),
 (2,'Systemhaus', 'default.values.companyType.systemVendor',4),
 (3,'Bodyleaser', 'default.values.companyType.personalAgency',3),
 (4,'Netzwerk', 'default.values.companyType.network',6),
 (5,'Mitbewerber', 'default.values.companyType.competitor',2),
 (6,'Lieferanten', 'default.values.companyType.suppliers',5);

-- Legal Forms
INSERT INTO `legal_form_group` (`legal_form_group_id`,`name`, name_key,`sort`) VALUES 
 (1,'Personengesellschaften', 'default.values.legalFormGroup.partnership',1),
 (2,'Kapitalgesellschaften', 'default.values.legalFormGroup.capital',2),
 (3,'Mischformen', 'default.values.legalFormGroup.hybrids',3),
 (4,'Sonderformen', 'default.values.legalFormGroup.special',4);

INSERT INTO `legal_form` (`legal_form_id`,`legal_form_group_id`,`name`,`sort`) VALUES 
 (1,2,'AG',1),
 (2,2,'GmbH',2),
 (3,1,'Einzelunternehmen',3),
 (4,1,'GbR',4),
 (5,3,'GmbH & Co.',5),
 (6,3,'GmbH & Co. KG',6),
 (7,1,'KG',7),
 (8,1,'OHG',8),
 (9,2,'KGaA',9),
 (10,4,'VVaG',11),
 (11,4,'Anstalt des öffentlichen Rechts',12),
 (12,4,'eG',10),
 (13,4,'Körperschaft des öffentlichen Rechts',13),
 (14,3,'GmbH & Co. KGaA',15),
 (15,4,'e.V.',14),
 (16,3,'AG & Co. KG',15),
 (17,3,'AG & Co. OHG',16);

-- Sectors
INSERT INTO `sector` (`sector_id`,`name`, name_key,`sort`) VALUES 
 (1,'Land- und Forstwirtschaft', 'default.values.sector.a', 1),
 (2,'Fischerei und Fischzucht', 'default.values.sector.b', 2),
 (3,'Bergbau', 'default.values.sector.c', 3),
 (4,'Verarbeitendes Gewerbe', 'default.values.sector.d', 4),
 (5,'Energie- und Wasserversorgung', 'default.values.sector.e', 5),
 (6,'Baugewerbe', 'default.values.sector.f', 6),
 (7,'Handel; Instandhaltung und Reparatur von Kraftfahrzeugen und Gebrauchsgütern', 'default.values.sector.g', 7),
 (8,'Gastgewerbe', 'default.values.sector.h', 8),
 (9,'Verkehr und Nachrichtenübermittlung', 'default.values.sector.i', 9),
 (10,'Kredit- und Versicherungsgewerbe', 'default.values.sector.j', 10),
 (11,'Grundstücks- und Wohnungswesen, Erbringung von wirtschaftlichen Dienstleistungen', 'default.values.sector.k', 11),
 (12,'Öffentliche Verwaltung, Verteidigung, Sozialversicherung', 'default.values.sector.l', 12),
 (13,'Erziehung und Unterricht', 'default.values.sector.m', 13),
 (14,'Gesundheits-, Veterinär- und Sozialwesen', 'default.values.sector.n', 14),
 (15,'Erbringung von sonstigen öffentlichen und persönlichen Dienstleistungen', 'default.values.sector.o', 15),
 (16,'Exterritoriale Organisationen und Körperschaften', 'default.values.sector.q', 16);

-- Ratings
INSERT INTO `rating` (`rating_id`, rating, `name`, name_key,`sort`) VALUES 
 (1, 0, 'very bad', 'default.values.rating.veryBad',1),
 (2, 1, 'bad', 'default.values.rating.bad',2),
 (3, 2, 'ok', 'default.values.rating.ok',3),
 (4, 3, 'good', 'default.values.rating.good',4),
 (5, 4, 'very good', 'default.values.rating.veryGood', 5);

-- Ratings
INSERT INTO `category` (`category_id`, `name`, name_key,`sort`) VALUES 
 (1, 'a customer', 'default.values.category.aCustomer',1),
 (2, 'b customer', 'default.values.category.bCustomer',2),
 (3, 'c customer', 'default.values.category.cCustomer',3);
 
-- Rights
INSERT INTO right_area (right_area_id, label, name_key, sort) VALUES
 (1, 'crm', 'right.crm', 2),
 (2, 'calendar', 'right.calendar', 3),
 (3, 'administration', 'right.administration', 4),
 (4, 'extern','right.extern', 5);

INSERT INTO right_group (right_group_id, right_area_id, label, name_key, sort) VALUES
 (1, 1, 'persons', 'right.crm.persons', 1),
 (2, 1, 'companies', 'right.crm.companies', 2),
 (3, 1, 'contacts', 'right.crm.contacts', 3),
 (4, 3, 'userProfile', 'right.administration.userProfile', 1),
 (5, 3, 'userManagement', 'right.administration.userManagement', 2),
 (6, 2, 'calendar', 'right.calendar.calendar', 1),
 (7, 3, 'role', 'right.administration.role', 3),
 (8, 3, 'usergroup', 'right.administration.usergroup', 4),
 (9, 4, 'webdav', 'right.extern.webdav', 2),
 (10,1, 'jobs', 'right.crm.jobs', 2),
 (11,4, 'webservice','right.extern.webservice',2),
 (12,3, 'configuration', 'right.administration.configuration', 5),
 (13,3, 'list', 'right.administration.list', 5);
 
INSERT INTO `rights` (`right_id`,`right_group_id`,`label`,`right_type`, name_key,`sort`) VALUES 
 ( 1, 1, null, 'READ', null, 1),
 ( 2, 1, null, 'WRITE', null, 1),
 ( 3, 2, null, 'READ', null, 1),
 ( 4, 2, null, 'WRITE', null, 1),
 ( 5, 3, null, 'READ', null, 1),
 ( 6, 3, null, 'WRITE', null, 1),
 ( 7, 4, null, 'READ', null, 1),
 ( 8, 4, null, 'WRITE', null, 1),
 ( 9, 5, null, 'READ', null, 1),
 (10, 5, null, 'WRITE', null, 1),
 (11, 6, null, 'READ', null, 1),
 (12, 6, null, 'WRITE', null, 1),
 (13, 7, null, 'READ', null, 1),
 (14, 7, null, 'WRITE', null, 1),
 (15, 8, null, 'READ', null, 1),
 (16, 8, null, 'WRITE', null, 1),
 (17, 5, 'SESSION_MONITOR', 'OTHER', 'right.administration.userManagement.sessionMonitor', 1),
 (18, 6, 'CHOOSE', 'OTHER', 'right.calendar.calendar.choose', 1),
 (19, 9, null, 'READ', null, 1),
 (20, 9, null, 'WRITE', null, 1),
 (21, 10, null, 'READ', null, 1),
 (22, 10, null, 'WRITE', null, 1),
 (23, 10, 'ASSIGN_USER', 'OTHER', 'right.crm.jobs.assignUser', 1),
 (24, 11, null, 'READ' , null, 1),
 (25, 11, null, 'WRITE' , null, 1),
 (26, 12, null, 'READ' , null, 1),
 (27, 12, null, 'WRITE' , null, 1),
 (28, 13, null, 'READ' , null, 1),
 (29, 13, null, 'WRITE' , null, 1),
 (30, 13, 'GLOBAL_LIST', 'OTHER' , 'right.administration.list.globalList', 1),
 (31, 1, 'DOWNLOAD', 'OTHER' , 'right.crm.persons.download', 1),
 (32, 2, 'DOWNLOAD', 'OTHER' , 'right.crm.companies.download', 1);
 
 INSERT INTO `right_role` (`role_id`,`right_id`) VALUES 
 (1,7),
 (1,8),
 (1,9),
 (1,10),
 (1,13),
 (1,14),
 (1,15),
 (1,16),
 (1,17),
 (1,26),
 (1,27),
 (2,1),
 (2,2),
 (2,3),
 (2,4),
 (2,5),
 (2,6),
 (2,7),
 (2,8),
 (2,11),
 (2,12),
 (2,18),
 (2,19),
 (2,20),
 (2,21),
 (2,22),
 (2,23),
 (2,24),
 (2,25),
 (2,28),
 (2,29),
 (2,30),
 (2,31),
 (2,32);