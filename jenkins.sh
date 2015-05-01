#!/bin/bash
if [ ! -f aws-local.sh ]; then
  echo "aws-local File not found!"
  exit 1
fi

export HIVEWING_WEB_RDS_PORT=`ruby -e "print 11000 + ENV['BUILD_NUMBER'].to_i % 50"`
export JDBC_CONNECTION_STRING="jdbc:postgresql://127.0.0.1:$HIVEWING_WEB_RDS_PORT/hivewing?user=hivewing&password=hivewing"

bash aws-local.sh start

lein with-profile test ragtime migrate -d "$JDBC_CONNECTION_STRING"

echo
echo
echo "===== Running Tests ===== "
echo
echo

lein test
test_exit_code=$?

echo
echo
echo "===== Finished Tests ===== "
echo
echo
bash aws-local.sh stop

echo 'Exiting...'
exit "$test_exit_code"
