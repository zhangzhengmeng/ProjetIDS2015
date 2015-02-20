# PHP Voxygen

![Screenshot de PHP Voxygen](http://i.imgur.com/C2ILxAN.png)

## Qu'est-ce que PHP Voxygen ?

PHP Voxygen est un service en ligne permettant d'utiliser la synthèse vocale de Voxygen.fr sans avoir à passer par leur interface.

## Euh, mais quelles différences entre ce fork et la version originale ?

* Correction de bugs (le nom des fichiers est désormais basé sur un md5 du texte et de la voix choisis, et non plus un nombre aléatoire, la déclaration UTF-8 respecte désormais la syntaxe conseillée pour le HTML5)
* Nettoyage du code (passage avec du code redondant simplifiés, remplacement de string_between() par un bête preg_match(), fonction curl_post() remplacée par une fonction custom plus légère)
* Citation d'Apcros enlevée (3615 personnal branling, j'écoute ?)
* Remplacement du player de Google par Dewplayer et mise par défaut du player flash (si l'utilisateur possède flash)
* Intégration des libraires en local
* Ajout d'un filtre anti-censure
* Vérifications lorsque l'utilisateur ne rentre pas de texte ou que Voxygen renvoie des trucs chelou
* Création d'une classe Voxygen

## On me dit que Voxygen a changé ses API, alors que PHP Voxygen fonctionne sur un autre serveur. Pourquoi ?

J'ai eu ce problème une fois, quand je voulais faire une démo dans un lieu où le pare-feu était assez restrictif. Vérifiez vos réglages réseaux et votre serveur web.

## Filtre GROMMO (anti-censure)

Ce filtre permet d'autoriser quelques mots "interdits" par VoxyGen.fr d'être prononcés. La base de données s'élève actuellement à un nombre limité de "traductions" mais si il y a des gens qui seraient intéressés pour m'aider, je suis prenneur, le système est fait, maintenant, il ne reste plus qu'à ajouter vos équivalents gros-mot -> charabia :)

## Dossier cache

PHP Voxygen ne dispose pas actuellement de système de vidage du cache.

> rm -rf cache/*

Démerdez-vous avec ça :>

Il serait néanmoins judicieux de faire un script cron effectuant la commande ci-dessus toutes les XX minutes.

## Utilisation de PHP Voxygen avec des applications tierces

PHP Voxygen a sa classe pour être utilisé dans vos propres applications. Libre à vous d'optimiser cette classe, j'accepte les pull-request (pour peu que vous me foutez pas de la merde ^.^).

Elle se trouve dans /engine.php, et elle s'utilise de la manière suivante :

```php
<?php
	$voxygenHandler = new Voxygen(Activation de GROMMO : (bool),Dossier de stockage du cache : (string));
	$voxygenHandler->voiceSynthesis(Voix : (string),Texte : (string)); // Retourne url du fichier stocké (string)
?>
```

## Licences, légal, etc.

L'utilisation de PHP Voxygen doit se faire uniquement dans le but d'évaluer les services proposés par [Voxygen SAS](http://voxygen.fr), conformément aux [mentions légales](http://voxygen.fr/fr/content/mentions-legales) du démonstrateur de Voxygen.

Les fichiers audio produits par Voxygen SAS sont protégés par le droit d'auteur, et ne doivent pas être diffusés sans l'autorisation de Voxygen SAS.

PHP Voxygen a été développé en toute indépendance de Voxygen SAS, et n'est pas affilié à Voxygen SAS.

Voxygen est une marqué déposée.

## Remerciements

 * [mGeek](http://mgeek.fr/) : code initial
 * [albinpopote](https://github.com/albinpopote) : bugfixes