package main

import (
	"errors"
	"fmt"
	"strconv"
	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type SimpleChaincode struct {
}

//type VoteChain struct {
//	id int
//	name string
//
//	questions [100] string
//	questionPointsYes [100] int
//	questionPointsNo [100] int
//	questionPointsAbstained [100] int
//	questionPointsNoVote [100] int
//
//	users [100] string
//	userPoints [100] int
//}
/*
	args[0] - id
	args[1] - name
	args[2] / args[n] - questions
	separated with argument ;
	args[n] / args[m] - user, points
*/
func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	fmt.Println("Arguments:")
	fmt.Println(args)
	fmt.Println("-------------")

	var questionTable string  = "questions"
	var userTable string = "users"
	var answerTable string = "answers"

	var _err error

	if _err != nil {
		return nil, errors.New("Expecting integer value for vote id")
	}

	err := stub.CreateTable(questionTable, []*shim.ColumnDefinition{
		&shim.ColumnDefinition{Name: "vote_question", Type: shim.ColumnDefinition_STRING, Key: true},
		&shim.ColumnDefinition{Name: "yes", Type: shim.ColumnDefinition_INT32, Key: false},
		&shim.ColumnDefinition{Name: "no", Type: shim.ColumnDefinition_INT32, Key: false},
		&shim.ColumnDefinition{Name: "novote", Type: shim.ColumnDefinition_INT32, Key: false},
		&shim.ColumnDefinition{Name: "type", Type: shim.ColumnDefinition_STRING, Key: false},
		&shim.ColumnDefinition{Name: "abstained", Type: shim.ColumnDefinition_STRING, Key: false},
	})

	if err != nil {
		return nil, errors.New("Failed creating question table.")
	}

	err = stub.CreateTable(userTable, []*shim.ColumnDefinition{
		&shim.ColumnDefinition{Name: "vote_user", Type: shim.ColumnDefinition_STRING, Key: true},
		&shim.ColumnDefinition{Name: "points", Type: shim.ColumnDefinition_INT32, Key: false},
	})

	if err != nil {
		return nil, errors.New("Failed creating user table.")
	}


	err = stub.CreateTable(answerTable, []*shim.ColumnDefinition{
		&shim.ColumnDefinition{Name: "vote_question_user", Type: shim.ColumnDefinition_STRING, Key: true},
		&shim.ColumnDefinition{Name: "answer", Type: shim.ColumnDefinition_STRING, Key: false},
	})

	if err != nil {
		return nil, errors.New("Failed creating answer table.")
	}



	return nil, nil
}

func errorJson(function string, errorMsg error) []byte {
	error := `{"error": { "code":-1,"message":"` + function + `","data":"` + errorMsg.Error() + `"}}`;
	return []byte(error);
}
// voteId, questions simple , questions cumulative , userid points
//String[] args = {"1", "question_simple", "." , "question_cunulative", ".", "1", "102"};

func (t *SimpleChaincode) register(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	var answer, qcol, question string
	var voteId int32
	var userId int32
	var points int32
	var throwError error
	var v int


	v, err := strconv.Atoi(args[0])

	if err != nil {
		throwError = errors.New("Expecting integer value for voteId to register")
		return errorJson("register", throwError), throwError
	}

	voteId = int32(v)


	flag := 0


	for i := 1; i < len(args); i++ {
		if args[i] == "." {
			flag++
			continue
		} else {
			if flag == 0 {
				question = args[i]
				qcol = strconv.Itoa(int(voteId)) + "_" + question
				ok, err := stub.InsertRow("questions", shim.Row{
					Columns: []*shim.Column{
						&shim.Column{Value: &shim.Column_String_{String_: qcol}},
						&shim.Column{Value: &shim.Column_Int32{Int32: 0}},
						&shim.Column{Value: &shim.Column_Int32{Int32: 0}},
						&shim.Column{Value: &shim.Column_Int32{Int32: 0}},
						&shim.Column{Value: &shim.Column_String_{String_: "simple"}},
						&shim.Column{Value: &shim.Column_String_{String_: ""}}},

				})

				if !ok && err == nil {
					throwError = errors.New("questions was already assigned.")
					return errorJson("register", throwError), throwError
				}
			} else if flag == 1{
				question = args[i]
				qcol = strconv.Itoa(int(voteId)) + "_" + question
				ok, err := stub.InsertRow("questions", shim.Row{
					Columns: []*shim.Column{
						&shim.Column{Value: &shim.Column_String_{String_: qcol}},
						&shim.Column{Value: &shim.Column_Int32{Int32: 0}},
						&shim.Column{Value: &shim.Column_Int32{Int32: 0}},
						&shim.Column{Value: &shim.Column_Int32{Int32: 0}},
						&shim.Column{Value: &shim.Column_String_{String_: "abstained"}},
						&shim.Column{Value: &shim.Column_String_{String_: question}}},

				})

				if !ok && err == nil {
					throwError = errors.New("questions was already assigned.")
					return errorJson("register", throwError), throwError
				}
			}else {
				u, err := strconv.Atoi(args[i])

				if err != nil {
					throwError = errors.New("Expecting integer value for userId to register")
					return errorJson("register", throwError), throwError
				}

				userId = int32(u)

				p, err := strconv.Atoi(args[i+1])

				if err != nil {
					throwError = errors.New("Expecting integer value for points to register")
					return errorJson("register", throwError), throwError
				}

				points = int32(p)
				answer = strconv.Itoa(int(voteId)) + "_" + strconv.Itoa(int(userId))
				ok, err := stub.InsertRow("users", shim.Row{
					Columns: []*shim.Column{
						&shim.Column{Value: &shim.Column_String_{String_: answer}},
						&shim.Column{Value: &shim.Column_Int32{Int32: points}}},

				})

				if !ok && err == nil {
					throwError = errors.New("users was already assigned.")
					return errorJson("register", throwError), throwError
				}
			}

		}

	}

	return nil, nil
}

//vote

func (t *SimpleChaincode) vote(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	var voteId int32
	var userId int32
	var question, qcol, anscol string
	var qType string
	var answer string
	var throwError error
	var yes , no, novote int32
	var astype , asans string
	var v, u int

	fmt.Println("Vote started --------------")

	if len(args) != 5 {
		throwError = errors.New("Incorrect number of arguments. Expecting 5. voteId, userId, question, qType, answer")
		return errorJson("vote", throwError), throwError
	}

	v, err := strconv.Atoi(args[0])
	if err != nil {
		throwError = errors.New("Expecting integer value for voteId to vote")
		return errorJson("vote", throwError), throwError
	}

	voteId = int32(v)

	u, err1 := strconv.Atoi(args[1])

	if err1 != nil {
		throwError = errors.New("Expecting integer value for userId to vote")
		return errorJson("vote", throwError), throwError
	}

	userId = int32(u)

	question = args[2]
	qType = args[3]
	answer = args[4]

	if len(qType) == 0 {
		throwError = errors.New("No question type is defined")
		return errorJson("vote", throwError), throwError
	}
	if len(answer) == 0 {
		throwError = errors.New("No answer is defined")
		return errorJson("vote", throwError), throwError
	}

	//get question by voteid and question name
	fmt.Println("get from question table ---------")

	qcol =  strconv.Itoa(int(voteId)) + "_" + question

	var columns []shim.Column
	nameColumn := shim.Column{Value: &shim.Column_String_{String_: qcol}}
	columns = append(columns, nameColumn)
	row, err := stub.GetRow("questions", columns)
	if err != nil {
		throwError = fmt.Errorf("Failed retrieving column FROM voteid [%s]: [%s]", voteId, err)
		return errorJson("vote", throwError), throwError
	}


	yes = row.Columns[1].GetInt32()
	no = row.Columns[2].GetInt32()
	novote = row.Columns[3].GetInt32()
	astype = row.Columns[4].GetString_()
	asans = row.Columns[5].GetString_()

	if answer == "YES" {
		yes = yes + 1
	} else if answer == "NO" {
		no = no + 1
	} else if answer == "NOVOTE"{
		novote = novote + 1
	}

	// update question
	fmt.Println("replace row in question table ---------")
	_, err = stub.ReplaceRow(
		"questions",
		shim.Row{
			Columns: []*shim.Column{
				&shim.Column{Value: &shim.Column_String_{String_: qcol}},
				&shim.Column{Value: &shim.Column_Int32{Int32: yes}},
				&shim.Column{Value: &shim.Column_Int32{Int32: no}},
				&shim.Column{Value: &shim.Column_Int32{Int32: novote}},
				&shim.Column{Value: &shim.Column_String_{String_: astype}},
				&shim.Column{Value: &shim.Column_String_{String_: asans}},
			},
		})
	if err != nil {
		throwError = errors.New("Failed update question row.")
		return errorJson("vote", throwError), throwError
	}

	//insert to answers table

	anscol = strconv.Itoa(int(voteId)) + "_" + question + "_" +strconv.Itoa(int(userId))

	fmt.Println("insert row to answers table ---------")
	ok, err := stub.InsertRow("answers", shim.Row{
		Columns: []*shim.Column{
			&shim.Column{Value: &shim.Column_String_{String_: anscol}},
			&shim.Column{Value: &shim.Column_String_{String_: answer}}},

	})

	if !ok && err == nil {
		throwError = errors.New("This user has already answered to this question")
		return errorJson("vote", throwError), throwError
	}


	return nil, nil
}

//transfer vote points

func (t *SimpleChaincode) transfer(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	var voteId int32
	var userFromId int32
	var userToId int32
	var userFromCol, userToCol string
	var points, pointsFrom, pointsTo int32
	var v, uf, ut , p int
	var throwError error



	if len(args) != 4 {
		throwError = errors.New("Incorrect number of arguments. Expecting 4. voteId, userFrom, userTo, point")
		return errorJson("transfer", throwError), throwError
	}

	v, err := strconv.Atoi(args[0])
	if err != nil {
		throwError = errors.New("Expecting integer value for voteId to vote")
		return errorJson("transfer", throwError), throwError
	}

	voteId = int32(v)

	uf, err1 := strconv.Atoi(args[1])

	if err1 != nil {
		throwError = errors.New("Expecting integer value for userFromId")
		return errorJson("transfer", throwError), throwError
	}

	userFromId = int32(uf)

	ut, err2 := strconv.Atoi(args[2])

	if err2 != nil {
		throwError = errors.New("Expecting integer value for userToId")
		return errorJson("transfer", throwError), throwError
	}

	userToId = int32(ut)

	p, err3 := strconv.Atoi(args[3])

	if err3 != nil {
		throwError = errors.New("Expecting integer value for points")
		return errorJson("transfer", throwError), throwError
	}

	points = int32(p)

	//get question by voteid and question name
	userFromCol = strconv.Itoa(int(voteId)) + "_" + strconv.Itoa(int(userFromId))
	var columns []shim.Column
	voteIdColumn := shim.Column{Value: &shim.Column_String_{String_: userFromCol}}
	columns = append(columns, voteIdColumn)
	rowFrom, err := stub.GetRow("users", columns)
	if err != nil {
		throwError = fmt.Errorf("Failed retrieving column FROM users [%s]: [%s]", voteId, err)
		return errorJson("transfer", throwError), throwError
	}


	pointsFrom = rowFrom.Columns[1].GetInt32()

	userToCol = strconv.Itoa(int(voteId)) + "_" +strconv.Itoa(int(userToId))
	var columnsTo []shim.Column
	voteIdColumnTo := shim.Column{Value: &shim.Column_String_{String_: userToCol}}
	columnsTo = append(columnsTo, voteIdColumnTo)
	rowTo, err := stub.GetRow("users", columnsTo)
	if err != nil {
		throwError = fmt.Errorf("Failed retrieving column FROM users [%s]: [%s]", userToId, err)
		return errorJson("transfer", throwError), throwError
	}


	pointsTo = rowTo.Columns[1].GetInt32()

	if points <= pointsFrom {
		pointsFrom = pointsFrom - points
		pointsTo = pointsTo + points
	} else {
		throwError = fmt.Errorf("User has not enough points to transfer [%s]: [%s]", pointsFrom, err)
		return errorJson("transfer", throwError), throwError
	}

	// update users from
	_, err = stub.ReplaceRow(
		"users",
		shim.Row{
			Columns: []*shim.Column{
				&shim.Column{Value: &shim.Column_String_{String_: userFromCol}},
				&shim.Column{Value: &shim.Column_Int32{Int32: pointsFrom}},
			},
		})
	if err != nil {
		throwError = errors.New("Failed update usersFrom row.")
		return errorJson("transfer", throwError), throwError
	}


	// update users to
	_, err = stub.ReplaceRow(
		"users",
		shim.Row{
			Columns: []*shim.Column{
				&shim.Column{Value: &shim.Column_String_{String_: userToCol}},
				&shim.Column{Value: &shim.Column_Int32{Int32: pointsTo}},
			},
		})
	if err != nil {
		throwError = errors.New("Failed update usersFrom row.")
		return errorJson("transfer", throwError), throwError
	}

	return nil, nil
}



func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	fmt.Println(args)
	// Handle different functions
	if function == "register" {
		// Register vote
		return t.register(stub, args)
	} else if function == "vote" {
		// Vote
		return t.vote(stub, args)
	} else if function == "transfer" {
		return t.transfer(stub, args)
	}
	return nil, errors.New("Received unknown function invocation")
}



func (t *SimpleChaincode) user_info(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	fmt.Println("Answer info...")
	var err error
	var qcol string
	var voteId, userId, points int32
	var v, u int
	var throwError error

	if len(args) != 2 {
		fmt.Println("Incorrect number of arguments. Expecting voteId and userId to query")
		return nil, errors.New("Incorrect number of arguments. Expecting voteId and userId to query")
	}

	v, err2 := strconv.Atoi(args[0])
	if err2 != nil {
		throwError = errors.New("Expecting integer value for voteId to vote")
		return errorJson("transfer", throwError), throwError
	}

	voteId = int32(v)

	u, err2 = strconv.Atoi(args[1])
	if err2 != nil {
		throwError = errors.New("Expecting integer value for voteId to vote")
		return errorJson("transfer", throwError), throwError
	}

	userId = int32(u)



	qcol = strconv.Itoa(int(voteId)) + "_" + strconv.Itoa(int(userId))

	var columns []shim.Column
	questionColumn := shim.Column{Value: &shim.Column_String_{String_: qcol}}
	columns = append(columns, questionColumn)

	row, err := stub.GetRow("users", columns)
	if err != nil {
		return nil, fmt.Errorf("Failed retrieving column users [%s]: [%s]", userId, err)
	}

	points = row.Columns[1].GetInt32()


	jsonResp := `{"voteId":"` + strconv.Itoa(v) +
		`","userId":"` + strconv.Itoa(u) + `","Points":"` + strconv.Itoa(int(points))  + `"}`
	fmt.Println("users info...done")
	return []byte(jsonResp), nil
}


func (t *SimpleChaincode) answer_info(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	fmt.Println("Answer info...")
	var err error
	var question, qcol, answer string
	var voteId, userId int32
	var v, u int
	var throwError error

	if len(args) != 3 {
		fmt.Println("Incorrect number of arguments. Expecting voteId,userId and question to query")
		return nil, errors.New("Incorrect number of arguments. Expecting voteId,userId and question to query")
	}

	v, err2 := strconv.Atoi(args[0])
	if err2 != nil {
		throwError = errors.New("Expecting integer value for voteId to vote")
		return errorJson("transfer", throwError), throwError
	}

	voteId = int32(v)

	u, err2 = strconv.Atoi(args[1])
	if err2 != nil {
		throwError = errors.New("Expecting integer value for voteId to vote")
		return errorJson("transfer", throwError), throwError
	}

	userId = int32(u)


	question = args[2]
	qcol = strconv.Itoa(int(voteId)) + "_" + question + "_" + strconv.Itoa(int(userId))

	var columns []shim.Column
	questionColumn := shim.Column{Value: &shim.Column_String_{String_: qcol}}
	columns = append(columns, questionColumn)

	row, err := stub.GetRow("answers", columns)
	if err != nil {
		return nil, fmt.Errorf("Failed retrieving column answers [%s]: [%s]", question, err)
	}

	answer = row.Columns[1].GetString_()


	jsonResp := `{"voteId":"` + strconv.Itoa(v) + `","question":"` + question + `",` +
		`","userId":"` + strconv.Itoa(u) + `","Answer":"` + answer  + `"}`
	fmt.Println("question's info...done")
	return []byte(jsonResp), nil
}

/*
Query question_info
{
  "jsonrpc" : "2.0",
  "method" : "query",
  "params" : {
    "type" : 1,
    "chaincodeID" : {
      "name" : "a2f6955ea6f35fe5b54c7fdac924b7a705d8c6adf7fc5121b17d84b395c23489fc420eb5a22375fc58c763ef30d25ca920dada799f6033a0fa0fcfc929e38acd"
    },
    "ctorMsg" : {
      "function" : "question",
      "args" : [ "1", "question1" ]
    }
  },
  "id" : 1
}

args : voteId - id голосования
       question - вопрос
*/
func (t *SimpleChaincode) question_info(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
	fmt.Println("Question info...")
	var err error
	var question, qtype, abstained , qcol string
	var voteId int32
	var v, y, n, nv int
	var yes, no, novote int32
	var throwError error

	if len(args) != 2 {
		fmt.Println("Incorrect number of arguments. Expecting voteId and userId to query")
		return nil, errors.New("Incorrect number of arguments. Expecting voteId and userId to query")
	}

	v, err2 := strconv.Atoi(args[0])
	if err2 != nil {
		throwError = errors.New("Expecting integer value for voteId to vote")
		return errorJson("transfer", throwError), throwError
	}

	voteId = int32(v)

	question = args[1]
	qcol = strconv.Itoa(int(voteId)) + "_" + question
	var columns []shim.Column
	questionColumn := shim.Column{Value: &shim.Column_String_{String_: qcol}}
	columns = append(columns, questionColumn)

	row, err := stub.GetRow("questions", columns)
	if err != nil {
		return nil, fmt.Errorf("Failed retrieving column question [%s]: [%s]", question, err)
	}

	yes = row.Columns[1].GetInt32()
	no = row.Columns[2].GetInt32()
	novote = row.Columns[3].GetInt32()
	qtype = row.Columns[4].GetString_()
	abstained = row.Columns[5].GetString_()

	y = int(yes)
	n = int(no)
	nv = int(novote)


	jsonResp := `{"voteId":"` + strconv.Itoa(v) + `","question":"` + question + `",` +
		`","YES":"` + strconv.Itoa(y) + `","NO":"` + strconv.Itoa(n) + `","NOVOTE":"` + strconv.Itoa(nv) + `","Question type":"` + qtype + `","Abstained":"` + abstained + `"}`
	fmt.Println("question's info...done")
	return []byte(jsonResp), nil
}

func (t *SimpleChaincode) Query(stub shim.ChaincodeStubInterface, function string, args []string) ([]byte, error) {
	fmt.Printf("Query [%s]", function)

	if function == "question" {
		// Get owners balance
		return t.question_info(stub, args)
	} else if function == "answer" {
		return t.answer_info(stub, args)
	} else if function == "user" {
		return t.user_info(stub, args)
	}

	return nil, errors.New("Received unknown function invocation")
}

func (t *SimpleChaincode) delete(stub shim.ChaincodeStubInterface, args []string) ([]byte, error) {
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



func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
