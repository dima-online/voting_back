package main

import (
	"errors"
	"fmt"
	"strconv"
	"encoding/json"
	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type SimpleChaincode struct {
}

type VoteChain struct {
	id int
	name string

	questions [100] string
	questionPointsYes [100] int
	questionPointsNo [100] int
	questionPointsAbstained [100] int
	questionPointsNoVote [100] int

	users [100] string
	userPoints [100] int
}
/*
	args[0] - id
	args[1] - name
	args[2] / args[n] - questions
	separated with argument ;
	args[n] / args[m] - user, points
*/
func (t *SimpleChaincode) Init(stub *shim.ChaincodeStub, function string, args []string) ([]byte, error) {
	fmt.Println("Arguments:")
	fmt.Println(args)
	fmt.Println("-------------")

	var voteChainInfo VoteChain

	var err error

	if len(args) <= 1 {
		return nil, errors.New("Incorrect number of arguments. Expecting more")
	}
	// Initialize the chaincode
	voteChainInfo.id, err = strconv.Atoi(args[0])

	if err != nil {
		return nil, errors.New("Expecting integer value for user id")
	}

	voteChainInfo.name = args[1]

	var flag bool
	flag = false

	qCounter := 0
	uCounter := 0

	for i := 2; i < len(args); i++ {
		if args[i] == ";" {
			flag = true
			continue
		} else {
			if !flag {
				voteChainInfo.questions[qCounter] = args[i]
				voteChainInfo.questionPointsYes[qCounter] = 0
				voteChainInfo.questionPointsNo[qCounter] = 0
				voteChainInfo.questionPointsAbstained[qCounter] = 0
				voteChainInfo.questionPointsNoVote[qCounter] = 0
				qCounter++
			} else {
				voteChainInfo.users[uCounter] = args[i]
				voteChainInfo.userPoints[uCounter], err = strconv.Atoi(args[i + 1])
				uCounter++
				if err != nil {
					return nil, errors.New("Expecting integer value for user points")
				}

				i++
			}

		}

	}

	fmt.Println("ID: " + strconv.Itoa(voteChainInfo.id))
	fmt.Println("NAME: " + voteChainInfo.name)

	// questions
	for i := 0; i < qCounter; i++ {
		fmt.Println(voteChainInfo.questions[i] + " : " +
			strconv.Itoa(voteChainInfo.questionPointsYes[i]) + "(Yes), " +
			strconv.Itoa(voteChainInfo.questionPointsNo[i]) + "(No), " +
			strconv.Itoa(voteChainInfo.questionPointsAbstained[i]) + "(Abstained), " +
			strconv.Itoa(voteChainInfo.questionPointsNoVote[i]) + "(NoVote). ")
	}

	fmt.Println("-----------------")

	// users
	for i := 0; i < uCounter; i++ {
		fmt.Println(voteChainInfo.users[i] + " : " + strconv.Itoa(voteChainInfo.userPoints[i]))
	}

	bytes, err := json.Marshal(voteChainInfo)

	if err != nil {
		return nil, errors.New("Couldn't convert to bytes")
	}

	err = stub.PutState(strconv.Itoa(voteChainInfo.id), bytes)
	if err != nil {
		return nil, err
	}

	return nil, nil
}

// VOTE
func (t *SimpleChaincode) Invoke(stub *shim.ChaincodeStub, function string, args []string) ([]byte, error) {
	fmt.Println(args)
	if function == "delete" {
		// Deletes an entity from its state
		return t.delete(stub, args)
	}

	var id int
	var user string
	var points int
	var voteChainInfo VoteChain

	_ = user
	_ = points

	var err error

	if len(args) != 3 {
		return nil, errors.New("Incorrect number of arguments. Expecting 3")
	}

	id, err = strconv.Atoi(args[0])
	user = args[1]
	points, err = strconv.Atoi(args[2])

	// Get the state from the ledger
	bytes, err := stub.GetState(strconv.Itoa(id))
	if err != nil {
		return nil, errors.New("Failed to get state")
	}

	if bytes == nil {
		return nil, errors.New("Entity not found")
	}

	json.Unmarshal(bytes, &voteChainInfo)

	fmt.Println(voteChainInfo)

	return nil, nil
}

func (t *SimpleChaincode) delete(stub *shim.ChaincodeStub, args []string) ([]byte, error) {
	if len(args) != 1 {
		return nil, errors.New("Incorrect number of arguments. Expecting 1")
	}

	id := args[0]

	// Delete the key from the state in ledger
	err := stub.DelState(id)
	if err != nil {
		return nil, errors.New("Failed to delete state")
	}

	return nil, nil
}

// Query callback representing the query of a chaincode
func (t *SimpleChaincode) Query(stub *shim.ChaincodeStub, function string, args []string) ([]byte, error) {
	if function != "query" {
		return nil, errors.New("Invalid query function name. Expecting \"query\"")
	}
	var id string

	if len(args) != 1 {
		return nil, errors.New("Incorrect number of arguments. Expecting id of the vote to query")
	}

	id = args[0]

	// Get the state from the ledger
	bytes, err := stub.GetState(id)
	if err != nil {
		jsonResp := "{\"Error\":\"Failed to get state for " + id + "\"}"
		return nil, errors.New(jsonResp)
	}

	if bytes == nil {
		jsonResp := "{\"Error\":\"Nil amount for " + id + "\"}"
		return nil, errors.New(jsonResp)
	}

	jsonResp := VoteChain {}
	json.Unmarshal(bytes, &jsonResp)

	fmt.Println("Query Response:%s\n", jsonResp)
	return bytes, nil
}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
