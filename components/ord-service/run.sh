#!/usr/bin/env bash

COMPONENT_DIR="$(pwd)/$(dirname $0)"

source "$COMPONENT_DIR/scripts/commons.sh"

NO_START=false
SKIP_TESTS=false
SKIP_DEPS=false

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
            shift
        ;;
        --*)
            echo "Unknown flag ${1}"
            exit 1
        ;;
    esac
done

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
    echo "AUTO_TERMINATE=${AUTO_TERMINATE}"
    if [[  ${AUTO_TERMINATE} == true ]]; then
        MAIN_APP_LOGFILE=main.log
        if [[ ${ARTIFACTS} ]]; then
           MAIN_APP_LOGFILE=${ARTIFACTS}/main.log
        fi

        java -jar "$COMPONENT_DIR/target/ord-service-$ARTIFACT_VERSION.jar" > ${MAIN_APP_LOGFILE} &
        MAIN_PROCESS_PID="$!"
        echo "MAIN_PROCESS_PID=$MAIN_PROCESS_PID"

        START_TIME=$(date +%s)
        SECONDS=0
        while (( SECONDS < 300 )) ; do
            CURRENT_TIME=$(date +%s)
            SECONDS=$((CURRENT_TIME-START_TIME))
            echo "Wait 10s ..."
            sleep 10
        done
        
        echo "Timeout of 5 min for running ord-service reached. Killing the process."
        echo -e "${GREEN}Kill main process..."
        kill -SIGTERM "${MAIN_PROCESS_PID}"
        echo -e "${GREEN}Delete build result and log..."
        rm main.log || true
        wait
    else 
        java -jar "$COMPONENT_DIR/target/ord-service-$ARTIFACT_VERSION.jar"
    fi
fi