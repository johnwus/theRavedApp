Param(
  [string]$Base = "http://localhost"
)

$services = @(
  @{ name = 'api-gateway'; port = 8080; path = '/actuator/health' },
  @{ name = 'user-service'; port = 8081; path = '/actuator/health' },
  @{ name = 'content-service'; port = 8082; path = '/actuator/health' },
  @{ name = 'social-service'; port = 8083; path = '/actuator/health' },
  @{ name = 'realtime-service'; port = 8084; path = '/actuator/health' },
  @{ name = 'ecommerce-service'; port = 8085; path = '/actuator/health' },
  @{ name = 'notification-service'; port = 8086; path = '/actuator/health' },
  @{ name = 'analytics-service'; port = 8087; path = '/actuator/health' },
  @{ name = 'eureka-server'; port = 8761; path = '/actuator/health' },
  @{ name = 'config-server'; port = 8888; path = '/actuator/health' }
)

Write-Host "Checking health against $Base ..."
foreach ($s in $services) {
  $url = "$Base:$($s.port)$($s.path)"
  try {
    $resp = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 5
    Write-Host "[$($s.name)] $url -> $($resp.StatusCode)"
  }
  catch {
    Write-Host "[$($s.name)] $url -> ERROR: $($_.Exception.Message)"
  }
}
