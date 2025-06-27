
--Inserisci l'admin username=gestore pw=pwadmin
INSERT INTO credentials (username, password, role) VALUES ('gestore', '$2a$10$JOX7nLjPEbvs/iUIp9D0b.S7MLOfKtsn1yzUr2ZsRgwN1d60v7SCa', 'PROVIDER');
--Inserisci un utente base username=utente pw=utente
INSERT INTO credentials (username, password, role) VALUES ('utente', '$2a$10$/1qzoQAwjUf/GA2Tgwz/m.SuaueQODPzaSWjsfnX8O9Ty5KAZ.HLK', 'UTENTE');

-- Inserimento in Utente
INSERT INTO app_user (name, surname, email, numero_telefonico) VALUES ('Mario', 'Rossi', 'mario.rossi@example.com', '1234567890');
INSERT INTO app_user (name, surname, email, numero_telefonico) VALUES ('Anna', 'Verdi', 'anna.verdi@example.com', '0987654321');
INSERT INTO app_user (name, surname, email, numero_telefonico) VALUES ('Giulia', 'Bianchi', 'giulia.bianchi@example.com', '1122334455');

-- Inserimento in Credenziali, con riferimento a Utente
--aaa
INSERT INTO credentials (username, password, role, user_id) VALUES ('mario_rossi', '$2a$10$9z6bRpThZOwGn0s4ALmaLOo3HxnePIedMS1pmw1bcqe.Lf5ZNrhhy', 'UTENTE', 1);
--aaa
INSERT INTO credentials (username, password, role, user_id) VALUES ('anna_verdi', '$2a$10$9z6bRpThZOwGn0s4ALmaLOo3HxnePIedMS1pmw1bcqe.Lf5ZNrhhy', 'UTENTE', 2);
INSERT INTO credentials (username, password, role, user_id) VALUES ('admin_giulia', 'adminpass789', 'ADMIN', 3);


-- Pizze
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Margherita', 'pizza', 'Pizza con pomodoro e mozzarella', 6.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Capricciosa', 'pizza', 'Pizza con pomodoro, mozzarella, prosciutto, funghi e olive', 8.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Vegetariana', 'pizza', 'Pizza con pomodoro, mozzarella, funghi, olive', 7.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Diavola', 'pizza', 'Pizza con pomodoro, mozzarella e salame piccante.', 7.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Quattro Formaggi', 'pizza', 'Pizza con mozzarella, gorgonzola, fontina e parmigiano.', 8.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Marinara', 'pizza', 'Pizza con pomodoro, aglio, origano e olio extravergine.', 5.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Bufalina', 'pizza', 'Pizza con pomodoro, mozzarella di bufala e basilico fresco.', 8.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Boscaiola', 'pizza', 'Pizza con salsiccia, funghi e panna.', 8.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Tonno e Cipolla', 'pizza', 'Pizza con pomodoro, mozzarella, tonno e cipolla rossa.', 7.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Napoli', 'pizza', 'Pizza con pomodoro, mozzarella e acciughe.', 7.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Speck e Brie', 'pizza', 'Pizza con mozzarella, speck affumicato e formaggio brie.', 8.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Parmigiana', 'pizza', 'Pizza con melanzane grigliate, pomodoro, mozzarella e parmigiano.', 8.00);

-- inserisci gli sfizi
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Crocchette di Patate', 'sfizio', 'Crocchette di patate fritte croccanti fuori e morbide dentro.', 4.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Arancini di Riso', 'sfizio', 'Riso con ragù e formaggio, impanati e fritti.', 5.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Frittelle di Baccalà', 'sfizio', 'Baccalà pastellato e fritto, servito caldo.', 6.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Mozzarella in Carrozza', 'sfizio', 'Fette di mozzarella impanate e fritte, servite calde.', 5.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Supplì al Telefono', 'sfizio', 'Riso al ragù con cuore filante di mozzarella, impanato e fritto.', 4.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Olive Ascolane', 'sfizio', 'Olive verdi ripiene di carne e fritte dorate.', 5.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Patatine Fritte', 'sfizio', 'Patatine dorate e croccanti, servite calde.', 3.50);

-- inserisci le bevande
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Acqua Naturale', 'bevanda', 'Acqua minerale naturale, servita fredda.', 1.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Acqua Frizzante', 'bevanda', 'Acqua minerale frizzante, servita fredda.', 1.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Birra Peroni', 'bevanda', 'Birra lager italiana, fresca e dissetante, in bottiglia o alla spina.', 3.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Coca Cola', 'bevanda', 'Bibita gassata e dolce, disponibile in bottiglia o lattina.', 2.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Coca Zero', 'bevanda', 'Bibita gassata, disponibile in bottiglia o lattina.', 2.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Fanta', 'bevanda', 'Bibita gassata al gusto di arancia, fresca e dissetante.', 2.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Tè al Limone', 'bevanda', 'Bevanda fresca al gusto di limone.', 2.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Tè alla Pesca', 'bevanda', 'Bevanda fresca al gusto di pesca.', 2.00);

-- inserisci i dolci
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Tiramisù', 'dolce', 'Dessert italiano a base di mascarpone, caffè e cacao, servito in coppa.', 4.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Cannoli Siciliani', 'dolce', 'Cialda croccante ripiena di ricotta e gocce di cioccolato, tipica siciliana.', 5.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Panna Cotta', 'dolce', 'Dolce al cucchiaio a base di panna, zucchero e vaniglia, servito con frutti di bosco.', 4.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Frutta Fresca', 'dolce', 'Mix di frutta fresca di stagione, leggera e dissetante.', 3.00);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Crostata di Marmellata', 'dolce', 'Pasta frolla ripiena di marmellata di frutta, croccante e gustosa.', 3.50);
INSERT INTO prodotto (nome, categoria, descrizione, prezzo) VALUES ('Torta al Cioccolato', 'dolce', 'Morbida torta al cioccolato fondente.', 4.00);




-- Martedì sera
INSERT INTO prenotazione (nome, data, posti, turno, utente_id) VALUES ('Marco', '2025-07-15', 6, 'cena', 1);
-- Giovedì sera
INSERT INTO prenotazione (nome, data, posti, turno, utente_id) VALUES ('Luca', '2025-07-17', 2, 'cena', 2);
-- Sabato pranzo
INSERT INTO prenotazione (nome, data, posti, turno, utente_id) VALUES ('Stefano', '2025-07-19', 5, 'pranzo', 3);
-- Domenica sera
INSERT INTO prenotazione (nome, data, posti, turno, utente_id) VALUES ('Marco', '2025-07-20', 8, 'cena', 1);
-- Venerdì sera
INSERT INTO prenotazione (nome, data, posti, turno, utente_id) VALUES ('Mauro', '2025-07-18', 10, 'cena', 2);
-- Sabato sera
INSERT INTO prenotazione (nome, data, posti, turno, utente_id) VALUES ('Laura', '2025-07-19', 9, 'cena', 3);
-- Sabato pranzo
INSERT INTO prenotazione (nome, data, posti, turno, utente_id) VALUES ('Gianni', '2025-07-19', 3, 'pranzo', 1);
-- Domenica pranzo
INSERT INTO prenotazione (nome, data, posti, turno, utente_id) VALUES ('Stefano', '2025-07-20', 3, 'pranzo', 1);
