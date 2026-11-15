# Kipas Football – application Android

Jeu de tirs au but + publicités Google AdMob (bannière en bas + pub plein écran toutes les 3 parties).

## Générer l'APK (gratuit)
1. Installe **Android Studio** sur un ordinateur (gratuit).
2. `File > Open` → choisis ce dossier `kipas-app`. Laisse Gradle se synchroniser.
3. Branche ton téléphone (mode développeur + débogage USB) et clique ▶ pour tester.
4. Pour l'APK/AAB : `Build > Generate Signed Bundle / APK`.

## Activer les vraies pubs
Les identifiants actuels sont des **IDs de TEST Google** (les pubs de test s'affichent, mais ne rapportent rien).
1. Crée un compte sur https://admob.google.com et ajoute l'application.
2. Remplace dans le projet :
   - `AndroidManifest.xml` → `APPLICATION_ID` (ca-app-pub-XXXX~YYYY)
   - `res/layout/activity_main.xml` → `ads:adUnitId` (bannière)
   - `MainActivity.java` → `INTERSTITIAL_ID` (plein écran)
3. Ne clique JAMAIS sur tes propres pubs en version finale (risque de bannissement).

## Publication
Compte Google Play Console (25 $ une seule fois) + politique de confidentialité en ligne obligatoire.

## Modifier le jeu
Le jeu est dans `app/src/main/assets/index.html`.
