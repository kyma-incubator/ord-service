#!/usr/bin/env bash

COMPONENT_DIR="$(pwd)/$(dirname $0)"

source "$COMPONENT_DIR/scripts/commons.sh"

# exit on error
set -e

NO_START=false
SKIP_TESTS=false
SKIP_DEPS=false
TERMINAION_TIMEOUT_IN_SECONDS=300

while [[ $# -gt 0 ]]
do
    key="$1"

    case ${key} in
        --no-start)
            NO_START=true
            shift # past argument
        ;;
        --skip-tests)
            SKIP_TESTS=true
            shift # past argument
        ;;
        --skip-deps)
            SKIP_DEPS=true
            shift # past argument
        ;;
        --migrations-path)
            MIGRATIONS_PATH=$2
            shift
            shift
        ;;
        --auto-terminate)
            AUTO_TERMINATE=true
            TERMINAION_TIMEOUT_IN_SECONDS=$2
            shift
            shift
        ;;
        --*)
            echo "Unknown flag ${1}"
            exit 1
        ;;
    esac
done

# Create dummy service account token file
touch /tmp/ord-service-account.txt

export SCHEMA_MIGRATION_VERSION=$(ls -lr $MIGRATIONS_PATH | head -n 2 | tail -n 1 | tr -s ' ' | cut -d ' ' -f9 | cut -d '_' -f1)

echo "Expected schema version" $SCHEMA_MIGRATION_VERSION

if [[ ${SKIP_DEPS} = false ]]; then
    source "$COMPONENT_DIR/scripts/install_dependencies.sh"
fi

cd $COMPONENT_DIR
if [[ ${SKIP_TESTS} = true ]]; then
    log_section "Proceeding with installing Open Resource Discovery Service without running tests..."
    source "$COMPONENT_DIR/scripts/build.sh" "-DskipTests"
else
    log_section "Proceeding with installing Open Resource Discovery Service..."
    source "$COMPONENT_DIR/scripts/build.sh"
fi

if [[ ${NO_START} = true ]]; then
    log_section "Skipping start of Open Resource Discovery Service."
else
    log_section "Starting Open Resource Discovery Service..."
    if [[  ${AUTO_TERMINATE} == true ]]; then
        log_section "Auto termination is set for ${TERMINAION_TIMEOUT_IN_SECONDS} seconds ..."
        MAIN_APP_LOGFILE=$COMPONENT_DIR/target/main.log

        java -jar "$COMPONENT_DIR/target/ord-service-$ARTIFACT_VERSION.jar" > ${MAIN_APP_LOGFILE} &
        MAIN_PROCESS_PID="$!"

        START_TIME=$(date +%s)
        SECONDS=0
        while (( SECONDS < ${TERMINAION_TIMEOUT_IN_SECONDS} )) ; do
            CURRENT_TIME=$(date +%s)
            SECONDS=$((CURRENT_TIME-START_TIME))
            SECONDS_LEFT=$((TERMINAION_TIMEOUT_IN_SECONDS-SECONDS))
            echo "[ORD Service] left ${SECONDS_LEFT} seconds. Wait ..."
            sleep 10
        done

        echo "Timeout of ${TERMINAION_TIMEOUT_IN_SECONDS} seconds for running ord-service reached. Killing the process."
        echo -e "${GREEN}Kill main process..."
        kill -SIGTERM "${MAIN_PROCESS_PID}"
        wait
    else
        java -jar "$COMPONENT_DIR/target/ord-service-$ARTIFACT_VERSION.jar"
    fi
fi