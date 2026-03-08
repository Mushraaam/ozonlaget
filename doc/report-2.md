# Rapport – innlevering 2
**Team:** *Ozonlaget* – *Alexander Nåmdal, Johs Grødem Larsen, Rein Endre Landmark, Sander Aubell Ahlgren, William Sjølett*

# Team/prosjekt

## Hvordan fungerer rollene i teamet? Trenger dere å oppdatere hvem som er teamlead eller kundekontakt?
    - Rollene funker greit, vi nærmer oss i mål med stifinning, så den korresponderende rollen må snart oppdateres

## Trenger dere andre roller? Skriv ned noen linjer om hva de ulike rollene faktisk innebærer for dere.
    - Vi er snart i mål med alle rammeverk trengs for implementering - så nå kommer det en ny del arbeidsoppgaver som går mer ut på implementering enn utvikling - eksempel: designe levels
    
    Utover dette er rollene vi er tildelt ganske selvforklarende


## Er det noen erfaringer enten teammessig eller mtp prosjektmetodikk som er verdt å nevne? Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet fungerer på?

    - For øyeblikket er det ganske stor variasjon på motivasjon og engasjement - vi har en liten fremtidig utfordring med å få balansert dette.

## Hvordan er gruppedynamikken? Er det uenigheter som bør løses?

    - Dynamikken funker greit, men ref. forrige punkt


## Hvordan fungerer kommunikasjonen for dere?

    - Kommunikasjon funker bra, vi snakker sammen på discord og booker møterom hver mandag

## Gjør et kort retrospektiv hvor dere vurderer hva dere har klart til nå, og hva som kan forbedres. Dette skal handle om prosjektstruktur, ikke kode. Dere kan selvsagt diskutere kode, men dette handler ikke om feilretting, men om hvordan man jobber og kommuniserer.

    - Vi har kommet i gang med kanban, noe som funker greit. Om det er en feil vi har gjort sånn rent organisasjonsmessig så er det muligens at vi har brukt veldig lite tid på å forklare hverandre koden som er skrevet og hvordan ting henger sammen. Vi er nå oppe i en såpass stor mengde kode at det kan være vanskelig å starte for de som ikke allerede har jobbet kontinuerlig med koden.

## Under vurdering vil det vektlegges at alle bidrar til kodebasen. Hvis det er stor forskjell i hvem som committer, må dere legge ved en kort forklaring for hvorfor det er sånn. Husk å committe alt. (Også designfiler)

    - Her har vi fortsatt en utfordring. Men det skal sies at en del har naturlige forklaringer som sykdom, familie og konte-eksamener. Dette skal utbedres. Det er også rimelig å si at noen av gruppemedlemmene kommer til å bidra vesentlig mer enn det som kan forventes av en gjennomsnittlig student - og burde ikke straffes for det.


## Referat fra møter siden forrige leveranse skal legges ved (mange av punktene over er typisk ting som havner i referat).

    - Vi har ikke skrevet detaljerte referater, men denne rapporten vil i dekke essensen av det ikke-tekniske vi har diskutert.

## Bli enige om maks tre forbedringspunkter fra retrospektivet, som skal følges opp under neste sprint.

    - Vi må få opp motivasjonen
    - Vi må diskutere kodebasen slik at alle har en forståelse av hvordan den funker
    - Vi bør muligens øke mengden og frekvensen på kommunikasjonen og presisere at det er lov å stille spørsmål etc
    
# Krav og spesifikasjon


## Oppdater hvilke krav dere har prioritert, hvor langt dere har kommet og hva dere har gjort siden forrige gang. Er dere kommet forbi MVP? Forklar hvordan dere prioriterer ny funksjonalitet.

    - Det har åpenbart vært litt synlig utvikling i programmet siden siste, som f.eks å få opp en UI-bar med helse, ammo og våpen

    - Mesteparten av arbeidet har vært "behind the scenes" der rammeverk har blitt utviklet, samt mye arbeid på stifinning. Det skal fremover være mye lettere å lage ting - f.eks:
        - om man nå legger til en vegg vil stifinning automatisk ta hensyn til den
        - samme med møbler - vi differensierer på møbler og vegger siden de skal oppføre seg ulikt basert på line of sight og kule-kollisjon
        - vi har en generisk NPC klasse, så om man vil implementere en ny fiende kan dette gjøres veldig lett
        - Samme med en generisk Gun klasse

## For hvert krav dere jobber med, må dere lage 1) ordentlige brukerhistorier, 2) akseptansekriterier og 3) arbeidsoppgaver. Husk at akseptansekriterier ofte skrives mer eller mindre som tester

    - For øyeblikket jobber vi med:
        - Implementere meny
            - Bruker ønsker en visuell meny der det går ann å navigere seg med musen
            - Bruker får opp meny(er) der hver knapp indikerer hva den gjør og har korrekt effekt når den blir trykket på
        
        - Implementere ny fiende med angrepsanimasjon og ranged attack
            - Bruker vil ha fiender som angriper og oppfører seg som fiender
            - En fiende skal oppsøke spilleren og utføre angrep - spiller skal ta skade om han blir truffet
            - Angrep skal være animerte

        - Designe level 1:
            - Bruker ønsker et leveldesign som er gjenkjennelig og som gir en viss immersion
            - level 1 skal være et lite nabolag med hus og interiør
            - zombies skal ha stifinning og navigere seg relativt sømløst
       


## Dersom dere har oppgaver som dere skal til å starte med, hvor dere har oversikt over både brukerhistorie, akseptansekriterier og arbeidsoppgaver, kan dere ta med disse i innleveringen også.
    - Ref. tidligere punkt

## Forklar kort hvordan dere har prioritert oppgavene fremover
    - Ref. tidligere punkt

## Har dere gjort justeringer på kravene som er med i MVP? Forklar i så fall hvorfor. Hvis det er gjort endringer i rekkefølge utfra hva som er gitt fra kunde, hvorfor er dette gjort?

    - Ja. Vi fikk forrige møte litt kritikk for en relativt vag definisjon av MVP - og her er en utbedret versjon:

    Vi skal ha en 2D top-down shooter der player kan bevege seg med WASD og bevegelsen ikke er hakkete. Player skal kunne skyte med mange ulike våpen og skal kunne gjøre dette med musen. Player skal være animert.

    Det skal være flere ulike fiender - alle animerte - og de skal ha stifinning som gjør at de oppsøker player og angriper.
    Noen fiender angriper i nærkamp, noen angriper med ranged attacks og noen fiender er hybrider.
    Fiender skal helst være noenlunde smarte og ikke stå i veien for hverandre mer enn nødvendig.

    Brettene skal være designet med vegger, gulv og møbler der vegger og møbler vil blokkere bevegelse mens vegger også vil blokkere kuler. Fiender skal være i stand til å navigere seg rundt disse hindringene for å nå player.

    Det skal være power-ups som player kan plukke opp med å bevege seg over dem - eksempler på dette kan være:
        -   Speed boost
        -   Damage boost
        -   Invincibility
    
    Det skal være lyd - både bakgrunnsmusikk, gunshot-lyder og musikk/lyd for powerups.

    Victory-condition vil enten være å overleve en viss timer, plukke opp en del gjenstander eller å beiseire alle fiender.

    Om vi får tid så er det mulighet for å utbrodere - og koden skal være designet slik at ny funksjonalitet lett skal kunne implementeres.
