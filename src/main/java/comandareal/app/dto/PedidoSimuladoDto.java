package comandareal.app.dto;

import java.util.List;

public record PedidoSimuladoDto(
    String nomeCliente,
    String telefoneCliente, // Número do destinatário do WhatsApp
    String telefoneLojista,
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