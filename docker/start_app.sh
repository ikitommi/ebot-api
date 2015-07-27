#!/bin/bash
set -o errexit

main () {
    echo "Starting cassandra $script_path"
    docker run -d --name ebot-api --link esports-mysql:mysql -p 3000:3000  ebot-api

}

main "$@"
