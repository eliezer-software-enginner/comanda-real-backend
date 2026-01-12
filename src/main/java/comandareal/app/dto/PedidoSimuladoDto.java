package comandareal.app.dto;

import java.util.List;

public record PedidoSimuladoDto(
    String lojistaId,
    String nomeCliente,
    String numero, // Número do destinatário do WhatsApp
    List<ItemPedidoDto> produtos,
    double total,
    String observacoes
) {
    
    public record ItemPedidoDto(
        String nome,
        int quantidade,
        double preco
    ) {}
}