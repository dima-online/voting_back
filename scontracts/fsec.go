package main

import (
	"errors"
	"fmt"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type SimpleChaincode struct {
}

func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	var NBRK, USER string
	var NBRKval, USERval int
	var err error

	if len(args) != 4 {
		return nil, errors.New("Incorrect number of arguments. Expecting 4")
	}

	NBRK = args[0]
	NBRKval, err = strconv.Atoi(args[1])

	if err != nil {
		return nil, errors.New("Expecting integer value for asset holding")
	}

	USER = args[2]
	USERval, err = strconv.Atoi(args[3])

	if err != nil {
		return nil, errors.New("Expecting integer value for asset holding")
	}

	fmt.Printf("NBRKval = %d, USERval = %d\n", NBRKval, USERval)

	err = stub.PutState(NBRK, []byte(strconv.Itoa(NBRKval)))
	if err != nil {
		return nil, err
	}

	err = stub.PutState(USER, []byte(strconv.Itoa(USERval)))
	if err != nil {
		return nil, err
	}

	return nil, nil
}

// Transaction makes payment of X units from NBRK to USER
func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	if function == "delete" {
		// Deletes an entity from its state
		return t.delete(stub, args)
	}

	var NBRK, USER string
	var NBRKval, USERval int
	var X int
	var err error

	if len(args) != 3 {
		return nil, errors.New("Incorrect number of arguments. Expecting 3")
	}

	NBRK = args[0]
	USER = args[1]

	NBRKvalbytes, err := stub.GetState(NBRK)
	if err != nil {
		return nil, errors.New("Failed to get state")
	}
	if NBRKvalbytes == nil {
		return nil, errors.New("Entity not found")
	}
	NBRKval, _ = strconv.Atoi(string(NBRKvalbytes))

	Bvalbytes, err := stub.GetState(USER)
	if err != nil {
		return nil, errors.New("Failed to get state")
	}
	if Bvalbytes == nil {
		return nil, errors.New("Entity not found")
	}
	USERval, _ = strconv.Atoi(string(Bvalbytes))

	// Perform the execution
	X, err = strconv.Atoi(args[2])
	if err != nil {
		return nil, errors.New("Invalid transaction amount, expecting a integer value")
	}
	NBRKval = NBRKval - X
	USERval = USERval + X
	fmt.Printf("NBRKval = %d, USERval = %d\n", NBRKval, USERval)

	// Write the state back to the ledger
	err = stub.PutState(NBRK, []byte(strconv.Itoa(NBRKval)))
	if err != nil {
		return nil, err
	}

	err = stub.PutState(USER, []byte(strconv.Itoa(USERval)))
	if err != nil {
		return nil, err
	}

	return nil, nil
}

// Deletes an entity from state
func (t *SimpleChaincode) delete(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	if len(args) != 1 {
		return nil, errors.New("Incorrect number of arguments. Expecting 1")
	}

	NBRK := args[0]

	// Delete the key from the state in ledger
	err := stub.DelState(NBRK)
	if err != nil {
		return nil, errors.New("Failed to delete state")
	}

	return nil, nil
}

// Query callback representing the query of a chaincode
func (t *SimpleChaincode) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	if function != "query" {
		return nil, errors.New("Invalid query function name. Expecting \"query\"")
	}
	var NBRK string // Entities
	var err error

	if len(args) != 1 {
		return nil, errors.New("Incorrect number of arguments. Expecting name of the person to query")
	}

	NBRK = args[0]

	// Get the state from the ledger
	NBRKvalbytes, err := stub.GetState(NBRK)
	if err != nil {
		jsonResp := "{\"Error\":\"Failed to get state for " + NBRK + "\"}"
		return nil, errors.New(jsonResp)
	}

	if NBRKvalbytes == nil {
		jsonResp := "{\"Error\":\"Nil amount for " + NBRK + "\"}"
		return nil, errors.New(jsonResp)
	}

	jsonResp := "{\"Name\":\"" + NBRK + "\",\"Amount\":\"" + string(NBRKvalbytes) + "\"}"
	fmt.Printf("Query Response:%s\n", jsonResp)
	return NBRKvalbytes, nil
}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}