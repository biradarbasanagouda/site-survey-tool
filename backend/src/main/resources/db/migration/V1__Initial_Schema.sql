-- ============================================================
-- V1__Initial_Schema.sql
-- Site Survey Tool — Complete Database Schema
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------------------------------------
-- Organizations
-- -------------------------------------------------------
CREATE TABLE organizations (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- -------------------------------------------------------
-- Users
-- -------------------------------------------------------
CREATE TABLE users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    email           VARCHAR(320) NOT NULL UNIQUE,
    full_name       VARCHAR(300) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    auth_provider   VARCHAR(100) NOT NULL DEFAULT 'LOCAL',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
);

-- -------------------------------------------------------
-- Memberships (User ↔ Organization with Role)
-- -------------------------------------------------------
CREATE TABLE memberships (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    user_id         BIGINT NOT NULL,
    role            ENUM('SUPER_ADMIN','ORG_ADMIN','PROJECT_MANAGER','FIELD_ENGINEER','VIEWER') NOT NULL DEFAULT 'VIEWER',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_membership (organization_id, user_id),
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- -------------------------------------------------------
-- Refresh Tokens
-- -------------------------------------------------------
CREATE TABLE refresh_tokens (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    token       VARCHAR(512) NOT NULL UNIQUE,
    expires_at  DATETIME NOT NULL,
    revoked     BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_refresh_token (token)
);

-- -------------------------------------------------------
-- Files
-- -------------------------------------------------------
CREATE TABLE files (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_type      VARCHAR(50) NOT NULL,
    owner_id        BIGINT NOT NULL,
    filename        VARCHAR(255) NOT NULL,
    content_type    VARCHAR(120),
    storage_key     VARCHAR(512) NOT NULL,
    size_bytes      BIGINT,
    checksum_sha256 CHAR(64),
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_files_owner (owner_type, owner_id)
);

-- -------------------------------------------------------
-- Properties (Sites)
-- -------------------------------------------------------
CREATE TABLE properties (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    name            VARCHAR(280) NOT NULL,
    address_line1   VARCHAR(280),
    address_line2   VARCHAR(280),
    city            VARCHAR(100),
    state           VARCHAR(100),
    postal_code     VARCHAR(40),
    country         VARCHAR(100),
    boundary_type   ENUM('POLYGON','MULTIPOLYGON','POINT') DEFAULT 'POLYGON',
    boundary_wkt    TEXT,
    centroid_lat    DECIMAL(10,7),
    centroid_lon    DECIMAL(10,7),
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE,
    INDEX idx_properties_org (organization_id)
);

-- -------------------------------------------------------
-- Buildings
-- -------------------------------------------------------
CREATE TABLE buildings (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id     BIGINT NOT NULL,
    name            VARCHAR(280) NOT NULL,
    code            VARCHAR(50),
    footprint_type  ENUM('POLYGON','MULTIPOLYGON','POINT') DEFAULT 'POLYGON',
    footprint_wkt   TEXT,
    floors_count    INT NOT NULL DEFAULT 1,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
    INDEX idx_buildings_property (property_id)
);

-- -------------------------------------------------------
-- Floors
-- -------------------------------------------------------
CREATE TABLE floors (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    building_id     BIGINT NOT NULL,
    level_label     VARCHAR(50) NOT NULL,
    elevation_m     DECIMAL(8,2) DEFAULT 0.00,
    plan_file_id    BIGINT,
    scale_ratio     VARCHAR(50),
    anchor_points   JSON,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (building_id) REFERENCES buildings(id) ON DELETE CASCADE,
    FOREIGN KEY (plan_file_id) REFERENCES files(id) ON DELETE SET NULL,
    INDEX idx_floors_building (building_id)
);

-- -------------------------------------------------------
-- Spaces
-- -------------------------------------------------------
CREATE TABLE spaces (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    floor_id        BIGINT NOT NULL,
    name            VARCHAR(280) NOT NULL,
    type            ENUM('APARTMENT','OFFICE','SERVER_ROOM','UTILITY','CORRIDOR','LOBBY','ROOFTOP','BASEMENT','PARKING','OTHER') DEFAULT 'OTHER',
    geometry_type   ENUM('POLYGON','POINT','LINESTRING') DEFAULT 'POLYGON',
    geometry_wkt    TEXT,
    area_sq_m       DECIMAL(10,2),
    elevation_m     DECIMAL(8,2),
    notes           TEXT,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (floor_id) REFERENCES floors(id) ON DELETE CASCADE,
    INDEX idx_spaces_floor (floor_id)
);

-- -------------------------------------------------------
-- Equipment
-- -------------------------------------------------------
CREATE TABLE equipment (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    space_id        BIGINT NOT NULL,
    type            ENUM('ROUTER','SWITCH','ACCESS_POINT','ANTENNA','OLT','ONT','CABINET','PATCH_PANEL','AMPLIFIER','SPLITTER','OTHER') NOT NULL,
    model           VARCHAR(280),
    vendor          VARCHAR(200),
    power_watts     DECIMAL(10,2),
    heat_load_btuh  DECIMAL(10,2),
    mounting        VARCHAR(100),
    geometry_type   ENUM('POINT','POLYGON') DEFAULT 'POINT',
    geometry_wkt    TEXT,
    serial_number   VARCHAR(120),
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (space_id) REFERENCES spaces(id) ON DELETE CASCADE,
    INDEX idx_equipment_space (space_id)
);

-- -------------------------------------------------------
-- Cable Paths
-- -------------------------------------------------------
CREATE TABLE cable_paths (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id     BIGINT NOT NULL,
    from_space_id   BIGINT,
    to_space_id     BIGINT,
    medium          ENUM('FIBER_SM','FIBER_MM','CAT6','CAT6A','CAT7','COAX','AERIAL','UNDERGROUND','OTHER') NOT NULL,
    length_m        DECIMAL(10,2),
    slack_loops     INT DEFAULT 0,
    splice_points   INT DEFAULT 0,
    geometry_type   ENUM('LINESTRING','MULTILINESTRING') DEFAULT 'LINESTRING',
    geometry_wkt    TEXT,
    notes           TEXT,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
    FOREIGN KEY (from_space_id) REFERENCES spaces(id) ON DELETE SET NULL,
    FOREIGN KEY (to_space_id) REFERENCES spaces(id) ON DELETE SET NULL,
    INDEX idx_cable_property (property_id)
);

-- -------------------------------------------------------
-- Splice Points
-- -------------------------------------------------------
CREATE TABLE splice_points (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    cable_path_id   BIGINT NOT NULL,
    geometry_type   ENUM('POINT') DEFAULT 'POINT',
    geometry_wkt    TEXT,
    enclosure_id    BIGINT,
    notes           TEXT,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cable_path_id) REFERENCES cable_paths(id) ON DELETE CASCADE
);

-- -------------------------------------------------------
-- Attachments (polymorphic)
-- -------------------------------------------------------
CREATE TABLE attachments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_type  VARCHAR(50) NOT NULL,
    owner_id    BIGINT NOT NULL,
    file_id     BIGINT NOT NULL,
    tags        VARCHAR(200),
    metadata    JSON,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE,
    INDEX idx_attachments_owner (owner_type, owner_id)
);

-- -------------------------------------------------------
-- Checklist Templates
-- -------------------------------------------------------
CREATE TABLE checklist_templates (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    organization_id BIGINT NOT NULL,
    name            VARCHAR(200) NOT NULL,
    scope           VARCHAR(50) NOT NULL COMMENT 'PROPERTY|BUILDING|FLOOR|SPACE|EQUIPMENT',
    version         INT NOT NULL DEFAULT 1,
    schema_json     JSON NOT NULL,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE,
    INDEX idx_templates_org (organization_id)
);

-- -------------------------------------------------------
-- Checklist Responses
-- -------------------------------------------------------
CREATE TABLE checklist_responses (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_id     BIGINT NOT NULL,
    target_type     VARCHAR(50) NOT NULL,
    target_id       BIGINT NOT NULL,
    answers_json    JSON,
    photos_manifest JSON,
    submitted_by    BIGINT,
    submitted_at    DATETIME,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (template_id) REFERENCES checklist_templates(id) ON DELETE CASCADE,
    FOREIGN KEY (submitted_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_responses_target (target_type, target_id)
);

-- -------------------------------------------------------
-- RF Scans
-- -------------------------------------------------------
CREATE TABLE rf_scans (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id     BIGINT NOT NULL,
    floor_id        BIGINT,
    tool            ENUM('VISTUMBLER','KISMET','SPLAT','MANUAL','OTHER') NOT NULL DEFAULT 'MANUAL',
    raw_file_id     BIGINT,
    parsed_json     JSON,
    heatmap_file_id BIGINT,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
    FOREIGN KEY (floor_id) REFERENCES floors(id) ON DELETE SET NULL,
    FOREIGN KEY (raw_file_id) REFERENCES files(id) ON DELETE SET NULL,
    FOREIGN KEY (heatmap_file_id) REFERENCES files(id) ON DELETE SET NULL,
    INDEX idx_rfscans_property (property_id)
);

-- -------------------------------------------------------
-- Reports
-- -------------------------------------------------------
CREATE TABLE reports (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id     BIGINT NOT NULL,
    requested_by    BIGINT NOT NULL,
    parameters      JSON,
    pdf_file_id     BIGINT,
    status          VARCHAR(30) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING|GENERATING|DONE|FAILED',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
    FOREIGN KEY (requested_by) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (pdf_file_id) REFERENCES files(id) ON DELETE SET NULL,
    INDEX idx_reports_property (property_id)
);

-- -------------------------------------------------------
-- Audit Logs
-- -------------------------------------------------------
CREATE TABLE audit_logs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    actor_id    BIGINT,
    action      VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id   BIGINT,
    change_set  JSON,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (actor_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_audit_entity (entity_type, entity_id),
    INDEX idx_audit_actor (actor_id)
);

SET FOREIGN_KEY_CHECKS = 1;
