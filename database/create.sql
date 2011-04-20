#========================================================================== #
# Project Filename:    C:\Projekte\OpenCustomer\database\opencustomer.dez   #
# Project Name:        OpenCustomer                                         #
# Author:              Thomas Bader                                         #
# DBMS:                MySQL 4                                              #
# Copyright:           Bader & Jene                                         #
# Generated on:        04.01.2007 15:58:13                                  #
#========================================================================== #

#GRANT ALL ON opencustomer.* TO 'opencustomer'@'localhost' IDENTIFIED BY 'opencustomer';

drop database IF EXISTS opencustomer;
create database opencustomer;
use opencustomer;

#========================================================================== #
#  Tables                                                                   #
#========================================================================== #

CREATE TABLE person (
    person_id INTEGER NOT NULL AUTO_INCREMENT,
    company_id INTEGER,
    person_type ENUM('FREELANCER','INDIVIDUAL','EMPLOYEE','BUSINESSMAN'),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    name_affix VARCHAR(255),
    title VARCHAR(255),
    gender ENUM('MALE','FEMALE','UNKNOWN'),
    role VARCHAR(255),
    degree VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    mobile VARCHAR(255),
    fax VARCHAR(255),
    url VARCHAR(255),
    notice TEXT,
    day_of_birth DATE,
    archived TINYINT(1) NOT NULL DEFAULT 0,
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (person_id),
    UNIQUE KEY IDX_person1(person_id),
    KEY IDX_person2(company_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE account (
    user_id INTEGER NOT NULL AUTO_INCREMENT,
    person_id INTEGER,
    profile_id INTEGER NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    locale VARCHAR(50) NOT NULL,
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER DEFAULT 0,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (user_id),
    KEY IDX_account1(person_id),
    UNIQUE KEY IDX_account2(user_id),
    KEY IDX_account3(profile_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE company (
    company_id INTEGER NOT NULL AUTO_INCREMENT,
    legal_form_id INTEGER,
    sector_id INTEGER,
    company_type_id INTEGER,
    company_state_id INTEGER,
    rating_id INTEGER,
    category_id INTEGER,
    assigned_user_id INTEGER,
    company_name VARCHAR(255),
    url VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    fax VARCHAR(255),
    notice TEXT,
    info TEXT,
    staff_count INTEGER,
    archived TINYINT(1) NOT NULL DEFAULT 0,
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (company_id),
    UNIQUE UN_company_1 (company_name),
    UNIQUE KEY IDX_company1(company_id),
    KEY IDX_company2(legal_form_id),
    KEY IDX_company3(sector_id),
    KEY IDX_company4(company_state_id),
    KEY IDX_company5(company_type_id),
    KEY IDX_company6(rating_id),
    KEY IDX_company7(category_id),
    KEY IDX_company8(assigned_user_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE address (
    address_id INTEGER NOT NULL AUTO_INCREMENT,
    company_id INTEGER,
    person_id INTEGER,
    address_type ENUM('MAIN','POSTAL','BILLING','DELIVERY'),
    name VARCHAR(255),
    street VARCHAR(255),
    postbox VARCHAR(255),
    zip VARCHAR(50),
    city VARCHAR(255),
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (address_id),
    KEY IDX_address1(company_id),
    KEY IDX_address2(person_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE legal_form (
    legal_form_id INTEGER NOT NULL AUTO_INCREMENT,
    legal_form_group_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),
    sort INTEGER NOT NULL,
    deleted BIT NOT NULL DEFAULT 0,
    PRIMARY KEY (legal_form_id),
    UNIQUE KEY IDX_legal_form1(legal_form_id),
    KEY IDX_legal_form2(legal_form_group_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE sector (
    sector_id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),
    sort INTEGER NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (sector_id),
    UNIQUE KEY IDX_sector1(sector_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE contact (
    contact_id INTEGER NOT NULL AUTO_INCREMENT,
    company_id INTEGER,
    job_id INTEGER,
    contact_timestamp DATETIME,
    subject VARCHAR(255) NOT NULL,
    content TEXT,
    content_type ENUM('PLAINTEXT','HTML') NOT NULL DEFAULT 'PLAINTEXT',
    contact_name VARCHAR(255),
    contact_type ENUM('TELEPHONE','EMAIL','LETTER','FAX','PERSONAL'),
    bound_type ENUM('IN','OUT'),
    import_type ENUM('NONE','NEW','ACCEPTED') NOT NULL DEFAULT 'NONE',
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (contact_id),
    KEY IDX_contact1(company_id),
    UNIQUE KEY IDX_contact2(contact_id),
    KEY IDX_contact3(job_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE person_contact (
    person_contact_id INTEGER NOT NULL AUTO_INCREMENT,
    contact_id INTEGER NOT NULL,
    person_id INTEGER NOT NULL,
    company_id INTEGER,
    relation_type ENUM('NONE','SENDER','TO','CC','BCC') NOT NULL DEFAULT 'NONE',
    PRIMARY KEY (person_contact_id),
    KEY IDX_person_contact1(contact_id),
    KEY IDX_person_contact2(person_id),
    KEY IDX_person_contact3(company_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE company_state (
    company_state_id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),
    sort INTEGER NOT NULL DEFAULT 0,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (company_state_id),
    UNIQUE KEY IDX_company_state1(company_state_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE company_type (
    company_type_id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),
    sort INTEGER NOT NULL DEFAULT 0,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (company_type_id),
    UNIQUE KEY IDX_company_type1(company_type_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE legal_form_group (
    legal_form_group_id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),
    sort INTEGER NOT NULL DEFAULT 0,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (legal_form_group_id),
    UNIQUE KEY IDX_legal_form_group1(legal_form_group_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE calendar (
    calendar_id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER,
    name VARCHAR(255),
    first_day_of_week ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL DEFAULT 'MONDAY',
    create_user INTEGER NOT NULL,
    create_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (calendar_id),
    KEY IDX_calendar1(user_id),
    UNIQUE KEY IDX_calendar2(calendar_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE event (
    event_id INTEGER NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    reminder_date DATETIME,
    all_day TINYINT(1) NOT NULL DEFAULT 0,
    occupied TINYINT(1) NOT NULL DEFAULT 0,
    unknown_participiants VARCHAR(255),
    recurrence_start_date DATETIME,
    recurrence_end_date DATETIME,
    recurrence_type ENUM('NONE','FOREVER','NUMBER_OF_TIMES','UNTIL_DATE') NOT NULL,
    recurrence_until_date DATETIME,
    recurrence_number_of_times INTEGER,
    recurrence_cycle INTEGER,
    recurrence_cycle_unit ENUM('DAY','WEEK','MONTH','YEAR'),
    recurrence_in_week INTEGER,
    recurrence_in_month ENUM('DAY_OF_WEEK','DAY_OF_MONTH'),
    create_user INTEGER NOT NULL,
    create_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (event_id),
    UNIQUE KEY IDX_event1(event_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE role (
    role_id INTEGER NOT NULL AUTO_INCREMENT,
    default_usergroup_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    admin TINYINT(1) NOT NULL DEFAULT 0,
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (role_id),
    UNIQUE UN_role_1 (name),
    UNIQUE KEY IDX_role1(role_id),
    KEY IDX_role2(default_usergroup_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE rights (
    right_id INTEGER NOT NULL AUTO_INCREMENT,
    right_group_id INTEGER NOT NULL,
    label VARCHAR(50),
    name_key VARCHAR(255),
    right_type ENUM('READ','WRITE','OTHER') NOT NULL,
    sort INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (right_id),
    UNIQUE KEY IDX_rights1(right_id),
    KEY IDX_rights2(right_group_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE right_role (
    right_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (right_id, role_id),
    KEY IDX_right_role1(right_id),
    KEY IDX_right_role2(role_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE right_group (
    right_group_id INTEGER NOT NULL AUTO_INCREMENT,
    right_area_id INTEGER NOT NULL,
    label VARCHAR(50) NOT NULL,
    name_key VARCHAR(255) NOT NULL,
    sort INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (right_group_id),
    UNIQUE KEY IDX_right_group1(right_group_id),
    KEY IDX_right_group2(right_area_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE right_area (
    right_area_id INTEGER NOT NULL AUTO_INCREMENT,
    label VARCHAR(50) NOT NULL,
    name_key VARCHAR(255) NOT NULL,
    sort INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (right_area_id),
    UNIQUE KEY IDX_right_area1(right_area_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE usergroup (
    usergroup_id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    admin TINYINT(1) NOT NULL DEFAULT 0,
    create_timestamp VARCHAR(255) NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (usergroup_id),
    UNIQUE KEY IDX_usergroup1(usergroup_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE event_calendar (
    event_calendar_id INTEGER NOT NULL AUTO_INCREMENT,
    calendar_id INTEGER NOT NULL,
    event_id INTEGER NOT NULL,
    participiant_type ENUM('HOST','GUEST') NOT NULL DEFAULT 'GUEST',
    invitation_status ENUM('NEW','ACCEPTED','REJECTED','DELETED') NOT NULL DEFAULT 'NEW',
    create_user INTEGER NOT NULL,
    create_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (event_calendar_id),
    KEY IDX_event_calendar1(calendar_id),
    KEY IDX_event_calendar2(event_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE event_person (
    event_person_id INTEGER NOT NULL AUTO_INCREMENT,
    person_id INTEGER NOT NULL,
    event_id INTEGER NOT NULL,
    invitation_status ENUM('NEW','ACCEPTED','REJECTED','DELETED') NOT NULL DEFAULT 'NEW',
    create_user INTEGER NOT NULL,
    create_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (event_person_id),
    KEY IDX_event_person1(person_id),
    KEY IDX_event_person2(event_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE user_calendar (
    user_id INTEGER NOT NULL,
    calendar_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, calendar_id),
    KEY IDX_user_calendar1(user_id),
    KEY IDX_user_calendar2(calendar_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE rating (
    rating_id INTEGER NOT NULL AUTO_INCREMENT,
    rating TINYINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),
    sort INTEGER NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (rating_id),
    UNIQUE KEY IDX_rating1(rating_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE category (
    category_id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    name_key VARCHAR(255),
    sort INTEGER NOT NULL,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (category_id),
    UNIQUE KEY IDX_category1(category_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE job (
    job_id INTEGER NOT NULL AUTO_INCREMENT,
    assigned_user_id INTEGER NOT NULL,
    referenced_person_id INTEGER,
    referenced_company_id INTEGER,
    parent_job_id INTEGER,
    subject VARCHAR(255) NOT NULL,
    info TEXT,
    due_date DATETIME,
    priority ENUM('LOW','MEDIUM','HIGH') NOT NULL,
    status ENUM('PLANNED','IN_PROGRESS','COMPLETED'),
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (job_id),
    KEY IDX_job1(assigned_user_id),
    UNIQUE KEY IDX_job2(job_id),
    KEY IDX_job3(referenced_person_id),
    KEY IDX_job4(referenced_company_id),
    KEY IDX_job5(parent_job_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE profile (
    profile_id INTEGER NOT NULL AUTO_INCREMENT,
    role_id INTEGER,
    default_usergroup_id INTEGER,
    locked TINYINT(1) NOT NULL DEFAULT 0,
    valid_from DATETIME,
    valid_until DATETIME,
    time_lock INTEGER NOT NULL DEFAULT 30,
    ip_pattern VARCHAR(255) NOT NULL DEFAULT '*',
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (profile_id),
    KEY IDX_profile1(role_id),
    KEY IDX_profile2(default_usergroup_id),
    UNIQUE KEY IDX_profile3(profile_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE profile_usergroup (
    usergroup_id INTEGER NOT NULL,
    profile_id INTEGER NOT NULL,
    PRIMARY KEY (usergroup_id, profile_id),
    KEY IDX_profile_usergroup1(usergroup_id),
    KEY IDX_profile_usergroup2(profile_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE ldap_group (
    ldap_group_id INTEGER NOT NULL AUTO_INCREMENT,
    profile_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    priority INTEGER NOT NULL DEFAULT 0,
    create_timestamp DATETIME NOT NULL,
    create_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    owner_user INTEGER NOT NULL,
    owner_group INTEGER NOT NULL,
    access_user ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_group ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    access_global ENUM('NONE','READ','WRITE','WRITE_SYSTEM') NOT NULL DEFAULT 'WRITE_SYSTEM',
    PRIMARY KEY (ldap_group_id),
    UNIQUE UC_ldap_group_name (name),
    UNIQUE UC_ldap_group_priority (priority),
    UNIQUE UN_role_1 (name),
    KEY IDX_ldap_group1(profile_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE user_login_log (
    user_login_log_id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    remote_host VARCHAR(50),
    result ENUM('VALID','INVALID','VALID_REJECTED') NOT NULL,
    login_date DATETIME NOT NULL,
    login_type ENUM('WEBAPP','WEBDAV','WEBSERVICE') NOT NULL,
    PRIMARY KEY (user_login_log_id),
    KEY IDX_user_login_log1(user_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE configuration (
    configuration_id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER,
    main_key VARCHAR(255) NOT NULL,
    sub_key VARCHAR(255),
    value VARCHAR(255) NOT NULL,
    create_user INTEGER NOT NULL,
    create_timestamp DATETIME NOT NULL,
    modify_user INTEGER NOT NULL,
    modify_timestamp DATETIME NOT NULL,
    version INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (configuration_id),
    KEY IDX_configuration1(user_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

CREATE TABLE list_configuration (
    list_configuration_id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER,
    list_type ENUM('PERSON','COMPANY') NOT NULL,
    default_list TINYINT(1) NOT NULL DEFAULT 0,
    name VARCHAR(50) NOT NULL DEFAULT '0',
    description TEXT,
    column_list VARCHAR(255) NOT NULL,
    CONSTRAINT PK_list_configuration PRIMARY KEY (list_configuration_id),
    UNIQUE UN_list_configuration_1 (user_id, name, list_type),
    KEY IDX_list_configuration1(user_id)
)
TYPE = InnoDB
CHARACTER SET utf8;

#========================================================================== #
#  Foreign Keys                                                             #
#========================================================================== #

ALTER TABLE person
    ADD FOREIGN KEY (company_id) REFERENCES company (company_id) ON DELETE CASCADE;

ALTER TABLE account
    ADD FOREIGN KEY (person_id) REFERENCES person (person_id);

ALTER TABLE account
    ADD FOREIGN KEY (profile_id) REFERENCES profile (profile_id);

ALTER TABLE company
    ADD FOREIGN KEY (legal_form_id) REFERENCES legal_form (legal_form_id);

ALTER TABLE company
    ADD FOREIGN KEY (sector_id) REFERENCES sector (sector_id);

ALTER TABLE company
    ADD FOREIGN KEY (company_state_id) REFERENCES company_state (company_state_id);

ALTER TABLE company
    ADD FOREIGN KEY (company_type_id) REFERENCES company_type (company_type_id);

ALTER TABLE company
    ADD FOREIGN KEY (rating_id) REFERENCES rating (rating_id);

ALTER TABLE company
    ADD FOREIGN KEY (category_id) REFERENCES category (category_id);

ALTER TABLE company
    ADD FOREIGN KEY (assigned_user_id) REFERENCES account (user_id);

ALTER TABLE address
    ADD FOREIGN KEY (company_id) REFERENCES company (company_id) ON DELETE CASCADE;

ALTER TABLE address
    ADD FOREIGN KEY (person_id) REFERENCES person (person_id) ON DELETE CASCADE;

ALTER TABLE legal_form
    ADD FOREIGN KEY (legal_form_group_id) REFERENCES legal_form_group (legal_form_group_id);

ALTER TABLE contact
    ADD FOREIGN KEY (company_id) REFERENCES company (company_id);

ALTER TABLE contact
    ADD FOREIGN KEY (job_id) REFERENCES job (job_id);

ALTER TABLE person_contact
    ADD FOREIGN KEY (contact_id) REFERENCES contact (contact_id) ON DELETE CASCADE;

ALTER TABLE person_contact
    ADD FOREIGN KEY (person_id) REFERENCES person (person_id);

ALTER TABLE person_contact
    ADD FOREIGN KEY (company_id) REFERENCES company (company_id);

ALTER TABLE calendar
    ADD FOREIGN KEY (user_id) REFERENCES account (user_id);

ALTER TABLE role
    ADD FOREIGN KEY (default_usergroup_id) REFERENCES usergroup (usergroup_id);

ALTER TABLE rights
    ADD FOREIGN KEY (right_group_id) REFERENCES right_group (right_group_id);

ALTER TABLE right_role
    ADD FOREIGN KEY (right_id) REFERENCES rights (right_id);

ALTER TABLE right_role
    ADD FOREIGN KEY (role_id) REFERENCES role (role_id);

ALTER TABLE right_group
    ADD FOREIGN KEY (right_area_id) REFERENCES right_area (right_area_id);

ALTER TABLE event_calendar
    ADD FOREIGN KEY (calendar_id) REFERENCES calendar (calendar_id);

ALTER TABLE event_calendar
    ADD FOREIGN KEY (event_id) REFERENCES event (event_id) ON DELETE CASCADE;

ALTER TABLE event_person
    ADD FOREIGN KEY (person_id) REFERENCES person (person_id);

ALTER TABLE event_person
    ADD FOREIGN KEY (event_id) REFERENCES event (event_id) ON DELETE CASCADE;

ALTER TABLE user_calendar
    ADD FOREIGN KEY (user_id) REFERENCES account (user_id);

ALTER TABLE user_calendar
    ADD FOREIGN KEY (calendar_id) REFERENCES calendar (calendar_id);

ALTER TABLE job
    ADD FOREIGN KEY (assigned_user_id) REFERENCES account (user_id);

ALTER TABLE job
    ADD FOREIGN KEY (referenced_person_id) REFERENCES person (person_id);

ALTER TABLE job
    ADD FOREIGN KEY (referenced_company_id) REFERENCES company (company_id);

ALTER TABLE job
    ADD FOREIGN KEY (parent_job_id) REFERENCES job (job_id);

ALTER TABLE profile
    ADD FOREIGN KEY (role_id) REFERENCES role (role_id);

ALTER TABLE profile
    ADD FOREIGN KEY (default_usergroup_id) REFERENCES usergroup (usergroup_id);

ALTER TABLE profile_usergroup
    ADD FOREIGN KEY (usergroup_id) REFERENCES usergroup (usergroup_id);

ALTER TABLE profile_usergroup
    ADD FOREIGN KEY (profile_id) REFERENCES profile (profile_id);

ALTER TABLE ldap_group
    ADD FOREIGN KEY (profile_id) REFERENCES profile (profile_id);

ALTER TABLE user_login_log
    ADD FOREIGN KEY (user_id) REFERENCES account (user_id);

ALTER TABLE configuration
    ADD FOREIGN KEY (user_id) REFERENCES account (user_id);

ALTER TABLE list_configuration
    ADD FOREIGN KEY (user_id) REFERENCES account (user_id);
