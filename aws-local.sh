#!/bin/bash

export HIVEWING_WEB_RDS_PORT=${HIVEWING_WEB_RDS_PORT-33010}
export HIVEWING_WEB_RDS_ENDPOINT=http://127.0.0.1:$HIVEWING_WEB_RDS_PORT
export HIVEWING_WEB_PROFILE=${HIVEWING_WEB_PROFILE-dev}
export HIVEWING_WEB_UNIQUE_ID="rds-$HIVEWING_WEB_RDS_PORT"
export AWS_LOCAL_FILENAME=".aws-docker-containers.$HIVEWING_WEB_PROFILE.$HIVEWING_WEB_UNIQUE_ID"

wait_for_processes() {
  echo "Waiting..."
  echo "waiting for things to spin up..."
  until nc -z localhost $HIVEWING_WEB_RDS_PORT </dev/null; do sleep 0.1; done
  sleep 4
  echo "Ports are open!"
}

start_aws_local() {
  echo "Starting RDS at $HIVEWING_WEB_RDS_PORT"
  PG_GUID=`sudo docker run -d -e POSTGRES_PASSWORD=hivewing -e POSTGRES_USER=hivewing -p $HIVEWING_WEB_RDS_PORT:5432 postgres`

  wait_for_processes

  echo "Migrating ragtime with profile $HIVEWING_WEB_PROFILE"
  lein with-profile $HIVEWING_WEB_PROFILE ragtime migrate -d "jdbc:postgresql://127.0.0.1:$HIVEWING_WEB_RDS_PORT/hivewing?user=hivewing&password=hivewing"

  echo "HIVEWING_WEB_RDS_PORT = $HIVEWING_WEB_RDS_PORT"
  echo "$PG_GUID" > $AWS_LOCAL_FILENAME
}

stop_aws_local() {
  sudo docker rm -f `cat $AWS_LOCAL_FILENAME`
}

if [ "$1" == "start" ]; then
  echo "Starting AWS Local..."
  stop_aws_local
  start_aws_local
elif [ "$1" == "stop" ]; then
  echo "Stopping AWS Local..."
  stop_aws_local
else
  echo "Need command [start, stop]"
fi
