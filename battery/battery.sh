#!/bin/bash

# Define o caminho onde esta o arquivo CSV
caminho_csv="./Documents/tcc/battery/battery.csv"

while true; do
    # Obtém o nível da bateria e o horário atual
    nivel_bateria=$(pmset -g batt | grep -Eo "\d+%" | cut -d% -f1)
    horario_atual=$(date +"%Y-%m-%d %H:%M:%S")
    
    #echo "Nível da bateria: $nivel_bateria%, Horário: $horario_atual"

    # Salva as informações em um arquivo CSV
    echo "$nivel_bateria,$horario_atual;" >> "$caminho_csv"
    
    # Espera 1 minutos antes de capturar novamente as informações
    sleep 60
done