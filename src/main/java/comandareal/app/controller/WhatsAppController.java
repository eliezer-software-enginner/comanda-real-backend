package comandareal.app.controller;

import comandareal.app.dto.PedidoSimuladoDto;
import comandareal.app.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
@CrossOrigin(origins = "http://localhost:5173")
public class WhatsAppController {

    @Value("${whatsapp.padrao}")
    private String whatsappPadrao;

    private final WhatsAppService whatsAppService;

    public WhatsAppController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/enviar-pedido")
    public ResponseEntity<String> enviarPedido(@RequestBody PedidoSimuladoDto pedido) {
        try {
            String numeroDestinatario = pedido.telefoneCliente();
            
            if (numeroDestinatario == null || numeroDestinatario.isEmpty()) {
                return ResponseEntity.badRequest().body("Número de destinatário é obrigatório");
            }
            
            // Remove o 55 se tiver
            if (numeroDestinatario.startsWith("55")) {
                numeroDestinatario = numeroDestinatario.substring(2);
            }
            
            whatsAppService.enviarPedidoSimulado(pedido, numeroDestinatario);
            return ResponseEntity.ok("✅ Pedido enviado com sucesso para WhatsApp: " + numeroDestinatario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Erro ao enviar pedido: " + e.getMessage());
        }
    }

    @PostMapping("/simular-pedido")
    public ResponseEntity<String> simularRecebimentoPedido(@RequestBody PedidoSimuladoDto pedido) {
        try {
            // Para teste, usa o número do pedido ou um padrão
            String numeroDestinatario = pedido.telefoneLojista()!= null ? pedido.telefoneLojista() : whatsappPadrao;
            
            // Remove o 55 se tiver
            if (numeroDestinatario.startsWith("55")) {
                numeroDestinatario = numeroDestinatario.substring(2);
            }
            
            whatsAppService.enviarPedidoSimulado(pedido, numeroDestinatario);
            return ResponseEntity.ok("✅ Pedido enviado com sucesso para WhatsApp: " + numeroDestinatario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Erro ao enviar pedido: " + e.getMessage());
        }
    }
}