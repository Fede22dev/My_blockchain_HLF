fablo prune
echo "====================================FABLO PRUNE DONE===================================="
# shellcheck disable=SC2046
docker rm -f $(docker ps -a -q)
echo "===========================DOCKER REMOVE ALL COINTAINERS DONE==========================="
# shellcheck disable=SC2046
docker volume rm $(docker volume ls -q)
echo "=============================DOCKER REMOVE ALL VOLUMES DONE============================="
