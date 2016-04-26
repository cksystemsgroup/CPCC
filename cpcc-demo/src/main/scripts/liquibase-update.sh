#!/bin/bash
# -----------------------------------------------------------------------------
# @(#) liquibase-update.sh - Liquibase update script
# -----------------------------------------------------------------------------
#
# Usage: liquibase-update.sh dbUrl
#

URL="$1";

. $(dirname $0)/profile.sh

[ "x$*" = "x" ] && die "Usage:  $(basename $0) dbUrl";

cd $CPCC_DIR/classes;

# CP="$CPCC_DIR/lib/hsqldb-${COM_HSQLDB_VERSION}.jar:$CPCC_DIR/lib/liquibase-core-${LIQUIBASE_VERSION}.jar:$CPCC_DIR/lib/snakeyaml-${SNAKEYAML_VERSION}.jar";
# DRV='org.hsqldb.jdbc.JDBCDriver';

CP="$CPCC_DIR/lib/h2-${COM_H2DB_VERSION}.jar:$CPCC_DIR/lib/liquibase-core-${LIQUIBASE_VERSION}.jar:$CPCC_DIR/lib/snakeyaml-${SNAKEYAML_VERSION}.jar";
DRV='org.h2.Driver';

java -classpath $CP liquibase.integration.commandline.Main \
        --driver="$DRV" \
        --url="$URL" \
        --username='sa' \
        --password='' \
        --changeLogFile=dbchange/update.xml \
    update