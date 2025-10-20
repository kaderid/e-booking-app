#!/bin/bash

echo "🧹 Nettoyage complet de Docker Desktop..."
echo ""

# Fermer Docker Desktop
echo "1️⃣ Fermeture de Docker Desktop..."
osascript -e 'quit app "Docker"' 2>/dev/null || echo "   Docker n'est pas en cours d'exécution"
sleep 3

# Supprimer la configuration Docker
echo "2️⃣ Suppression de la configuration Docker..."
rm -rf ~/.docker

# Supprimer les containers et données Docker Desktop
echo "3️⃣ Suppression des containers et données..."
sudo rm -rf ~/Library/Containers/com.docker.docker
sudo rm -rf ~/Library/Application\ Scripts/group.com.docker
sudo rm -rf ~/Library/Application\ Support/com.bugsnag.Bugsnag/com.docker.docker

# Supprimer les crash reports
echo "4️⃣ Suppression des crash reports..."
sudo rm -f ~/Library/Application\ Support/CrashReporter/docker-*.plist

# Supprimer l'application Docker Desktop
echo "5️⃣ Suppression de Docker Desktop..."
sudo rm -rf /Applications/Docker.app

# Supprimer les helpers et daemons
echo "6️⃣ Suppression des helpers système..."
sudo rm -rf /Library/PrivilegedHelperTools/com.docker.*
sudo rm -rf /Library/LaunchDaemons/com.docker.*

# Supprimer les liens symboliques
echo "7️⃣ Suppression des liens symboliques..."
sudo rm -f /usr/local/bin/docker
sudo rm -f /usr/local/bin/docker-compose
sudo rm -f /usr/local/bin/docker-credential-desktop
sudo rm -f /usr/local/bin/docker-credential-ecr-login
sudo rm -f /usr/local/bin/docker-credential-osxkeychain
sudo rm -f /usr/local/bin/hub-tool
sudo rm -f /usr/local/bin/hyperkit
sudo rm -f /usr/local/bin/kubectl
sudo rm -f /usr/local/bin/kubectl.docker
sudo rm -f /usr/local/bin/vpnkit

echo ""
echo "✅ Nettoyage terminé !"
echo ""
echo "📝 Fichiers CONSERVÉS (vos projets) :"
echo "   - docker-compose.yml"
echo "   - Dockerfile"
echo "   - .dockerignore"
echo ""
echo "🔄 Prochaines étapes :"
echo "   1. Télécharger Docker Desktop : https://www.docker.com/products/docker-desktop/"
echo "   2. Installer et démarrer Docker Desktop"
echo "   3. Lancer votre projet avec : cd ops && docker-compose up --build"
