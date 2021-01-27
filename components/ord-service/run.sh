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
        --*)
            echo "Unknown flag ${1}"
            exit 1
        ;;
    esac
done

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
    java -jar "$COMPONENT_DIR/target/ord-service-$ARTIFACT_VERSION.jar"
fi