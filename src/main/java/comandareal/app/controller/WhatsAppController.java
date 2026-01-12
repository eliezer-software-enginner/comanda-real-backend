package comandareal.app.controller;

import comandareal.app.dto.PedidoSimuladoDto;
import comandareal.app.service.WhatsAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    public WhatsAppController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/enviar-pedido")
    public ResponseEntity<String> enviarPedido(@RequestBody PedidoSimuladoDto pedido) {
        try {
            String numeroDestinatario = pedido.numero();
            
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
            String numeroDestinatario = pedido.numero() != null ? pedido.numero() : "32998008182";
            
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