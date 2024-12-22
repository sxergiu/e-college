Backend: Controller ul de autentificare are injectate servicii de user si de item. Cele 2 servicii au la randul lor fiecare cat un atribut user care retine pe tot parcursul login u lui unui user pe pagina web datele acestuia, cel mai
important UID ul.Bazat pe asta am facut o conditie de a nu lua itemele cu sellerId == loggedUser.id in query ul de returnare a tuturor itemelor folosit pt dashboard.

Frontend: Bara de search implementata in dashboard.Cautarea nu se face pt match 1 to 1 ci orice contine bucati din categoria unui item sau din nume.Ma gandeam in timp ce iti scriam descria la ce am lucrat ca bara de search foarte posibil
sa nu tina cont de user ul logat si sa returneze iteme ce apartin lui.

De acuma de fiecare data cand testam ceva pe aplicatie trebuie sa ne logam prima data, da asta oricum trbuia facut pt ca nu i bine sa intram direct pe ultimu cont ce am fost logati.
