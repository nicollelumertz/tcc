# Path do arquivo de saida
# cd C:\Users\Nicolle\Documents\tcc
# .\bateria.ps1

# Imprimi o que vai sendo adicionado no csv
# Write-Output "bat: $nivelBateria tempo: $horarioAtual"

$csvPath = "battery.csv"

while ($true) {
    # Obtém o nível da bateria e o horário atual
    $nivelBateria = (Get-WmiObject -Class Win32_Battery).EstimatedChargeRemaining
    $horarioAtual = (Get-Date).ToString("yyyy-MM-dd HH:mm:ss")

    # Adiciona as informações ao arquivo CSV
    "$nivelBateria,$horarioAtual" | Out-File -Append -FilePath $csvPath

    # Espera 5 minutos antes de capturar novamente as informações
     Start-Sleep -Seconds 300
}