#!/bin/bash
set -o errexit

main () {

    script_path=$(pwd)/sql:/data/
    docker run -d --name esports-mysql  -v $script_path -p 3308:3306  -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=ebotv3 -e MYSQL_USER=ebotv3 -e MYSQL_PASSWORD=ebotv3 mysql/mysql-server:latest

    sleep 15
    # docker exec -it esports-mysql sh -c 'mysql  -uroot -p"$MYSQL_ROOT_PASSWORD" ebotv3 < /data/schema.sql'
    docker exec -it esports-mysql sh -c 'mysql  -uroot -p"$MYSQL_ROOT_PASSWORD" ebotv3 < /data/CSGO_Finland_Ebot.sql'

}

main "$@"
