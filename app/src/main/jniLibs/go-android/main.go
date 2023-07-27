package main

import "C"
import (
	"dp-android/common"
	"dp-android/common/hexutil"
	"dp-android/core/types"
	"dp-android/crypto"
	"unsafe"
	"dp-android/params"
	"fmt"
	"math/big"
	"strings"
	"strconv"
)

type Transaction struct {
	Transaction []TransactionDetails `json:"transaction"`
}

type TransactionDetails struct {
	FromAddress common.Address `json:"fromAddress"`
	ToAddress   common.Address `json:"toAddress"`
	Nonce       uint64         `json:"nonce"`
	GasLimit    uint64         `json:"gasLimit"`
	GasPrice    *big.Int       `json:"gasPrice"`
	Value       *big.Int       `json:"value"`
	Data        []byte         `json:"data"`
	ChainId     *big.Int       `json:"chainId"`
}

func main() { }

//export PublicKeyToAddress
func PublicKeyToAddress(pKey_str *C.char, pk_count int) (*C.char, *C.char) {
    pubBytes := C.GoBytes(unsafe.Pointer(pKey_str), C.int(pk_count))
    address :=  common.BytesToAddress(crypto.Keccak256(pubBytes[1:])[12:]).String()
    return C.CString(address), nil
}

//export TxMessage
func TxMessage(from, nonce, to, value, gasLimit, gasPrice, data, chainId *C.char) (*C.char, *C.char){
	ts := transaction(C.GoString(from), C.GoString(nonce), C.GoString(to),
	C.GoString(value), C.GoString(gasLimit), C.GoString(gasPrice), C.GoString(data), C.GoString(chainId))

	tx := types.NewTransaction(ts.Transaction[0].Nonce,
		ts.Transaction[0].ToAddress, ts.Transaction[0].Value,
		ts.Transaction[0].GasLimit, ts.Transaction[0].GasPrice,
		ts.Transaction[0].Data)

	signer := types.NewEIP155Signer(ts.Transaction[0].ChainId)
	signerHash := signer.Hash(tx)

	var message strings.Builder
	for i := 0; i < len(signerHash); i++ {
		sh := signerHash[i]
		message.WriteString(string(sh))
	}

	return  C.CString(message.String()), nil
}

//export TxHash
func TxHash(from, nonce, to, value, gasLimit, gasPrice, data, chainId,
    pKey_str, sig_str *C.char, pk_count int, sig_count int) (*C.char, *C.char) {
    ts := transaction(C.GoString(from), C.GoString(nonce), C.GoString(to),
    C.GoString(value), C.GoString(gasLimit), C.GoString(gasPrice), C.GoString(data), C.GoString(chainId))

	tx := types.NewTransaction(ts.Transaction[0].Nonce,
		ts.Transaction[0].ToAddress, ts.Transaction[0].Value,
		ts.Transaction[0].GasLimit, ts.Transaction[0].GasPrice,
		ts.Transaction[0].Data)

	signer := types.NewEIP155Signer(ts.Transaction[0].ChainId)

    //pubBytes := []byte(C.GoString(pKey_str))
    pubBytes := C.GoBytes(unsafe.Pointer(pKey_str), C.int(pk_count))
    sigBytes := C.GoBytes(unsafe.Pointer(sig_str), C.int(sig_count))

	signTx, err := types.SignTx(tx, signer, pubBytes, sigBytes)
	if err != nil {
		return nil, C.CString(err.Error())
	}

	return C.CString(signTx.Hash().String()),  nil
}

//export TxData
func TxData(from, nonce, to, value, gasLimit, gasPrice, data, chainId,
    pKey_str, sig_str *C.char, pk_count int, sig_count int) (*C.char, *C.char) {
    ts := transaction(C.GoString(from), C.GoString(nonce), C.GoString(to),
    C.GoString(value), C.GoString(gasLimit), C.GoString(gasPrice), C.GoString(data), C.GoString(chainId))

	tx := types.NewTransaction(ts.Transaction[0].Nonce,
		ts.Transaction[0].ToAddress, ts.Transaction[0].Value,
		ts.Transaction[0].GasLimit, ts.Transaction[0].GasPrice,
		ts.Transaction[0].Data)

	signer := types.NewEIP155Signer(ts.Transaction[0].ChainId)

    //pubBytes := []byte(C.GoString(pKey_str))
    pubBytes := C.GoBytes(unsafe.Pointer(pKey_str), C.int(pk_count))
    sigBytes := C.GoBytes(unsafe.Pointer(sig_str), C.int(sig_count))

	signTx, err := types.SignTx(tx, signer, pubBytes, sigBytes)
	if err != nil {
		return nil, C.CString(err.Error())
    }

	signTxBinary, err := signTx.MarshalBinary()
	if err != nil {
		return nil, C.CString(err.Error())
	}

	signTxEncode := hexutil.Encode(signTxBinary)
	return C.CString(signTxEncode), nil
}

//export DogeProtocolToWei
func DogeProtocolToWei(quantity *C.char) (*C.char, *C.char) {
	dp := new(big.Float)
	//fmt.Sscan(C.GoString(quantity), dp)
	_, err := fmt.Sscan(C.GoString(quantity), dp)
	if err != nil {
		return nil, C.CString(err.Error())
	}
	truncInt, _ := dp.Int(nil)
	truncInt = new(big.Int).Mul(truncInt, big.NewInt(params.DogeProtocol))
	fracStr := strings.Split(fmt.Sprintf("%.18f", dp), ".")[1]
	fracStr += strings.Repeat("0", 18-len(fracStr))
	fracInt, _ := new(big.Int).SetString(fracStr, 10)
	wei := new(big.Int).Add(truncInt, fracInt)
	return C.CString(wei.String()), nil
}

//export ParseBigFloat
func ParseBigFloat(quantity *C.char) (*C.char, *C.char) {
	var value string
	value = C.GoString(quantity)
	f := new(big.Float)
	f.SetPrec(236) //  IEEE 754 octuple-precision binary floating-point format: binary256
	f.SetMode(big.ToNearestEven)
	//fmt.Sscan(value, f)
	_, err := fmt.Sscan(value, f)
	if err != nil {
        return nil, C.CString(err.Error())
	}
	return C.CString(f.String()), nil
}

//export WeiToDogeProtocol
func WeiToDogeProtocol(quantity *C.char) (*C.char, *C.char) {
	wei := new(big.Int)
	//fmt.Sscan(C.GoString(quantity), wei)
	_, err := fmt.Sscan(C.GoString(quantity), wei)
	if err != nil {
        return nil, C.CString(err.Error())
	}
	f := new(big.Float)
	f.SetPrec(236) //  IEEE 754 octuple-precision binary floating-point format: binary256
	f.SetMode(big.ToNearestEven)
	fWei := new(big.Float)
	fWei.SetPrec(236) //  IEEE 754 octuple-precision binary floating-point format: binary256
	fWei.SetMode(big.ToNearestEven)
	dp := f.Quo(fWei.SetInt(wei), big.NewFloat(params.DogeProtocol))
	return C.CString(dp.String()), nil
}

func transaction(args0, args1, args2, args3, args4, args5, args6, args7 string) (transaction Transaction) {

    var fromAddress = common.HexToAddress(args0)
    n , _ := strconv.Atoi(args1)
    var nonce = uint64(n)
    var toAddress = common.HexToAddress(args2)
    var value, _ = new(big.Int).SetString(args3,0);
    g , _ := strconv.Atoi(args4)
    var gasLimit = uint64(g)
    var gasPrice, _ = new(big.Int).SetString(args5,0); //big.NewInt(1000000000)
    var data []byte //args6.String()
    var chainId, _ =  new(big.Int).SetString(args7,0);

	transactionDetails := TransactionDetails{
		FromAddress: fromAddress, ToAddress: toAddress, Nonce: nonce, GasLimit: gasLimit,
		GasPrice: gasPrice, Value: value, Data: data, ChainId: chainId}

	var t Transaction
	t.Transaction = append(t.Transaction, transactionDetails)

	return t
}
