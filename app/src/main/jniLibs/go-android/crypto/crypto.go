package crypto

import (
	"golang.org/x/crypto/sha3"
	"hash"
)

const CRYPTO_MESSAGE_LEN = 32
const CRYPTO_PUBLICKEY_BYTES = 32 + 897
const CRYPTO_SIGNATURE_BYTES = CRYPTO_MESSAGE_LEN + 2 + 2 + 64 + 40 + 690 //Nonce + 2 for size

type KeccakState interface {
	hash.Hash
	Read([]byte) (int, error)
}

// NewKeccakState creates a new KeccakState
func NewKeccakState() KeccakState {
	return sha3.NewLegacyKeccak256().(KeccakState)
}

func Keccak256(data ...[]byte) []byte {
	b := make([]byte, 32)
	d := NewKeccakState()
	for _, b := range data {
		d.Write(b)
	}
	d.Read(b)
	return b
}
