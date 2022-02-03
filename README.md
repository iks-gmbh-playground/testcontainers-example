## Einleitung
Hallo :) 
Du willst deine Spring Boot Anwendung lokal integriert testen?
Am liebsten so realitätsnah wie möglich?
Und auch so, dass du diese Tests auch in deine Buildpipeline integrieren kannst?
Dann möchte ich dir heute den heiligen Gral des CI-Testings für die JVM vorstellen. 
Mein Name ist Christoph und ich bin Entwickler bei der IKS GmbH mit einer Vorliebe für DevOps und CI/CD. 
Heute gebe ich dir einen kleinen Einblick in das Integrationstesting mit Spring Boot und Testcontainers.
Los geht's!


## Testcontainers in a nutshell.
Testcontainers ist im Grunde nix anderes als eine Docker-API-Implementierung für die JVM und ermöglicht uns, den Lifecycle der Container an unsere JVM-Laufzeit koppeln zu können. Dadurch müssen keine Container im Voraus gestartet werden und es bleiben auch keine Container nach der Testausführung zurück. 

Damit das funktioniert instanziert Testcontainers einen speziellen Sidecar-Container namens 'Ryuk'. 
Ryuk managed und überwacht den Lifecycle unserer selbst gestarteten Container - ausserhalb unserer JVM. 

Neben einer generischen Docker-API, in der wir bequem Container, Netzwerke und Volumes in Java konfigurieren können, bietet Testcontainers explizite Apis zu einigen der gängigsten Technologien wie Datenbanken und Suchengines an.

## Beispiel
Schauen wir uns doch einmal ein kleines Beispiel an:

Ich möchte gerne meinen Lieblingskaffee bei meinem CoffeeService bestellen. Dazu platziere ich dort einfach eine Bestellung.
Der CoffeeService fragt nun seine Redis, welcher Kaffee mein Lieblingskaffee ist, ruft die entsprechende Information ab und gibt sie weiter an den OrderService. Dieser speichert die Order in einer Postgres und bestätigt den Erhalt der Order. Das wars auch schon und ich kann nun meinen Lieblingskaffee heiss und lecker genießen.

Die Services sind bereits mittels Spring Boot geschrieben und einsatzbereit und warten sehnsüchtig darauf, von uns getestet zu werden.


### OrderService
Schauen wir uns doch mal den OrderService an. Ein erster Test ist schnell geschrieben. 
Wir schicken einfach einen gültigen Request gegen das Backend und erwarten, dass wir a) ein OK zurückbekommen und b) unsere Order in der Datenbank zu finden ist. 

Damit wir den Service jedoch wirklich integriert testen können, benötigen wir eine Postgres-Instanz auf unserem System. Wir wollen schließlich darauf verzichten, unsere Implementierung gegen eine andere Datenbanktechnologie - wie z.B. die In-Memory-DB h2 - zu testen. Aber muss ich jetzt wirklich eine komplette Postgres lokal aufsetzen? Natürlich nicht. Alles was wir benötigen, ist Docker und Testcontainers. Wer sich ausserdem bei Testcontainers ein wenig umschaut wird feststellen, dass es für die gängigen Datenbanken bereits vorgefertigte Pakete gibt. In unserem Fall verwenden wir deshalb einfach die Postgres-API. Jetzt gilt es unsere Datenbank zu konfigurieren.

Deshalb: Rein in den Code

Zuerst erstellen wir eine Postgres-Klasse, welche den Super-Contructor mit dem zu verwenden Docker-Image aufruft. 
In unserem Fall ist es das Default Postgres-Image in der Version 14. 

Als Nächstes benötigen wir für unseren Spring Boot Test eine Konfiguration, welche unsere Postgres beinhaltet und die Connection-Parameter für die Datenbank und später auch die Datasource von Spring konfiguriert. 

Wir können den Postgres-Testcontainer, wie auch in diesem Fall, statisch an die Configuration hängen. Dadurch wird die Datenbank nicht für jeden SpringBootTest einzeln hochgefahren, sondern verbleibt über die gesamte Laufzeit an Ort und Stelle. Dadurch können wir die selbe Datenbank in mehreren Tests wiederverwenden.
Jetzt nur noch ein wenig Konfiguration und wir können unsere Datenbank hochfahren. Die Konfigurationen werden übrigens einfach per Environment-Variablen an den Postgres-Container übergeben. 
Statt der typisierten API könnten wir folglich dem Container auch einfach die entsprechenden Variablen mitgeben.

Als nächstes bestücken wir die Datasource unserer Spring-Anwendung mit den entsprechenden Connection-Params der Postgres.  
Dafür erstellen wir eine Bean für die neue Datasource. Da diese im ApplicationContext bereits vorhanden ist, müssen wir a) sie per `@Primary` annotieren und b) das Überschreiben von Beans in der `application-ittest.yaml` aktivieren.
Nun können wir unsere Datasource mit den Werten aus unserem Testcontainer bestücken. Dafür holen wir uns einfach 
- jdbc-url
- user
- und Passwort

Zack fertig. Konfiguration.

Dann wollen wir doch mal den Test starten.

Schaut man sich die Container an, welche während der Testausführung gestartet werden, werden wir sowohl den Ryuk, als auch einen Postgres-Container mit generischem Namen sehen. Sobald der Test beendet ist, verschwinden sie wieder. 

Fertig - Das war Testcontainers in a nutshell.

Ich bedanke mich für die Aufmerksamkeit und wünsche euch aus ganzem Herzen 

Have a nice testing :)






