fablo prune
echo "========================== \ō͡≡o˞̶️  FABLO PRUNE |DONE|  \ō͡≡o˞̶ =========================="
# shellcheck disable=SC2046
docker rm -f $(docker ps -a -q)
echo "================= \ō͡≡o˞̶  DOCKER REMOVE ALL COINTAINERS |DONE|  \ō͡≡o˞̶ ================="
# shellcheck disable=SC2046
docker volume rm $(docker volume ls -q)
echo "=================== \ō͡≡o˞̶  DOCKER REMOVE ALL VOLUMES |DONE|  \ō͡≡o˞̶ ==================="
