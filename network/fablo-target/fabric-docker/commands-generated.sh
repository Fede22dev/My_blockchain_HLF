#!/usr/bin/env bash

generateArtifacts() {
  printHeadline "Generating basic configs" "U1F913"

  printItalics "Generating crypto material for Orderer" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-orderer.yaml" "peerOrganizations/orderer.project.it" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating crypto material for Landlord1" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-landlord1.yaml" "peerOrganizations/landlord1.project.it" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating crypto material for Tenant1" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-tenant1.yaml" "peerOrganizations/tenant1.project.it" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating crypto material for Guest1" "U1F512"
  certsGenerate "$FABLO_NETWORK_ROOT/fabric-config" "crypto-config-guest1.yaml" "peerOrganizations/guest1.project.it" "$FABLO_NETWORK_ROOT/fabric-config/crypto-config/"

  printItalics "Generating genesis block for group group1" "U1F3E0"
  genesisBlockCreate "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config" "Group1Genesis"

  # Create directory for chaincode packages to avoid permission errors on linux
  mkdir -p "$FABLO_NETWORK_ROOT/fabric-config/chaincode-packages"
}

startNetwork() {
  printHeadline "Starting network" "U1F680"
  (cd "$FABLO_NETWORK_ROOT"/fabric-docker && docker-compose up -d)
  sleep 4
}

generateChannelsArtifacts() {
  printHeadline "Generating config for 'home1'" "U1F913"
  createChannelTx "home1" "$FABLO_NETWORK_ROOT/fabric-config" "Home1" "$FABLO_NETWORK_ROOT/fabric-config/config"
}

installChannels() {
  printHeadline "Creating 'home1' on Landlord1/peer0" "U1F63B"
  docker exec -i cli.landlord1.project.it bash -c "source scripts/channel_fns.sh; createChannelAndJoinTls 'home1' 'Landlord1MSP' 'peer0.landlord1.project.it:7041' 'crypto/users/Admin@landlord1.project.it/msp' 'crypto/users/Admin@landlord1.project.it/tls' 'crypto-orderer/tlsca.orderer.project.it-cert.pem' 'orderer0.group1.orderer.project.it:7030';"

  printItalics "Joining 'home1' on  Tenant1/peer0" "U1F638"
  docker exec -i cli.tenant1.project.it bash -c "source scripts/channel_fns.sh; fetchChannelAndJoinTls 'home1' 'Tenant1MSP' 'peer0.tenant1.project.it:7061' 'crypto/users/Admin@tenant1.project.it/msp' 'crypto/users/Admin@tenant1.project.it/tls' 'crypto-orderer/tlsca.orderer.project.it-cert.pem' 'orderer0.group1.orderer.project.it:7030';"
  printItalics "Joining 'home1' on  Guest1/peer0" "U1F638"
  docker exec -i cli.guest1.project.it bash -c "source scripts/channel_fns.sh; fetchChannelAndJoinTls 'home1' 'Guest1MSP' 'peer0.guest1.project.it:7081' 'crypto/users/Admin@guest1.project.it/msp' 'crypto/users/Admin@guest1.project.it/tls' 'crypto-orderer/tlsca.orderer.project.it-cert.pem' 'orderer0.group1.orderer.project.it:7030';"
}

installChaincodes() {
  if [ -n "$(ls "$CHAINCODES_BASE_DIR/../house-supervisor-chaincode")" ]; then
    local version="0.0.1"
    printHeadline "Packaging chaincode 'chaincode1'" "U1F60E"
    chaincodeBuild "chaincode1" "java" "$CHAINCODES_BASE_DIR/../house-supervisor-chaincode"
    chaincodePackage "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "chaincode1" "$version" "java" printHeadline "Installing 'chaincode1' for Landlord1" "U1F60E"
    chaincodeInstall "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.project.it-cert.pem"
    chaincodeApprove "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "home1" "chaincode1" "$version" "orderer0.group1.orderer.project.it:7030" "OR('Landlord1MSP.member', 'Tenant1MSP.member', 'Guest1MSP.member')" "false" "crypto-orderer/tlsca.orderer.project.it-cert.pem" ""
    printHeadline "Installing 'chaincode1' for Tenant1" "U1F60E"
    chaincodeInstall "cli.tenant1.project.it" "peer0.tenant1.project.it:7061" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.project.it-cert.pem"
    chaincodeApprove "cli.tenant1.project.it" "peer0.tenant1.project.it:7061" "home1" "chaincode1" "$version" "orderer0.group1.orderer.project.it:7030" "OR('Landlord1MSP.member', 'Tenant1MSP.member', 'Guest1MSP.member')" "false" "crypto-orderer/tlsca.orderer.project.it-cert.pem" ""
    printHeadline "Installing 'chaincode1' for Guest1" "U1F60E"
    chaincodeInstall "cli.guest1.project.it" "peer0.guest1.project.it:7081" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.project.it-cert.pem"
    chaincodeApprove "cli.guest1.project.it" "peer0.guest1.project.it:7081" "home1" "chaincode1" "$version" "orderer0.group1.orderer.project.it:7030" "OR('Landlord1MSP.member', 'Tenant1MSP.member', 'Guest1MSP.member')" "false" "crypto-orderer/tlsca.orderer.project.it-cert.pem" ""
    printItalics "Committing chaincode 'chaincode1' on channel 'home1' as 'Landlord1'" "U1F618"
    chaincodeCommit "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "home1" "chaincode1" "$version" "orderer0.group1.orderer.project.it:7030" "OR('Landlord1MSP.member', 'Tenant1MSP.member', 'Guest1MSP.member')" "false" "crypto-orderer/tlsca.orderer.project.it-cert.pem" "peer0.landlord1.project.it:7041,peer0.tenant1.project.it:7061,peer0.guest1.project.it:7081" "crypto-peer/peer0.landlord1.project.it/tls/ca.crt,crypto-peer/peer0.tenant1.project.it/tls/ca.crt,crypto-peer/peer0.guest1.project.it/tls/ca.crt" ""
  else
    echo "Warning! Skipping chaincode 'chaincode1' installation. Chaincode directory is empty."
    echo "Looked in dir: '$CHAINCODES_BASE_DIR/../house-supervisor-chaincode'"
  fi

}

notifyOrgsAboutChannels() {
  printHeadline "Creating new channel config blocks" "U1F537"
  createNewChannelUpdateTx "home1" "Landlord1MSP" "Home1" "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config"
  createNewChannelUpdateTx "home1" "Tenant1MSP" "Home1" "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config"
  createNewChannelUpdateTx "home1" "Guest1MSP" "Home1" "$FABLO_NETWORK_ROOT/fabric-config" "$FABLO_NETWORK_ROOT/fabric-config/config"

  printHeadline "Notyfing orgs about channels" "U1F4E2"
  notifyOrgAboutNewChannelTls "home1" "Landlord1MSP" "cli.landlord1.project.it" "peer0.landlord1.project.it" "orderer0.group1.orderer.project.it:7030" "crypto-orderer/tlsca.orderer.project.it-cert.pem"
  notifyOrgAboutNewChannelTls "home1" "Tenant1MSP" "cli.tenant1.project.it" "peer0.tenant1.project.it" "orderer0.group1.orderer.project.it:7030" "crypto-orderer/tlsca.orderer.project.it-cert.pem"
  notifyOrgAboutNewChannelTls "home1" "Guest1MSP" "cli.guest1.project.it" "peer0.guest1.project.it" "orderer0.group1.orderer.project.it:7030" "crypto-orderer/tlsca.orderer.project.it-cert.pem"

  printHeadline "Deleting new channel config blocks" "U1F52A"
  deleteNewChannelUpdateTx "home1" "Landlord1MSP" "cli.landlord1.project.it"
  deleteNewChannelUpdateTx "home1" "Tenant1MSP" "cli.tenant1.project.it"
  deleteNewChannelUpdateTx "home1" "Guest1MSP" "cli.guest1.project.it"
}

upgradeChaincode() {
  local chaincodeName="$1"
  if [ -z "$chaincodeName" ]; then
    echo "Error: chaincode name is not provided"
    exit 1
  fi

  local version="$2"
  if [ -z "$version" ]; then
    echo "Error: chaincode version is not provided"
    exit 1
  fi

  if [ "$chaincodeName" = "chaincode1" ]; then
    if [ -n "$(ls "$CHAINCODES_BASE_DIR/../house-supervisor-chaincode")" ]; then
      printHeadline "Packaging chaincode 'chaincode1'" "U1F60E"
      chaincodeBuild "chaincode1" "java" "$CHAINCODES_BASE_DIR/../house-supervisor-chaincode"
      chaincodePackage "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "chaincode1" "$version" "java" printHeadline "Installing 'chaincode1' for Landlord1" "U1F60E"
      chaincodeInstall "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.project.it-cert.pem"
      chaincodeApprove "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "home1" "chaincode1" "$version" "orderer0.group1.orderer.project.it:7030" "OR('Landlord1MSP.member', 'Tenant1MSP.member', 'Guest1MSP.member')" "false" "crypto-orderer/tlsca.orderer.project.it-cert.pem" ""
      printHeadline "Installing 'chaincode1' for Tenant1" "U1F60E"
      chaincodeInstall "cli.tenant1.project.it" "peer0.tenant1.project.it:7061" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.project.it-cert.pem"
      chaincodeApprove "cli.tenant1.project.it" "peer0.tenant1.project.it:7061" "home1" "chaincode1" "$version" "orderer0.group1.orderer.project.it:7030" "OR('Landlord1MSP.member', 'Tenant1MSP.member', 'Guest1MSP.member')" "false" "crypto-orderer/tlsca.orderer.project.it-cert.pem" ""
      printHeadline "Installing 'chaincode1' for Guest1" "U1F60E"
      chaincodeInstall "cli.guest1.project.it" "peer0.guest1.project.it:7081" "chaincode1" "$version" "crypto-orderer/tlsca.orderer.project.it-cert.pem"
      chaincodeApprove "cli.guest1.project.it" "peer0.guest1.project.it:7081" "home1" "chaincode1" "$version" "orderer0.group1.orderer.project.it:7030" "OR('Landlord1MSP.member', 'Tenant1MSP.member', 'Guest1MSP.member')" "false" "crypto-orderer/tlsca.orderer.project.it-cert.pem" ""
      printItalics "Committing chaincode 'chaincode1' on channel 'home1' as 'Landlord1'" "U1F618"
      chaincodeCommit "cli.landlord1.project.it" "peer0.landlord1.project.it:7041" "home1" "chaincode1" "$version" "orderer0.group1.orderer.project.it:7030" "OR('Landlord1MSP.member', 'Tenant1MSP.member', 'Guest1MSP.member')" "false" "crypto-orderer/tlsca.orderer.project.it-cert.pem" "peer0.landlord1.project.it:7041,peer0.tenant1.project.it:7061,peer0.guest1.project.it:7081" "crypto-peer/peer0.landlord1.project.it/tls/ca.crt,crypto-peer/peer0.tenant1.project.it/tls/ca.crt,crypto-peer/peer0.guest1.project.it/tls/ca.crt" ""

    else
      echo "Warning! Skipping chaincode 'chaincode1' upgrade. Chaincode directory is empty."
      echo "Looked in dir: '$CHAINCODES_BASE_DIR/../house-supervisor-chaincode'"
    fi
  fi
}

stopNetwork() {
  printHeadline "Stopping network" "U1F68F"
  (cd "$FABLO_NETWORK_ROOT"/fabric-docker && docker-compose stop)
  sleep 4
}

networkDown() {
  printHeadline "Destroying network" "U1F916"
  (cd "$FABLO_NETWORK_ROOT"/fabric-docker && docker-compose down)

  printf "\nRemoving chaincode containers & images... \U1F5D1 \n"
  docker rm -f $(docker ps -a | grep dev-peer0.landlord1.project.it-chaincode1-0.0.1-* | awk '{print $1}') || echo "docker rm failed, Check if all fabric dockers properly was deleted"
  docker rmi $(docker images dev-peer0.landlord1.project.it-chaincode1-0.0.1-* -q) || echo "docker rm failed, Check if all fabric dockers properly was deleted"
  docker rm -f $(docker ps -a | grep dev-peer0.tenant1.project.it-chaincode1-0.0.1-* | awk '{print $1}') || echo "docker rm failed, Check if all fabric dockers properly was deleted"
  docker rmi $(docker images dev-peer0.tenant1.project.it-chaincode1-0.0.1-* -q) || echo "docker rm failed, Check if all fabric dockers properly was deleted"
  docker rm -f $(docker ps -a | grep dev-peer0.guest1.project.it-chaincode1-0.0.1-* | awk '{print $1}') || echo "docker rm failed, Check if all fabric dockers properly was deleted"
  docker rmi $(docker images dev-peer0.guest1.project.it-chaincode1-0.0.1-* -q) || echo "docker rm failed, Check if all fabric dockers properly was deleted"

  printf "\nRemoving generated configs... \U1F5D1 \n"
  rm -rf "$FABLO_NETWORK_ROOT/fabric-config/config"
  rm -rf "$FABLO_NETWORK_ROOT/fabric-config/crypto-config"
  rm -rf "$FABLO_NETWORK_ROOT/fabric-config/chaincode-packages"

  printHeadline "Done! Network was purged" "U1F5D1"
}
