-- Crear esquemas
CREATE SCHEMA IF NOT EXISTS enanos_schema;
CREATE SCHEMA IF NOT EXISTS photos_schema;
CREATE SCHEMA IF NOT EXISTS orders_schema;

-- Verificar
SELECT schema_name FROM information_schema.schemata;