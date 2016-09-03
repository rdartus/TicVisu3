TicVisu3

Mongodb :
assurez vous d'avoir ajouter mongo dans votre PATH lors de l'installation de mongo.
utilisez mongoimport pour importer les fichier JSON dans votre base de donnée.

$ mongoimport -file <nom de la table au pluriel> -db <nom de la base de donnée>

une fois fini les imports de tous les fichiers utilisez mongod pour lancer le démon.

$mongod

RestApi:
dans la rest API lancez la commande :

$ npm install

cela installera les dépendances du projet et permettra l'utilisation de la REST API
