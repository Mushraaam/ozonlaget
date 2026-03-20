# Rapport – innlevering 3
**Team:** *Ozonlaget* – *Alexander Nåmdal, Johs Grødem Larsen, Rein Endre Landmark, Sander Aubell Ahlgren, William Sjølett*

## Team / Prosjekt

### Roller i teamet
Hvordan fungerer rollene i teamet? Trenger dere å oppdatere hvem som er teamlead eller kundekontakt?

### Eventuelle nye roller
Trenger dere andre roller? Skriv ned noen linjer om hva de ulike rollene faktisk innebærer for dere.

### Erfaringer med samarbeid og prosjektmetodikk
Er det noen erfaringer enten teammessig eller mtp prosjektmetodikk som er verdt å nevne? Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet fungerer på?

### Gruppedynamikk
Hvordan er gruppedynamikken? Er det uenigheter som bør løses?

### Kommunikasjon i teamet
Hvordan fungerer kommunikasjonen for dere?

### Retrospektiv
Gjør et kort retrospektiv hvor dere vurderer hva dere har klart til nå, og hva som kan forbedres. Dette skal handle om prosjektstruktur, ikke kode. Dere kan selvsagt diskutere kode, men dette handler ikke om feilretting, men om hvordan man jobber og kommuniserer.

### Bidrag til kodebasen
Under vurdering vil det vektlegges at alle bidrar til kodebasen. Hvis det er stor forskjell i hvem som committer, må dere legge ved en kort forklaring for hvorfor det er sånn. Husk å committe alt. (Også designfiler)

### Referat fra møter
Referat fra møter siden forrige leveranse skal legges ved (mange av punktene over er typisk ting som havner i referat).

### Forbedringspunkter til neste sprint
Bli enige om maks tre forbedringspunkter fra retrospektivet, som skal følges opp under neste sprint.

---

## Krav og spesifikasjon

### Status på krav
Beskriv hvilke krav som er prioritert og hvor langt dere har kommet.

### MVP-status
Forklar om dere har nådd MVP og hvordan dere prioriterer videre funksjonalitet.

### Brukerhistorier
Beskriv brukerhistoriene for kravene dere jobber med.

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

### Akseptansekriterier
Definer hva som må være oppfylt for at funksjonaliteten skal være ferdig, Husk at akseptansekriterier ofte skrives mer eller mindre som tester.

### Arbeidsoppgaver
List opp konkrete utviklingsoppgaver som må gjøres for å implementere kravene.

### Planlagte oppgaver
Dersom dere har oppgaver som dere skal til å starte med, hvor dere har oversikt over både brukerhistorie, akseptansekriterier og arbeidsoppgaver, kan dere ta med disse i innleveringen også.

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



