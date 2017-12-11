package main

import (
	"errors"
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
)

/**
 * Created by Olzhas.Pazyldayev on 11.12.2017
 */
type VotingChaincode struct {
}

func (t *VotingChaincode) Init(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	fmt.Println("Init args:")
	fmt.Println(args)
	fmt.Println("-------------")

	var votingTableName string
	if len(args) == 0 {
		votingTableName = "voting"
	} else {
		if (len(args) == 1) {
			votingTableName = args[0]
		} else {
			return nil, errors.New("Incorrect number of arguments. Expecting 0 or 1, tableName(required=false)")
		}
	}

	//create global voting table with name $votingTableName, and columns:
	//decision_document_hash - hash of whole voting decision of a voter
	//voter_iin - voter idn
	//voter_account_number - voter accountNumber, can be accountNumber of sharing voter
	//voting_id - id of voting
	err := stub.CreateTable(votingTableName, []*shim.ColumnDefinition{
		&shim.ColumnDefinition{Name: "decision_document_hash", Type: shim.ColumnDefinition_STRING, Key: true},
		&shim.ColumnDefinition{Name: "voter_iin", Type: shim.ColumnDefinition_STRING, Key: false},
		&shim.ColumnDefinition{Name: "account_number", Type: shim.ColumnDefinition_STRING, Key: false},
		&shim.ColumnDefinition{Name: "voting_id", Type: shim.ColumnDefinition_STRING, Key: false},
	})

	if err != nil {
		return nil, errors.New("Failed creating global voting table: " + votingTableName)
	}

	return nil, nil
}

func errorJson(function string, errorMsg error) []byte {
	error := `{"error": { "code":-1,"message":"` + function + `","data":"` + errorMsg.Error() + `"}}`;
	return []byte(error);
}

func (t *VotingChaincode) register(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	fmt.Println("register... args:")
	fmt.Println(args)
	fmt.Println("-------------")

	var decisionDocument, voterIin, accountNumber, votingId, tableName string
	var throwError error

	if (len(args) < 4 || len(args) > 5) {
		throwError = errors.New("Incorrect number of arguments. Expecting 4 or 5. decision_document_hash, " +
			"voter_iin, voter_account_number, voting_id, tableName(required=false)")
		return errorJson("register", throwError), throwError
	}

	decisionDocument = args[0]
	voterIin = args[1]
	accountNumber = args[2]
	votingId = args[3]
	tableName = "voting"
	if (len(args) == 5) {
		tableName = args[4]
	}

	//registering voter decision
	fmt.Printf("Register voter [%s] with accountNumber [%s] in voting [%s] with decision [%s]",
		voterIin, accountNumber, votingId, decisionDocument)

	ok, err := stub.InsertRow(tableName, shim.Row{
		Columns: []*shim.Column{
			&shim.Column{Value: &shim.Column_String_{String_: decisionDocument}},
			&shim.Column{Value: &shim.Column_String_{String_: voterIin}},
			&shim.Column{Value: &shim.Column_String_{String_: accountNumber}},
			&shim.Column{Value: &shim.Column_String_{String_: votingId}}},
	})

	if !ok && err == nil {
		throwError = errors.New("couldn't register decision")
		return errorJson("register", throwError), throwError
	}

	fmt.Println("register...done!")
	return nil, err
}

func (t *VotingChaincode) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	fmt.Println(args)
	// Handle different functions
	if function == "voting" {
		// Register vote
		return t.register(stub, args)
	}
	return nil, errors.New("Received unknown function invocation")
}

func (t *VotingChaincode) decision(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	fmt.Println("decision document info...")
	fmt.Println(args)
	fmt.Println("-------------")

	var decisionDocument, voterIin, accountNumber, votingId, tableName string
	var throwError error

	if (len(args) < 1 || len(args) > 2) {
		throwError = errors.New("Incorrect number of arguments. Expecting 1 or 2. decision_document_hash, tableName(required=false)")
		return errorJson("register", throwError), throwError
	}

	decisionDocument = args[0]
	tableName = "voting"
	if (len(args) == 2) {
		tableName = args[1]
	}

	var columns []shim.Column
	decisionColumn := shim.Column{Value: &shim.Column_String_{String_: decisionDocument}}
	columns = append(columns, decisionColumn)

	decisionRow, err := stub.GetRow(tableName, columns)
	if err != nil {
		return nil, fmt.Errorf("Failed retrieving column descisionDocument [%s]: [%s]", decisionDocument, err)
	}

	voterIin = decisionRow.Columns[1].GetString_()
	accountNumber = decisionRow.Columns[2].GetString_()
	votingId = decisionRow.Columns[3].GetString_()

	fmt.Printf("Voter [%s] with accountNumber [%s] in voting [%s] with decision [%s]",
		voterIin, accountNumber, votingId, decisionDocument)

	jsonResp := `{"decisionDocumentHash":"` + decisionDocument + `","voterIin":"` + voterIin + `",` +
		`"accountNumber":"` + accountNumber + `","votingId":"` + votingId + `"}`
	fmt.Println("decision document info...done")

	return []byte(jsonResp), nil
}

func (t *VotingChaincode) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	fmt.Printf("Query [%s]", function)

	if function == "decision" {
		// get info by decision
		return t.decision(stub, args)
	}

	return nil, errors.New("Received unknown function invocation")
}

func main() {
	err := shim.Start(new(VotingChaincode))
	if err != nil {
		fmt.Printf("Error starting Voting Chaincode %s", err)
	}
}
