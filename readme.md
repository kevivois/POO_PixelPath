# Pixel Path

## Description

"Pixel Path" est un jeu d'arcade en 2D captivant qui mettra à l'épreuve vos réflexes et votre capacité à éviter des obstacles dangereux tout en progressant à travers des niveaux variés. Inspiré par des jeux classiques comme Crossy Road, Pixel Path vous plonge dans une aventure palpitante où votre personnage doit naviguer à travers différents environnements remplis de dangers.

## Prérequis

Avant de pouvoir exécuter ce projet, assurez-vous d'avoir les éléments suivants installés sur votre machine :

- Java 8 ou version supérieure
- Scala 2.12 ou version supérieure
- Bibliothèques GDX2D

## Installation

1. Clonez le dépôt :

    ```sh
    git clone https://github.com/votre-utilisateur/pixel-path.git
    cd pixel-path
    ```

2. Installez les dépendances contenues dans le dosser "libs" :

    ```sh
    - gdx2d-desktop-1.2.2.jar
    - gdx2d-desktop-1.2.2-sources.jar
    ```

## Utilisation

Pour jouer à "Pixel Path", suivez ces étapes simples :

1.  Éxecutez le fichier "src/MainPixelPath.scala"

## Manuel du Jeu

### Objectif

Le but principal de Pixel Path est de voir combien de temps vous pouvez survivre et combien de points vous pouvez accumuler en évitant les obstacles. Dépassez vos scores précédents et défiez vos amis pour voir qui peut aller le plus loin.

### Contrôles

- **W** : Déplacez le personnage vers le haut
- **S** : Déplacez le personnage vers le bas
- **A** : Déplacez le personnage vers la gauche
- **D** : Déplacez le personnage vers la droite

### Gameplay

- **Personnage Principal** : Vous contrôlez un personnage en pixel art que vous devez guider à travers différents niveaux en évitant divers obstacles.
- **Routes de Voitures** : Les premiers obstacles sont des routes encombrées de voitures. Vous devez éviter ces voitures pour avancer dans le jeu.
- **Obstacles Futurs** :
    - Trains et Rails : Des niveaux avec des rails de trains seront ajoutés. Les trains sont plus rapides et nécessitent une meilleure stratégie pour les éviter.
    - Rivières et Nénuphars : Des rivières avec des nénuphars mouvants et des plateformes qui disparaissent seront introduites pour des défis supplémentaires.

## Classes Principales

- **Car** : La classe Car est la base de toutes les entités de voiture dans le jeu. Elle contient des méthodes pour contrôler les mouvements, les animations et les interactions des voitures avec l'environnement du jeu. Chaque voiture du jeu, qu'il s'agisse de voitures normales, de trains ou d'autres obstacles mobiles, est basée sur cette classe.


- **Road** : La classe Road est responsable de la génération et de la gestion des routes dans le jeu. Elle permet d'ajouter des voitures sur la route, de contrôler leur mouvement et de vérifier les collisions avec d'autres éléments du jeu. La classe Road joue un rôle crucial dans la création d'un environnement dynamique et réaliste pour le déplacement du personnage.


- **LayerHelper** : LayerHelper est un objet utilitaire conçu pour faciliter la manipulation des couches de tuiles dans le jeu. Il offre des méthodes pour étendre les couches de tuiles, récupérer des informations sur les tuiles et effectuer d'autres opérations de manipulation de cartes. Cette classe simplifie le processus de gestion des niveaux et de l'interaction avec les tuiles de la carte.


- **MainPixelPath**: MainPixelPath est la classe principale du projet. Elle agit comme le point d'entrée de l'application, orchestrant l'initialisation et la gestion globale du jeu. Cette classe est responsable de la configuration initiale de l'environnement de jeu, de la gestion des événements de rendu et d'entrée, ainsi que de la coordination des différentes parties du jeu, telles que les entités de voiture et les routes.

## Images du jeu
