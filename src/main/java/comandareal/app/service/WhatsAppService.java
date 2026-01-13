package comandareal.app.service;

import comandareal.app.dto.PedidoSimuladoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173/")
@Service
public class WhatsAppService {

    @Value("${whatsapp.evolution.api.url}")
    private String evolutionApiUrl;

    @Value("${whatsapp.evolution.api.token}")
    private String evolutionApiToken;

    @Value("${whatsapp.evolution.api.instanceName}")
    private String instanceName;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarPedidoSimulado(PedidoSimuladoDto pedido, String numeroDestinatario) {
        try {
            String mensagem = formatarMensagemPedido(pedido);
            enviarMensagemWhatsApp(numeroDestinatario, mensagem);
            
            System.out.println("✅ Pedido enviado com sucesso para WhatsApp: " + numeroDestinatario);
            System.out.println("Cliente: " + pedido.nomeCliente());
            System.out.println("Total: R$ " + pedido.total());
            System.out.println("Mensagem formatada: " + mensagem);
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar pedido via WhatsApp: " + e.getMessage());
            throw new RuntimeException("Falha ao enviar mensagem para WhatsApp", e);
        }
    }

    private void enviarMensagemWhatsApp(String numeroDestinatario, String mensagem) {
        String url = evolutionApiUrl + "/message/sendText/" + instanceName;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", evolutionApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, Object> body = new HashMap<>();
        body.put("number", numeroDestinatario.startsWith("55") ? numeroDestinatario : "55" + numeroDestinatario);
        body.put("text", mensagem);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        
        // Log da requisição para debug
        System.out.println("🔍 URL da Evolution API: " + url);
        System.out.println("🔍 API Key: " + evolutionApiToken);
        System.out.println("🔍 Instance: " + instanceName);
        System.out.println("🔍 Number: " + body.get("number"));
        System.out.println("🔍 Body JSON: " + body);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("✅ Resposta Evolution API: " + response.getBody());
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Falha na API Evolution: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao chamar Evolution API: " + e.getMessage());
            System.err.println("⚠️  Verifique se a Evolution API está rodando em: " + evolutionApiUrl);
            throw new RuntimeException("No momento não é possivel enviar notificações por Whatsapp tente em alguns instantes.");
        }
    }

    private String formatarMensagemPedido(PedidoSimuladoDto pedido) {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("📝 *NOVO PEDIDO*\n\n");
        mensagem.append("👤 Cliente: ").append(pedido.nomeCliente()).append("\n");
        mensagem.append("📋 Itens do pedido:\n");
        
        for (PedidoSimuladoDto.ItemPedidoDto item : pedido.produtos()) {
            mensagem.append(String.format("• %s (%dx) - R$ %.2f\n", 
                item.nome(), item.quantidade(), item.preco()));
        }
        
        mensagem.append(String.format("\n💰 *Total: R$ %.2f*\n", pedido.total()));
        
        if (pedido.observacoes() != null && !pedido.observacoes().isEmpty()) {
            mensagem.append("📝 Observações: ").append(pedido.observacoes()).append("\n");
        }
        
        mensagem.append("\n⏰ Pedido recebido em: ").append(java.time.LocalDateTime.now());
        
        return mensagem.toString();
    }
}