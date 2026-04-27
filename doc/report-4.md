# Rapport – innlevering 4
**Team:** *Ozonlaget* – *Alexander Nåmdal, Johs Grødem Larsen, Rein Endre Landmark, Sander Aubell Ahlgren, William Sjølett*

## Team / Prosjekt

Ozonlaget

### Roller i teamet
- Rein: TeamLead/HeadHoncho/GloriousLeaderOfTheRepublic
        - Arbeidsfordeling/Samkjøring
        - System-design
        - Musikk/Lyd
        - NPC design
    
    - Alexander:
        - Algoritmeansvarlig
        - Stifinning
        - Pickup-items

    - Johs:
        - Resource manager
        - Grand artist of sprites
        - Accessibility

    - Sander:
        - Menyansvarlig
        - Assistant level designer

    - William:
        - Character controls
        - Bøllefrø (les: idemyldrer)
        - Lead level designer

### Erfaringer med samarbeid og prosjektmetodikk
- I starten var rapportene ikke opp til par, i tillegg til at arbeidsmetodikken ikke fulgte noen spesifikk form. Men etter "reiterasjoner" av rapportene og over tid har både samarbeidskvaliteten og strukturen forbedret seg kraftig. Ved bruk av kanban og git-issue boardet som en virituell tavle, har folk plukket opp arbeidsoppgaver og gjennomført de på en god måte. Vi har nå et godt samarbeid der det er tydelig hvem som gjør hva, samt at alle har et klart bilde av hva som er neste steg i utviklingen. Det bør påpekes at vi har fordelt arbeidsoppgaver utover dette vha. kommunikasjon i discord, rundt forelesninger og i avtalte møter. Dette er skrevet opp i meldinger i discord gruppen eller i møtereferater.


### Gruppedynamikk
- Gruppedynamikken er gjevnt over god, vi har god kommunikasjon i tillegg til deltakende medlemmer. Vi har dog diskutert proaktivitet i forhold til det å være kreativ og kunne ta til seg arbeidsoppgaver som man selv ser som nødvendig/forbedringer. Vi strever etter at alle er komfortable med det de holder på med, diskuterer kode konstruktivt og at det skal være lov å komme med meninger som vi diskuterer og eventuelt stemmer over.

### Kommunikasjon i teamet
- Vi har kommunisert bra, både med ukentlige møter, samt discord. Vi kunne derimot ha avtalt møter litt tydligere og vurdert en frist for å si ifra når/hvis man kommer.

### Retrospektiv
 - Dette var ikke noe vi startet med i begynnelsen av prosjektet, men er nå inkorporert i fellesmøtene våre og gjøres helt i starten slik at vi har et utgangspunkt. Det har fungert bra, selv om vi er relativt nytt.

    - Ting som har blitt nevnt er som følger:
        - Skrevet tester fra starten av
        - Benyttet kanban tidligere
        - Mer effort ifht. rapportskriving
        - Jevne ut antall commits

### Bidrag til kodebasen
- Vi har prøvd å få en gjevn fordeling av hvem som bidrar, det bør nevnes at noen har bidratt mer enn man kan forvente og at dette ikke bør trekke ned de andre. Noen commits er også en felles commit fra en person sin bruker. 

### Referat fra møter
Referat fra møter siden forrige leveranse skal legges ved (mange av punktene over er typisk ting som havner i referat).

Møtereferatene er lagt til i en egen mappe (Ozonlaget\doc\møtereferat).

### Forbedringspunkter til neste sprint
Vi skal få skrevet tester slik at minimum test coverage er dekket som et absolutt minimum. 

### Siste innlevering
For siste innlevering: Gjør et retrospektiv hvor dere vurderer hvordan hele prosjektet har gått. Hva har dere gjort bra, hva hadde dere gjort annerledes hvis dere begynte på nytt?

---

## Krav og spesifikasjon

### Status på krav
Siden forrige møte har vi fokusert veldig på å få et mer "spillbart spill". 

    - Vi har implementert en generisk NPC klasse og en fullt fungerende fiende.
    - Vi har implementert skyting og at fiender dør når de har tatt nok skade
        - 3 ulike våpen
    - Vi har implementert at fiender "vandrer rundt tilfeldig" frem til de ser player
    - Fiender angriper player og player tar skade og dør om HP <= 0
    - Vi har implementert buffs, helse/skjold og ammunisjon pickup items
    - Du kan nå utføre objektet i spillet og vinne med å fly avgårde i et helikopter
    - Vi har nå musikk for mange ulike omstendigheter, samt skytelyder og en del andre lydeffekter
    - Kartet er nå ferdig "møblert" med hus og inventar/terreng


### MVP-status
##### Meny:

    Startmeny: ferdig
    Settings: ferdig
    Help-meny: ferdig

##### Player
    Kan bevege player: ferdig
    Player er animert: ferdig
    Player kan skyte: ferdig
    Player tar skade og kan dø: ferdig
    3 ulike våpen: ferdig

##### Fiender
    NPC med stifinning: ferdig
    Fiendetyper: 4/5 - delvis ferdig
    Fiender animert: 4/5 - delvis ferdig
    Fiender oppsøker player og angriper: ferdig
    Fiender kan bli skutt og dør: ferdig

##### Lyd
    Lydeffekter: ferdig
    Musikk: ferdig

##### Kart

    Vegger: ferdig
    Møbler: ferdig
    Terreng/gulv: ferdig

##### Items

    Buffs:
        - RainbowBuff: ferdig
        - DamageBuff: ferdig
        - SpeedBuff: ferdig
    Pickup-items:
        - Ammo: ferdig
        - Helse/skjold: ferdig
        - Nøkkel/Bensin: ferdig

##### Victory-condition og objektiver
    Implementere bensinkanner/helikopterkeycard: ferdig
    Implementere gate/gatekey: ferdig
    Implementere helikopter: ferdig
    Animere helikopter: ferdig
    Victory-condition (drepe final boss, levere keycard og bensin til helikopter og fly avgårde): ferdig
    Kan restarte etter game-over eller victory: ferdig


### Brukerhistorier
#### Brukerhistorie 1:
    Historie:
    - Bruker vil kunne skyte zombier

    Akseptansekriterie:
    - Bruker kan skyte zombier, zombier tar skade og dør når de blir skutt tilstrekkelig ganger

    Konkrete arbeidsoppgave(r):
    - Implementere gunshots med kollisjon
    - Implementere takeDamage for zombier
    - Implementere zombie død

#### Brukerhistorie 2:
    Historie:
    - Bruker vil kunne plukke opp items og power-ups

    Akseptansekriterie:
    - Bruker kan bevege karakteren sin over items som ammo og power-ups og få en korresponderende effekt

    Konkrete arbeidsoppgave(r):
    - Implementere pickup-able-objects
    - Impementere power-up effekter
    - Implementere healthpacks og ammo crates

#### Brukerhistorie 3:
    Historie:
    - Bruker vil ha et godt designet level med mange detaljer.

    Akseptansekriterie:
    - Bruker kan flytte karakteren rundt på levelet ettersom hvor stien går og komme til alle de viktigste punktene.

    Konkrete arbeidsoppgave(r):
    - Implementere et level med en klar og tydelig sti.
    - Implementere bygninger med rom som bruker kan gå inn i.

#### Brukerhistorie 4:
    Historie:
    - Brukeren ønsker en startmeny med flere animerte knapper slik at det er enkelt og oversiktlig å navigere i spillet og velge ulike funksjoner ved hjelp av museklikk.

    Akseptansekriterie:
    - Brukeren kan se en startmeny når spillet starter.
    - Startmenyen inneholder flere knapper (f.eks. Start, Settings, Help).
    - Knappene er animerte.
    - Brukeren kan trykke på knappene med musen.
    - Hver knapp utfører riktig handling.

    Konkrete arbeidsoppgave(r):
    - Implementere en startmeny med bakgrunn og tittel.
    - Implementere animerte knapper som beveger seg inn på skjermen.
    - Implementere hitboxer for knappene.
    - Implementere museklikk for interaksjon med knappene.
    - Koble knappene til riktig funksjonalitet (bytte GameState).
    
#### Brukerhistorie 5:
    Historie:
    - Bruker vil oppleve en utfordrende overlevelsesmodus der fiendene føles smarte og aktivt jakter på karakteren.
    
    Akseptansekriterie:
    - Fiender kan oppdage og navigere mot brukeren, uavhengig av hvor på kartet brukeren gjemmer seg.
    - Fiender patruljerer (wandering) området dynamisk når de ikke har oppdaget brukeren.
    - Brukeren kan ikke stå stille på ett sted uten å bli funnet over tid (ingen 100% trygge soner).
    - Nivået har en tydelig og oppnåelig vinnerbetingelse.

    Konkrete arbeidsoppgave(r):
    - Implementere en tydelig vinnerbetingelse for nivået.
    - Implementere patruljeringslogikk (wandering) for fiender som ikke har et mål.
    - Implementere pathfinding (A*-algoritme) og forfølgelseslogikk slik at fiender effektivt kan navigere rundt hindringer for å ta brukeren.

#### Brukerhistorie 6:
    Historie:
        - Bruker er sensitiv for blinkende lys og vil ha en mulighet for å unngå dette
    
    Akseptansekriterie:
        - Bruker har mulighet for å skru av blinkende lys

    Konkrete arbeidsoppgave(r):
        - Legge til en knapp i settings for å skru av blinkende lys
    
#### Brukerhistorie 7:
    Historie:
        - Bruker har en svært svak, relativt sett, pc som sliter med å kjøre spillet med darkness og vil ha en måte å skru dette av på.

    Akseptansekritere: 
        - Bruker har mulighet for å skru av darkness og dermed kjøre spillet bedre.

    Konkrete arbeidsoppgave(r):
        - Legge til en knapp i settings for å skru av darkness.

### Planlagte oppgaver
    - Istedenfor å liste opp alle punktene på tavla vår så kan vi heller nevne de viktigste punktene vi skal jobbe med fremover. Vi har nå et feature complete spill, videre fram mot innleveringsfristen er det en del finpuss og testing som må gjøres.

        - Accessability options:
            - Ekstra stor font?

        - Implementere større variasjon av fiender
            - Alle skal implementere 1 fiende - dette gjøres ved bruk av den generiske NPC klassen
        
        - Testing:
            - Rein
                - Model
                - Grid
            - Alexander
                - Pathfinding
                - Items/Buffs
            - William
                - Movement
                - Collision
            - Sander
                - Controller
                - NPC minus stiffing og movement
            - Johs
                - Factory
                - Projectiles
                - Levels
        Testing utvides ved behov.

### Prioritering fremover
Forklar hvordan dere prioriterer oppgavene videre.

### Endringer i MVP-krav
Har dere gjort justeringer på kravene som er med i MVP? Forklar i så fall hvorfor. Hvis det er gjort endringer i rekkefølge utfra hva som er gitt fra kunde, hvorfor er dette gjort?

### Fremdrift siden forrige rapport
Oppdater hvilke krav dere har prioritert, hvor langt dere har kommet og hva dere har gjort siden forrige gang.

### Kjente bugs
Husk å skrive hvilke bugs som finnes i de kravene dere har utført (dersom det finnes bugs).


Kravlisten er lang, men det er ikke nødvendig å levere på alle kravene hvis det ikke er realistisk. Det er viktigere at de oppgavene som er utført holder høy kvalitet. Utførte oppgaver skal være ferdige.

---

## Kode

### Refaktorering
Har dere gjort eller burde dere gjøre noen store endringer / refaktoreringer?

### Arkitektur og designvalg
Hvordan er arkitektur, designvalg etc? Er det lett/vanskelig å få ting til å henge sammen?

### Kodekvalitet
Hvordan er det med kodekvalitet, kodestil osv? Fungerer det OK å utvide og vedlikeholde koden? Kan alle forstå / bruke alle deler av koden?

### Testing
Hvordan ligger dere an med testing?

### Behov for hjelp eller ny kunnskap
Er det noe dere trenger hjelp med eller må sette dere inn i til neste gang?




