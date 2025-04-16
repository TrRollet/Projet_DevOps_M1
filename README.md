# Projet_DevOps_M1

![Tests](https://github.com/TrRollet/Projet_DevOps_M1/actions/workflows/test-coverage.yml/badge.svg)
[![Deploy to GitHub Packages](https://github.com/TrRollet/Projet_DevOps_M1/actions/workflows/deploy-maven.yml/badge.svg)](https://github.com/TrRollet/Projet_DevOps_M1/actions/workflows/deploy-maven.yml)
[![Deploy to GitHub Pages](https://github.com/TrRollet/Projet_DevOps_M1/actions/workflows/deploy-github-pages.yml/badge.svg)](https://github.com/TrRollet/Projet_DevOps_M1/actions/workflows/deploy-github-pages.yml)
![Coverage](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/TrRollet/ac9b3459fd4861224847a25640876620/raw/coverage.json)

La documentation du projet est disponible [ici](https://TrRollet.github.io/Projet_DevOps_M1/)

# Fonctionnalités Implémentées

## Création de DataFrames
- Construction à partir de colonnes de données
- Construction à partir de fichiers CSV avec détection automatique des types

## Affichage et Sélection 
### Construction à partir de colonnes de données
- Support des types String, Integer et Double
- Vérification de cohérence des dimensions
- Ajout dynamique de colonnes

### Construction à partir de fichiers CSV
- Lecture des en-têtes automatique
- Détection automatique des types :
  - Integer : valeurs entières uniquement
  - Double : valeurs numériques décimales
  - String : valeurs textuelles ou mixtes
- Gestion des valeurs manquantes (null)

## Statistiques
- Calcul de la moyenne pour les colonnes numériques
- Calcul de la médiane 
- Détermination des valeurs min et max 
- Calcul de l'écart-type 
- Gestion des valeurs nulles dans les calculs statistiques

## Tests et Qualité de Code
- Tests unitaires complets avec JUnit
- Couverture de code >90% avec Jacoco
- Intégration continue via GitHub Actions:
  - Tests automatiques
  - Rapport de couverture
  - Déploiement Maven automatique
  - Génération de la documentation

## Choix d'outil
- Maven pour la gestion de projet et les dépendances
- JUnit 4 pour les tests unitaires
- JaCoCo pour la couverture de code
- GitHub Actions pour l'intégration continue
- GitHub Pages pour la documentation
- GitHub Packages pour le déploiement du package maven

## Workflow Git
- Utilisation de branches de fonctionnalités (feature branches)
- Revue de code via Pull Requests
- Protection de la branche principale
- Validation automatique des PR avec tests et couverture

## Déploiement
- Publication automatique sur GitHub Packages
- Génération et déploiement de la documentation sur GitHub Pages
- Processus de validation des Pull Request :
  - Vérification automatique de la couverture de code (minimum 80%)
  - Revue de code obligatoire par au moins un autre membre
  - Tests automatiques passants
  - Résolution des conflits avant fusion
  - Respect des conventions de codage (JavaDoc, formatage)

## Feedback
- **GitHub Actions** : Simple à utiliser, s'intègre parfaitement avec GitHub. Pratique pour automatiser les tests et déploiements, mais configuration parfois complexe.
- **Maven** : Bon outil pour gérer le projet et les dépendances. Les plugins sont utiles (tests, docs, couverture).
- **JaCoCo** : Permet de voir facilement la couverture du code et facilite l'identification des zones à améliorer.
- **GitHub Pages** : Facile à mettre en place pour héberger la documentation du projet.
- **GitHub Packages** : Pratique pour publier et partager des packages Maven. Intégration fluide avec GitHub.