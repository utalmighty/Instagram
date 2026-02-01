# Instagram backbone using Spring 🌿

## Prerequisites
- **Consul**: 
    - Start Consul: `consul agent -server -bootstrap-expect=1 -data-dir=Instagram_Consul_Config -ui -bind=127.0.0.1`
    - Consul Link: [http://localhost:8500/ui](http://localhost:8500/ui)

## Services Ports:
- Gateway: `9999`
- Post MS: `9090`
- Profile MS: `9091`
- Follow MS: `9092`
- SOS MS: `9093`
- Userfeed MS: `9094`
    