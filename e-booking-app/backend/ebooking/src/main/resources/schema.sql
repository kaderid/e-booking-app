-- Table des utilisateurs (Clients, Prestataires, Admins)
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    prenom VARCHAR(50) NOT NULL,
    nom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(20) UNIQUE NOT NULL,
    mot_de_passe TEXT NOT NULL,
    role VARCHAR(10) CHECK (role IN ('CLIENT', 'PRO', 'ADMIN')) NOT NULL,
    statut VARCHAR(10) CHECK (statut IN ('ACTIF', 'BLOQUE')) DEFAULT 'ACTIF'
);

-- Table des services proposés
CREATE TABLE IF NOT EXISTS service_entity (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
);

-- Table des prestataires
CREATE TABLE IF NOT EXISTS prestataire (
    id SERIAL PRIMARY KEY,
    user_id INT UNIQUE NOT NULL,
    specialite VARCHAR(100) NOT NULL,
    adresse TEXT NOT NULL,
    service_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES service_entity(id) ON DELETE CASCADE
);

-- Table des disponibilités des prestataires
CREATE TABLE IF NOT EXISTS disponibilite (
    id SERIAL PRIMARY KEY,
    prestataire_id INT NOT NULL,
    jour_semaine VARCHAR(15) CHECK (jour_semaine IN ('Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche')) NOT NULL,
    heure_debut TIME NOT NULL,
    heure_fin TIME NOT NULL,
    FOREIGN KEY (prestataire_id) REFERENCES prestataire(id) ON DELETE CASCADE
);

-- Table des rendez-vous
CREATE TABLE IF NOT EXISTS rendez_vous (
    id SERIAL PRIMARY KEY,
    client_id INT NOT NULL,
    prestataire_id INT NOT NULL,
    date DATE NOT NULL,
    heure TIME NOT NULL,
    statut VARCHAR(15) CHECK (statut IN ('EN_ATTENTE', 'CONFIRME', 'ANNULE')) DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (client_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (prestataire_id) REFERENCES prestataire(id) ON DELETE CASCADE
);
