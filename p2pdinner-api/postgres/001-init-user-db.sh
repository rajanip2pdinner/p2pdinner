#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username="$POSTGRES_USER" <<-EOSOL
    CREATE USER p2pdinner;
    CREATE DATABASE p2pdinner;
    GRANT ALL PRIVILEGES ON DATABASE p2pdinner TO p2pdinner;
EOSOL