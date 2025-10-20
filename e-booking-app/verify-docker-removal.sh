#!/bin/bash

echo "🔍 Vérification de la suppression de Docker..."
echo ""

# 1. Vérifier l'application Docker Desktop
echo "1️⃣ Application Docker Desktop :"
if [ -d "/Applications/Docker.app" ]; then
    echo "   ❌ PRÉSENT : /Applications/Docker.app"
else
    echo "   ✅ SUPPRIMÉ"
fi
echo ""

# 2. Vérifier la configuration utilisateur
echo "2️⃣ Configuration Docker (~/.docker) :"
if [ -d "$HOME/.docker" ]; then
    echo "   ❌ PRÉSENT : ~/.docker"
else
    echo "   ✅ SUPPRIMÉ"
fi
echo ""

# 3. Vérifier les containers
echo "3️⃣ Données Docker Desktop :"
if [ -d "$HOME/Library/Containers/com.docker.docker" ]; then
    echo "   ❌ PRÉSENT : ~/Library/Containers/com.docker.docker"
else
    echo "   ✅ SUPPRIMÉ"
fi
echo ""

# 4. Vérifier les commandes Docker
echo "4️⃣ Commandes Docker :"
if command -v docker &> /dev/null; then
    echo "   ❌ PRÉSENT : docker commande disponible"
    which docker
else
    echo "   ✅ SUPPRIMÉ : commande docker non trouvée"
fi
echo ""

if command -v docker-compose &> /dev/null; then
    echo "   ❌ PRÉSENT : docker-compose commande disponible"
    which docker-compose
else
    echo "   ✅ SUPPRIMÉ : commande docker-compose non trouvée"
fi
echo ""

# 5. Vérifier les helpers système (nécessite sudo)
echo "5️⃣ Helpers système :"
if ls /Library/PrivilegedHelperTools/com.docker.* 2>/dev/null; then
    echo "   ❌ PRÉSENT : Helpers système Docker trouvés"
else
    echo "   ✅ SUPPRIMÉ : Aucun helper système Docker"
fi
echo ""

# 6. Vérifier les daemons
echo "6️⃣ Daemons système :"
if ls /Library/LaunchDaemons/com.docker.* 2>/dev/null; then
    echo "   ❌ PRÉSENT : Daemons Docker trouvés"
else
    echo "   ✅ SUPPRIMÉ : Aucun daemon Docker"
fi
echo ""

# 7. Vérifier tous les fichiers Docker restants
echo "7️⃣ Recherche globale des fichiers Docker système :"
echo "   (Cela peut prendre quelques secondes...)"
DOCKER_FILES=$(find ~ -name "*docker*" 2>/dev/null | grep -v "node_modules" | grep -v "\.vscode" | grep -v "IdeaProjects" | grep -v "\.m2" | grep -v "getting-started" | grep -v "mon_app" | grep -v "mini_projet" | grep -v "ticketing" | grep -v "Tools" | grep -v "\.oh-my-zsh" | grep -v "Library/Caches/JetBrains" | grep -v "Library/Application Support/JetBrains")

if [ -z "$DOCKER_FILES" ]; then
    echo "   ✅ Aucun fichier Docker système trouvé"
else
    echo "   ⚠️  Fichiers Docker restants :"
    echo "$DOCKER_FILES"
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 RÉSULTAT FINAL :"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

ALL_CLEAR=true

[ -d "/Applications/Docker.app" ] && ALL_CLEAR=false
[ -d "$HOME/.docker" ] && ALL_CLEAR=false
[ -d "$HOME/Library/Containers/com.docker.docker" ] && ALL_CLEAR=false
command -v docker &> /dev/null && ALL_CLEAR=false

if [ "$ALL_CLEAR" = true ]; then
    echo "✅ Docker Desktop est COMPLÈTEMENT SUPPRIMÉ"
    echo "   Vous pouvez maintenant réinstaller proprement."
else
    echo "⚠️  Certains fichiers Docker sont encore présents"
    echo "   Relancez le script clean-docker.sh avec sudo"
fi
echo ""
