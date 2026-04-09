# Rapport – innlevering 3
**Team:** *Ozonlaget* – *Alexander Nåmdal, Johs Grødem Larsen, Rein Endre Landmark, Sander Aubell Ahlgren, William Sjølett*

## Team / Prosjekt

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

### Eventuelle nye roller

    - Siden sist har vi sett oss nødt til å fordele noen nye roller, nemlig:
        - Musikk/Lyd
        - NPC design
        - Pickup-items
        - Lead level designer
        - Assistant level designer

    Disse er fordelt i punktet over

### Erfaringer med samarbeid og prosjektmetodikk
    - Vi har jobbet mye med kanban, og har benyttet git-issue-boardet som en virituell tavle. Vi har brukt dette delvis med at folk har "plukket opp" arbeidsoppgaver og litt med at arbeidsoppgaver har blitt fordelt. Dette har funket veldig greit, slik at vi har oversikt over hva som må gjøres, hvem som gjør hva (slik at vi ikke jobber på samme ting), samt fungert veldig bra for planlegging og for å se "neste steg" i utviklingen.

### Gruppedynamikk

    - Vi har diskutert litt om proaktivitet innen det å være kreativ og å ta til seg arbeidsoppgaver som ligger utenfor komfortsonen. Vi jobber med å sørge for at alle er komfortable med det de holder på med, diskuterer kode og har en holdning der det er lov å gjøre egne kreative valg om man kommer på en god ide.

### Kommunikasjon i teamet
    - Vi har kommunisert bra, både med ukentlige møter samt på discord. 

### Retrospektiv
    - Dette var ikke noe vi startet med i begynnelsen av prosjektet, men er nå inkorporert i fellesmøtene våre og gjøres helt i starten slik at vi har et utgangspunkt. Det har fungert bra, selv om vi er relativt nye på dette og ikke alltid har veldig mye input å komme med.

    - Ting som har blitt nevnt er som følger:
        - Jobbet mer med tester fra starten av
        - Benyttet kanban tidligere
        - Mer effort med rapport-skriving
        - Jevne ut antall commits

### Referat fra møter
Referat fra møter siden forrige leveranse skal legges ved (mange av punktene over er typisk ting som havner i referat).

### Forbedringspunkter til neste sprint
    ref retrospektiv

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
    Settings: ikke ferdig
    Help-meny: ferdig

##### Player
    Kan bevege player: ferdig
    Player er animert: ferdig
    Player kan skyte: ferdig
    Player tar skade og kan dø: ferdig
    3 ulike våpen: ferdig

##### Fiender
    NPC med stifinning: ferdig
    Fiendetyper: 1/5 - delvis ferdig
    Fiender animert: 1/5 - delvis ferdig
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
    Implementere bensinkanner/helikopternøkkel: ferdig
    Implementere helikopter: ferdig
    Animere helikopter: ferdig
    Victory-condition (levere nøkkel og bensin til helikopter og fly avgårde): ferdig
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
    

### Planlagte oppgaver
    - Istedenfor å liste opp alle punktene på tavla vår så kan vi heller nevne de viktigste punktene vi skal jobbe med fremover.
    - Vi har nå et "ferdig spill" og nå er det en del finpuss og accessability options som må gjøres.

        - Accessability options:
            - ikke-blinkende rainbowBuff
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
        Testing utvides etter behov

            


### Prioritering fremover

    - Testing skal prioriteres fremover, samt få implementert alt i MVP

### Fremdrift siden forrige rapport
    - Vi har i utgangpunktet prioritert rammeverk for å få "bakgrunnslogikken" og system-designet til å funke. 
    Som resultat var det lite synlig progresjon i starten, men tilsvarende stor progresjon senere i prosjektet.

    Til forrige rapport hadde vi essensielt bare en animert player og en placeholder fiende med stifinning og kollisjon.
    Nå har vi i essens et ferdig spill med 
        - skyting 
        - skade 
        - objekter/mål
        - win/loss condition
        - buffs med visuelle komponenter
        - pickup-able items
        - kompliserte fiender
            - flere typer angrep
            - relativt god stifinning
        - detaljert kart med hus og møbler.
        - lydeffekter
        - musikk


### Kjente bugs

    - Det er en bug der fiender kan få en livelock mens de er i vandre-modus og står i veien for hverandre i en trang passasje. Dette kan føre til klogging av fiender, men er en relativt liten bug som for øyeblikket ikke er en prioritet. 

## Kode

### Refaktorering
    - Vi har refaktorert Map og IMap til å bli Model og IModel for å ha en bedre navnekonvensjon iht MVC konseptet

### Arkitektur og designvalg

    - Arkitekturen vår er veldig modulær og følger MVC konseptet. Vi har valgt å designe det slik fordi det fører til færre konflikter i git, gjør endringer i programmet lettere å utføre, samt at det gjør det lettere å utvide med flere ting senere. Vi har jobbet hard med å benytte oss av interfaces (vi har for øyeblikket 19stk), samt abstrakte klasser som NPC og GUN klassene som gjør det veldig lett å implementere flere slike klasser som oppfører seg relativt likt.

    Vi har hele tiden jobbet med MVC konseptet i tankene der vi har de tre følgende "kjernefunksjonene":
        - Model:
            - Inneholder all "data", alle objekter og gamestates
        - Controller:
            - Oversetter input fra bruker til funksjonskall på objekter i Model
            - Bruker timere til å gjøre automatiske funksjonkall på objekter i Model for å "få spillet til å gå"
        - View:
            - Henter ut objekter fra Model og tegner de
            - Inkluderer en relativt komplisert debug-modus

### Kodekvalitet
Hvordan er det med kodekvalitet, kodestil osv? Fungerer det OK å utvide og vedlikeholde koden? Kan alle forstå / bruke alle deler av koden?

### Testing
    - Vi har skrevet en del tester, men her er det en jobb å gjøre og dette blir fokus fremover. Vi har ganske åpenbart ikke bedrevet test-drevet utvikling.

### Behov for hjelp eller ny kunnskap
    - Dette tror jeg vi har kontroll på, og vi diskuterer dette in-house dersom noen trenger hjelp.



