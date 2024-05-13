## Como rodar o projeto:
Precisa abrir o arquivo html usando um navegador web que aceite conexões Bluetooth, uma sugestão é usar o Google Chrome
    
Esse codigo foi baseado no trabalho do Aaron Christophel disponivel no [Github](https://github.com/atc1441/atc1441.github.io/blob/master/TelinkFlasher.html)

- instalar o node 
- instalar as bibliotecas necessarias
    - `npm install express`
    - `npm install csv-writer`
    - `npm install moment-timezone`
- a pasta node_modules já tem tudo baixado e pronto assim esse passo pode ser pulado    
- rodar `node server.js` no terminal
- entrar no link http://localhost:63343/weather/TelinkFlasher.html
- apertar Connect e se conectar com LYWSD03MMC
- agora os dados serão coletados e irão aparecer no arquivo CSV
