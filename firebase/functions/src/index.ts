import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import * as crypto from 'crypto';

// Inicialização do aplicativo Firebase.
const app = admin.initializeApp(functions.config().firebase);

// Referência à base de dados do Firestore.
const firestore = app.firestore();

// Cria o sha256
const hash = crypto.createHash('sha256');

interface Produto {
    
}

// Função pra atualizar o hash dos produtos.
exports.AtualizaHashProduto = functions.firestore
    .document('produtos/{produtoID}')
    .onWrite(event => {
        const collProdutos = firestore.collection('produtos');
        collProdutos.get()
            .then(snapshot => {
                snapshot.forEach(doc => {
                    console.log(doc.id, '=>', doc.data());
                });
            })
            .catch(err => {
                console.log('Error getting documents', err);
            });
        const batch = firestore.batch();

    });