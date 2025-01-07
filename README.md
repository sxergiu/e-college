bun, ce am facut acuma nu i mare lucru, iti zic pe scurt.
1. am rezolvat sa poti schimba poza de profil
2. am rezolvat sa listezi iteme, in special am lucrat cu pozele lor
Acuma niste posibile probleme. Itemele au fost gandite sa aiba mai multe poze, n am testat asta, am incercat doar cu o poza dar ar trebui sa mearga pt ca de pe front trimiti pe back o lista de 1 string ce reprezinta o poza.N ar trebui sa fie
problema. Ce ar putea ridica unele batai de cap este design ul la mai multe poze, cum sa dai display
Iti explic pe scurt cum functioneaza cu pozele. Ideea de upload functioneza la fel peste tot: tu dai upload la poze din calculator pe un server in cloud si fiecarei poze este atribuita o adresa url.Cum am facut noi?
Folosim cloudinary care este un server cloud pt a stoca pozele si a folosi adresele lor url.Practic front incarca poza pe cloud, dupa ii ia url u si trimite json u catre back cu datele de care are nevoie + adresa url a imaginii.
Ne am complicat cu un alt server cloud pt ca Firebase cere bani ca sa ai storage cloud.

Seara faina, ne auzim pe maine cu chat u si payment u. Peace!
