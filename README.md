Projeto simples de publisher e subscriber em protocolo UDP com suporte para SBE(simple binary enconding) de mensagens
via schema.xml

Para executar o projeto desejado no intellj é necessario adicionar em vm options do debug/run configurations :
--add-opens java.base/sun.nio.ch=ALL-UNNAMED
Para executar o projeto via terminal:
java --add-opens java.base/sun.nio.ch=ALL-UNNAMED -jar target/sbe-quotation-publisherWithSchema-1.0-SNAPSHOT.jar

Obs.: O servidor que roda o Subscriber precisa permitir conexões na porta UDP 40123.

Para liberar a porta 40123:

Linux (Ubuntu/Debian)
sudo ufw allow 40123/udp

Windows (PowerShell como Administrador)
New-NetFirewallRule -DisplayName "Aeron UDP" -Direction Inbound -Protocol UDP -LocalPort 40123 -Action Allow

Mac
Cheque se o firewall embutido está ativo
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --getglobalstate

Se o resultado for Firewall is enabled, significa que ele está ativo.

Para liberar a porta 40123/UDP, execute:

sudo /usr/libexec/ApplicationFirewall/socketfilterfw --add /usr/bin/java
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --unblockapp /usr/bin/java

Isso garante que aplicações Java (como Aeron) possam se comunicar via UDP.

Se quiser liberar uma porta específica para tráfego UDP, use:

sudo pfctl -f /etc/pf.conf -e
echo "pass in proto udp from any to any port 40123" | sudo pfctl -a com.apple/250.ApplicationFirewall -f -
sudo pfctl -sr | grep 40123

Isso adiciona uma regra para permitir entrada de pacotes UDP na porta 40123.

AWS (Se estiver na Nuvem)

1.Vá até o Security Group da sua instância.

2.Adicione uma regra de entrada para permitir tráfego UDP na porta 40123.

3.Selecione "Anywhere (0.0.0.0/0)" ou defina um IP específico.

Caso deseje gerar as classes de modelos do schema:
1 - Vá até a pasta raiz do projeto que deseja executar e digite:
java -jar -Dsbe.output.dir=target/generated-sources/java sbe-all-1.34.1.jar src/main/resources/schema.xml



