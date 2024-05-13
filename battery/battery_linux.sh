#!/bin/bash

caminho_csv="./Documents/tcc/bateria/battery.csv"

while true; do
    # Obtém o nível da bateria e o horário atual
    nivel_bateria=$(acpi -b | awk '{print $4}' | tr -d '%,')
    #nivel_bateria=$(acpi -b | grep -P -o '[0-9]+(?=%)')
    # nivel_bateria=$(acpi -b | grep -P -o '\d+(?=%)')
    horario_atual=$(date +"%Y-%m-%d %H:%M:%S")
    
    # Salva as informações em um arquivo CSV
    echo "$nivel_bateria,$horario_atual" >> "$caminho_csv"
    
    # Espera 5 minutos antes de capturar novamente as informações
    sleep 300
done