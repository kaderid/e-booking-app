-- Insertion de données de test (optionnel)

-- Insérer un admin (mot de passe: admin123)
INSERT INTO users (prenom, nom, email, telephone, mot_de_passe, role, statut)
VALUES ('Admin', 'System', 'admin@ebooking.com', '+221770000000', '$2a$10$XQSXx9GXZ0PsXL8Y.a8xzOXxKJ3Z2a6H5E3Z5Q0Q7Z8Z9Z0Z1Z2Z3', 'ADMIN', 'ACTIF')
ON CONFLICT DO NOTHING;

-- Insérer quelques services
INSERT INTO service_entity (nom, description)
VALUES
    ('Médecine', 'Consultations médicales générales'),
    ('Coiffure', 'Services de coiffure et de soins capillaires'),
    ('Conseil juridique', 'Consultation et conseil juridique')
ON CONFLICT DO NOTHING;
