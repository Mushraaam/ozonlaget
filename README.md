# INF112 Project – *Kurt-Mario in the Land of the Mushroom Princess*

* Team: *Ozonlaget* (Gruppe 2): *Alexander Nåmdal, Johs Grødem Larsen, Rein Endre Landmark, Sander Aubell Ahlgren, William Sjølett*
* Lenke til [Gitlab](git.app.uib.no/inf112/26v/proj/ozonlaget)

## Om spillet
Velkommen til Kurt-Mario in the Land of the Mushroom Princess – vårt hjertebarn av et 2D top-down overlevelsesspill, og et levende bevis på at nok koffein kan konverteres til (stort sett) fungerende Java-kode.

Konseptet er fryktelig enkelt: Du er fanget på et kart fullt av fiender. Målet ditt er å samle nok bensinkanner, finne en helikopternøkkel, og komme deg vekk før du blir zombiemat. Hvorfor Kurt-Mario roter rundt her inne for en sopp-prinsesse? Ikke tenk for mye på det, vi trengte bare et kult navn.

Slik overlever du (eller i det minste prøver):

Bevegelse & skyting: WASD for å flytte beina, sikt med musa og venstreklikk for å skyte løs.

Våpenarsenal: Trykk 1 for Pistol, 2 for MP5 (når du har dårlig tid) og 3 for Hagle (når de kommer ubehagelig nærme).

Ryggsekk (Inventory): Trykk B for å sjekke hvor mange bensinkanner og nøkler du har klart å klore til deg uten å dø.

Snacks på veien: Plukk opp helse, rustning og ammo rundt omkring på kartet. Vi har også lagt inn power-ups som Speed Buff, Damage Buff og en smått psykedelisk "Rainbow Buff" for å holde moralen oppe når ting røyner på.


## Kjøring
* Kompileres med `mvn clean package -DskipTests` - Skip kompilering av tester (tidkrevende)
* Kjøres med `java -jar target/ozonlaget-1.26-SNAPSHOT-fat.jar`
* Krever Java 21 eller senere


