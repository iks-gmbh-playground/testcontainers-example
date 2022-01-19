## Einleitung
Du willst deine Spring Boot Anwendung lokal integriert testen?
Am liebsten so realitätsnah wie möglich?
Und auch so, dass du diese Tests auch in deine Buildpipeline integrieren kannst?
Dann möchte ich dir heute den heiligen Gral des CI-Testings für die JVM vorstellen. 
Mein Name ist Christoph und ich bin Entwickler bei der IKS GmbH mit einer Vorliebe für DevOps und CI/CD. 
Heute gebe ich dir einen kleinen Einblick in das Integrationstesting mit Spring Boot und Testcontainers.
Los geht's!


## Was ist Testcontainers
Testcontainers ist im Grunde nix anderes als eine Docker-API-Implementierung für die JVM und ermöglicht uns, den Lifecycle der Container an unsere JVM-Laufzeit - welche die Junit-Tests ausführt - koppeln zu können. Dadurch müssen keine Container im Voraus gestartet werden und es bleiben auch keine Container nach der Testausführung liegen. 

Damit das funktioniert instanziert Testcontainers einen speziellen Sidecar-Container namens 'Ryuk'. 
Ryuk managed und überwacht den Lifecycle unserer selbst gestarteten Container - ausserhalb unserer JVM. 

Neben einer generischen Docker-API, in der wir bequem Container, Netzwerke und Volumes per Java-API konfigurieren können, bietet Testcontainers explizite Apis zu einigen der gängisten Technologien wie Datenbanken und Suchengines an.

## Beispiel
Schauen wir uns doch einmal ein kleines Beispiel an:

Ich möchte gerne meinen Lieblingskaffee bei meinem CoffeeService bestellen. Dazu platziere ich dort einfach eine Bestellung.
Der CoffeeService fragt nun seine Redis, welcher Kaffee mein Lieblingskaffee ist, ruft die entsprechende Information ab und gibt sie weiter an den OrderService. Dieser speichert die Order in einer Postgres und bestätigt den Erhalt der Order. Mehr braucht es dann auch nicht mehr und ich kann nun meinen Lieblingskaffee heiss und lecker genießen.

Die Services sind bereits mittels Spring Boot geschrieben und einsatzbereit und warten sehnsüchtig darauf, von uns getestet zu werden.

Schauen wir uns zuerst den etwas leichteren Fall an: Den OrderService. Ein erster Test ist schnell geschrieben. 

Wir schicken einfach einen Request gültigen Request gegen das Backend und erwarten, dass wir damit auch unseren Lieblingskaffee erhalten. 

Damit wir den Service jedoch integriert testen können, benötigen wir eine Postgres-Instanz auf unserem System. Wir wollen schließlich darauf verzichten, unsere Implementierung gegen eine andere Datenbanktechnologie - wie z.B. die In-Memory-DB h2 - zu testen. Wir wollen gegen die Technologien testen, welche später auch in Produktion zum Einsatz kommen. Aber muss ich jetzt wirklich eine komplette Postgres lokal aufsetzen? Natürlich nicht. Alles was wir benötigen, ist Docker auf unserem System und Testcontainers in unseren Testdependencies. Wer sich bei Testcontainers ein wenig umschaut wird ausserdem feststellen, dass es für die gängigen Datenbanken bereits vorgefertigte Testcontainers gibt. In unserem Fall verwenden wir deshalb einfach die Postgres-API. Jetzt gilt es unsere Datenbank zu konfigurieren.





