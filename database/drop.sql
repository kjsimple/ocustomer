#========================================================================== #
# DROP SCRIPT                                                               #
#========================================================================== #
# Project Filename:    C:\Projekte\OpenCustomer\database\opencustomer.dez   #
# Project Name:        OpenCustomer                                         #
# Author:              Thomas Bader                                         #
# DBMS:                MySQL 4                                              #
# Copyright:           Bader & Jene                                         #
# Generated on:        04.01.2007 15:58:15                                  #
#========================================================================== #

#========================================================================== #
#  Drop Indexes                                                             #
#========================================================================== #
ALTER TABLE person DROP INDEX IDX_person1;
ALTER TABLE person DROP INDEX IDX_person2;
ALTER TABLE account DROP INDEX IDX_account1;
ALTER TABLE account DROP INDEX IDX_account2;
ALTER TABLE account DROP INDEX IDX_account3;
ALTER TABLE company DROP INDEX IDX_company1;
ALTER TABLE company DROP INDEX IDX_company2;
ALTER TABLE company DROP INDEX IDX_company3;
ALTER TABLE company DROP INDEX IDX_company4;
ALTER TABLE company DROP INDEX IDX_company5;
ALTER TABLE company DROP INDEX IDX_company6;
ALTER TABLE company DROP INDEX IDX_company7;
ALTER TABLE company DROP INDEX IDX_company8;
ALTER TABLE address DROP INDEX IDX_address1;
ALTER TABLE address DROP INDEX IDX_address2;
ALTER TABLE legal_form DROP INDEX IDX_legal_form1;
ALTER TABLE legal_form DROP INDEX IDX_legal_form2;
ALTER TABLE sector DROP INDEX IDX_sector1;
ALTER TABLE contact DROP INDEX IDX_contact1;
ALTER TABLE contact DROP INDEX IDX_contact2;
ALTER TABLE contact DROP INDEX IDX_contact3;
ALTER TABLE person_contact DROP INDEX IDX_person_contact1;
ALTER TABLE person_contact DROP INDEX IDX_person_contact2;
ALTER TABLE person_contact DROP INDEX IDX_person_contact3;
ALTER TABLE company_state DROP INDEX IDX_company_state1;
ALTER TABLE company_type DROP INDEX IDX_company_type1;
ALTER TABLE legal_form_group DROP INDEX IDX_legal_form_group1;
ALTER TABLE calendar DROP INDEX IDX_calendar1;
ALTER TABLE calendar DROP INDEX IDX_calendar2;
ALTER TABLE event DROP INDEX IDX_event1;
ALTER TABLE role DROP INDEX IDX_role1;
ALTER TABLE role DROP INDEX IDX_role2;
ALTER TABLE rights DROP INDEX IDX_rights1;
ALTER TABLE rights DROP INDEX IDX_rights2;
ALTER TABLE right_role DROP INDEX IDX_right_role1;
ALTER TABLE right_role DROP INDEX IDX_right_role2;
ALTER TABLE right_group DROP INDEX IDX_right_group1;
ALTER TABLE right_group DROP INDEX IDX_right_group2;
ALTER TABLE right_area DROP INDEX IDX_right_area1;
ALTER TABLE usergroup DROP INDEX IDX_usergroup1;
ALTER TABLE event_calendar DROP INDEX IDX_event_calendar1;
ALTER TABLE event_calendar DROP INDEX IDX_event_calendar2;
ALTER TABLE event_person DROP INDEX IDX_event_person1;
ALTER TABLE event_person DROP INDEX IDX_event_person2;
ALTER TABLE user_calendar DROP INDEX IDX_user_calendar1;
ALTER TABLE user_calendar DROP INDEX IDX_user_calendar2;
ALTER TABLE rating DROP INDEX IDX_rating1;
ALTER TABLE category DROP INDEX IDX_category1;
ALTER TABLE job DROP INDEX IDX_job1;
ALTER TABLE job DROP INDEX IDX_job2;
ALTER TABLE job DROP INDEX IDX_job3;
ALTER TABLE job DROP INDEX IDX_job4;
ALTER TABLE job DROP INDEX IDX_job5;
ALTER TABLE profile DROP INDEX IDX_profile1;
ALTER TABLE profile DROP INDEX IDX_profile2;
ALTER TABLE profile DROP INDEX IDX_profile3;
ALTER TABLE profile_usergroup DROP INDEX IDX_profile_usergroup1;
ALTER TABLE profile_usergroup DROP INDEX IDX_profile_usergroup2;
ALTER TABLE ldap_group DROP INDEX IDX_ldap_group1;
ALTER TABLE user_login_log DROP INDEX IDX_user_login_log1;
ALTER TABLE configuration DROP INDEX IDX_configuration1;
ALTER TABLE list_configuration DROP INDEX IDX_list_configuration1;

#========================================================================== #
#  Drop Tables                                                              #
#========================================================================== #
DROP TABLE list_configuration;
DROP TABLE configuration;
DROP TABLE user_login_log;
DROP TABLE ldap_group;
DROP TABLE profile_usergroup;
DROP TABLE profile;
DROP TABLE job;
DROP TABLE category;
DROP TABLE rating;
DROP TABLE user_calendar;
DROP TABLE event_person;
DROP TABLE event_calendar;
DROP TABLE usergroup;
DROP TABLE right_area;
DROP TABLE right_group;
DROP TABLE right_role;
DROP TABLE rights;
DROP TABLE role;
DROP TABLE event;
DROP TABLE calendar;
DROP TABLE legal_form_group;
DROP TABLE company_type;
DROP TABLE company_state;
DROP TABLE person_contact;
DROP TABLE contact;
DROP TABLE sector;
DROP TABLE legal_form;
DROP TABLE address;
DROP TABLE company;
DROP TABLE account;
DROP TABLE person;
