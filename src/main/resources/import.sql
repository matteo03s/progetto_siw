-- Iniziamo popolando la tabella Ingrediente
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Pomodoro', 0.50, 'images/pomodoro.png');
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Mozzarella', 1.00, 'images/mozzarella.png');
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Prosciutto', 1.50, 'images/prosciutto.png');
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Funghi', 0.75, 'images/funghi.png');
--INSERT INTO ingrediente (nome, prezzo, url_image) VALUES ('Olive', 0.30, 'images/olive.png');
-- Successivamente, popoliamo la tabella Pizza
-- Assumiamo che i nuovi ID delle pizze siano generati automaticamente, quindi non li includiamo nella query
-- Per associare le pizze agli ingredienti, dovrai fare riferimento agli ID degli ingredienti (ad esempio, ingredienti_id)


--Inserisci l'admin username=gestore pw=pwadmin
INSERT INTO credentials (username, password, role) VALUES ('gestore', '$2a$10$JOX7nLjPEbvs/iUIp9D0b.S7MLOfKtsn1yzUr2ZsRgwN1d60v7SCa', 'PROVIDER');
INSERT INTO credentials (username, password, role) VALUES ('user', 'resu', 'UTENTE');

-- Prima pizza
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Margherita', 'pizza', 'Pizza con pomodoro e mozzarella', 6.00, 'https://stanzedicinema.com/wp-content/uploads/2010/05/inception-poster4.jpg');
-- Seconda pizza
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Capricciosa', 'pizza', 'Pizza con pomodoro, mozzarella, prosciutto, funghi e olive', 8.00, 'images/capricciosa.png');
-- Terza pizza
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Vegetariana', 'pizza', 'Pizza con pomodoro, mozzarella, funghi, olive', 7.50, 'images/vegetariana.png');

--inserisci gli sfizi
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Crocchette di Patate', 'sfizio', 'Crocchette di patate fritte croccanti fuori e morbide dentro.', 4.00, 'https://www.cucchiaio.it/content/cucchiaio/it/ricette/2009/11/ricetta-crocchette-patate/_jcr_content/header-par/image_single.img.jpg/1596464005541.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Arancini di Riso', 'sfizio', 'Riso con ragù e formaggio, impanati e fritti.', 5.50, 'https://arancinotto.com/cdn/shop/articles/Ricette-71.jpg?v=1535464729');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Frittelle di Baccalà', 'sfizio', 'Baccalà pastellato e fritto, servito caldo.', 6.00, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9ZFh7u4KtrizmH8VP6-amnKVcggsGdFnS1A&s');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Mozzarella in Carrozza', 'sfizio', 'Fette di mozzarella impanate e fritte, servite calde.', 5.00, 'https://www.lamozzarellaincarrozza.it/IMMAGINI/0-mozzarella-in-carrozza-la-mozzarella-in-carrozza-ricette-blog.jpg');

--inserisci le bevande
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Coca Cola', 'bevanda', 'Bibita gassata e dolce, disponibile in bottiglia o lattina.', 2.50, 'http://example.com/coca_cola.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Coca Zero', 'bevanda', 'Bibita gassata, disponibile in bottiglia o lattina.', 2.50, 'http://example.com/coca_cola.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Fanta', 'bevanda', 'Bibita gassata al gusto di arancia, fresca e dissetante.', 2.50, 'http://example.com/fanta.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Acqua Naturale', 'bevanda', 'Acqua minerale naturale, servita fredda.', 1.00, 'http://example.com/acqua_naturale.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Acqua Frizzante', 'bevanda', 'Acqua minerale frizzante, servita fredda.', 1.00, 'http://example.com/acqua_naturale.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Birra Peroni', 'bevanda', 'Birra lager italiana, fresca e dissetante, in bottiglia o alla spina.', 3.00, 'http://example.com/birra_peroni.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Sprite', 'bevanda', 'Bibita gassata al gusto di limone, leggera e frizzante.', 2.50, 'http://example.com/sprite.jpg');

--inserisci i dolci
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Tiramisù', 'dolce', 'Dessert italiano a base di mascarpone, caffè e cacao, servito in coppa.', 4.50, 'http://example.com/tiramisu.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Cannoli Siciliani', 'dolce', 'Cialda croccante ripiena di ricotta e gocce di cioccolato, tipica siciliana.', 5.00, 'http://example.com/cannoli.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Panna Cotta', 'dolce', 'Dolce al cucchiaio a base di panna, zucchero e vaniglia, servito con frutti di bosco.', 4.00, 'http://example.com/panna_cotta.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Frutta Fresca', 'dolce', 'Mix di frutta fresca di stagione, leggera e dissetante.', 3.00, 'http://example.com/frutta_fresca.jpg');
INSERT INTO prodotto (nome, categoria, descrizione, prezzo, url_image) VALUES ('Crostata di Marmellata', 'dolce', 'Pasta frolla ripiena di marmellata di frutta, croccante e gustosa.', 3.50, 'http://example.com/crostata.jpg');