#!/usr/bin/env bash

source "$FABLO_NETWORK_ROOT/fabric-docker/scripts/channel-query-functions.sh"

set -eu

channelQuery() {
  echo "-> Channel query: " + "$@"

  if [ "$#" -eq 1 ]; then
    printChannelsHelp

  elif [ "$1" = "list" ] && [ "$2" = "landlord1" ] && [ "$3" = "peer0" ]; then

    peerChannelListTls "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "tenant1" ] && [ "$3" = "peer0" ]
  then

    peerChannelListTls "cli.tenant1.project.it" "peer0.tenant1.project.it:7061" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif
    [ "$1" = "list" ] && [ "$2" = "guest1" ] && [ "$3" = "peer0" ]
  then

    peerChannelListTls "cli.guest1.project.it" "peer0.guest1.project.it:7081" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif

    [ "$1" = "getinfo" ] && [ "$2" = "home1" ] && [ "$3" = "landlord1" ] && [ "$4" = "peer0" ]
  then

    peerChannelGetInfoTls "home1" "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "home1" ] && [ "$4" = "landlord1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchConfigTls "home1" "cli.landlord1.project.it" "${FILE_NAME}" "peer0.landlord1.project.it:7041" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "lastBlock" ] && [ "$3" = "home1" ] && [ "$4" = "landlord1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchLastBlockTls "home1" "cli.landlord1.project.it" "${FILE_NAME}" "peer0.landlord1.project.it:7041" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "firstBlock" ] && [ "$3" = "home1" ] && [ "$4" = "landlord1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchFirstBlockTls "home1" "cli.landlord1.project.it" "${FILE_NAME}" "peer0.landlord1.project.it:7041" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "block" ] && [ "$3" = "home1" ] && [ "$4" = "landlord1" ] && [ "$5" = "peer0" ] && [ "$#" = 8 ]; then
    FILE_NAME=$6
    BLOCK_NUMBER=$7

    peerChannelFetchBlockTls "home1" "cli.landlord1.project.it" "${FILE_NAME}" "${BLOCK_NUMBER}" "peer0.landlord1.project.it:7041" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "home1" ] && [ "$3" = "tenant1" ] && [ "$4" = "peer0" ]
  then

    peerChannelGetInfoTls "home1" "cli.tenant1.project.it" "peer0.tenant1.project.it:7061" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "home1" ] && [ "$4" = "tenant1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchConfigTls "home1" "cli.tenant1.project.it" "${FILE_NAME}" "peer0.tenant1.project.it:7061" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "lastBlock" ] && [ "$3" = "home1" ] && [ "$4" = "tenant1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchLastBlockTls "home1" "cli.tenant1.project.it" "${FILE_NAME}" "peer0.tenant1.project.it:7061" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "firstBlock" ] && [ "$3" = "home1" ] && [ "$4" = "tenant1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchFirstBlockTls "home1" "cli.tenant1.project.it" "${FILE_NAME}" "peer0.tenant1.project.it:7061" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "block" ] && [ "$3" = "home1" ] && [ "$4" = "tenant1" ] && [ "$5" = "peer0" ] && [ "$#" = 8 ]; then
    FILE_NAME=$6
    BLOCK_NUMBER=$7

    peerChannelFetchBlockTls "home1" "cli.tenant1.project.it" "${FILE_NAME}" "${BLOCK_NUMBER}" "peer0.tenant1.project.it:7061" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif
    [ "$1" = "getinfo" ] && [ "$2" = "home1" ] && [ "$3" = "guest1" ] && [ "$4" = "peer0" ]
  then

    peerChannelGetInfoTls "home1" "cli.guest1.project.it" "peer0.guest1.project.it:7081" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "config" ] && [ "$3" = "home1" ] && [ "$4" = "guest1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchConfigTls "home1" "cli.guest1.project.it" "${FILE_NAME}" "peer0.guest1.project.it:7081" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "lastBlock" ] && [ "$3" = "home1" ] && [ "$4" = "guest1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchLastBlockTls "home1" "cli.guest1.project.it" "${FILE_NAME}" "peer0.guest1.project.it:7081" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "firstBlock" ] && [ "$3" = "home1" ] && [ "$4" = "guest1" ] && [ "$5" = "peer0" ] && [ "$#" = 7 ]; then
    FILE_NAME=$6

    peerChannelFetchFirstBlockTls "home1" "cli.guest1.project.it" "${FILE_NAME}" "peer0.guest1.project.it:7081" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  elif [ "$1" = "fetch" ] && [ "$2" = "block" ] && [ "$3" = "home1" ] && [ "$4" = "guest1" ] && [ "$5" = "peer0" ] && [ "$#" = 8 ]; then
    FILE_NAME=$6
    BLOCK_NUMBER=$7

    peerChannelFetchBlockTls "home1" "cli.guest1.project.it" "${FILE_NAME}" "${BLOCK_NUMBER}" "peer0.guest1.project.it:7081" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  else

    printChannelsHelp
  fi

}

printChannelsHelp() {
  echo "Channel management commands:"
  echo ""

  echo "fablo channel list landlord1 peer0"
  echo -e "\t List channels on 'peer0' of 'Landlord1'".
  echo ""

  echo "fablo channel list tenant1 peer0"
  echo -e "\t List channels on 'peer0' of 'Tenant1'".
  echo ""

  echo "fablo channel list guest1 peer0"
  echo -e "\t List channels on 'peer0' of 'Guest1'".
  echo ""

  echo "fablo channel getinfo home1 landlord1 peer0"
  echo -e "\t Get channel info on 'peer0' of 'Landlord1'".
  echo ""
  echo "fablo channel fetch config home1 landlord1 peer0 <fileName.json>"
  echo -e "\t Download latest config block to current dir. Uses first peer 'peer0' of 'Landlord1'".
  echo ""
  echo "fablo channel fetch lastBlock home1 landlord1 peer0 <fileName.json>"
  echo -e "\t Download last, decrypted block to current dir. Uses first peer 'peer0' of 'Landlord1'".
  echo ""
  echo "fablo channel fetch firstBlock home1 landlord1 peer0 <fileName.json>"
  echo -e "\t Download first, decrypted block to current dir. Uses first peer 'peer0' of 'Landlord1'".
  echo ""

  echo "fablo channel getinfo home1 tenant1 peer0"
  echo -e "\t Get channel info on 'peer0' of 'Tenant1'".
  echo ""
  echo "fablo channel fetch config home1 tenant1 peer0 <fileName.json>"
  echo -e "\t Download latest config block to current dir. Uses first peer 'peer0' of 'Tenant1'".
  echo ""
  echo "fablo channel fetch lastBlock home1 tenant1 peer0 <fileName.json>"
  echo -e "\t Download last, decrypted block to current dir. Uses first peer 'peer0' of 'Tenant1'".
  echo ""
  echo "fablo channel fetch firstBlock home1 tenant1 peer0 <fileName.json>"
  echo -e "\t Download first, decrypted block to current dir. Uses first peer 'peer0' of 'Tenant1'".
  echo ""

  echo "fablo channel getinfo home1 guest1 peer0"
  echo -e "\t Get channel info on 'peer0' of 'Guest1'".
  echo ""
  echo "fablo channel fetch config home1 guest1 peer0 <fileName.json>"
  echo -e "\t Download latest config block to current dir. Uses first peer 'peer0' of 'Guest1'".
  echo ""
  echo "fablo channel fetch lastBlock home1 guest1 peer0 <fileName.json>"
  echo -e "\t Download last, decrypted block to current dir. Uses first peer 'peer0' of 'Guest1'".
  echo ""
  echo "fablo channel fetch firstBlock home1 guest1 peer0 <fileName.json>"
  echo -e "\t Download first, decrypted block to current dir. Uses first peer 'peer0' of 'Guest1'".
  echo ""

}
