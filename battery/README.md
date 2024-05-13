### Como rodar em cada sistema operacional:
- Windows:
    - abra o powershell
    - abra o diretorio onde esta o projeto
    - rode `.\battery.ps1`
    - agora os dados serão coletados e irão aparecer no arquivo CSV

-  MacOS:
    - abra o terminal
    - instale o pmset, caso tenha o homebrew rode `brew install pmset`
    - o proximo comando tem o objetivo de dar permisão para o pmset acessar a bateria
    - rode `sudo chmod +x Documents/tcc/battery/battery.sh `   
    - rode `./Documents/tcc/battery/battery.sh`
    - agora os dados serão coletados e irão aparecer no arquivo CSV 

-  Linux:
    - abra o terminal
    - abra o diretorio onde esta o projeto
    - rode `.\battery_linux.sh`
    - agora os dados serão coletados e irão aparecer no arquivo CSV    