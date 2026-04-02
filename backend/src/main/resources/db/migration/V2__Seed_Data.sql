-- ============================================================
-- V2__Seed_Data.sql
-- Default organization, users and checklist templates
-- ============================================================

-- Default organization
INSERT INTO organizations (name) VALUES ('ISP Demo Organization');

-- Default users (passwords are BCrypt hashed)
-- admin@isp.com / Admin@123
-- engineer@isp.com / Engineer@123
INSERT INTO users (email, full_name, password_hash, auth_provider) VALUES
('admin@isp.com',    'System Admin',     '$2a$12$LYjNBh7b6P2ByKvCjGZ1YOnlbYWOmCcJFNjmIRSjMU8l/vTDdRiKe', 'LOCAL'),
('engineer@isp.com', 'Field Engineer',   '$2a$12$9oKl5M3Fz4NQaKmFxHZ.6ugVRQCvLR3oKk4oGFk0YhRl4MPZqiN/y', 'LOCAL');

-- Memberships
INSERT INTO memberships (organization_id, user_id, role) VALUES
(1, 1, 'SUPER_ADMIN'),
(1, 2, 'FIELD_ENGINEER');

-- Sample checklist template — Power & Cooling
INSERT INTO checklist_templates (organization_id, name, scope, version, schema_json, is_active) VALUES
(1, 'Power & Cooling Assessment', 'SPACE', 1,
 JSON_OBJECT(
   'sections', JSON_ARRAY(
     JSON_OBJECT(
       'title', 'Power Availability',
       'questions', JSON_ARRAY(
         JSON_OBJECT('id','q1','type','boolean','label','Is there a dedicated power outlet?','required',true),
         JSON_OBJECT('id','q2','type','number','label','Number of available power sockets','required',true),
         JSON_OBJECT('id','q3','type','boolean','label','UPS/backup power available?','required',false),
         JSON_OBJECT('id','q4','type','select','label','Power phase','options',JSON_ARRAY('Single Phase','Three Phase'),'required',true)
       )
     ),
     JSON_OBJECT(
       'title', 'Cooling & Temperature',
       'questions', JSON_ARRAY(
         JSON_OBJECT('id','q5','type','number','label','Ambient temperature (°C)','required',true),
         JSON_OBJECT('id','q6','type','boolean','label','HVAC present in space?','required',false),
         JSON_OBJECT('id','q7','type','text','label','Cooling notes','required',false)
       )
     )
   )
 ),
 true),

(1, 'Accessibility & Safety Checklist', 'BUILDING', 1,
 JSON_OBJECT(
   'sections', JSON_ARRAY(
     JSON_OBJECT(
       'title', 'Physical Access',
       'questions', JSON_ARRAY(
         JSON_OBJECT('id','q1','type','boolean','label','Key/badge access required?','required',true),
         JSON_OBJECT('id','q2','type','text','label','Access contact name','required',false),
         JSON_OBJECT('id','q3','type','select','label','Access difficulty','options',JSON_ARRAY('Easy','Moderate','Difficult'),'required',true)
       )
     ),
     JSON_OBJECT(
       'title', 'Safety',
       'questions', JSON_ARRAY(
         JSON_OBJECT('id','q4','type','boolean','label','Hazardous materials present?','required',true),
         JSON_OBJECT('id','q5','type','boolean','label','Working at height required?','required',true),
         JSON_OBJECT('id','q6','type','text','label','Safety notes','required',false)
       )
     )
   )
 ),
 true);
